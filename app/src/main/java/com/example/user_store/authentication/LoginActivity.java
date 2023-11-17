package com.example.user_store.authentication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.inout.SplachActivity;
//import com.google.firebase.auth.FirebaseAuth;

import com.derohimat.sweetalertdialog.SweetAlertDialog;
import com.example.user_store.MainActivity;
import com.example.user_store.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    EditText Emaillogin,passwordlogin;
    TextView Registration,forgetpassword,arabiclanuage,englishLanguage;
    Button login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);
        firebaseAuth = FirebaseAuth.getInstance();
        Emaillogin = findViewById(R.id.edtUserNameLogin);
        passwordlogin = findViewById(R.id.edtUserPassword);
        login = findViewById(R.id.btnLogin);
        Registration = findViewById(R.id.tvRegistration);
        forgetpassword = findViewById(R.id.tvForgetPasswordLogin);
        arabiclanuage = findViewById(R.id.arabicLanguage);
        englishLanguage = findViewById(R.id.englishLanguage);
        if (isOnline()) {
            englishLanguage.setOnClickListener(v -> {
                changeLanguageToEnglish();
                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });
            arabiclanuage.setOnClickListener(v -> {
                changeLanguageToArabic();
                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });

            forgetpassword.setOnClickListener(v -> LoginActivity.this.
                    startActivity(new Intent(LoginActivity.this, ForgetPssword.class)));
            login.setOnClickListener(v -> checkIf());
            Registration.setOnClickListener(v -> {
                startActivity(new Intent(LoginActivity.this,registration.class));
            });
            loadLanguage();
        } else {
            try {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.Info)
                        .setMessage(R.string.Internetnotavailable)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("open_WIFI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            }
                        }).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                 }) .show();
               } catch (Exception e) {
            }
        }

    }
    private void loadLanguage(){
        String show =getSharedPreferences("language", MODE_PRIVATE).getString("my_language","");
        if (show.equals("Ar"))
            changeLanguageToArabic();
        else
            changeLanguageToEnglish();
    }

    private void changeLanguageToEnglish()
    {
        setLanguage("En");
    }
    private void changeLanguageToArabic() {
        setLanguage("Ar");
    }
    private void setLanguage(String lang) {
        getSharedPreferences("language", MODE_PRIVATE)
                .edit()
                .putString("my_language",lang)
                .apply();
        Locale l = new Locale(lang);
        Locale.setDefault(l);
        Configuration configuration = new Configuration();
        configuration.locale = l;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }
    private void checkIf() {
        String email = Emaillogin.getText().toString().trim();
        String pass = passwordlogin.getText().toString().trim();
        if (email.isEmpty()) {
            Emaillogin.requestFocus();
            Emaillogin.setError(getString(R.string.pleaseEnterEmail));
            return;
        }
        if (pass.isEmpty())
        {
            passwordlogin.requestFocus();
            passwordlogin.setError(getString(R.string.pleaseEnterPassword));
            return;
        }
        if (pass.length()<8)
        {
            passwordlogin.requestFocus();
            passwordlogin.setError(getString(R.string.PasswordMustBe));
            return;
        }
        loginWithFirebase(email,pass);
    }
    private void loginWithFirebase(String email, String pass) {
        firebaseAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText(getString(R.string.Loading));
                        pDialog.setCancelable(false);
                        pDialog.show();
                        getSharedPreferences("login", MODE_PRIVATE)
                                .edit()
                                .putBoolean("isLogin", true)
                                .apply();
                                gotoLogin();

                    }else {
                        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText(getString(R.string.wrong))
                                .show();
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        boolean isLogin = getSharedPreferences("login", MODE_PRIVATE).getBoolean("isLogin", false);
        if (isLogin) {
            gotoLogin();
        }
    }
    void gotoLogin() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));

        finish();
    }
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
