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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


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

                Date start = new Date();
                start.setTime((long) mValues.get(position).getStart());
                Date end = new Date();
                end.setTime((long) mValues.get(position).getEnd());
                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yy", Locale.getDefault());



                holder.car.setText(car.getName()+": "+ sdf.format(start) + " - " + sdf.format(end));
            break;
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
           // button = view.findViewById(R.id.button2);

            car = (TextView) view.findViewById(R.id.carname);
           // accept = view.findViewById(R.id.accept);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + car.getText() + "'";
        }
    }
}
