package tech.carrental.azizproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import tech.carrental.azizproject.Utils.PermissionsUtils;
import tech.carrental.azizproject.models.User;

import static java.security.AccessController.getContext;

public class ActivityProfile extends AppCompatActivity {
    TextView mName;
    TextView mPassword;
    TextView mPhone;
    ProgressBar progressBar;
    Button submit;
    ImageButton uploadimg;
    User userdet;
    private DatabaseReference mDatabase;

    private static final int PICK_IMAGE = 1;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 2;
    Context Appl;

    public static Uri imageUri=null;
    public static Bitmap bitmap;

    ImageView licence_img;
    private StorageReference sref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        Appl = getApplication();
        assert Appl != null;
        if (ContextCompat.checkSelfPermission(Appl, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionsUtils.requestPermission((AppCompatActivity) Appl, STORAGE_PERMISSION_REQUEST_CODE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, true);
        }
        sref = FirebaseStorage.getInstance().getReference();

        if (Activity_renter.user != null) {
            userdet = Activity_renter.user;
        } else if (Activity_SP.user != null) {
            userdet = Activity_SP.user;
        }


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mName = findViewById(R.id.update_name);
        mPassword = findViewById(R.id.update_pass);
        mPhone = findViewById(R.id.update_phone);
        mName.setText(userdet.getName());
        mPhone.setText(userdet.getMobile());
        progressBar = findViewById(R.id.update_progress);
        licence_img = findViewById(R.id.image_license);
        uploadimg = findViewById(R.id.upload_lic);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String uid = FirebaseAuth.getInstance().getUid();

        submit = findViewById(R.id.update_submit);


        sref.child("images/licenses/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'

                        Glide.with(getApplicationContext())
                                .load(uri)
                                .into(licence_img);
                        licence_img.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submit.setEnabled(false);

                if(isValidate()) {
                    progressBar.setVisibility(View.VISIBLE);
                    StorageReference ref = sref.child("images/licenses/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ref.putFile(imageUri)            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // progressDialog.dismiss();
                            Toast.makeText(ActivityProfile.this, "Uploaded license", Toast.LENGTH_SHORT).show();
                        }
                    })    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //  progressDialog.dismiss();
                            Toast.makeText(ActivityProfile.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    if(mPassword.getText().toString().length() > 0) {
                        FirebaseAuth.getInstance().getCurrentUser().updatePassword(mPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                userdet.setName(mName.getText().toString());
                                userdet.setMobile(mPhone.getText().toString());
                                mPassword.setText("");
                                Toast.makeText(ActivityProfile.this, "Password have been changes successfully", Toast.LENGTH_SHORT).show();
                                mDatabase.child("users").child(uid).setValue(userdet).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //  if(FragmentActivity.fa != null){
                                        //      FragmentActivity.fa.finish();
                                        //  }
                                        //    finish();
                                        progressBar.setVisibility(View.GONE);
                                        submit.setEnabled(true);
                                        Toast.makeText(ActivityProfile.this, "Profile details updated!", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        });
                    }else{
                        mDatabase.child("users").child(uid).setValue(userdet).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //  if(FragmentActivity.fa != null){
                                //      FragmentActivity.fa.finish();
                                //  }
                                //    finish();
                                progressBar.setVisibility(View.GONE);
                                submit.setEnabled(true);
                                Toast.makeText(ActivityProfile.this, "Profile details updated!", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                }
            }
        });



    }
    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PICK_IMAGE:
                if(resultCode == RESULT_OK){
                    imageUri = data.getData();
                    String[] projecttion = {MediaStore.Images.Media.DATA}; //fetch media path

                    Cursor cursor = Appl.getContentResolver().query(imageUri, projecttion, null, null, null);
                    cursor.moveToFirst();

                    int column_index = cursor.getColumnIndex(projecttion[0]);
                    String filepath = cursor.getString(column_index);
                    cursor.close();

                    bitmap = BitmapFactory.decodeFile(filepath);
                    Glide.with(getApplicationContext())
                            .load(imageUri)
                            .into(licence_img);
                    licence_img.setVisibility(View.VISIBLE);
                }
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();

            return false;
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean isValidate() {
        boolean valid = true ;
        String name = mName.getText().toString().trim();
        String user_pass = mPassword.getText().toString().trim();
        String phone = mPhone.getText().toString().trim();


        if((name.length() == 0)){
            mName.setError("Name required");
            valid=false;
        }
        else mName.setError(null);
        if(user_pass.length() > 0) {
            if (user_pass.length() > 10 || user_pass.length() < 4) {
                mPassword.setError("Password should be 4 to 10 characters long");
                valid = false;
            } else mPassword.setError(null);
        }

        if((phone.length() != 10) || !Patterns.PHONE.matcher(phone).matches()){
            mPhone.setError("Invalid Phone Number");
            valid=false;
        }else{
            mPhone.setError(null);
        }
        return valid;
    }
}
