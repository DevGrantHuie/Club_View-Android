package com.tinhat.ClubView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.tinhat.ClubView.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Arrays;

public class PersonalGroupList extends AppCompatActivity {

    Button addGroup;
    Button settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_group_list);

        // --------------------------------------- (Start) Create List View ---------------------------------------
        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
        // Grabs all the data saved in personal storage
        String rawClubList = personalInfo.getString("clubsList", "No Clubs Joined");
        // Splits the long list into subCategories
        String[][] refinedClubList = Splitter(rawClubList);
        String[] GroupNames = VerticalDataSetSelector(refinedClubList, 3);

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, GroupNames);

        ListView lv = findViewById(R.id.GroupList);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PersonalGroupList.this, com.tinhat.ClubView.GroupPage1.class);
                startActivity(intent);
                GrabAdminPriv(view, i);
            }
        });

        // --------------------------------------- (End) Create List View -----------------------------------------

        addGroup = findViewById(R.id.addGroup);
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalGroupList.this, com.tinhat.ClubView.JoinGroup.class);
                startActivity(intent);
            }
        });

        settings = findViewById(R.id.settingsButton);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalGroupList.this, Settings.class);
                PersonalGroupList.this.startActivity(intent);
            }
        });

    }


    public void GrabAdminPriv(View view, int i) {

        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
        personalInfo.edit().putInt("groupPosition", i).apply();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = true;//jsonResponse.getBoolean("success");

                    if (success) {
                        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);

                        // Grabs all the data saved in personal storage
                        String rawClubList = personalInfo.getString("clubsList", "No Clubs Joined");
                        int GroupPosition = personalInfo.getInt("groupPosition", 0);
                        // Splits the long list into subCategories
                        String[][] refinedClubList = Splitter(rawClubList);

                        if (!refinedClubList[GroupPosition][7].equals(jsonResponse.getString("AdminPriv"))) {
                            refinedClubList[GroupPosition][7] = jsonResponse.getString("AdminPriv");
                            String restitch = Connector(refinedClubList);
                            personalInfo.edit().putString("clubsList", restitch).apply();
                            Toast.makeText(PersonalGroupList.this, "Your Group Privileges Have Been Altered", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(PersonalGroupList.this, "An error has occurred loading page!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        // Grabs all the data saved in personal storage
        String rawClubList = personalInfo.getString("clubsList", "No Clubs Joined");
        int GroupPosition = personalInfo.getInt("groupPosition", 0);
        // Splits the long list into subCategories
        String[][] refinedClubList = Splitter(rawClubList);
        String[] HorizontalDataSet = HorizontalDataSetSelector(refinedClubList, GroupPosition);
        String UserID = HorizontalDataSet[1];
        String GroupID = HorizontalDataSet[2];

        com.tinhat.ClubView.GrabAdminPrivRequest createRequest = new com.tinhat.ClubView.GrabAdminPrivRequest(UserID, GroupID, responseListener);
        createRequest.setShouldCache(false);
        Volley.newRequestQueue(PersonalGroupList.this).add(createRequest);
    }


    public void onBackPressed() {

        //Toast.makeText(ADMIN_CreateEvent.this, "Back!", Toast.LENGTH_LONG).show();

    }


    /* ----- USER DATA SELECTION -----
    GroupList[i][0] = ("ID");
    GroupList[i][1] = ("UserID");
    GroupList[i][2] = ("GroupID");
    GroupList[i][3] = ("GroupName");
    GroupList[i][4] = ("GroupDescription");
    GroupList[i][5] = ("GroupPosition");
    GroupList[i][6] = ("Points");
    GroupList[i][7] = ("AdminPriv");
     */

    public String Connector(String[][] doubleArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < doubleArray.length; i++) {
            for (int index = 0; index < doubleArray[i].length; index++) {
                stringBuilder.append(doubleArray[i][index]).append("@object");
            }
            stringBuilder.append("@array");
        }
        return stringBuilder.toString();
    }

    public String[][] Splitter(String list) {
        String[] GroupList = list.split("@array");
        int count = GroupList.length;
        String[][] refinedClubList = new String[count][];
        for (int i = 0; i < count; i++) {
            refinedClubList[i] = GroupList[i].split("@object");
        }

        return refinedClubList;
    }

    public String DataSelector(String[][] refinedList, int position, int dataID) {
        return refinedList[position][dataID];
    }

    public String[] HorizontalDataSetSelector(String[][] refinedList, int dataID) {
        int returnedData = refinedList[0].length;
        String[] GroupNames = new String[returnedData];
        for (int i = 0; i < returnedData; i++) {
            GroupNames[i] = refinedList[dataID][i];
        }

        return GroupNames;
    }

    public String[] VerticalDataSetSelector(String[][] refinedList, int dataValue) {
        int count = refinedList.length;
        String[] GroupNames = new String[count];
        for (int i = 0; i < count; i++) {
            GroupNames[i] = refinedList[i][dataValue];
        }

        return GroupNames;
    }



}
