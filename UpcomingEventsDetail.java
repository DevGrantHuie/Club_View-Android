package com.tinhat.ClubView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class UpcomingEventsDetail extends AppCompatActivity {

    public TextView EventName;
    public TextView createdTimeStamp;
    public TextView Description;
    public TextView Date;
    public TextView Time;
    public TextView Location;
    public TextView Other;
    public TextView PointValue;
    public TextView confirmationValue;
    public TextView unsureValue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events_detail);

        // Initialization Stage
        EventName = findViewById(R.id.eventName);
        createdTimeStamp = findViewById(R.id.createdTimeStamp);
        Description = findViewById(R.id.description);
        Date = findViewById(R.id.date);
        Time = findViewById(R.id.time);
        Location = findViewById(R.id.location);
        Other = findViewById(R.id.other);
        PointValue = findViewById(R.id.pointValue);
        confirmationValue = findViewById(R.id.confirmationValue);
        unsureValue = findViewById(R.id.unsureValue);

        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
        int eventPosition = personalInfo.getInt("eventPosition", 0);
        String UpcomingEventsString = personalInfo.getString("UpcomingEvents", "No events yet scheduled");

        String[][] UnpackedArray = Splitter(UpcomingEventsString);

        SyncPage(UnpackedArray, eventPosition);
    }


    public void SyncPage(String[][] UnpackedArray, int eventPosition) {
        EventName.setText(UnpackedArray[eventPosition][2]);
        String createdBy = "Event Created by " + UnpackedArray[eventPosition][13];
        createdTimeStamp.setText(createdBy);
        Description.setText(UnpackedArray[eventPosition][3]);
        Date.setText(UnpackedArray[eventPosition][4]);
        String time = "Starts at [" + UnpackedArray[eventPosition][5] + "] and Ends at [" +
                UnpackedArray[eventPosition][6] + "]";
        Time.setText(time);
        Location.setText(UnpackedArray[eventPosition][7]);
        String other = "* " + UnpackedArray[eventPosition][8];
        Other.setText(other);
        String pointValue = "* Event worth " + UnpackedArray[eventPosition][12] + " points per hour";
        PointValue.setText(pointValue);
        String confirmation = "* Confirmed Members: " + UnpackedArray[eventPosition][9] + "/" + UnpackedArray[eventPosition][10];
        confirmationValue.setText(confirmation);
        String unsure = "* Unsure Members: " + UnpackedArray[eventPosition][11];
        unsureValue.setText(unsure);
    }



    /*public void onBackPressed() {
        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
        int GroupPosition = personalInfo.getInt("groupPosition", 0);

        String rawClubList = personalInfo.getString("clubsList", "No Clubs Joined");
        String[][] refinedClubList = Splitter(rawClubList);
        String[] ExactInfo = HorizontalDataSetSelector(refinedClubList, GroupPosition);
        String adminPriv = ExactInfo[7];

        if (adminPriv.equals("1")) {
            Intent intent = new Intent(UpcomingEventsDetail.this, ADMIN_UpcomingEvents.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(UpcomingEventsDetail.this, UpcomingEvents.class);
            startActivity(intent);
        }
    }*/

    /*
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
