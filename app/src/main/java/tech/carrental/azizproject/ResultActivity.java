package tech.carrental.azizproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        text = findViewById(R.id.resulttext);
        if(getIntent().hasExtra("message")){
            text.setText(getIntent().getStringExtra("message"));
        }



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Close after 1.5 second
                finish();
            }
        }, 1500);
    }
}
