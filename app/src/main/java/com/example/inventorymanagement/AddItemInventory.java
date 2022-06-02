package com.example.inventorymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddItemInventory extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    String item="";
    String Quantity;
    int operator;
    Intent i;
    transaction record;
    itemInInventory itemInInventory=new itemInInventory();

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_inventory);

        //initialize the spinner for stock in/out
        // create array of Strings
        // and store name of courses
        Spinner spinner1=findViewById(R.id.stockspinner);
        String[] choice = { "Stock In","Stock Out" };
        ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_spinner_item,choice);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                operator=parent.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                operator=parent.getSelectedItemPosition();

            }
        });



        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference().child("item").child(uid);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                              @Override
                                              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                  item=parent.getItemAtPosition(position).toString();
                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> parent) {
                                                  item=parent.getItemAtPosition(0).toString();

                                              }
                                          }
        );

        List<String> names = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot inventorySnapshot : snapshot.getChildren()) {
                    String name = inventorySnapshot.child("name").getValue(String.class);
                    names.add(name);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddItemInventory.this, android.R.layout.simple_spinner_item, names);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        BottomNavigationView nav=findViewById(R.id.bottomNavigationView);
        nav.setOnItemSelectedListener(navlistener);

        Button sendDatabtn = findViewById(R.id.idBtnSendData);
        EditText itemQuantity = findViewById(R.id.idEdtQuantity);

        // adding on click listener for our button.
        sendDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference2=firebaseDatabase.getReference().child("inventory").child(uid);

                // getting text from our edittext fields.
                String quantity = itemQuantity.getText().toString();
                itemInInventory.setName(item);
                itemInInventory.setQuantity(quantity);

                // below line is for checking weather the
                // edittext fields are empty or not.
                if (TextUtils.isEmpty(quantity)) {
                    // if the text fields are empty
                    // then show the below message.
                    Toast.makeText(AddItemInventory.this, "Please add the quantity.", Toast.LENGTH_SHORT).show();
                } else if (item==""){
                    Toast.makeText(AddItemInventory.this, "Please add item to item list before continue.", Toast.LENGTH_SHORT).show();
                }else {
                    // else call the method to add
                    // data to our database.

                    itemQuantity.getText().clear();

                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();

                    if (operator==0){
                        record = new transaction("Stock in",item,quantity,formatter.format(date));
                    }else{
                        record = new transaction("Stock out",item,quantity,formatter.format(date));
                    }
                    firebaseDatabase.getReference().child("transaction").child(uid).push().setValue(record);

                    databaseReference2.orderByChild("name").equalTo(item).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                for (DataSnapshot Snapshot: snapshot.getChildren()) {
                                    String original=Snapshot.child("quantity").getValue(String.class);

                                    itemInInventory.setQuantity(operation(original,quantity,operator));
                                    Snapshot.getRef().setValue(itemInInventory);
                                    if (operator==0){
                                        Toast.makeText(AddItemInventory.this, "Stock In Completed", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(AddItemInventory.this, "Stock Out Completed", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }else{
                                databaseReference2.push().setValue(itemInInventory);
                                Toast.makeText(AddItemInventory.this, "Item added", Toast.LENGTH_SHORT).show();
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public String operation(String a,String b,int operator){
        if (operator==0){
            return Integer.toString( Integer.parseInt(a)+Integer.parseInt(b));
        }else{
            return Integer.toString( Integer.parseInt(a)-Integer.parseInt(b));
        }


    }

    private NavigationBarView.OnItemSelectedListener navlistener=new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Activity selected =null;

            switch (item.getItemId()){
                case R.id.item:
                    i=new Intent(AddItemInventory.this,ViewItem.class);
                    startActivity(i);
                    break;
                case R.id.inventory:
                    i=new Intent(AddItemInventory.this,ViewItemInventory.class);
                    startActivity(i);
                    break;
                case R.id.transaction:
                    i=new Intent(AddItemInventory.this,ViewTransaction.class);
                    startActivity(i);
                    break;
            }
            return false;
        }
    };

}

