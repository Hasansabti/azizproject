package tech.carrental.azizproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tech.carrental.azizproject.models.User;

public class ActivityProfile extends AppCompatActivity {
    TextView mName;
    TextView mPassword;
    TextView mPhone;
    ProgressBar progressBar;
    Button submit;
    User userdet;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        if (Activity_renter.user != null) {
            userdet = Activity_renter.user;
        } else if (Activity_SP.user != null) {
            userdet = Activity_SP.user;
        }

        mName = findViewById(R.id.update_name);
        mPassword = findViewById(R.id.update_pass);
        mPhone = findViewById(R.id.update_phone);
        mName.setText(userdet.getName());
        mPhone.setText(userdet.getMobile());
        progressBar = findViewById(R.id.update_progress);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String uid = FirebaseAuth.getInstance().getUid();

        submit = findViewById(R.id.update_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                submit.setEnabled(false);

                if(isValidate()) {
                    FirebaseAuth.getInstance().getCurrentUser().updatePassword(mPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            userdet.setName(mName.getText().toString());
                            userdet.setMobile(mPhone.getText().toString());
                            mPassword.setText("");
                            Toast.makeText(ActivityProfile.this,"Password have been changes successfully",Toast.LENGTH_SHORT).show();
                            mDatabase.child("users").child(uid).setValue(userdet).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //  if(FragmentActivity.fa != null){
                                    //      FragmentActivity.fa.finish();
                                    //  }
                                    //    finish();
                                    progressBar.setVisibility(View.GONE);
                                    submit.setEnabled(true);
                                    Toast.makeText(ActivityProfile.this,"Profile details updated!",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    });

                }
            }
        });



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
        if(user_pass.length()>10||user_pass.length()<4){
            mPassword.setError("Password should be 4 to 10 characters long");
            valid=false;
        }

        else mPassword.setError(null);
        if(phone.length()>10||user_pass.length()<4){
            mPassword.setError("Password should be 4 to 10 characters long");
            valid=false;
        }
        if((phone.length() != 10) || !Patterns.PHONE.matcher(phone).matches()){
            mPhone.setError("Invalid Phone Number");
            valid=false;
        }
        return valid;
    }
}
