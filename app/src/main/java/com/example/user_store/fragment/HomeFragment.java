package com.example.user_store.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user_store.Adapter.RetrieveDataAdapter;
import com.example.user_store.Model.RetrieveDataModel;
import com.example.user_store.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {
    RecyclerView rc1;
    List<RetrieveDataModel> list;
    RetrieveDataAdapter adapter;
    FirebaseFirestore db;
    SwipeRefreshLayout refreshLayout;
    public static  String collectionname = RetrieveDataModel.Contract.COLLECTION_phone ;
    Button category_phone,category_clothes,category_electronics,category_headphones,category_hours,category_shoes;
    TextView tv;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rc1 = view.findViewById(R.id.rcy1);
        tv = view.findViewById(R.id.tvcat);
        refreshLayout = view.findViewById(R.id.SwipeRefreshLayout_Home);
        category_phone = view.findViewById(R.id.category_phone);
        category_clothes = view.findViewById(R.id.category_clothes);
        category_electronics = view.findViewById(R.id.category_electronics);
        category_headphones = view.findViewById(R.id.category_headphones);
        category_hours = view.findViewById(R.id.category_hours);
        category_shoes = view.findViewById(R.id.category_shoes);
        category_phone.setOnClickListener(this);
        category_clothes.setOnClickListener(this);
        category_electronics.setOnClickListener(this);
        category_headphones.setOnClickListener(this);
        category_hours.setOnClickListener(this);
        category_shoes.setOnClickListener(this);
        getData();
        refreshLayout.setOnRefreshListener(() -> {
            clear();
            getData();
            refreshLayout.setRefreshing(false);
        });
        return view;
    }
    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.category_phone:
            tv.setText(RetrieveDataModel.Contract.COLLECTION_phone);
            collectionname= tv.getText().toString();
            clear();
            getData();
        break;
        case R.id.category_clothes:
            tv.setText("");
            tv.setText(RetrieveDataModel.Contract.COLLECTION_clothes);
            collectionname= tv.getText().toString();
            clear();
            getData();
        break;
        case R.id.category_electronics:
            tv.setText("");
            tv.setText(RetrieveDataModel.Contract.COLLECTION_electronics);
            collectionname= tv.getText().toString();
            clear();
            getData();
        break;
        case R.id.category_headphones:
            tv.setText("");
            tv.setText(RetrieveDataModel.Contract.COLLECTION_headphones);
            collectionname= tv.getText().toString();
            clear();
            getData();
        break;
        case R.id.category_hours:
            tv.setText("");
            tv.setText(RetrieveDataModel.Contract.COLLECTION_hours);
            collectionname= tv.getText().toString();
            clear();
            getData();
        break;
        case R.id.category_shoes:
            tv.setText("");
            tv.setText(RetrieveDataModel.Contract.COLLECTION_shoes);
            collectionname= tv.getText().toString();
            clear();
            getData();
        break;
      }

    }

    private void getData(){
        list= new ArrayList<>();
        adapter = new RetrieveDataAdapter( list, getActivity());
        rc1.setHasFixedSize(true);
        rc1.setLayoutManager(new GridLayoutManager(getActivity(),2));
        rc1.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        db.collection(RetrieveDataModel.Contract.COLLECTION)
                .document(collectionname)
                .collection(collectionname)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                     List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : documents) {
                          RetrieveDataModel s = documentSnapshot.toObject(RetrieveDataModel.class);
                          if (documentSnapshot.getId()==null){


                          }
                          else {
                              s.setId(documentSnapshot.getId());
                              list.add(s);
                          }

                           Log.d("ID", "doucment ID" + s.getId());


                        }
                      adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), R.string.NodatafoundinDatabas, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void clear() {
        int size = list.size();
        list.clear();
        adapter.notifyItemRangeRemoved(0, size);
    }
}

