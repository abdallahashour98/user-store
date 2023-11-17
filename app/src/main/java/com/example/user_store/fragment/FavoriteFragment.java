package com.example.user_store.fragment;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user_store.Adapter.FavAdapter;
import com.example.user_store.Adapter.RetrieveDataAdapter;
import com.example.user_store.Model.FavModel;
import com.example.user_store.Model.RetrieveDataModel;
import com.example.user_store.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    RecyclerView rc;
    List<FavModel> favModels;
    FavAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String Uid= firebaseAuth.getUid();
    ImageView noProduct;
    SwipeRefreshLayout refresh;
    private boolean first = true;





    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_favorite, container, false);
        rc=view.findViewById(R.id.rc_fav);
        noProduct=view.findViewById(R.id.noProduct_fav);
        refresh=view.findViewById(R.id.SwipeRefreshLayout);
        favModels = new ArrayList<>();
        adapter = new FavAdapter(getActivity(),favModels);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc.setAdapter(adapter);
        getDataInRecyclerView();
        refresh.setOnRefreshListener(() -> {
            first =false;
            clear();
            getDataInRecyclerView();
            refresh.setRefreshing(false);
        });
        Log.d("LoginUser",""+firebaseAuth.getCurrentUser());
        return view;
    }


    private void getDataInRecyclerView(){
        db.collection("Favorite")
                .document(Uid)
                .collection(Uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list) {
                            FavModel c = documentSnapshot.toObject(FavModel.class);

                            c.setID(documentSnapshot.getId());
                            favModels.add(c);

                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        noProduct.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    public void clear() {
        int size = favModels.size();
        favModels.clear();
       adapter.notifyItemRangeRemoved(0, size);
    }
}