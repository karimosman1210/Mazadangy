package com.mazad.mazadangy.gui.signup;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mazad.mazadangy.R;
import com.mazad.mazadangy.gui.login.LoginActivity;
import com.mazad.mazadangy.utels.HelperMethods;

public class User_Profile_image extends AppCompatActivity {
    Button cancel, upload_image;
    ImageView profile_image;
    private static final int RUSLET_LOAD_IMAGE = 1;
    Uri uri_usre_profile_image;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__profile_image);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mStorageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        upload_image = (Button) findViewById(R.id.Upload_user_image_upload);
        cancel = (Button) findViewById(R.id.Upload_user_image_cancel);
        profile_image = (ImageView) findViewById(R.id.Upload_user_image_add);
        upload_image.setVisibility(View.GONE);
        listnerClick();
    }

    private void listnerClick() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_Profile_image.this, LoginActivity.class));
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, RUSLET_LOAD_IMAGE);
            }
        });

        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HelperMethods.showDialog(User_Profile_image.this, "من فضلك امنظر", "جاري التحميل ....");
                //set pass from storge
                StorageReference filepath = mStorageReference.child("Photos").child(uri_usre_profile_image.getLastPathSegment());
                filepath.putFile(uri_usre_profile_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(User_Profile_image.this, " تم تحميل الصورة", Toast.LENGTH_SHORT).show();

//                        Uri uri_load=  taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();


                        final Intent open = new Intent(User_Profile_image.this, SignUpActivity.class);
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    //createNewPost(imageUrl);
                                    open.putExtra("imgeUrlProfile", imageUrl);
                                    startActivity(open);
                                    finish();
                                    HelperMethods.hideDialog(User_Profile_image.this);
                                }
                            });
                        }


                        HelperMethods.hideDialog(User_Profile_image.this);
//                        startActivity(new Intent(User_Profile_image.this , HomeScreen.class));

                    }
                });

            }
        });

    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == RUSLET_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
                Uri selectedimage = data.getData();
                profile_image.setImageURI(selectedimage);
                uri_usre_profile_image = selectedimage;
                profile_image.setBackground(null);
                upload_image.setVisibility(View.VISIBLE);
            }
        }


    @Override
    public void onBackPressed() {
        finish();
    }
}
