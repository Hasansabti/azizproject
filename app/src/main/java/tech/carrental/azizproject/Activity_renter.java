package tech.carrental.azizproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

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

import android.view.Menu;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tech.carrental.azizproject.models.User;
import tech.carrental.azizproject.ui.login.LoginActivity;

public class Activity_renter extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //date
    final Calendar myCalendar = Calendar.getInstance();
    TextView edittextstart;
    TextView edittextend;
    Button submit;
    ProgressBar pb_addr;

    Calendar calendar;
    Date startDate;
    Date endDate;
    SimpleDateFormat sdf;
    ImageView cityimage;

    View location;
    Spinner city;
    TextView tvlocation;

    double lng;
    double lat;
    public static String address;
    String citytxt;
    String pincode;

    CheckBox gps;
    CheckBox deliver;

    public static User user;

    private DatabaseReference mDatabase;
    
    
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        sdf=new SimpleDateFormat("dd/MM/yy",Locale.getDefault());
        calendar=Calendar.getInstance();



        location = findViewById(R.id.locationll);
        cityimage = findViewById(R.id.cityimg);
        pb_addr = findViewById(R.id.pb_address);
        city  =findViewById(R.id.renter_phone);
        tvlocation = findViewById(R.id.input_license);
        gps = findViewById(R.id.gps);
        deliver = findViewById(R.id.deliver);
        edittextstart = findViewById(R.id.start);
        edittextend = findViewById(R.id.end);
        submit = findViewById(R.id.search);

        //Drawer
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

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gps.isChecked()){
                    //get gps location
                    startActivityForResult(new Intent(getApplicationContext(), MapsActivity.class),10);

                }else{
                    //get city
                }
            }
        });


//Cant move until fill the fields
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edittextend.getText().toString().equalsIgnoreCase("End Date") || edittextstart.getText().toString().equalsIgnoreCase("Start Date")){
                    Toast.makeText(Activity_renter.this,"Missing info",Toast.LENGTH_SHORT).show();
                    if(edittextend.getText().toString().equalsIgnoreCase("End Date"))
                    edittextend.setError("Required");
                    if(edittextstart.getText().toString().equalsIgnoreCase("Start Date"))
                        edittextstart.setError("Required");


                    return;

                }
                if(gps.isChecked()){
                    if(tvlocation.getText().toString().equalsIgnoreCase("Your location")) {
                        Toast.makeText(Activity_renter.this, "Missing location", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else{
                    if(city.getSelectedItemPosition() == -1) {
                        Toast.makeText(Activity_renter.this,"Missing city",Toast.LENGTH_SHORT).show();
                        return;

                    }
                }
                edittextstart.setError(null);
                edittextend.setError(null);
                Intent intent = new Intent(Activity_renter.this,RenterSearchItemListActivity.class);
                intent.putExtra("start",startDate.getTime());
                intent.putExtra("end",endDate.getTime());
                if(gps.isChecked())
                    intent.putExtra("city",citytxt);
                else
                    intent.putExtra("city",city.getSelectedItem().toString());

                intent.putExtra("deliver",deliver.isChecked());
                startActivity(intent);

            }
        });

//Start date
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

        //End date
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
        mDatabase = FirebaseDatabase.getInstance().getReference();

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
    }
    private void makeToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 1) {
            lng = data.getDoubleExtra("longitude", 0);
            lat = data.getDoubleExtra("latitude", 0);
            Log.d("cars", "onActivityResult: result recieved from map");
            byte arr[] = data.getByteArrayExtra("byteArray");
            Bitmap bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);
            // mapSnapshot.setImageBitmap(bitmap);
            LoadAddressAsyncTask task = new LoadAddressAsyncTask(getApplicationContext());
            task.execute();
        }
    }

    public class LoadAddressAsyncTask extends AsyncTask<Void, Void, Void> {

        Context context;
        Geocoder geocoder;
        Address myLoc;

        public LoadAddressAsyncTask(Context context) {
            this.context = context;
            geocoder=new Geocoder(context);
            Log.d("cars", "LoadAddressAsyncTask: "+ Geocoder.isPresent());
        }

        @Override
        protected void onPreExecute() {
            pb_addr.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Log.d("cars", "LoadAddressAsyncTask: "+ lat+" "+lng);
                List<Address> al= geocoder.getFromLocation(lat,lng,5);
                myLoc=al.get(0);
                for(Address a:al){
                    Log.d("cars", "doInBackground: "+a.toString());
                }
                address=myLoc.getAddressLine(0);
                citytxt=myLoc.getLocality();
                pincode=myLoc.getPostalCode();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pb_addr.setVisibility(View.GONE);
            if(address!=null) {
                tvlocation.setText(address);
               // mCity.setText(city + "," + pincode);
               // mapSnapshot.setVisibility(View.VISIBLE);
            }
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

        if (id == R.id.profile) {

            Intent intent = new Intent(this,ActivityProfile.class);
            startActivity(intent);

            // Handle the camera action
        } else if (id == R.id.search) {
            Intent intent = new Intent(this,RenterSearchItemListActivity.class);
            intent.putExtra("start","");
            intent.putExtra("end","");
            startActivity(intent);
        } else if (id == R.id.renter_history) {
            startActivity(new Intent(this, RideListActivity.class));


        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.logout) {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
