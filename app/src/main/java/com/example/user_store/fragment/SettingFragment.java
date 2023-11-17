package com.example.user_store.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.derohimat.sweetalertdialog.SweetAlertDialog;
import com.example.user_store.ui.EditSetting;
import com.example.user_store.authentication.LoginActivity;
import com.example.user_store.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class SettingFragment extends Fragment {

    TextView email, phone, address,  username, editProfiel;
    SwitchMaterial SwitchMaterial;
    CircleImageView circleImageView;
    LinearLayout choseLanguages,logout;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private ProgressBar progressBar;


    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);


        username = view.findViewById(R.id.username_setting);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        editProfiel = view.findViewById(R.id.edit_profiel);
        circleImageView = view.findViewById(R.id.profile_image_setting);
        email = view.findViewById(R.id.email_user_setting);
        phone = view.findViewById(R.id.phone_user_setting);
        address = view.findViewById(R.id.address_user_setting);
        progressBar = view.findViewById(R.id.progress_bar_setting);
        SwitchMaterial = view.findViewById(R.id.Switchcompat);
        choseLanguages = view.findViewById(R.id.choseLanguages);
        editProfiel.setOnClickListener(v ->
        startActivity(new Intent(getActivity(), EditSetting.class)));
        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
            firebaseAuth.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("login", MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();

        });
        choseLanguages.setOnClickListener(v -> showChangeLanguageDialog());





getUserDataWithFirebase();
        return  view;
    }

    private void showChangeLanguageDialog() {
        AlertDialog builder = new AlertDialog.Builder(requireActivity()).create();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.language, null);
        builder.setView(view);
        ImageView cancel_alert = view.findViewById(R.id.cancel_alert);
        cancel_alert.setOnClickListener(v -> {
//             builder.dismiss();
        });
        RadioButton radio_en = view.findViewById(R.id.radio_english);
        RadioButton radio_ar = view.findViewById(R.id.radio_arabic);
        view.findViewById(R.id.save_language).setOnClickListener(v -> {
            if (radio_en.isChecked()) {
                setLanguage("En");
            }
            else if (radio_ar.isChecked()) {
                setLanguage("Ar");
            }
        });
        builder.show();
    }
    private void setLanguage(String lang) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("language", MODE_PRIVATE).edit();
        editor.putString("my_language", lang);
        editor.apply();
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void getUserDataWithFirebase() {
        if (getActivity() != null) {
            if (firebaseAuth.getCurrentUser() != null) {
                progressBar.setVisibility(View.VISIBLE);
                String uID = firebaseAuth.getCurrentUser().getUid();
                db.collection("user").document(uID)
                        .get().addOnSuccessListener(documentSnapshot -> {
                    String emaildb = documentSnapshot.getString("email");
                    String userdb = documentSnapshot.getString("username");
                    String imagedb = documentSnapshot.getString("image");
                    String addressdb = documentSnapshot.getString("address");
                    String phonedb = documentSnapshot.getString("phone");
                    email.setText(emaildb);
                    username.setText(getString(R.string.hi) +"  "+ userdb);
                    address.setText(addressdb);
                    phone.setText(phonedb);
                    if (imagedb != null) {
                        assert imagedb != null;
                        Glide.with(getActivity())
                                .load(imagedb)
                                .placeholder(R.drawable.products)
                                .into(circleImageView);
                    }
                    progressBar.setVisibility(View.GONE);
                }).addOnFailureListener(e -> Toast.makeText(getActivity(), R.string.NodatafoundinDatabas, Toast.LENGTH_SHORT).show());
            }
        }
    }


}