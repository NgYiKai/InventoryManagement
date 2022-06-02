package com.example.inventorymanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class itemAdapter extends FirebaseRecyclerAdapter<
        item, itemAdapter.itemsViewholder> {

    public itemAdapter(
            @NonNull FirebaseRecyclerOptions<item> options)
    {
        super(options);
    }

    // Function to bind the view in Card view(here
    // "item.xml") with data in
    // model class(here "item.class")
    @Override
    protected void
    onBindViewHolder(@NonNull itemsViewholder holder,
                     int position, @NonNull item model)
    {

        // Add firstname from model class (here
        // "item.class")to appropriate view in Card
        // view (here "item.xml")
        holder.name.setText(model.getName());

        // Add lastname from model class (here
        // "item.class")to appropriate view in Card
        // view (here "item.xml")
        holder.details.setText(model.getPrice());

        // Add age from model class (here
        // "item.class")to appropriate view in Card
        // view (here "item.xml")
        holder.price.setText(model.getDetails());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //holder.getAdapterPosition();
                //String position=Integer.toString( holder.getAdapterPosition());

                int pos = holder.getAdapterPosition();
                Toast.makeText(v.getContext(),"Item removed", Toast.LENGTH_SHORT).show();
                itemAdapter.this.getRef(pos).removeValue();
            }
        });


    }

    // Function to tell the class about the Card view (here
    // "item.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public itemsViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new itemAdapter.itemsViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "item.xml")
    class itemsViewholder
            extends RecyclerView.ViewHolder {
        TextView name,details,price,delete,button;
        public itemsViewholder(@NonNull View itemView)
        {
            super(itemView);

            name
                    = itemView.findViewById(R.id.name);
            details = itemView.findViewById(R.id.details);
            price = itemView.findViewById(R.id.price);
            //delete=itemView.findViewById(R.id.delete);
            button=itemView.findViewById(R.id.button);
        }
    }
}
