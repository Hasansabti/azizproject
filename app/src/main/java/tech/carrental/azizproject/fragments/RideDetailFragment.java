package tech.carrental.azizproject.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import tech.carrental.azizproject.Activity_SPRequest;
import tech.carrental.azizproject.R;
import tech.carrental.azizproject.ResultActivity;
import tech.carrental.azizproject.RideDetailActivity;
import tech.carrental.azizproject.RideListActivity;
import tech.carrental.azizproject.dummy.DummyContent;
import tech.carrental.azizproject.models.Renter_Car;
import tech.carrental.azizproject.models.User;

/**
 * A fragment representing a single Ride detail screen.
 * This fragment is either contained in a {@link RideListActivity}
 * in two-pane mode (on tablets) or a {@link RideDetailActivity}
 * on handsets.
 */
public class RideDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Renter_Car mItem;
    private String carname;
    private String rentid;
    private DatabaseReference mDatabase;

    private Button submit;
    private RatingBar rating;
    private EditText review;
    private ProgressBar progress;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RideDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            rentid = getArguments().getString(ARG_ITEM_ID);
            carname = getArguments().getString("carname");
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(carname);
            }
            mDatabase.child("rents").child(rentid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mItem = dataSnapshot.getValue(Renter_Car.class);



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ride_detail, container, false);
        submit = rootView.findViewById(R.id.submit_review);
        rating = rootView.findViewById(R.id.rating);
        review = rootView.findViewById(R.id.review);
        progress = rootView.findViewById(R.id.progress_review);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                mDatabase.child("rents").child(rentid).child("review").setValue(review.getText().toString());
                mDatabase.child("rents").child(rentid).child("rating").setValue(rating.getRating()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progress.setVisibility(View.GONE);
                        Intent intent = new Intent(getActivity(), ResultActivity.class);
                        intent.putExtra("message","Your ride has been evaluated!");
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

            }
        });

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
          // ((TextView) rootView.findViewById(R.id.ride_detail)).setText(mItem.details);
        }

        return rootView;
    }
}
