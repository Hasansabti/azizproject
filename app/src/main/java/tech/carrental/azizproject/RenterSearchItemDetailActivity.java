package tech.carrental.azizproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.view.MenuItem;

import tech.carrental.azizproject.fragments.RenterSearchItemDetailFragment;

/**
 * An activity representing a single RenterSearchItem detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RenterSearchItemListActivity}.
 */
public class RenterSearchItemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentersearchitem_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);



        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(RenterSearchItemDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(RenterSearchItemDetailFragment.ARG_ITEM_ID));

            arguments.putString("start",
                    getIntent().getStringExtra("start"));

            arguments.putString("end",
                    getIntent().getStringExtra("end"));


            RenterSearchItemDetailFragment fragment = new RenterSearchItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rentersearchitem_list, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            navigateUpTo(new Intent(this, RenterSearchItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
