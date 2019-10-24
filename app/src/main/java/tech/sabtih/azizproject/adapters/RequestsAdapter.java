package tech.sabtih.azizproject.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import tech.sabtih.azizproject.R;

import tech.sabtih.azizproject.listeners.OnRequestClickListener;
import tech.sabtih.azizproject.models.SPrequest;

import java.util.List;


public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {

    private final List<SPrequest> mValues;
    private final OnRequestClickListener mListener;

    public RequestsAdapter(List<SPrequest> items, OnRequestClickListener listener) {
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
        holder.car.setText(mValues.get(position).getName() +"-"+ mValues.get(position).getCarname());


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
        public SPrequest mItem;
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
