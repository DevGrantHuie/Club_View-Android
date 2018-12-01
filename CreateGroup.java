package com.tinhat.ClubView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.tinhat.ClubView.CreateGroupRequest;
import com.tinhat.ClubView.GroupPage1;
import com.tinhat.ClubView.R;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateGroup extends AppCompatActivity {
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        submit = findViewById(R.id.Submit);
        final EditText GroupTitle = findViewById(R.id.GroupTitle);
        final EditText GroupDesc = findViewById(R.id.GroupDesc);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String GroupName = GroupTitle.getText().toString();
                final String GroupDescription = GroupDesc.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                if (jsonResponse.getBoolean("success")) {
                                    Intent intent = new Intent(CreateGroup.this, GroupPage1.class);
                                    CreateGroup.this.startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                };


                CreateGroupRequest createRequest = new CreateGroupRequest(GroupName, GroupDescription, responseListener);
                RequestQueue queue = Volley.newRequestQueue(CreateGroup.this);
                queue.add(createRequest);
            }
        });
    }
}
