package tech.carrental.azizproject.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.UUID;

import tech.carrental.azizproject.R;
import tech.carrental.azizproject.RenterSearchItemDetailActivity;
import tech.carrental.azizproject.RenterSearchItemListActivity;
import tech.carrental.azizproject.ResultActivity;
import tech.carrental.azizproject.models.Car;
import tech.carrental.azizproject.models.Renter_Car;

/**
 * A fragment representing a single RenterSearchItem detail screen.
 * This fragment is either contained in a {@link RenterSearchItemListActivity}
 * in two-pane mode (on tablets) or a {@link RenterSearchItemDetailActivity}
 * on handsets.
 */
public class RenterSearchItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    ProgressBar pb_addr;
    TextView chooseYourLocation;
    ImageView mapSnapshot;
    Button confirmBtn;
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

    String start;
    String end;


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
            start = getArguments().getString("start");
            end = getArguments().getString("end");
            city = getArguments().getString("city");
            deliver = getArguments().getBoolean("deliver");
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
        pb_addr = rootView.findViewById(R.id.pb_address);
        mLocation = rootView.findViewById(R.id.location);
        distanceTV = rootView.findViewById(R.id.startDate);
        fareTV = rootView.findViewById(R.id.endDate);
        driverCostTV = rootView.findViewById(R.id.input_city);
        totalTV = rootView.findViewById(R.id.total);
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
}
