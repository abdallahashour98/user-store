package com.example.user_store.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user_store.Model.CartModel;
import com.example.user_store.Model.PersonModel;
import com.example.user_store.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    LayoutInflater layoutInflater;
    List<CartModel> list;
    List<PersonModel> personModelList;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Object price_;
    String pro;

    public CartAdapter(Context context, List<CartModel> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.cart_model, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartModel model = list.get(position);
        holder.price_Cart.setText("" + model.getPrice());

        holder.name_Cart.setText(model.getName());
        Picasso.with(context)
                .load(model.getImageProduct())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView name_Cart, price_Cart, removeItem, save;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_Cart);
            name_Cart = itemView.findViewById(R.id.name_Cart);
            price_Cart = itemView.findViewById(R.id.price_Cart);
            removeItem = itemView.findViewById(R.id.removeItem);
            save = itemView.findViewById(R.id.saveItem);
            removeItem.setOnClickListener(this);
            save.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.removeItem:
                    delete();
                    break;
                case R.id.saveItem:
                   getData();
                    break;
            }

        }
        private void delete(){
            CartModel model = list.get(getAdapterPosition());
            String Uid = firebaseAuth.getUid();
            db.collection("Cart")
                    .document(Uid)
                    .collection(Uid)
                    .document(model.getId())
                    .delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(context,R.string.refresh, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context,R.string.Failed, Toast.LENGTH_SHORT).show();
                }
            });
        }
        private void getData(){
            CartModel m = list.get(getAdapterPosition());
            String uid = firebaseAuth.getUid();
            db.collection("Cart")
                    .document(uid)
                    .collection(uid)
                    .orderBy("Name", Query.Direction.ASCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot documentSnapshot : documents) {
                                CartModel cartModel1 = documentSnapshot.toObject(CartModel.class);
                                String nameProduct = m.getName();
                                Object priceProduct = m.getPrice();
                                assert cartModel1 != null;
                                cartModel1.setId(documentSnapshot.getId());
                                list.add(cartModel1);
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                                String name = preferences.getString("Name", "");
                                String address = preferences.getString("Address", "");
                                Map<String, Object> map = new HashMap<>();
                                map.put("Name", name);
                                map.put("address", address);
                                map.put("Price", priceProduct);
                                map.put("ProductName", nameProduct);
                                db.collection("orders")
                                        .document(m.getId())
                                        .set(map)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Toast.makeText(context,R.string.ProductSave, Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(e -> {
                                });

                                delete();
                            }
                        } else {
                            Toast.makeText(context,R.string.NodatafoundinDatabas, Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }


}
