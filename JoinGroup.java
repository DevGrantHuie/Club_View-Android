package com.tinhat.ClubView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.tinhat.ClubView.JoinGroupRequest;
import com.tinhat.ClubView.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class JoinGroup extends AppCompatActivity {
    Button JoinGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        JoinGroup = findViewById(R.id.JoinClubButton);
        final EditText GroupName = findViewById(R.id.ClubTextValue);

        JoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String GroupTitle = GroupName.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonResponse = new JSONArray(response);

                            String name = "";
                            for(int i = 0; i < jsonResponse.length(); i++) {
                                JSONObject jsonObject = jsonResponse.getJSONObject(i);

                                name = jsonObject.getString("user_id");
                            }

                            Toast.makeText(JoinGroup.this, name, Toast.LENGTH_SHORT).show();

                            /*if (Respo.equals("success")) {
                                Toast.makeText(JoinGroup.this, "done", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(JoinGroup.this, PersonalGroupList.class);
                                JoinGroup.this.startActivity(intent);
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
                String userID = personalInfo.getString("userID", "");

                JoinGroupRequest createRequest = new JoinGroupRequest(userID, GroupTitle, responseListener);
                Volley.newRequestQueue(JoinGroup.this).add(createRequest);
            }
        });
    }

    public void RefreshGroupData(String NewGroupList) {
        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
        personalInfo.edit().putString("clubsJoinedList", NewGroupList).apply();
    }
}
