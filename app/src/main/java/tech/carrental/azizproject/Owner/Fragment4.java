package tech.carrental.azizproject.Owner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import tech.carrental.azizproject.R;
import tech.carrental.azizproject.Utils.PermissionsUtils;

import static android.app.Activity.RESULT_OK;
import static tech.carrental.azizproject.Owner.Fragment3.Fair;
import static tech.carrental.azizproject.Owner.Fragment3.Rent;


public class Fragment4 extends Fragment {
    private static final String TAG = "Image";
    private static final int PICK_IMAGE = 1;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 2;

    private Button Upload;
    private Button Next;
    private ImageView imageView;
    public static Uri imageUri=null;
    public static Bitmap bitmap;
    Context Appl;
    private FragmentActivity fa;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment4_layout, container, false);
        fa=((FragmentActivity)getActivity());

        imageView = (ImageView) view.findViewById(R.id.carImage);
        Next = (Button) view.findViewById(R.id.next);
        Appl = getContext();
        assert Appl != null;
        if (ContextCompat.checkSelfPermission(Appl, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionsUtils.requestPermission((AppCompatActivity) Appl, STORAGE_PERMISSION_REQUEST_CODE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, true);
        }
        Log.d(TAG, "onCreateView: started.");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri!=null) {
                    openSecondActivity();
                }
                else
                    Toast.makeText(getContext(),"Please select image", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    public void openSecondActivity(){
        Intent intent=new Intent(getContext(), SecondActivity.class);
        startActivity(intent.putExtra("data",
                 fa.details.toString()));
    }

    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PICK_IMAGE:
                if(resultCode == RESULT_OK){
                    imageUri = data.getData();
                    String[] projecttion = {MediaStore.Images.Media.DATA}; //fetch media path

                    Cursor cursor = Appl.getContentResolver().query(imageUri, projecttion, null, null, null);
                    cursor.moveToFirst();

                    int column_index = cursor.getColumnIndex(projecttion[0]);
                    String filepath = cursor.getString(column_index);
                    cursor.close();

                    bitmap = BitmapFactory.decodeFile(filepath);
                    Glide.with(getContext())
                            .load(imageUri)
                            .into(imageView);
                }
        }
    }

    public boolean validate(){
        boolean valid = true;


        if(Fragment3.fair.length() == 0) {
            Fair.setError("Fair is invalid");
            valid = false;
        }else Fair.setError(null);

        if(Fragment3.rent.length() == 0) {
            Rent.setError("Fair is invalid");
            valid = false;
        }else Rent.setError(null);


        if(Fragment1.car_name ==null||Fragment1.car_name.length() == 0){
            Fragment1.Carname.setError("Invalid Carname");
            valid=false;
        }
        else Fragment1.Carname.setError(null);

        /*if((model.length() == 0)){
            Model.setError("Invalid Model");
            valid=false;
        }
        else Model.setError(null);*/

        if(Fragment1.regno==null||Fragment1.regno.length() == 0){
            Fragment1.RegNo.setError("Invalid Registration Number");
            valid=false;
        }
        else Fragment1.RegNo.setError(null);



        if((Fragment1.seats==null||Fragment2.seats.length() == 0)){
            Fragment2.Seats.setError("Invalid Seats");
            valid=false;
        }
        else Fragment2.Seats.setError(null);

        if((Fragment2.city==null||Fragment2.city.length() == 0)){
            Fragment2.City.setError("Invalid City");
            valid=false;
        }
        else Fragment2.City.setError(null);

        if((Fragment2.fType ==null||Fragment2.fType.length() == 0) || (!(Fragment2.fType.toUpperCase().equals("PETROL") || Fragment2.fType.toUpperCase().equals("DIESEL")))){
            Fragment2.Ctype.setError("Invalid Car Type");
            valid=false;
        }
        else Fragment2.Ctype.setError(null);


        return valid;
    }
}
