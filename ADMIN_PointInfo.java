package com.tinhat.ClubView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ADMIN_PointInfo extends AppCompatActivity {

    private Button ADMINpoints;
    private Button personalPointLog;
    private Button groupPointLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__point_info);

        ADMINpoints = findViewById(R.id.ADMINpoints);
        ADMINpoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ADMIN_PointInfo.this, "Not yet developed...", Toast.LENGTH_SHORT).show();
            }
        });
        personalPointLog = findViewById(R.id.personalPointLog);
        personalPointLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ADMIN_PointInfo.this, PointInfo.class);
                startActivity(intent);
            }
        });
        groupPointLog = findViewById(R.id.groupPointLog);
        groupPointLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ADMIN_PointInfo.this, "Not yet developed...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
