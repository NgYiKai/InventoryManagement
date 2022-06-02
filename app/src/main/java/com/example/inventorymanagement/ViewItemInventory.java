package com.example.inventorymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.file.Path;

public class ViewItemInventory extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    private RecyclerView recyclerView;
    itemInInventoryAdapter
            adapter; // Create Object of the Adapter class
    DatabaseReference mbase; // Create object of the
    // Firebase Realtime Database
    FloatingActionButton addButton;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item_inventory);

        recyclerView = findViewById(R.id.recycler2);
        addButton=findViewById(R.id.add_button2);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(ViewItemInventory.this,AddItemInventory.class);
                startActivity(i);
            }
        });

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        // Create a instance of the database and get
        // its reference
        mbase
                = FirebaseDatabase.getInstance().getReference("inventory/"+uid);


        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<itemInInventory> options
                = new FirebaseRecyclerOptions.Builder<itemInInventory>()
                .setQuery(mbase, itemInInventory.class)
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new itemInInventoryAdapter(options);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);


        BottomNavigationView nav=findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(R.id.inventory);
        nav.setOnItemSelectedListener(navlistener);

    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

    private NavigationBarView.OnItemSelectedListener navlistener=new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Activity selected =null;Intent i;

            switch (item.getItemId()){
                case R.id.item:
                    i=new Intent(ViewItemInventory.this,ViewItem.class);
                    startActivity(i);
                    break;
                case R.id.inventory:
                    i=new Intent(ViewItemInventory.this,ViewItemInventory.class);
                    startActivity(i);
                    break;
                case R.id.transaction:
                    i=new Intent(ViewItemInventory.this,ViewTransaction.class);
                    startActivity(i);
                    break;
            }
            return false;
        }
    };
}