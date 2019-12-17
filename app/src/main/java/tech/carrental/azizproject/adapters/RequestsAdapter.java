package tech.carrental.azizproject.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import tech.carrental.azizproject.Activity_SP;
import tech.carrental.azizproject.R;

import tech.carrental.azizproject.listeners.OnRequestClickListener;
import tech.carrental.azizproject.models.Car;
import tech.carrental.azizproject.models.Renter_Car;
import tech.carrental.azizproject.models.SPrequest;

import java.util.List;


public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {

    private final List<Renter_Car> mValues;
    private final OnRequestClickListener mListener;

    public RequestsAdapter(List<Renter_Car> items, OnRequestClickListener listener) {
        mValues = items;
        mListener = listener;
    }

    //ثابته في الاندرويد

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_provider_item, parent, false);
        return new ViewHolder(view);
    }
//تعبي التكستفيو
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        for(Car car : Activity_SP.cars) {
            if(car.getUuid().equalsIgnoreCase(mValues.get(position).getCarid())) {
                holder.car.setText(car.getName()+": "+ mValues.get(position).getStart() + " - " + mValues.get(position).getEnd());
            }
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   take to sp request detail
                    mListener.onRequestClick(holder.mItem);
             //   }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    //create textview, buttons
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final TextView car;
        Button button;
        public Renter_Car mItem;
        //public Button accept;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            button = view.findViewById(R.id.button2);

            car = (TextView) view.findViewById(R.id.carname);
           // accept = view.findViewById(R.id.accept);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + car.getText() + "'";
        }
    }
}
