package tech.carrental.azizproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tech.carrental.azizproject.fragments.RideDetailFragment;
import tech.carrental.azizproject.models.Car;
import tech.carrental.azizproject.models.Renter_Car;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Rides. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RideDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RideListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private DatabaseReference mDatabase;

    ArrayList<Renter_Car> rents;
    private static ArrayList<Car> cars;
    private SimpleItemRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_list);
        rents = new ArrayList<>();
        cars = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        if (findViewById(R.id.ride_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.ride_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        mDatabase.child("cars").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (cars.size() > 0)
                    cars.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Car car = ds.getValue(Car.class);
                    cars.add(car);



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
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
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new SimpleItemRecyclerViewAdapter(this, rents, mTwoPane);

        mDatabase.child("rents").orderByChild("requesterid").equalTo(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (rents.size() > 0)
                    rents.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Renter_Car car = ds.getValue(Renter_Car.class);
                    rents.add(car);


                    Log.d("car", car.getCarid());
                }
                if (adapter != null)
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        recyclerView.setAdapter(adapter);


    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final RideListActivity mParentActivity;
        private final List<Renter_Car> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Renter_Car item = (Renter_Car) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(RideDetailFragment.ARG_ITEM_ID, item.getId());
                    RideDetailFragment fragment = new RideDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.ride_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RideDetailActivity.class);
                    intent.putExtra(RideDetailFragment.ARG_ITEM_ID, item.getId());
                    for(Car car : cars){
                        if(car.getUuid().equalsIgnoreCase(item.getCarid())){

                            intent.putExtra("carname", car.getName());

                            break;
                        }



                    }


                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(RideListActivity parent,
                                      List<Renter_Car> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.previous_rents_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            for(Car car : cars){
                if(car.getUuid().equalsIgnoreCase(mValues.get(position).getCarid())){

                    holder.carname.setText(car.getName());

                    break;
                }



            }
            holder.price.setText(mValues.get(position).getPrice()+" SR");

            holder.date.setText("Status: "+mValues.get(position).getStatus());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView carname;
            final TextView date;
            final TextView price;

            ViewHolder(View view) {
                super(view);
                carname = (TextView) view.findViewById(R.id.carname);
                date = (TextView) view.findViewById(R.id.date);
                price = (TextView) view.findViewById(R.id.date_end);
            }
        }
    }
}
