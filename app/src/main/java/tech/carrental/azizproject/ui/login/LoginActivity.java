package tech.carrental.azizproject.ui.login;

import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import tech.carrental.azizproject.Activity_SP;
import tech.carrental.azizproject.Activity_renter;
import tech.carrental.azizproject.R;
import tech.carrental.azizproject.models.User;

public class LoginActivity extends AppCompatActivity {


    private EditText id;
    private EditText password;
    private TextView info;
    private Button login;
    private Button register;
    private int counter = 5;
    JSONObject loginDetails;
    public String in_password ;
    public static String email;
    private ProgressBar progress;
    User userdet = null;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    String TAG = "Login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_login);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        id = (EditText)findViewById(R.id.Email);
        password = (EditText)findViewById(R.id.Password);
        progress = findViewById(R.id.progress_login);

        login = (Button)findViewById(R.id.btnLogin);
        register = (Button)findViewById(R.id.regB);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(LoginActivity.this, Activity_renter.class));


                if(validate()){
                    progress.setVisibility(View.VISIBLE);
                    login.setEnabled(false);
                    loginDetails=new JSONObject();
                    email = id.getText().toString().trim();
                    final String pw = password.getText().toString().trim();
                    try {
                        loginDetails.put("email", email);
                        //loginDetails.put("password", pw);

                        mAuth.signInWithEmailAndPassword(email, pw)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progress.setVisibility(View.INVISIBLE);
                                        login.setEnabled(true);
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            updateUI(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                                            Toast.makeText(LoginActivity.this, "Login failed.",
                                                    Toast.LENGTH_SHORT).show();
                                            updateUI(null);
                                        }

                                        // ...
                                    }
                                });
                    }

                    catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReg();
            }
        });

    }

    private void updateUI(final FirebaseUser user) {
        if(user != null){

            mDatabase.child("users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userdet = dataSnapshot.getValue(User.class);

                    if(userdet.isSp()){
                        Activity_SP.user = userdet;
                        startActivity(new Intent(LoginActivity.this, Activity_SP.class));

                    }else{
                        Activity_renter.user = userdet;
                        startActivity(new Intent(LoginActivity.this, Activity_renter.class));

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });






            Toast.makeText(LoginActivity.this, "Login Successful",
                    Toast.LENGTH_SHORT).show();
            finish();
        }else{

        }
    }

    public static String getID(){
        return email; }
    public static void setID(String id){
        email=id;
    }

    private void openReg(){
        Intent newA = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(newA);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mAuth.getCurrentUser() != null){
            login.setEnabled(false);
            progress.setVisibility(View.VISIBLE);
            if(userdet == null){
                mDatabase.child("users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userdet = dataSnapshot.getValue(User.class);

                        if(userdet.isSp()){
                            Activity_SP.user = userdet;
                            startActivity(new Intent(LoginActivity.this, Activity_SP.class));

                        }else{
                            Activity_renter.user = userdet;
                            startActivity(new Intent(LoginActivity.this, Activity_renter.class));

                        }
                        progress.setVisibility(View.VISIBLE);
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        login.setEnabled(true);
                        progress.setVisibility(View.GONE);

                    }
                });
            }else{
                if(userdet.isSp()){
                    Activity_SP.user = userdet;
                    startActivity(new Intent(LoginActivity.this, Activity_SP.class));

                }else{
                    Activity_renter.user = userdet;
                    startActivity(new Intent(LoginActivity.this, Activity_renter.class));

                }

                finish();
            }



        }

    }

    private boolean validate(){
        boolean valid = true ;
        String user_id = id.getText().toString().trim();
        String user_pass = password.getText().toString().trim();

        if((user_id.length() == 0) || !Patterns.EMAIL_ADDRESS.matcher(user_id).matches()){
            id.setError("Invalid Email");
            valid=false;
        }
        else id.setError(null);
        if(user_pass.length()>10||user_pass.length()<4){
            password.setError("Password should be 4 to 10 characters long");
            valid=false;
        }
        return valid;
    }

    /*private boolean check(String rec, String input){
        boolean valid = false;
        if(rec.equals(input))
            valid = true;
        else
            valid = false;
        return valid;
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.login_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
          //  case R.id.ipadress: {

           // }
            //return true;
            default:
                return false;
        }
    }
}
