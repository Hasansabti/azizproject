package tech.carrental.azizproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import tech.carrental.azizproject.dummy.DummyRentercar;
import tech.carrental.azizproject.fragments.RenterSearchItemDetailFragment;
import tech.carrental.azizproject.models.Car;
import tech.carrental.azizproject.models.Renter_Car;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of SearchItems. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RenterSearchItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RenterSearchItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private DatabaseReference mDatabase;
    ArrayList<Car> carDetails;
    private SimpleItemRecyclerViewAdapter adapter;

    public int days = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentersearchitem_list);
        carDetails=new ArrayList<>();

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]


        if (findViewById(R.id.rentersearchitem_list) != null) {

        }

        View recyclerView = findViewById(R.id.rentersearchitem_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new SimpleItemRecyclerViewAdapter(this,carDetails,mTwoPane);

        mDatabase.child("cars").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (carDetails.size() > 0)
                    carDetails.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Car car = ds.getValue(Car.class);
                    carDetails.add(car);


                    Log.d("car", car.getName());
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

        private final RenterSearchItemListActivity mParentActivity;
        private final List<Car> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Car item = (Car) view.getTag();
                RenterSearchItemDetailFragment.mItem = item;
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(RenterSearchItemDetailFragment.ARG_ITEM_ID, ""+item.getUuid());
                    RenterSearchItemDetailFragment fragment = new RenterSearchItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.rentersearchitem_list, fragment)
                            .commit();

                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RenterSearchItemDetailActivity.class);
                    intent.putExtra(RenterSearchItemDetailFragment.ARG_ITEM_ID, ""+item.getUuid());
                    intent.putExtra("start",mParentActivity.getIntent().getStringExtra("start"));
                    intent.putExtra("end",mParentActivity.getIntent().getStringExtra("end"));
                    intent.putExtra("city",mParentActivity.getIntent().getStringExtra("city"));
                    intent.putExtra("deliver",mParentActivity.getIntent().getBooleanArrayExtra("deliver"));

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(RenterSearchItemListActivity parent,
                                      List<Car> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_result_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.car = mValues.get(position);
            holder.name.setText(holder.car.getName() +" "  + holder.car.getModel());
            holder.owner.setText(holder.car.getCompany());
            holder.price.setText(holder.car.getFair()+" SAR");
            holder.type.setText(holder.car.getFuel());

            StorageReference storageRef =
                    FirebaseStorage.getInstance().getReference();
            storageRef.child("images/"+mValues.get(position).getUuid()).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            Glide.with(mParentActivity)
                                    .load(uri)
                                    .into(holder.image);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });


            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            Car car;
            final TextView name;
            final TextView owner;
            final TextView price;
            final TextView type;
            final ImageView image;

            ViewHolder(View view) {
                super(view);
                name =  view.findViewById(R.id.update_name);
                owner =  view.findViewById(R.id.company);
                price =  view.findViewById(R.id.price);
                image = view.findViewById(R.id.car_image);
                type = view.findViewById(R.id.type);

            }
        }
    }
}
