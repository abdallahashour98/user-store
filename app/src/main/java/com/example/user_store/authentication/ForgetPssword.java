package com.example.user_store.authentication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.derohimat.sweetalertdialog.SweetAlertDialog;
import com.example.user_store.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPssword extends AppCompatActivity {
    EditText emailregest;
    Button sendregest;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pssword);


        emailregest=findViewById(R.id.edtUserNameForgetPassword);
        sendregest=findViewById(R.id.btnsendemail);
        firebaseAuth= FirebaseAuth.getInstance();
        sendregest.setOnClickListener(v ->
                    firebaseAuth.sendPasswordResetEmail(emailregest.getText().toString())
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText(getString(R.string.changePasswordFromEmail))
                                            .setContentText("You clicked the button!")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    finish();
                                                }
                                            })
                                            .show();
                                } else {
                                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText(getString(R.string.wrong))
                                            .show();    }
                            }));
        }
    }
