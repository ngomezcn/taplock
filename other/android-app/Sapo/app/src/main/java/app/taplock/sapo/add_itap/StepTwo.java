package app.taplock.sapo.add_itap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import app.taplock.sapo.R;

public class StepTwo extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_two);


        findViewById(R.id.come_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StepTwo.this, AddItap.class);
                startActivity(intent);
            }
        });
    }
}