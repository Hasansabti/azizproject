package tech.carrental.azizproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import tech.carrental.azizproject.Owner.FragmentActivity;
import tech.carrental.azizproject.adapters.RequestsAdapter;
import tech.carrental.azizproject.listeners.OnRequestAcceptListener;
import tech.carrental.azizproject.listeners.OnRequestClickListener;
import tech.carrental.azizproject.models.Car;
import tech.carrental.azizproject.models.Renter_Car;
import tech.carrental.azizproject.models.SPrequest;
import tech.carrental.azizproject.models.User;
import tech.carrental.azizproject.ui.login.LoginActivity;

public class Activity_SP extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnRequestAcceptListener, OnRequestClickListener {
    public static User user;
    RecyclerView requests;
    TextView norequests;
    private DatabaseReference mDatabase;
    private RequestsAdapter adapter;
    private ArrayList<Renter_Car> requestdata;
    public static ArrayList<Car> cars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__sp);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        final TextView show_name = hView.findViewById(R.id.show_name);
        final TextView show_email = hView.findViewById(R.id.show_email);
        requests = findViewById(R.id.sp_requests);


        norequests = findViewById(R.id.norequests);

         requestdata = new ArrayList<>();
         cars = new ArrayList<>();
       // requestdata.add(new SPrequest(0,"Aziz","Cirato","Alhasa"));
       // requestdata.add(new SPrequest(1,"Ali","Camry","Alhasa"));

        // Set the adapter
        if (requests instanceof RecyclerView) {
            Context context = requests.getContext();
            adapter = new RequestsAdapter(requestdata, this);


            requests.setLayoutManager(new LinearLayoutManager(context));
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requests.getContext(),
                    ((LinearLayoutManager)requests.getLayoutManager()).getOrientation());
            requests.addItemDecoration(dividerItemDecoration);
            requests.setAdapter(adapter);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("rents").orderByChild("ownerid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (requestdata.size() > 0)
                    requestdata.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Renter_Car car = ds.getValue(Renter_Car.class);
                    requestdata.add(car);


                    Log.d("car", car.getCarid());
                }
                if(requestdata.size() == 0){
                    norequests.setVisibility(View.VISIBLE);
                }else{
                    norequests.setVisibility(View.INVISIBLE);
                }
                if (adapter != null)
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        mDatabase.child("users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                show_name.setText(user.getName());
                show_email.setText(user.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.child("cars").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (cars.size() > 0)
                    cars.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Car car = ds.getValue(Car.class);
                    cars.add(car);



                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__s, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.add_car) {
          //  Intent intent = new Intent(this,AddCarActivity.class);
          //  startActivity(intent);


            startActivity(new Intent(Activity_SP.this, FragmentActivity.class));


        } else if (id == R.id.edit_profile) {
            Intent intent = new Intent(this,ActivityProfile.class);
            startActivity(intent);

        } else if (id == R.id.mycars) {

            Intent intent = new Intent(this,SPcarListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestAccept(SPrequest item) {
        item.setAccepted(true);
    }

    @Override
    public void onRequestClick(Renter_Car item) {
        Intent intent = new Intent(this,Activity_SPRequest.class);

        for(Car car : cars){
            if(car.getUuid().equalsIgnoreCase(item.getCarid())){
                intent.putExtra("car",car.getName());
                intent.putExtra("rentid",item.getId());
                intent.putExtra("carid",item.getCarid());
                intent.putExtra("requesterid",item.getRequesterid());
                intent.putExtra("details","Start: "+ item.getStart() +" - End: "+item.getEnd() +"\nLocation: " + item.getLocation() +"\nPrice: " + item.getPrice() +"\nDeliver:"+(item.isDeliver() ? "yes" : "no")+"\nStatus: " + item.getStatus()  );
                startActivity(intent);
                return;
            }
        }
        Toast.makeText(this,"Error: the car is not available",Toast.LENGTH_SHORT).show();


    }
}
