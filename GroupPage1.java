package com.tinhat.ClubView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.IDNA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.signin.SignIn;
import com.tinhat.ClubView.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.Delayed;

public class GroupPage1 extends AppCompatActivity {

    private Button settingsButton;
    private Button pointHistory;
    private Button upcomingEvents;
    private Button helpfulDocumentsButton;
    private Button votingButton;
    private Button membersListButton;
    public TextView Points;
    public TextView Role;
    public TextView Team;
    public String GroupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page1);

        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
        String rawClubList = personalInfo.getString("clubsList", "No Clubs Joined");
        int GroupPosition = personalInfo.getInt("groupPosition", 0);

        String[][] refinedClubList = Splitter(rawClubList);
        GroupID = DataSelector(refinedClubList, GroupPosition, 2);

        // Initialization Stage
        settingsButton = findViewById(R.id.settingsButtonID);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenSettings(view);
            }
        });
        pointHistory = findViewById(R.id.pointHistoryButton);
        pointHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenPointInfo(view);
            }
        });
        upcomingEvents = findViewById(R.id.upcomingEventsButton);
        upcomingEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenUpcomingEvents(view);
            }
        });
        helpfulDocumentsButton = findViewById(R.id.helpfulDocumentsButton);
        helpfulDocumentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { OpenHelpfulDocuments(view);
            }
        });
        votingButton = findViewById(R.id.votingButton);
        votingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenVoting(view);
            }
        });
        membersListButton = findViewById(R.id.membersListButton);
        membersListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenMembersList(view);
            }
        });

        Points = findViewById(R.id.ChangablePoints);
        Role = findViewById(R.id.ChangableRole);
        Team = findViewById(R.id.ChangableTeam);

        UpdatePage();
    }

    // ----------------------------------------------------- Buttons -----------------------------------------------
    public void OpenSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void OpenPointInfo(View view) {
        PageAuthentication(PointInfo.class, ADMIN_PointInfo.class);
    }

    public void OpenUpcomingEvents(View view) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    boolean Success = jsonResponse.getBoolean("success");

                    if (Success) {
                        // ------------------------------------- Personal (Group) Grab From Server --------------
                        JSONArray upcomingEventsData = jsonResponse.getJSONArray("upcomingEventData");

                        StringBuilder stringBuilder = new StringBuilder(); // Makes an array based on the JSONArray
                        for (int i = 0; i < upcomingEventsData.length(); i++) {
                            JSONObject NewGroup = upcomingEventsData.getJSONObject(i); // Grabs event info of array id "i"
                            stringBuilder.append(NewGroup.getString("ID")).append("@object");
                            stringBuilder.append(NewGroup.getString("GroupID")).append("@object");
                            stringBuilder.append(NewGroup.getString("EventName")).append("@object");
                            stringBuilder.append(NewGroup.getString("EventDescription")).append("@object");
                            stringBuilder.append(NewGroup.getString("EventDate")).append("@object");
                            stringBuilder.append(NewGroup.getString("StartTime")).append("@object");
                            stringBuilder.append(NewGroup.getString("EndTime")).append("@object");
                            stringBuilder.append(NewGroup.getString("Location")).append("@object");
                            stringBuilder.append(NewGroup.getString("Other")).append("@object");
                            stringBuilder.append(NewGroup.getString("ConfirmedPositions")).append("@object");
                            stringBuilder.append(NewGroup.getString("TotalPositions")).append("@object");
                            stringBuilder.append(NewGroup.getString("UnsurePositions")).append("@object");
                            stringBuilder.append(NewGroup.getString("PointValue")).append("@object");
                            stringBuilder.append(NewGroup.getString("SignedBy"));
                            stringBuilder.append("@array");
                        }
                        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
                        personalInfo.edit().putString("UpcomingEvents", stringBuilder.toString()).apply();
                    }
                    else {
                        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
                        personalInfo.edit().putString("UpcomingEvents", "").apply();
                    }

                    PageAuthentication(UpcomingEvents.class, ADMIN_UpcomingEvents.class);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        UpcomingEventRequest createRequest = new UpcomingEventRequest(GroupID, responseListener);
        createRequest.setShouldCache(false);
        Volley.newRequestQueue(GroupPage1.this).add(createRequest);
    }

    public void OpenHelpfulDocuments(View view) {
        Toast.makeText(this, "Not yet developed...", Toast.LENGTH_SHORT).show();
    }

    public void OpenVoting(View view) {
        Toast.makeText(this, "Not yet developed...", Toast.LENGTH_SHORT).show();
    }

    public void OpenMembersList(View view) {
        Toast.makeText(this, "Not yet developed...", Toast.LENGTH_SHORT).show();
    }

    public void PageAuthentication(Class normal, Class admin) {
        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
        int GroupPosition = personalInfo.getInt("groupPosition", 0);

        String rawClubList = personalInfo.getString("clubsList", "No Clubs Joined");
        String[][] refinedClubList = Splitter(rawClubList);
        String[] ExactInfo = HorizontalDataSetSelector(refinedClubList, GroupPosition);
        String adminPriv = ExactInfo[7];

        if (adminPriv.equals("1")) {
            Intent intent = new Intent(GroupPage1.this, admin);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(GroupPage1.this, normal);
            startActivity(intent);
        }
    }

    public void UpdatePage() {
        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);

        // Grabs all the data saved in personal storage
        String rawClubList = personalInfo.getString("clubsList", "No Clubs Joined");
        int GroupPosition = personalInfo.getInt("groupPosition", 0);
        // Splits the long list into subCategories
        String[][] refinedClubList = Splitter(rawClubList);
        String[] ExactInfo = HorizontalDataSetSelector(refinedClubList, GroupPosition);
        Points.setText(ExactInfo[6]);
        Role.setText(ExactInfo[5]);
        Team.setText("null");
    }







    /*public void onBackPressed() {
        Intent intent = new Intent(GroupPage1.this, PersonalGroupList.class);
        startActivity(intent);
    }*/

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
