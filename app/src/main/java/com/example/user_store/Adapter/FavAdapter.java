package com.example.user_store.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_store.Model.CartModel;
import com.example.user_store.Model.FavModel;
import com.example.user_store.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder>{

    Context context;
    LayoutInflater layoutInflater;
    List<FavModel> list;
    ImageView imageView, fav;
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public FavAdapter(Context context, List<FavModel> list) {
        this.context = context;
        this.list = list;
        layoutInflater=LayoutInflater.from(context);
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.fav_model,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavModel model = list.get(position);
        holder.price_Fav.setText(""+model.getPrice());
        holder.name_Fav.setText(model.getName());
        Picasso.with(context)
                .load(model.getImageProduct())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_Fav,price_Fav;
        ImageView imageView, fav;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.FavImage);
            name_Fav=itemView.findViewById(R.id.nameProductFav);
            price_Fav=itemView.findViewById(R.id.priceProductFav);
            fav=itemView.findViewById(R.id.fav_del);
            fav.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fav_del:
                    FavModel model = list.get(getAdapterPosition());
                    FavModel favModel = new FavModel();
                    String Uid = firebaseAuth.getUid().toString();

                    db.collection("Favorite")
                            .document(Uid)
                            .collection(Uid)
                            .document(model.getID())
                            .delete().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            Toast.makeText(context, R.string.producthasbeendeleted, Toast.LENGTH_SHORT).show();
                            fav.setImageResource(R.drawable.ic_favorite_empty);
                        } else {

                            Toast.makeText(context, R.string.NodatafoundinDatabas, Toast.LENGTH_SHORT).show();
                        }
                    });

                    break;



            }
        }
    }

}

