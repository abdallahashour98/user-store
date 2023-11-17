package com.example.user_store.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user_store.Adapter.CartAdapter;
import com.example.user_store.Adapter.PersonAdapter;
import com.example.user_store.Model.CartModel;
import com.example.user_store.Model.FavModel;
import com.example.user_store.Model.PersonModel;
import com.example.user_store.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShopingCart extends AppCompatActivity {

    List<CartModel> modelList;
    List<PersonModel> personModelList;
    CartAdapter adapter;
    PersonAdapter personAdapter;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView rc;
    TextView totalPrice;
    Button saveItem;
    SwipeRefreshLayout refreshLayout;
    private boolean first = true;
    LinearLayout linearLayout;
    ImageView noProduct;
    String Uid = firebaseAuth.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoping_cart);
        rc =findViewById(R.id.rc_off_cart);
        refreshLayout=findViewById(R.id.SwipeRefreshLayout);
        linearLayout=findViewById(R.id.ln);
        noProduct=findViewById(R.id.noProduct);

        modelList=new ArrayList<>();
        personModelList=new ArrayList<>();
        adapter=new CartAdapter(this,modelList);
        personAdapter=new PersonAdapter(personModelList,this);
        rc.setAdapter(adapter);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(this));

        getData();
        refreshLayout.setOnRefreshListener(() -> {
            first =false;
            clear();
            getData();
            refreshLayout.setRefreshing(false);
        });
    }



    private void getData(){

        String Uid = firebaseAuth.getUid().toString();
            db.collection("Cart")
                    .document(Uid)
                    .collection(Uid)
                    .orderBy("Name", Query.Direction.ASCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!first){ modelList.clear();}

                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot documentSnapshot : list) {
                                CartModel cartModel = documentSnapshot.toObject(CartModel.class);
                                cartModel.setId(documentSnapshot.getId());
                                modelList.add(cartModel);
                                String Product_name =cartModel.getName();
                                String price =cartModel.getPrice().toString();

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("ProductName",Product_name);
                                editor.putString("Price",price);
                                editor.apply();
                                adapter.notifyDataSetChanged();
                                getDataPerson();

                            }
                        } else {
                            noProduct.setVisibility(View.VISIBLE);

                        }
                    });

        }

    private void getDataPerson() {
        String uid = firebaseAuth.getUid().toString();

        db.collection("user")
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            PersonModel personModel1 = document.toObject(PersonModel.class);
                            personModelList.add(personModel1);
                            String name =personModel1.getUsername();
                            String address =personModel1.getAddress();
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("Name",name);
                            editor.putString("Address",address);
                            editor.apply();

                        } else {
                        }
                    } else {
                    }
                });
    }

    public void clear() {
        int size = modelList.size();
        modelList.clear();
        adapter.notifyItemRangeRemoved(0, size);
    }

}