package com.tinhat.ClubView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.oob.SignUp;
import com.google.android.gms.signin.SignIn;
import com.tinhat.ClubView.UpcomingEventsDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpcomingEvents extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);

        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
        String UpcomingEventsString = personalInfo.getString("UpcomingEvents", "");

        if (!UpcomingEventsString.equals("")) {
            String[][] UnpackedArray = Splitter(UpcomingEventsString);

            String[] UpcomingEventsList = CreateBracket(UnpackedArray);


            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, UpcomingEventsList);
            ListView lv = findViewById(R.id.EventList);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
                    personalInfo.edit().putInt("eventPosition", i).apply();
                    Intent intent = new Intent(UpcomingEvents.this, UpcomingEventsDetail.class);
                    startActivity(intent);
                }
            });
        }
    }

    public String[] CreateBracket(String[][] UpcomingEvents) {
        String[] UpcomingEventsList = new String[UpcomingEvents.length];
        for (int i = 0; i < UpcomingEvents.length; i++) {
            String name = DataSelector(UpcomingEvents,i,2);
            String Date = DataSelector(UpcomingEvents,i,4);
            UpcomingEventsList[i] = "" + name + "    Date:" + Date;
        }
        return UpcomingEventsList;
    }




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
