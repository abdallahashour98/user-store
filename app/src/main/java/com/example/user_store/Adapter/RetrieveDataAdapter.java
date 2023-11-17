package com.example.user_store.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_store.Model.RetrieveDataModel;
import com.example.user_store.R;
import com.example.user_store.ui.ProductDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RetrieveDataAdapter extends RecyclerView.Adapter<RetrieveDataAdapter.ViewHolder> {
    List<RetrieveDataModel> list;
    LayoutInflater layoutInflater;
    Context context;


    public RetrieveDataAdapter(List<RetrieveDataModel> list, Context context) {
        this.list = list;
        this.context = context;
        layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =layoutInflater.inflate(R.layout.modelgetdata,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RetrieveDataModel model = list.get(position);
        if (model.getName()!=null){
            holder.name.setText(model.getName());
        }
        if (model.getPrice()!=null){
            holder.price.setText(""+model.getPrice());

        }
        if (holder.imageView!=null){
            Picasso.with(context)
                    .load(model.getImageProduct())
                    .into(holder.imageView);
        }


    }

    @Override
    public int getItemCount() {
        return list.size() ;


    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView price,name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photoOffProduct);
            price = itemView.findViewById(R.id.tvPriceOffProduct);
            name = itemView.findViewById(R.id.tvNameOffProduct);

            itemView.setOnClickListener(v -> {
                RetrieveDataModel courses = list.get(getAdapterPosition());
                Intent i = new Intent(context, ProductDetails.class);
                i.putExtra("ID", courses);
                context.startActivity(i);
                Log.d("sss", " "+courses.getId() );


            });
        }
    }
}
