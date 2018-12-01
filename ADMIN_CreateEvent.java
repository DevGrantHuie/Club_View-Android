package com.tinhat.ClubView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.tinhat.ClubView.CreateEventRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ADMIN_CreateEvent extends AppCompatActivity{

    public String GroupID;
    public TextView EventName;
    public TextView Description;
    public TextView DateDay;

    public Spinner DateMonth;
    public String dateMonth;

    public TextView DateYear;
    public TextView StartTimeNumber;

    public Spinner StartTimeAMPM;
    public String startTimeAMPM;

    public TextView EndTimeNumber;

    public Spinner EndTimeAMPM;
    public String endTimeAMPM;

    public TextView Location;
    public TextView Other;
    public TextView TotalPositions;
    public TextView PointValue;
    public Button CreateButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__create_event);

        EventName = findViewById(R.id.EventNameText);
        Description = findViewById(R.id.DescriptionText);
        DateDay = findViewById(R.id.DateDayText);

        DateMonth = findViewById(R.id.DateMonthSpinner);
        DateMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dateMonth = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                dateMonth = "January";
            }
        });

        DateYear = findViewById(R.id.DateYearText);
        StartTimeNumber = findViewById(R.id.StartTimeText);

        StartTimeAMPM = findViewById(R.id.StartTimeAMPM);
        StartTimeAMPM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                startTimeAMPM = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                startTimeAMPM = "A.M.";
            }
        });

        EndTimeNumber = findViewById(R.id.EndTimeText);

        EndTimeAMPM = findViewById(R.id.EndTimeAMPM);
        EndTimeAMPM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                endTimeAMPM = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                endTimeAMPM = "A.M.";
            }
        });

        Location = findViewById(R.id.LocationText);
        Other = findViewById(R.id.OtherText);
        TotalPositions = findViewById(R.id.TotalPositions);
        PointValue = findViewById(R.id.PointText);

        CreateButton = findViewById(R.id.CreateEventButton);
        CreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitEvent();
            }
        });
    }

    public void SubmitEvent() {
        // --------------------------------------- GRABS STRING DATA ------------------------------------------
        final String eventName = EventName.getText().toString();
        final String description = Description.getText().toString();
        final String dateDay = DateDay.getText().toString();
        final String dateYear = DateYear.getText().toString();
        final String startTimeNumber = StartTimeNumber.getText().toString();
        final String endTimeNumber = EndTimeNumber.getText().toString();
        final String location = Location.getText().toString();
        final String other = Other.getText().toString();
        final String totalPositions = TotalPositions.getText().toString();
        final String pointValue = PointValue.getText().toString();

        // ------------------------------------- ALTERING DATA FOR API ----------------------------------------
        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
        String rawClubList = personalInfo.getString("clubsList", "No Clubs Joined");
        int GroupPosition = personalInfo.getInt("groupPosition", 0);

        String[][] refinedClubList = Splitter(rawClubList);
        GroupID = DataSelector(refinedClubList, GroupPosition, 2);

        String eventDate = "" + dateMonth + " " + dateDay + ": " + dateYear;

        String totalStartTime = "" + startTimeNumber + " " + startTimeAMPM;
        String totalEndTime = "" + endTimeNumber + " " + endTimeAMPM;

        String createdBy = personalInfo.getString("UserID", "0");

        // ------------------------------------------- API CALL ----------------------------------------------
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

                        Toast.makeText(ADMIN_CreateEvent.this, "Successfully Uploaded Event!", Toast.LENGTH_LONG).show();
                        ADMIN_CreateEvent.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ADMIN_CreateEvent.this.finish();
                            }
                        });
                    }
                    else {
                        Toast.makeText(ADMIN_CreateEvent.this, "An error has occurred loading page!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


        CreateEventRequest createRequest = new CreateEventRequest(GroupID, eventName, description, eventDate,
                totalStartTime, totalEndTime, location, other, totalPositions, pointValue, createdBy,responseListener);
        createRequest.setShouldCache(false);
        Volley.newRequestQueue(ADMIN_CreateEvent.this).add(createRequest);
    }



    /*
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

    public String[] VerticalDataSetSelector(String[][] refinedClubList, int dataValue) {
        int count = refinedClubList.length;
        String[] GroupNames = new String[count];
        for (int i = 0; i < count; i++) {
            GroupNames[i] = refinedClubList[i][dataValue];
        }

        return GroupNames;
    }
}
