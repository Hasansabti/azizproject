package tech.carrental.azizproject.Owner;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import tech.carrental.azizproject.R;

/**
 * Created by User on 4/9/2017.
 */

public class Fragment2 extends Fragment {
    private static final String TAG = "Fragment1";

    private Button CTypeNext;

    //Details
    public static EditText Seats;
    public static EditText Ctype;
    public static EditText City;

    //Var
    public  static String seats;
    public  static String fType;
    public  static String city;

    private FragmentActivity fa;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment2_layout, container, false);
        fa=((FragmentActivity)getActivity());


        CTypeNext = (Button) view.findViewById(R.id.CartypeNext);
        Seats = (EditText) view.findViewById(R.id.seats);
        Ctype = (EditText) view.findViewById(R.id.type);
        City = (EditText) view.findViewById(R.id.city_car);


        Seats.setText(Fragment1.getSeats());
        Ctype.setText(Fragment1.getFuel());


        CTypeNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()){
                    JSONObject details= fa.details;
                    try {
                        details.put("city",city);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ((FragmentActivity)getActivity()).setViewPager(2);
                }
            }
        });


        return view;
    }
    private boolean validate(){
        boolean valid = true ;
        seats = Seats.getText().toString().trim();
        fType = Ctype.getText().toString().trim();
        city = City.getText().toString().trim();

        if((seats.length() == 0)){
            Seats.setError("Invalid Seats");
            valid=false;
        }
        else Seats.setError(null);

        if((city.length() == 0)){
            City.setError("Invalid City");
            valid=false;
        }
        else City.setError(null);

        if((fType.length() == 0) || (!(fType.toUpperCase().equals("91") || fType.toUpperCase().equals("95")))){
            Ctype.setError("Invalid Fuel Type");
            valid=false;
        }
        else Ctype.setError(null);
        return valid;
    }
}
