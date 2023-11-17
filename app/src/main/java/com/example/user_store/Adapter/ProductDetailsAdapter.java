package com.example.user_store.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_store.R;
import com.example.user_store.Model.ProductDetailsModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.ViewHolder> {
    List<ProductDetailsModel> list;
    LayoutInflater layoutInflater;
    Context context;


    public ProductDetailsAdapter(List<ProductDetailsModel> list, Context context) {
        this.list = list;
        this.context = context;
        layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ProductDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =layoutInflater.inflate(R.layout.item_model,parent,false);
        ViewHolder viewHolder = new ProductDetailsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductDetailsModel model = list.get(position);
        holder.name.setText(model.getName());
        holder.price.setText(model.getPrice().toString());
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
        Button saveItemToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photoOffProductDetails);
            price = itemView.findViewById(R.id.tvPriceOffProductDetails);
            name = itemView.findViewById(R.id.tvNameOffProductDetails);
            saveItemToCart = itemView.findViewById(R.id.saveItemToCart);

        }
    }
}
