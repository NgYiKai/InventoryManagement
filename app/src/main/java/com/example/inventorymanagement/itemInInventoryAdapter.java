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

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class itemInInventoryAdapter extends FirebaseRecyclerAdapter<
        itemInInventory, itemInInventoryAdapter.itemInInventorysViewholder> {

    public itemInInventoryAdapter(
            @NonNull FirebaseRecyclerOptions<itemInInventory> options)
    {
        super(options);
    }


    // Function to bind the view in Card view(here
    // "item.xml") with data in
    // model class(here "item.class")

    @Override
    protected void onBindViewHolder(@NonNull itemInInventorysViewholder holder,
                     int position, @NonNull itemInInventory model)
    {

        // Add firstname from model class (here
        // "item.class")to appropriate view in Card
        // view (here "item.xml")
        holder.item_name_inventory.setText(model.getName());

        // Add lastname from model class (here
        // "item.class")to appropriate view in Card
        // view (here "item.xml")
        holder.item_quantity_inventory.setText(model.getQuantity());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //holder.getAdapterPosition();
                //String position=Integer.toString( holder.getAdapterPosition());

                int pos = holder.getAdapterPosition();
                Toast.makeText(v.getContext(),"Item removed", Toast.LENGTH_SHORT).show();
                itemInInventoryAdapter.this.getRef(pos).removeValue();
            }
        });

    }

    // Function to tell the class about the Card view (here
    // "item.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public itemInInventorysViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_in_inventory, parent, false);
        return new itemInInventoryAdapter.itemInInventorysViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "item.xml")
    class itemInInventorysViewholder
            extends RecyclerView.ViewHolder {
        TextView item_name_inventory,item_quantity_inventory,delete;
        public itemInInventorysViewholder(@NonNull View itemView)
        {
            super(itemView);

            item_name_inventory
                    = itemView.findViewById(R.id.item_name_inventory);
            item_quantity_inventory = itemView.findViewById(R.id.item_quantity_inventory);
            delete=itemView.findViewById(R.id.deleteInventory);

        }
    }
}
