package tech.carrental.azizproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tech.carrental.azizproject.models.User;

public class Activity_SPRequest extends AppCompatActivity {
TextView details;
TextView userdetails;
Button accept;
    Button reject;
User requester;
String rentid;
ProgressBar progress;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprequest);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        details = findViewById(R.id.details);
        userdetails = findViewById(R.id.user_details);
        accept = findViewById(R.id.acceptbtn);
        reject = findViewById(R.id.rejectbtn);
        progress = findViewById(R.id.progress);
        setTitle(getIntent().getStringExtra("car"));
        details.setText(getIntent().getStringExtra("details"));
        rentid = getIntent().getStringExtra("rentid");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(getIntent().getStringExtra("requesterid")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requester = dataSnapshot.getValue(User.class);
                userdetails.setText("Name: " + requester.getName() +"\nEmail: " + requester.getEmail() +"\nPhone: " + requester.getMobile());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                mDatabase.child("rents").child(rentid).child("status").setValue("accepted").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progress.setVisibility(View.GONE);
                        Intent intent = new Intent(Activity_SPRequest.this, ResultActivity.class);
                        intent.putExtra("message","The request has been accepted");
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                mDatabase.child("rents").child(rentid).child("status").setValue("declined").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progress.setVisibility(View.GONE);
                        Intent intent = new Intent(Activity_SPRequest.this, ResultActivity.class);
                        intent.putExtra("message","The request has been Denied");
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });


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
}
