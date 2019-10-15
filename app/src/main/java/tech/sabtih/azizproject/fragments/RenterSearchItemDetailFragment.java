package tech.sabtih.azizproject.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import tech.sabtih.azizproject.R;
import tech.sabtih.azizproject.RenterSearchItemDetailActivity;
import tech.sabtih.azizproject.RenterSearchItemListActivity;
import tech.sabtih.azizproject.ResultActivity;
import tech.sabtih.azizproject.dummy.DummyRentercar;
import tech.sabtih.azizproject.models.Renter_Car;

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

    Button bookbtn;
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Renter_Car mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RenterSearchItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyRentercar.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

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
        bookbtn = rootView.findViewById(R.id.acceptbtn);
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.details)).setText(mItem.getDetails());
        }

        bookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ResultActivity.class);
                intent.putExtra("message","Your request has been placed");
                startActivity(intent);
                getActivity().finish();
            }
        });

        return rootView;
    }
}
