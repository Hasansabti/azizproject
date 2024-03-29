package tech.carrental.azizproject.ui.login;

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
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import tech.carrental.azizproject.ActivityProfile;
import tech.carrental.azizproject.Activity_SP;
import tech.carrental.azizproject.Activity_renter;
import tech.carrental.azizproject.R;
import tech.carrental.azizproject.Utils.PermissionsUtils;
import tech.carrental.azizproject.models.User;

public class RegisterActivity extends AppCompatActivity {

    String TAG = "register";
    Button mRegisterButton;
    EditText mName;
    EditText mEmail;
    EditText mPassword;
    EditText natID;
    EditText mMobileNo;

    String mPort="8000";
    JSONObject registrationDetails;
    RadioGroup type;
    boolean isrenter = false;
    User userdet;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    ProgressBar progressBar;

    RadioButton renterrb;
    RadioButton sprb;

    ImageButton license;
    TextView liclbl;

    public static Uri imageUri=null;
    public static Bitmap bitmap;
    private static final int PICK_IMAGE = 1;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 2;
    Context Appl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


// ...
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        Appl = this;
        assert Appl != null;
        if (ContextCompat.checkSelfPermission(Appl, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionsUtils.requestPermission((AppCompatActivity) Appl, STORAGE_PERMISSION_REQUEST_CODE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, true);
        }

//        registrationDetails=new JSONObject();
        mRegisterButton=findViewById(R.id.register_button);
        mName =findViewById(R.id.input_name);
        mEmail =findViewById(R.id.input_email);
        mPassword=findViewById(R.id.update_pass);
        natID = findViewById(R.id.input_natid);
//        mCity = findViewById(R.id.input_license);
        mMobileNo = findViewById(R.id.input_mobile_no);
        type = findViewById(R.id.type);
        progressBar = findViewById(R.id.progress_reg);
        renterrb = findViewById(R.id.type1);
        sprb = findViewById(R.id.type2);
        license = findViewById(R.id.input_license);
        liclbl = findViewById(R.id.liceselbl);

        license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        sprb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    license.setVisibility(View.VISIBLE);
                    liclbl.setVisibility(View.VISIBLE);
                }else {
                    license.setVisibility(View.INVISIBLE);
                    liclbl.setVisibility(View.INVISIBLE);
                }
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(RegistrationActivity.this,DateSelector.class));
                if(validate()){
                    progressBar.setVisibility(View.VISIBLE);
                    mRegisterButton.setEnabled(false);
                    registrationDetails=new JSONObject();
                    final String email=mEmail.getText().toString().trim();
                    final String name=mName.getText().toString().trim();
                    String pw= mPassword.getText().toString().trim();
                    final String mobileno = mMobileNo.getText().toString().trim();
                    final String id = natID.getText().toString().trim();
                   // final String city = mCity.getText().toString().trim();

                    int selectedtype = type.getCheckedRadioButtonId();
                    if(selectedtype == R.id.type1){
                        isrenter = true;
                    }else if(selectedtype == R.id.type2){
                        isrenter = false;
                    }


                    LoginActivity.setID(email);
                    try {
                        mAuth.createUserWithEmailAndPassword(email, pw)
                                .addOnCompleteListener(RegisterActivity.this,new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {


                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "createUserWithEmail:success");
                                             userdet = new User(name,email,id,isrenter,mobileno);
                                            final FirebaseUser user = mAuth.getCurrentUser();
                                            mDatabase.child("users").child(user.getUid()).setValue(userdet).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                  //  if(FragmentActivity.fa != null){
                                                  //      FragmentActivity.fa.finish();
                                                  //  }
                                                //    finish();
                                                    progressBar.setVisibility(View.GONE);
                                                    mRegisterButton.setEnabled(true);
                                                    updateUI(user);

                                                }
                                            });

                                            if(imageUri != null) {
                                                StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/licenses/" + task.getResult().getUser().getUid());
                                                ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        // progressDialog.dismiss();
                                                      //  Toast.makeText(RegisterActivity.this, "Uploaded license", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        //  progressDialog.dismiss();
                                                        Toast.makeText(RegisterActivity.this, "Failed license" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }



                                        } else {

                                            // If sign in fails, display a message to the user.
                                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                                mEmail.setError(task.getException().getMessage());
                                            }else {
                                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                                updateUI(null);
                                            }
                                        }


                                    }
                                });
                    }

                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private boolean validate() {
        boolean valid =true;
        String name=mName.getText().toString().trim();
        String email=mEmail.getText().toString().trim();
        String pw= mPassword.getText().toString().trim();
        String mobileno = mMobileNo.getText().toString().trim();
        String id = natID.getText().toString().trim();
       // String city = mCity.getText().toString().trim();
        if(!renterrb.isChecked() && !sprb.isChecked()){
            valid = false;
            Toast.makeText(RegisterActivity.this,"Select renter or service provider",Toast.LENGTH_SHORT).show();
        }
        if((name.length() == 0) ){
            mName.setError("Name is required");
            valid=false;
        }else
            mName.setError(null);
        if((email.length() == 0) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Invalid Email");
            valid=false;
        }
        else mEmail.setError(null);
        if((mobileno.length() != 10) || !Patterns.PHONE.matcher(mobileno).matches()){
            mMobileNo.setError("Invalid Phone Number");
            valid=false;
        }
        if(pw.length()>10||pw.length()<4){
            mPassword.setError("Password should be 4 to 10 characters long");
            valid=false;
        }
        else mPassword.setError(null);
        if(id.length()==0){
            natID.setError("Please enter your name");
            valid=false;
        }
        else natID.setError(null);

        if(sprb.isChecked() && imageUri == null){
            valid = false;
            Toast.makeText(this,"Please updoad your license",Toast.LENGTH_SHORT).show();
        }




        return valid;
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
                            .into(license);
                    license.setVisibility(View.VISIBLE);
                }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.register,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.settings: {

            }
            return true;
            default:
                return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null){
            if(userdet != null)
            if(userdet.isSp()){
                Activity_SP.user = userdet;
            }else{
                Activity_renter.user = userdet;
            }
            Toast.makeText(RegisterActivity.this, "Registration Successful",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }


}
