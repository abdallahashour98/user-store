package com.example.user_store.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_store.Model.CartModel;
import com.example.user_store.Model.PersonModel;
import com.example.user_store.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {
    List<PersonModel> list;
    LayoutInflater layoutInflater;
    Context context;

    public PersonAdapter(List<PersonModel> list, Context context) {
        this.list = list;
        this.context = context;
        layoutInflater=LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.cart_model,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PersonModel model = list.get(position);
        holder.name.setText(model.getUsername());
        holder.address.setText(model.getAddress());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,address;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_person);
            address = itemView.findViewById(R.id.person_address);




        }
    }

}
