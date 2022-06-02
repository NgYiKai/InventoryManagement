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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class transactionAdapter extends FirebaseRecyclerAdapter<
        transaction, transactionAdapter.transactionsViewholder> {

    public transactionAdapter(
            @NonNull FirebaseRecyclerOptions<transaction> options)
    {
        super(options);
    }


    // Function to bind the view in Card view(here
    // "item.xml") with data in
    // model class(here "item.class")

    @Override
    protected void onBindViewHolder(@NonNull transactionsViewholder holder,
                                    int position, @NonNull transaction model)
    {

        // Add firstname from model class (here
        // "item.class")to appropriate view in Card
        // view (here "item.xml")
        holder.name.setText(model.getName());
        holder.type.setText(model.getType());
        holder.quantity.setText(model.getQuantity());
        holder.time.setText(model.getDate());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //holder.getAdapterPosition();
                //String position=Integer.toString( holder.getAdapterPosition());

                int pos = holder.getAdapterPosition();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();

                FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
                Query query=firebaseDatabase.getReference().child("inventory").child(uid).orderByChild("name").equalTo(model.getName());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot Snapshot: snapshot.getChildren()){
                                String original=Snapshot.child("quantity").getValue(String.class);
                                //Toast.makeText(AddItemInventory.this, test, Toast.LENGTH_SHORT).show();
                                //databaseReference2.setValue(operation(original,quantity,0));
                                String operator = model.getType();
                                itemInInventory item=new itemInInventory();
                                item.setQuantity(operation(original,model.getQuantity(),operator));
                                item.setName(model.getName());
                                Toast.makeText(v.getContext(), "Transaction undo successfully", Toast.LENGTH_SHORT).show();
                                Snapshot.getRef().setValue(item);

                            }
                        }else{
                            Toast.makeText(v.getContext(), "Items doesn't exist anymore in inventory", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                transactionAdapter.this.getRef(pos).removeValue();
            }
        });



    }

    // Function to tell the class about the Card view (here
    // "item.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public transactionsViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction, parent, false);
        return new transactionAdapter.transactionsViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "item.xml")
    class transactionsViewholder
            extends RecyclerView.ViewHolder {
        TextView name,type,quantity,time,delete;
        public transactionsViewholder(@NonNull View itemView)
        {
            super(itemView);

            name
                    = itemView.findViewById(R.id.nameTransaction);
            type=itemView.findViewById(R.id.typeTransaction);
            quantity=itemView.findViewById(R.id.quantityTransaction);
            time=itemView.findViewById(R.id.timeTransaction);
            delete=itemView.findViewById(R.id.deleteTransaction);
        }
    }

    public String operation (String a,String b,String c){
        if(c=="Stock in"){
            return Integer.toString((Integer.parseInt(a) - Integer.parseInt(b)));
        }else return Integer.toString((Integer.parseInt(a) + Integer.parseInt(b)));
    }
}
