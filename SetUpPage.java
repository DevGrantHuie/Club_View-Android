package com.tinhat.ClubView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tinhat.ClubView.CreateGroup;
import com.tinhat.ClubView.JoinGroup;
import com.tinhat.ClubView.R;

public class SetUpPage extends AppCompatActivity {
    Button Join;
    Button Create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_page);
        Join = findViewById(R.id.JoinButton);
        Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JoinGroup(view);
            }
        });

        Create = findViewById(R.id.CreateButton);
        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateGroup(view);
            }
        });
    }

    public void JoinGroup(View view) {
        Intent intent = new Intent(this, JoinGroup.class);
        startActivity(intent);
    }

    public void CreateGroup(View view) {
        Intent intent = new Intent(this, CreateGroup.class);
        startActivity(intent);
    }
}
