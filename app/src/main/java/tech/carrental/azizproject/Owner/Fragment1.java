package tech.carrental.azizproject.Owner;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import tech.carrental.azizproject.R;

/**
 * Created by User on 4/9/2017.
 */

public class Fragment1 extends Fragment {
    private static final String TAG = "Fragment1";

    private Button CarDetails;
    private FragmentActivity fa;
    JSONObject selected;

    //CarDetail
    public static EditText Carname;
    //private EditText Model;
    public static EditText RegNo;
    public static Spinner company;


    //Values
    public  static String seats;
    public  static String cType;
    public  static String Type;
    public  static String model_no;
    public  static String car_name;
    public  static String model;
    public  static String regno;
    public  static String company_name;

    public static String getCarname() {
        return car_name;
    }

    public static String getModel() {
        return model;
    }

    public static String getFuel() {
        return cType;
    }

    public static String gettype() {
        return Type;
    }

    public static String getSeats() {
        return seats;
    }

    public static String getModel_no() {
        return regno;
    }

    public static String getregno() {
        return regno;
    }

    public static String getCompany_name() {
        return company_name;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        fa=((FragmentActivity)getActivity());
        View view  = inflater.inflate(R.layout.fragment1_layout, container, false);

        //Values of CarDetails
        CarDetails = (Button) view.findViewById(R.id.CarDetailsNext);
        Carname = (EditText) view.findViewById(R.id.manu_name);
        //Model = (EditText) view.findViewById(R.id.model_name);
        RegNo = (EditText) view.findViewById(R.id.regNo);
        company = (Spinner) view.findViewById(R.id.spinner);

//        retrieveData();

        company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                  company_name = company.getSelectedItem().toString();


              }

              @Override
              public void onNothingSelected(AdapterView<?> adapterView) {

              }

          });



        CarDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()){
                    JSONObject details = fa.details;
                    try {
                        details.put("regNo",regno);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    populate(model.trim());
                    fa.setViewPager(1);
                }
            }
        });
      //  company.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, fa.mode_name));

        return view;
    }
    private boolean validate(){
        boolean valid = true ;
        car_name = Carname.getText().toString().trim();
        regno = RegNo.getText().toString().trim();

        if((car_name.length() == 0)){
            Carname.setError("Invalid Carname");
            valid=false;
        }
        else Carname.setError(null);

        /*if((model.length() == 0)){
            Model.setError("Invalid Model");
            valid=false;
        }
        else Model.setError(null);*/

       // if((regno.length() == 0)){
       //     RegNo.setError("Invalid Registration Number");
       //     valid=false;
       // }
       // else RegNo.setError(null);
        return valid;
    }
//        public void populate(final String model_nm){
//        try{
//            selected = new JSONObject();
//            selected.put("model",model_nm);
//            RequestQueue queue = Volley.newRequestQueue(getContext());
//            PostRequestHandler handler=new PostRequestHandler("model/car",new ResponseObject() {
//                @Override
//                public void onResponse(JSONObject res) {
//                    try {
//                        if(res != null) {;
//
//
//                            fType = res.getString("fueltype");
//                            seats = res.getString("seats");
//                            // Type = selected.getString("type");
//                            model_no = res.getString("model_no");
//
////
//                        }
//                        else
//                            Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            StringRequest req =handler.postStringRequest(selected);
//            queue.add(req);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//    }
}
