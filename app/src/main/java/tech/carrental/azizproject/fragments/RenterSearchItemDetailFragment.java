package tech.carrental.azizproject.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.UUID;

import tech.carrental.azizproject.R;
import tech.carrental.azizproject.RenterSearchItemDetailActivity;
import tech.carrental.azizproject.RenterSearchItemListActivity;
import tech.carrental.azizproject.ResultActivity;
import tech.carrental.azizproject.Utils.MapUtils;
import tech.carrental.azizproject.models.Car;
import tech.carrental.azizproject.models.Renter_Car;

/**
 * A fragment representing a single RenterSearchItem detail screen.
 * This fragment is either contained in a {@link RenterSearchItemListActivity}
 * in two-pane mode (on tablets) or a {@link RenterSearchItemDetailActivity}
 * on handsets.
 */
public class RenterSearchItemDetailFragment extends Fragment  implements OnMapReadyCallback {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    ProgressBar pb_addr;

    private GoogleMap mMap;


    TextView mLocation;
    TextView distanceTV;
    TextView fareTV;
    TextView driverCostTV;
    TextView totalTV;
    String address;
    String city;
    String pincode;
    Button bookbtn;

    boolean deliver;

    long start;
    long end;
    String latlang = "18.51,73.85";

    private DatabaseReference mDatabase;
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    public static Car mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RenterSearchItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (getArguments().containsKey(ARG_ITEM_ID)) {



            start = getArguments().getLong("start");
            end = getArguments().getLong("end");
            city = getArguments().getString("city");
            latlang = getArguments().getString("latlong");
            deliver = getArguments().getBoolean("deliver");

            Log.d("Start",""+start);
            Log.d("End",""+end);

            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //  mItem = DummyRentercar.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rentersearchitem_detail, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mymap);
        mapFragment.getMapAsync(this);
        pb_addr = rootView.findViewById(R.id.pb_address);
        mLocation = rootView.findViewById(R.id.renter_phone);
        distanceTV = rootView.findViewById(R.id.renter_name);
        fareTV = rootView.findViewById(R.id.renter_id);
        driverCostTV = rootView.findViewById(R.id.renter_phone);
        totalTV = rootView.findViewById(R.id.date_start);
        bookbtn = rootView.findViewById(R.id.acceptbtn);
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            // ((TextView) rootView.findViewById(R.id.owner)).setText(mItem.getDetails());
            distanceTV.setText("" + mItem.getDays());
            fareTV.setText("" + mItem.getFair());
            totalTV.setText("" + Integer.parseInt(mItem.getFair()) * mItem.getDays());

        }

        bookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Renter_Car rent = new Renter_Car(UUID.randomUUID().toString(), mItem.getUuid(), FirebaseAuth.getInstance().getUid(), mItem.getUserid(), start, end, Integer.parseInt(mItem.getFair()) * mItem.getDays(), city, deliver, "waiting");

                mDatabase.child("rents").child(rent.getId()).setValue(rent).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(getContext(), ResultActivity.class);
                        intent.putExtra("message", "Your request has been placed");
                        startActivity(intent);
                        getActivity().finish();
                        //finish();

                    }
                });
            }
        });

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double lat = Double.parseDouble( latlang.split(",")[0]);
        double lon = Double.parseDouble( latlang.split(",")[1]);

        LatLng pune = new LatLng(lat, lon);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pune, MapUtils.STDZOOM));
        //enableMyLocation(true);
    }
}
