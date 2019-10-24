package tech.sabtih.azizproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Activity_SPRequest extends AppCompatActivity {
TextView details;
Button accept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprequest);

        details = findViewById(R.id.details);
        accept = findViewById(R.id.acceptbtn);
        setTitle(getIntent().getStringExtra("car"));
        details.setText(getIntent().getStringExtra("details"));

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_SPRequest.this, ResultActivity.class);
                intent.putExtra("message","The request has been accepted");
                startActivity(intent);
                finish();
            }
        });

    }
}
