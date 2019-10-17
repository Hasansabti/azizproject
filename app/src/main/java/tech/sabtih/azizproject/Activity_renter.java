package tech.sabtih.azizproject;

import android.app.DatePickerDialog;
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

import android.view.Menu;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Activity_renter extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //date
    final Calendar myCalendar = Calendar.getInstance();
    TextView edittextstart;
    TextView edittextend;
    Button submit;
    Calendar calendar;
    Date startDate;
    Date endDate;
    SimpleDateFormat sdf;
    ImageView cityimage;

    View location;
    View city;


    CheckBox gps;
    CheckBox deliver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renter);
        sdf=new SimpleDateFormat("dd/MM/yy",Locale.getDefault());
        calendar=Calendar.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        location = findViewById(R.id.locationll);
        cityimage = findViewById(R.id.cityimg);
        city  =findViewById(R.id.city);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        gps = findViewById(R.id.gps);

        gps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    location.setVisibility(View.VISIBLE);
                    city.setVisibility(View.GONE);
                    cityimage.setVisibility(View.GONE);
                }else{
                    location.setVisibility(View.GONE);
                    city.setVisibility(View.VISIBLE);
                    cityimage.setVisibility(View.VISIBLE);
                }
            }
        });

        edittextstart = findViewById(R.id.start);
        edittextend = findViewById(R.id.end);
        submit = findViewById(R.id.search);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edittextend.getText().toString().isEmpty() || edittextstart.getText().toString().isEmpty()){
                    Toast.makeText(Activity_renter.this,"Missing info",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(Activity_renter.this,RenterSearchItemListActivity.class);
                startActivity(intent);

            }
        });


        edittextstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    final DatePickerDialog pickerDialog=new DatePickerDialog(Activity_renter.this);
                    pickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendar.set(year,month,dayOfMonth);
                            startDate = calendar.getTime();
                            if(startDate.before(new Date())){
                                makeToast("Invalid start date");
                            }
                            else{
                                edittextstart.setText(sdf.format(startDate));
                                pickerDialog.dismiss();
                            }
                        }
                    });
                    pickerDialog.show();
                }
            }
        });
        edittextend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    final DatePickerDialog pickerDialog=new DatePickerDialog(Activity_renter.this);
                    pickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendar.set(year,month,dayOfMonth);
                            endDate = calendar.getTime();
                            if(!endDate.after(new Date())){
                                makeToast("Invalid end date");
                            }else{
                                edittextend.setText(sdf.format(endDate));
                                pickerDialog.dismiss();
                            }
                        }
                    });
                    pickerDialog.show();
                }
            }
        });


    }
    private void makeToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    private void updateLabel(EditText edittext) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
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
        getMenuInflater().inflate(R.menu.activity_renter, menu);
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
            // Handle the camera action
        } else if (id == R.id.edit_profile) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.logout) {
            finish();

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
