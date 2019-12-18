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
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tech.carrental.azizproject.models.User;

public class Activity_SPRequest extends AppCompatActivity {

Button accept;
    Button reject;
User requester;
String rentid;
String status;

TextView mName;
TextView mRID;
TextView mRPhone;
TextView mStart;
TextView mEnd;
TextView mEval;
TextView mPrice;

TextView mStatus;

ProgressBar progress;
    private DatabaseReference mDatabase;
    private long start;
    private long end;
    private double price;
    float rating;

    RatingBar rb;
    Button ratebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprequest);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }




        mName = findViewById(R.id.renter_name);
        mRID = findViewById(R.id.renter_id);
        mRPhone = findViewById(R.id.renter_phone);
        mEval = findViewById(R.id.renter_eval);
        mStart = findViewById(R.id.date_start);
        mEnd = findViewById(R.id.date_end);
        mPrice = findViewById(R.id.price);
        mStatus = findViewById(R.id.status);
        rb = findViewById(R.id.rating);
        ratebtn = findViewById(R.id.ratebtn);



        accept = findViewById(R.id.acceptbtn);
        reject = findViewById(R.id.rejectbtn);
        progress = findViewById(R.id.progress);
        setTitle(getIntent().getStringExtra("car"));
       // details.setText(getIntent().getStringExtra("details"));
        price = getIntent().getDoubleExtra("price",0);
        start = getIntent().getLongExtra("start",0);
        end = getIntent().getLongExtra("end",0);
        status = getIntent().getStringExtra("status");
        rating = getIntent().getFloatExtra("rating",-1);
        if(rating != -1){
            rb.setRating(rating);
        }

        if (status.equalsIgnoreCase("declined") || status.equalsIgnoreCase("accepted")){
            if(status.equalsIgnoreCase("accepted")){
                ratebtn.setVisibility(View.VISIBLE);
                rb.setVisibility(View.VISIBLE);
            }else{
                mStatus.setVisibility(View.VISIBLE);
                ratebtn.setVisibility(View.GONE);
                rb.setVisibility(View.GONE);
            }
            accept.setVisibility(View.GONE);
            reject.setVisibility(View.GONE);
            mStatus.setText(status);

            if(rating >-1){
                rb.setEnabled(false);
                ratebtn.setVisibility(View.GONE);
            }

        }else{
            ratebtn.setVisibility(View.GONE);
            rb.setVisibility(View.GONE);
            accept.setVisibility(View.VISIBLE);
            reject.setVisibility(View.VISIBLE);
            mStatus.setVisibility(View.GONE);
        }

        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                mDatabase.child("rents").child(rentid).child("sprate").setValue(rb.getRating()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progress.setVisibility(View.GONE);
                        Intent intent = new Intent(Activity_SPRequest.this, ResultActivity.class);
                        intent.putExtra("message","User has been rated!");
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });


        rentid = getIntent().getStringExtra("rentid");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(getIntent().getStringExtra("requesterid")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requester = dataSnapshot.getValue(User.class);
               // userdetails.setText("Name: " + requester.getName() +"\nEmail: " + requester.getEmail() +"\nPhone: " + requester.getMobile());
                mName.setText(requester.getName());
                mRID.setText(""+requester.getNatid());
                mRPhone.setText(""+requester.getMobile());
                mEval.setText(""+requester.getEvaluation());

                Date startdt = new Date();
                startdt.setTime(start);
                Date enddt = new Date();
                enddt.setTime(end);
                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

                mStart.setText(sdf.format(startdt));
                mEnd.setText(sdf.format(enddt));
                mPrice.setText(""+price);
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
