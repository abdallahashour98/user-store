package com.example.user_store.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user_store.Adapter.ProductDetailsAdapter;
import com.example.user_store.MainActivity;
import com.example.user_store.Model.FavModel;
import com.example.user_store.Model.RetrieveDataModel;
import com.example.user_store.Model.ProductDetailsModel;
import com.example.user_store.R;
import com.example.user_store.fragment.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetails extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rc;
    List<ProductDetailsModel> list;
    List<FavModel> favModels;
    ProductDetailsAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProductDetailsModel model;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Button saveItemToCart;
    ImageView fav_item;
    RetrieveDataModel ID;
    MainActivity Idd;
    FavModel favModel;
    String Uid= firebaseAuth.getUid().toString();
    String  value , s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ID = (RetrieveDataModel ) getIntent().getSerializableExtra("ID");
//        Log.d("TAG2", "ID: " + ID.getId());
//        Log.d("TAG1", "ID: " + ID.getCollection());
//        Log.d("TAG1", "ID: " + COLLECTION.getId());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             value = extras.getString("Collection");
        }
        rc=findViewById(R.id.rc_item);
        saveItemToCart=findViewById(R.id.saveItemToCart);
        fav_item=findViewById(R.id.fav_item);
        list = new ArrayList<>();
        favModels = new ArrayList<>();
        adapter = new ProductDetailsAdapter(list,this);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setAdapter(adapter);
        s = HomeFragment.collectionname;
        getData();
        getFavorite();
        fav_item.setOnClickListener(this);
        Log.d("TAGa", ""+s);

    }
    private void getData() {

//            Log.d("collection","Id"+value);
//            Log.d("TGa", "" + ID.getId());

            db.collection(RetrieveDataModel.Contract.COLLECTION)
                    .document(s)
                    .collection(s)
                    .document(ID.getId())
                    .get()
                    .addOnCompleteListener(task -> {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            model = document.toObject(ProductDetailsModel.class);
                            list.add(model);
                            adapter.notifyDataSetChanged();
                            saveItemToCart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("ImageProduct", model.getImageProduct());
                                    map.put("Name", model.getName().toString());
                                    map.put("Price", model.getPrice());
                                    db.collection("Cart")
                                            .document(Uid)
                                            .collection(Uid)
                                            .add(map)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Toast.makeText(ProductDetails.this, "Product Save to Cart", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(e -> {
                                            });
                                }
                            });

                        } else {
                            Log.d("TAG", "No such document");
                        }
                    });


    }
    private void getFavorite(){
        db.collection("Favorite")
                .document(Uid)
                .collection(Uid)
                .document(ID.getId())
                .get()
                .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                     favModel = document.toObject(FavModel.class);
                     favModels.add(favModel);
                     fav_item.setImageResource(R.drawable.ic_favorite);
                     adapter.notifyDataSetChanged();

                } else {
                }
            } else {
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fav_item) {
            if (v.getId() == R.id.fav_item) {
                Uid = firebaseAuth.getUid().toString();
                Map<String,Object> map = new HashMap<>();
                map.put("ID",ID.getId());
                map.put("ImageProduct", model.getImageProduct());
                map.put("Name", model.getName().toString());
                map.put("Price", model.getPrice());
//                map.put("Favorite",favModel.isFav());
                db.collection("Favorite")
                        .document(Uid)
                        .collection(Uid)
                        .document(ID.getId())
                        .set(map)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()){
                                Toast.makeText(ProductDetails.this,R.string.Favorite, Toast.LENGTH_SHORT).show();
                                fav_item.setImageResource(R.drawable.ic_favorite);
                            }
                        }).addOnFailureListener(e -> {

                        });
            }
        }
    }
}

