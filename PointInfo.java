package com.tinhat.ClubView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PointInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_info);
        GrabData();
    }

    public void GrabData() {
        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
        String rawClubList = personalInfo.getString("clubsList", "No Clubs Joined");
        int GroupPosition = personalInfo.getInt("groupPosition", 0);

        String[][] refinedClubList = Splitter(rawClubList);
        String[] ExactInfo = HorizontalDataSetSelector(refinedClubList, GroupPosition);
        String GroupID = ExactInfo[2];
        String UserID = ExactInfo[1];


        // ------------------------------------------- API CALL ----------------------------------------------
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    String Success = jsonResponse.getString("success");

                    if (Success.equals("true")) {
                        JSONArray personalPointData = jsonResponse.getJSONArray("personalPointData");

                        StringBuilder stringBuilder = new StringBuilder(); // Makes an array based on the JSONArray
                        for (int i = 0; i < personalPointData.length(); i++) {
                            JSONObject NewGroup = personalPointData.getJSONObject(i); // Grabs event info of array id "i"
                            stringBuilder.append(NewGroup.getString("ID")).append("@object");
                            stringBuilder.append(NewGroup.getString("GroupID")).append("@object");
                            stringBuilder.append(NewGroup.getString("UserID")).append("@object");
                            stringBuilder.append(NewGroup.getString("PointValue")).append("@object");
                            stringBuilder.append(NewGroup.getString("EventName")).append("@object");
                            stringBuilder.append(NewGroup.getString("PointDescription")).append("@object");
                            stringBuilder.append(NewGroup.getString("DateAssigned")).append("@object");
                            stringBuilder.append(NewGroup.getString("SignedBy")).append("@object");
                            stringBuilder.append("@array");
                        }
                        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
                        personalInfo.edit().putString("PointData", stringBuilder.toString()).apply();
                        DisplayData();
                    }
                    else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


        PointInfoRequest createRequest = new PointInfoRequest(GroupID, UserID, responseListener);
        createRequest.setShouldCache(false);
        Volley.newRequestQueue(PointInfo.this).add(createRequest);
    }

    public void DisplayData() {
        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
        String PointList = personalInfo.getString("PointData", "");
        String[][] split = Splitter(PointList);
        String[] pointList = Reorganize(split);

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pointList);
        ListView lv = findViewById(R.id.PointList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
                personalInfo.edit().putInt("eventPosition", i).apply();
                Intent intent = new Intent(PointInfo.this, UpcomingEventsDetail.class);
                startActivity(intent);
            }
        });
    }

    public String[] Reorganize(String[][] split) {
        String[] pointList = new String[split.length];
        for (int i = 0; i < split.length; i++) {
            String text = "Event Name: " + split[i][4] + "      ||      Points earned: " + split[i][3];
            pointList[i] = text;
        }
        return pointList;
    }





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



    /* ----- (clubsList) SELECTION -----
    GroupList[i][0] = ("ID");
    GroupList[i][1] = ("UserID");
    GroupList[i][2] = ("GroupID");
    GroupList[i][3] = ("GroupName");
    GroupList[i][4] = ("GroupDescription");
    GroupList[i][5] = ("GroupPosition");
    GroupList[i][6] = ("Points");
    GroupList[i][7] = ("AdminPriv");
     */

    /* ----- (PointData) SELECTION -----
    GroupList[i][0] = ("ID");
    GroupList[i][1] = ("GroupID");
    GroupList[i][2] = ("UserID");
    GroupList[i][3] = ("PointValue");
    GroupList[i][4] = ("EventName");
    GroupList[i][5] = ("PointDescription");
    GroupList[i][6] = ("DateAssigned");
    GroupList[i][7] = ("SignedBy");
     */

    /* ----- (UpcomingEvents) SELECTION -----
    UpcomingEventsArray[0] = getString("ID"));
    UpcomingEventsArray[1] = getString("GroupID");
    UpcomingEventsArray[2] = getString("EventName");
    UpcomingEventsArray[3] = getString("EventDescription");
    UpcomingEventsArray[4] = getString("EventDate");
    UpcomingEventsArray[5] = getString("StartTime");
    UpcomingEventsArray[6] = getString("EndTime");
    UpcomingEventsArray[7] = getString("Location");
    UpcomingEventsArray[8] = getString("Other");
    UpcomingEventsArray[9] = getString("ClosedPositions");
    UpcomingEventsArray[10] = getString("OpenPositions");
    UpcomingEventsArray[11] = getString("PossiblePositions");
    UpcomingEventsArray[12] = getString("PointValue");
    UpcomingEventsArray[13] = getString("CreatedBy");
     */

}
