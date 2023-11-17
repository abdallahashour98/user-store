package com.example.user_store.authentication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.user_store.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class registration extends AppCompatActivity {
    EditText username, password,confirmPassword, email, phone, address;
    TextInputLayout password_toggle;
    Button save;
    CircleImageView profileImage;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private Uri imageUri = null;
    private static String ImageURL = null;
    private String userID;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        username = findViewById(R.id.edtUserNameReg);
        password = findViewById(R.id.edtUserPasswordReg);
        confirmPassword = findViewById(R.id.edtUserPasswordConfirmInReg);
        password_toggle = findViewById(R.id.password_toggle);
        email = findViewById(R.id.edtUserEmailReg);
        phone = findViewById(R.id.edtUserPhoneReg);
        address = findViewById(R.id.edtUserAddressReg);
        profileImage = findViewById(R.id.profile_image);
        save = findViewById(R.id.btnSaveRig);
        save.setOnClickListener(v -> {registration.this.checkIf();});
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        profileImage.setOnClickListener(v -> checkPermission());
        password.setOnTouchListener((v, event) -> {
            password_toggle.setVisibility(View.VISIBLE);
            return false;
        });
        }


    private void checkIf() {
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirminpass = confirmPassword.getText().toString().trim();
        String Email = email.getText().toString().trim();
        String Phone = phone.getText().toString().trim();
        String addres = address.getText().toString().trim();
     if (user.isEmpty()) {
            username.requestFocus();
            username.setError(getString(R.string.Name));
            return;
     }
     if (pass.isEmpty()) {
            password.requestFocus();
            password.setError(getString(R.string.pleaseEnterPassword));
            return;
     }
     if (pass.length() < 8 || confirminpass.length() < 8 ) {
         password.requestFocus();
         password.setError(getString(R.string.PasswordMustBe));
         confirmPassword.requestFocus();
         confirmPassword.setError(getString(R.string.PasswordMustBe));
            return;
     }
     if (!pass.equals(confirminpass)){
         confirmPassword.requestFocus();
         confirmPassword.setError(getString(R.string.Password));
         return ;
     }
     if (Email.isEmpty()) {
            email.requestFocus();
            email.setError(getString(R.string.pleaseEnterEmail));
            return;
     }

     if (Phone.isEmpty()) {
            phone.requestFocus();
            phone.setError(getString(R.string.Phone));
            return;
     }
     if (Phone.length() < 11 || phone.length()>11) {
            phone.requestFocus();
            phone.setError(getString(R.string.phonedigit));
            return;
     }
     if (addres.isEmpty()) {
            address.requestFocus();
            address.setError(getString(R.string.Address));
            return;
     }
     if (ImageURL==null){
         profileImage.requestFocus();
         ImageURL= "";
        }

     signupfirebase(Email, pass);
    }
    private void signupfirebase(String email, String pass) {
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        saveUserDate();
                    } else {
                    Toast.makeText(registration.this, "Exception : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void saveUserDate() {
        String uID =firebaseAuth.getCurrentUser().getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("id", uID);
        user.put("username", username.getText().toString().trim());
        user.put("password", password.getText().toString().trim());
        user.put("email", email.getText().toString().trim());
        user.put("phone", phone.getText().toString().trim());
        user.put("address", address.getText().toString().trim());
        user.put("image", ImageURL);
        db.collection("user")
                .document(uID)
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                         Toast.makeText(registration.this, "account Created Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(registration.this, LoginActivity.class));
                    } else {
                     Toast.makeText(registration.this, "Error + \\n" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkPermission() {

        //use permission to READ_EXTERNAL_STORAGE For Device >= Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(registration.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(registration.this, "permissionDenied", Toast.LENGTH_LONG).show();

                // to ask admin to reade external storage
                ActivityCompat.requestPermissions(registration.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            } else {
                OpenGalleryImagePicker();
            }

            //implement code for device < Marshmallow
        } else {

            OpenGalleryImagePicker();
        }
    }
    private void OpenGalleryImagePicker() {
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                imageUri = result.getUri();

                // set image admin in ImageView ;
                profileImage.setImageURI(imageUri);

                uploadImage();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(registration.this, "Error : " + error, Toast.LENGTH_LONG).show();

            }
        }
    }
    private void uploadImage() {

        if (firebaseAuth.getCurrentUser() != null) {

            // chick if admin image is null or not
            if (imageUri != null) {

                userID = firebaseAuth.getCurrentUser().getUid();

                // mack progress bar dialog
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("uploading");
                progressDialog.setCancelable(false);
                progressDialog.show();

                // mack collection in fireStorage
                final StorageReference ref = storageReference.child("profile_image_user").child(userID + ".jpg");

                // get image admin and give to imageUserPath
                ref.putFile(imageUri).addOnProgressListener(taskSnapshot -> {

                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("upload " + (int) progress + "%");

                }).continueWithTask(task -> {
                    if (!task.isSuccessful()) {

                        throw task.getException();

                    }
                    return ref.getDownloadUrl();

                }).addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        progressDialog.dismiss();
                        Uri downloadUri = task.getResult();

                        assert downloadUri != null;
                        ImageURL = downloadUri.toString();
                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(registration.this, " Error in addOnCompleteListener " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
    }


}