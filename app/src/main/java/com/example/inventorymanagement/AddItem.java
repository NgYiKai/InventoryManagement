package com.example.inventorymanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//class responsible for the add item activity
public class AddItem extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // creating variables for
    // EditText and buttons.
    private EditText itemNameEdt, itemDetailsEdt, itemPriceEdt;
    private Button sendDatabtn;
    String uid = user.getUid();

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for
    // our object class
    item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);


        // initializing our edittext and button
        itemNameEdt = findViewById(R.id.idEdtName);
        itemDetailsEdt = findViewById(R.id.idEdtDetails);
        itemPriceEdt = findViewById(R.id.idEdtPrice);

        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("item");

        // initializing our object
        // class variable.
        item = new item();
        sendDatabtn = findViewById(R.id.idBtnSendData);
        BottomNavigationView nav=findViewById(R.id.bottomNavigationView);
        nav.setOnItemSelectedListener(navlistener);

        // adding on click listener for our button.
        sendDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting text from our edittext fields.
                String name = itemNameEdt.getText().toString();
                String details = itemDetailsEdt.getText().toString();
                String price = itemPriceEdt.getText().toString();

                // below line is for checking weather the
                // edittext fields are empty or not.
                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(details) && TextUtils.isEmpty(price)) {
                    // if the text fields are empty
                    // then show the below message.
                    Toast.makeText(AddItem.this, "Please add some data.", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(name) ){
                    Toast.makeText(AddItem.this, "Please enter item's name.", Toast.LENGTH_SHORT).show();
                }else {
                    // else call the method to add
                    // data to our database.
                    item.setName(name);
                    item.setDetails(details);
                    item.setPrice(price);
                    databaseReference.child(uid).push().setValue(item);
                    itemNameEdt.getText().clear();
                    itemDetailsEdt.getText().clear();
                    itemPriceEdt.getText().clear();
                    Toast.makeText(AddItem.this, "Item added.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addDatatoFirebase(String name, String details, String price) {
        // below 3 lines of code is used to set
        // data in our object class.
        item.setName(name);
        item.setDetails(details);
        item.setPrice(price);

        databaseReference.child(uid).push().setValue(item);

    }

    private NavigationBarView.OnItemSelectedListener navlistener=new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Activity selected =null;
            Intent i;

            switch (item.getItemId()){
                case R.id.item:
                    i=new Intent(AddItem.this,ViewItem.class);
                    startActivity(i);
                    break;
                case R.id.inventory:
                    i=new Intent(AddItem.this,ViewItemInventory.class);
                    startActivity(i);
                    break;
                case R.id.transaction:
                    i=new Intent(AddItem.this,ViewTransaction.class);
                    startActivity(i);
                    break;
            }
            return false;
        }
    };
}