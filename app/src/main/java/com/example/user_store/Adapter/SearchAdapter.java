package com.example.user_store.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_store.Model.SearchModel;
import com.example.user_store.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    LayoutInflater layoutInflater;
    Context context;
    List<SearchModel> list ;


    public SearchAdapter(Context context, List<SearchModel> list) {
        this.context = context;
        this.list = list;
        layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= layoutInflater.inflate(R.layout.serach_model,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchModel model = list.get(position);

        holder.name.setText(model.getName());
        holder.price.setText(context.getString(R.string.Egy)+model.getPrice().toString());
        Picasso.with(context)
                .load(model.getImageProduct())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView price,name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.searchImage);
            price = itemView.findViewById(R.id.priceProductSearch);
            name = itemView.findViewById(R.id.nameProductSearch);

        }
     }
}
