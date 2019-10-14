package tech.sabtih.azizproject.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import tech.sabtih.azizproject.R;
import tech.sabtih.azizproject.dummy.DummyContent.DummyItem;
import tech.sabtih.azizproject.listeners.OnRequestAcceptListener;
import tech.sabtih.azizproject.models.SPrequest;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySPrequestRecyclerViewAdapter extends RecyclerView.Adapter<MySPrequestRecyclerViewAdapter.ViewHolder> {

    private final List<SPrequest> mValues;
    private final OnRequestAcceptListener mListener;

    public MySPrequestRecyclerViewAdapter(List<SPrequest> items, OnRequestAcceptListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_provider_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.car.setText(mValues.get(position).getName() +"-"+ mValues.get(position).getCarname());


        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onRequestAccept(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final TextView car;
        public SPrequest mItem;
        public Button accept;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            car = (TextView) view.findViewById(R.id.carname);
            accept = view.findViewById(R.id.accept);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + car.getText() + "'";
        }
    }
}
