package com.example.user_store;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.derohimat.sweetalertdialog.SweetAlertDialog;
import com.example.user_store.Adapter.SearchAdapter;
import com.example.user_store.Model.RetrieveDataModel;
import com.example.user_store.Model.SearchModel;
import com.example.user_store.ui.ShopingCart;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FragmentContainerView nav;
    NavController navController;
    Toolbar toolbar;
    RecyclerView rc;
    List<SearchModel> list;
    SearchAdapter adapter;
    FirebaseFirestore db =FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        rc = findViewById(R.id.search_ry);
        nav = findViewById(R.id.navHostFragment);
        toolbar = findViewById(R.id.toolbar);
        list = new ArrayList<>();
        adapter = new SearchAdapter(this,list);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setAdapter(adapter);
        setSupportActionBar(toolbar);
        navController= Navigation.findNavController(this , R.id.navHostFragment);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        rc.setVisibility(View.GONE);
        nav.setVisibility(View.VISIBLE);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.favoriteFragment:
                    navController.navigate(R.id.favoriteFragment);
                    return true;
                    case R.id.settingFragment:
                    navController.navigate(R.id.settingFragment);
                    return true;
                    case R.id.homeFragment:
                    navController.navigate(R.id.homeFragment);
                    return true;
                default:
            }
            return false;

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
         searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        MenuItem cart = menu.findItem(R.id.Cart);

        cart.setOnMenuItemClickListener(item -> {
            Intent i = new Intent(MainActivity.this, ShopingCart.class);
            startActivity(i);
            return false;
        });

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchUsers(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchUsers(newText);
                return false;
            }
        });

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            rc.setVisibility(View.GONE);
            nav.setVisibility(View.VISIBLE);
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        }
        return super.onOptionsItemSelected(item);


    }

    private void searchUsers(String recherche) {
        rc.setVisibility(View.VISIBLE);
        nav.setVisibility(View.GONE);
        list.clear();
        allCollection(recherche);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.Areyousure))
                .setContentText(getString(R.string.Exit))
                .setConfirmText(getString(R.string.Yes))
                .setConfirmClickListener(sDialog -> {
                    finish();
                }).setCancelText("No")
                .setCancelClickListener(sweetAlertDialog -> sweetAlertDialog.dismiss())
                .show();
    }
    private void allCollection(String recherche){
        db.collection(RetrieveDataModel.Contract.COLLECTION)
                .document(RetrieveDataModel.Contract.COLLECTION_phone)
                .collection(RetrieveDataModel.Contract.COLLECTION_phone)
                .whereGreaterThanOrEqualTo("Name", recherche.toLowerCase())
                .get().addOnCompleteListener(task -> {
            for (DocumentSnapshot doc : task.getResult()) {
                SearchModel user = doc.toObject(SearchModel.class);
                list.add(user);
                user.setId(doc.getId());
                adapter = new SearchAdapter(MainActivity.this,list);
                rc.setAdapter(adapter);
            }
        });
        db.collection(RetrieveDataModel.Contract.COLLECTION)
                .document(RetrieveDataModel.Contract.COLLECTION_electronics)
                .collection(RetrieveDataModel.Contract.COLLECTION_electronics)
                .whereGreaterThanOrEqualTo("Name", recherche.toLowerCase())
                .get().addOnCompleteListener(task -> {
            for (DocumentSnapshot doc : task.getResult()) {
                SearchModel user = doc.toObject(SearchModel.class);
                list.add(user);
                user.setId(doc.getId());
                adapter = new SearchAdapter(MainActivity.this,list);
                rc.setAdapter(adapter);
            }
        });
        db.collection(RetrieveDataModel.Contract.COLLECTION)
                .document(RetrieveDataModel.Contract.COLLECTION_clothes)
                .collection(RetrieveDataModel.Contract.COLLECTION_clothes)
                .whereGreaterThanOrEqualTo("Name", recherche.toLowerCase())
                .get().addOnCompleteListener(task -> {
            for (DocumentSnapshot doc : task.getResult()) {
                SearchModel user = doc.toObject(SearchModel.class);
                list.add(user);
                user.setId(doc.getId());
                adapter = new SearchAdapter(MainActivity.this,list);
                rc.setAdapter(adapter);
            }
        });
        db.collection(RetrieveDataModel.Contract.COLLECTION)
                .document(RetrieveDataModel.Contract.COLLECTION_headphones)
                .collection(RetrieveDataModel.Contract.COLLECTION_headphones)
                .whereGreaterThanOrEqualTo("Name", recherche.toLowerCase())
                .get().addOnCompleteListener(task -> {
            for (DocumentSnapshot doc : task.getResult()) {
                SearchModel user = doc.toObject(SearchModel.class);
                list.add(user);
                user.setId(doc.getId());
                adapter = new SearchAdapter(MainActivity.this,list);
                rc.setAdapter(adapter);
            }
        });
        db.collection(RetrieveDataModel.Contract.COLLECTION)
                .document(RetrieveDataModel.Contract.COLLECTION_hours)
                .collection(RetrieveDataModel.Contract.COLLECTION_hours)
                .whereGreaterThanOrEqualTo("Name", recherche.toLowerCase())
                .get().addOnCompleteListener(task -> {
            for (DocumentSnapshot doc : task.getResult()) {
                SearchModel user = doc.toObject(SearchModel.class);
                list.add(user);
                user.setId(doc.getId());
                adapter = new SearchAdapter(MainActivity.this,list);
                rc.setAdapter(adapter);
            }
        });
        db.collection(RetrieveDataModel.Contract.COLLECTION)
                .document(RetrieveDataModel.Contract.COLLECTION_shoes)
                .collection(RetrieveDataModel.Contract.COLLECTION_shoes)
                .whereGreaterThanOrEqualTo("Name", recherche.toLowerCase())
                .get().addOnCompleteListener(task -> {
            for (DocumentSnapshot doc : task.getResult()) {
                SearchModel user = doc.toObject(SearchModel.class);
                list.add(user);
                user.setId(doc.getId());
                adapter = new SearchAdapter(MainActivity.this,list);
                rc.setAdapter(adapter);
            }
        });
    }

}