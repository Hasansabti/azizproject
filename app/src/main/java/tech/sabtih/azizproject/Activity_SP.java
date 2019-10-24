package tech.sabtih.azizproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;

import java.util.ArrayList;

import tech.sabtih.azizproject.adapters.RequestsAdapter;
import tech.sabtih.azizproject.listeners.OnRequestAcceptListener;
import tech.sabtih.azizproject.listeners.OnRequestClickListener;
import tech.sabtih.azizproject.models.SPrequest;

public class Activity_SP extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnRequestAcceptListener, OnRequestClickListener {
RecyclerView requests;
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

        requests = findViewById(R.id.sp_requests);

        ArrayList<SPrequest> requestdata = new ArrayList<>();
        requestdata.add(new SPrequest(0,"Aziz","Cirato","Alhasa"));
        requestdata.add(new SPrequest(1,"Ali","Camry","Alhasa"));

        // Set the adapter
        if (requests instanceof RecyclerView) {
            Context context = requests.getContext();


            requests.setLayoutManager(new LinearLayoutManager(context));

            requests.setAdapter(new RequestsAdapter(requestdata, this));
        }
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
            Intent intent = new Intent(this,AddCarActivity.class);
            startActivity(intent);
        } else if (id == R.id.edit_profile) {
            Intent intent = new Intent(this,ActivityProfile.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

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
    public void onRequestClick(SPrequest item) {
        Intent intent = new Intent(this,Activity_SPRequest.class);
        intent.putExtra("car",item.getCarname());
        intent.putExtra("details","Details about "+item.getCarname());
        startActivity(intent);
    }
}
