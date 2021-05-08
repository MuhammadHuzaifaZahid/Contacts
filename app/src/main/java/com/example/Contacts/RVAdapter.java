package com.example.Contacts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class RVAdapter extends RecyclerView.Adapter<RVAdapter.RVViewHolder> {
    List<ContactModel> Contacts_List;
    public  Context Rv_Context;
    public RVAdapter(List<ContactModel> ls, Context ctx) {
        this.Contacts_List =ls;
        this.Rv_Context =ctx;
    }
    
    @NonNull
    @Override
    public RVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);

        return new RVViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull final RVViewHolder holder, final int position) {

        holder.Name.setText(Contacts_List.get(position).getName());
        holder.Phone_No.setText(Contacts_List.get(position).getPhone_No());
        if(Contacts_List.get(position).getImage()!=null) {
            holder.Image.setImageURI(Uri.parse(Contacts_List.get(position).getImage()));
        }
        //On Click Listener Initialization
        holder.Row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Contact_Details= new Intent(Rv_Context,Contact_Details.class);

                Contact_Details.putExtra("Id",Contacts_List.get(position).getContact_ID());

                Rv_Context.startActivity(Contact_Details);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Contacts_List.size();
    }

    public class RVViewHolder extends RecyclerView.ViewHolder {
        TextView Name,Phone_No;
        LinearLayout Row;
        CircleImageView Image;

        public RVViewHolder(@NonNull View itemView) {
            super(itemView);

            Name=itemView.findViewById(R.id.name);

            Phone_No=itemView.findViewById(R.id.phone_no);

            Image=itemView.findViewById(R.id.image);

            Row=itemView.findViewById(R.id.row);


        }
    }
}
