package com.example.user_store.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.user_store.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditSetting extends AppCompatActivity {
    //initialize variable
    TextView saveUserNameEdit,saveUserPhoneEdit, saveUserAddressEdit;
    EditText userNameEdit, userPasswordEdit, userPhoneEdit, userAddressEdit;
    CircleImageView circleImageView;
    ImageView changeProfielSetting;
    private ProgressBar progressbar;
    private Uri imageUri = null;
    private static String ImageURL = null;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private String userID;
    SwipeRefreshLayout refreshLayout;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editsetting);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        getImage();
        progressbar = findViewById(R.id.progress_bar);
        refreshLayout = findViewById(R.id.SwipeRefreshLayout);
        userNameEdit = findViewById(R.id.userNameEdit);
        userPhoneEdit = findViewById(R.id.userPhoneEdit);
        userAddressEdit = findViewById(R.id.userAddressEdit);
        circleImageView = findViewById(R.id.profile_image1);
        changeProfielSetting = findViewById(R.id.changeProfielSetting1);
        saveUserNameEdit = findViewById(R.id.saveUserNameEdit);
        saveUserPhoneEdit = findViewById(R.id.saveUserPhoneEdit);
        saveUserAddressEdit = findViewById(R.id.saveUserAddressEdit);
        changeProfielSetting.setOnClickListener(v -> checkPermission());
        saveUserNameEdit.setOnClickListener(v -> saveChangeUserName());
        saveUserAddressEdit.setOnClickListener(v -> saveChangeUserAddress());
        saveUserPhoneEdit.setOnClickListener(v -> saveChangeUserPhone());
        progressbar.setVisibility(View.VISIBLE);
        refreshLayout.setOnRefreshListener(() -> {
            getImage();
            refreshLayout.setRefreshing(false);
        });
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permissionDenied", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            else {
                OpenGalleryImagePicker();
            }
        } else {
            OpenGalleryImagePicker();
        }
    }
    private void OpenGalleryImagePicker() {
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON) .start(this);
    }
    private void uploadImage() {
        if (firebaseAuth.getCurrentUser() != null) {
            if (imageUri != null) {
                userID = firebaseAuth.getCurrentUser().getUid();
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("uploading");
                progressDialog.setCancelable(false);
                progressDialog.show();
                // mack collection in fireStorage
                final StorageReference ref = storageReference.child("profile_image_user").child(userID + ".png");
                // get image user and give to imageUserPath
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
                        Log.d("ImageURL",""+ImageURL);
                        saveChangeImage();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(this, " Error in addOnCompleteListener " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                // set image user in ImageView ;
                circleImageView.setImageURI(imageUri);
                uploadImage();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error : " + error, Toast.LENGTH_LONG).show();
            }
        }
    }
    private void saveChangeImage() {
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            Map<String, Object> user = new HashMap<>();
            user.put("image", ImageURL);
            db.collection("user")
                    .document(userId)
                    .update(user)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void getImage() {
         if (firebaseAuth.getCurrentUser() != null) {
                String uID = firebaseAuth.getCurrentUser().getUid();
                db.collection("user")
                        .document(uID)
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String imagedb = documentSnapshot.getString("image");
                        Log.d("done",imagedb);
                        if (imagedb != null) {
                            assert imagedb != null;
                            Glide.with(EditSetting.this)
                                    .load(imagedb)
                                    .placeholder(R.drawable.product)
                                    .into(circleImageView);
                        }
                        progressbar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(e ->
                        Toast.makeText(this, "Error getting data", Toast.LENGTH_SHORT).show());
            }
        }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveChangeUserName() {
        String email = userNameEdit.getText().toString().trim();
        if (email.isEmpty()) {
            userNameEdit.requestFocus();
            userNameEdit.setError("pleaseentername");
            return;
        } else {
            String userId = firebaseAuth.getCurrentUser().getUid();
            Map<String, Object> userNameSave = new HashMap<>();
            userNameSave.put("username", userNameEdit.getText().toString().trim());
            db.collection("user")
                    .document(userId)
                    .update(userNameSave)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "done", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
    private void saveChangeUserAddress() {
        if (firebaseAuth.getCurrentUser()!=null)
        {
            String address = userAddressEdit.getText().toString().trim();
            if (address.isEmpty()) {
                userAddressEdit.requestFocus();
                userAddressEdit.setError("pleaseenterpassword");
            } else {
                String userId = firebaseAuth.getCurrentUser().getUid();
                Map<String, Object> userAddressSave = new HashMap<>();
                userAddressSave.put("address", userAddressEdit.getText().toString().trim());
                db.collection("user")
                        .document(userId)
                        .update(userAddressSave)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "done", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }
    private void saveChangeUserPhone() {
        if (firebaseAuth.getCurrentUser()!=null) {
            String Phone = userPhoneEdit.getText().toString().trim();
            if (Phone.isEmpty()) {
                userPhoneEdit.requestFocus();
                userPhoneEdit.setError("pleaseenterphone");
            } else if (Phone.length() < 11) {
                userPhoneEdit.requestFocus();
                userPhoneEdit.setError("pleaseenter11digit");
            } else {
                String userId = firebaseAuth.getCurrentUser().getUid();
                Map<String, Object> userPhoneSave = new HashMap<>();
                userPhoneSave.put("phone", userPhoneEdit.getText().toString().trim());
                db.collection("user")
                        .document(userId)
                        .update(userPhoneSave)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "done", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}