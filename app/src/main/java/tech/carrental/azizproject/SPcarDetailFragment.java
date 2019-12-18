package tech.carrental.azizproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import tech.carrental.azizproject.models.Car;

/**
 * A fragment representing a single SPcar detail screen.
 * This fragment is either contained in a {@link SPcarListActivity}
 * in two-pane mode (on tablets) or a {@link SPcarDetailActivity}
 * on handsets.
 */
public class SPcarDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    TextView mModel;
    TextView mFF;
    TextView mSeats;
    TextView mLocation;
    TextView mPrice;
    ImageView mImage;
    Button deletebtn;
    ProgressBar prog;


    /**
     * The dummy content this fragment is presenting.
     */
    private Car mItem;
    private String carid;

    private String title = "";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SPcarDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            title = getArguments().getString("carname");
            carid = getArguments().getString("carid");
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
         //   mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            for(Car car : Activity_SP.cars){
                if(carid.equalsIgnoreCase(car.getUuid())){
                    mItem = car;
                    break;
                }
            }

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(title);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.spcar_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            mModel = rootView.findViewById(R.id.renter_name);
            mFF = rootView.findViewById(R.id.renter_id);
            mLocation = rootView.findViewById(R.id.renter_phone);
            mSeats = rootView.findViewById(R.id.date_start);
            mPrice = rootView.findViewById(R.id.date_end);
            mImage = rootView.findViewById(R.id.carimg);
            deletebtn = rootView.findViewById(R.id.delete_car);
            prog = rootView.findViewById(R.id.delete_prog);

            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prog.setVisibility(View.VISIBLE);
                    FirebaseDatabase.getInstance().getReference().child("cars").child(mItem.getUuid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            prog.setVisibility(View.GONE);
                            Intent intent = new Intent(getActivity(), ResultActivity.class);
                            intent.putExtra("message","Car " + mItem.getName() +" has been deleted!");
                            startActivity(intent);
                            getActivity().finish();

                        }
                    });
                }
            });

            mModel.setText(""+mItem.getModel());
            mFF.setText(""+mItem.getFuel());
            mLocation.setText(mItem.getCity());
            mSeats.setText(""+mItem.getSeats());
            mPrice.setText(""+mItem.getFair());


            StorageReference storageRef =
                    FirebaseStorage.getInstance().getReference();
            storageRef.child("images/"+mItem.getUuid()).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            Glide.with(getActivity())
                                    .load(uri)
                                    .into(mImage);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });


        }

        return rootView;
    }
}
