package com.tinhat.ClubView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.oob.SignUp;
import com.google.android.gms.signin.SignIn;
import com.tinhat.ClubView.LoginRequest;
import com.tinhat.ClubView.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class StartUpPage extends AppCompatActivity {

    Button SignIn;
    Button SignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_page);

        SignIn = findViewById(R.id.signIn);
        SignUp = findViewById(R.id.signUp);
        final EditText EmailEntered = findViewById(R.id.emailInput);
        final EditText PasswordEntered = findViewById(R.id.passwordInput);


        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String loginEmail = EmailEntered.getText().toString();
                final String loginPassword = PasswordEntered.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                        // ------------------------------------- Personal (Data) Grab From Server ----------------
                            JSONObject PersonalData = jsonResponse.getJSONObject("PersonalData");

                            String UserID = PersonalData.getString("UserID");
                            String FirstName = PersonalData.getString("FirstName");
                            String LastName = PersonalData.getString("LastName");
                            String Password = PersonalData.getString("Password");
                            String PrivateMode = PersonalData.getString("PrivateMode");
                            String PersonalNumber = PersonalData.getString("PersonalNumber");
                            String PersonalEmail = PersonalData.getString("PersonalEmail");
                            String EmergencyRelation = PersonalData.getString("EmergencyRelation");
                            String EmergencyName = PersonalData.getString("EmergencyName");
                            String EmergencyNumber = PersonalData.getString("EmergencyNumber");
                            String EmergencyEmail = PersonalData.getString("EmergencyEmail");
                        // ------------------------------------- Personal (Group) Grab From Server --------------
                            JSONArray GroupListData = jsonResponse.getJSONArray("GroupListData");
                            String[][] GroupList = SplitGroupsArray(GroupListData);
                                SaveAllData(UserID, FirstName, LastName, Password,
                                            PrivateMode, PersonalNumber, PersonalEmail, EmergencyRelation,
                                            EmergencyName, EmergencyNumber, EmergencyEmail, GroupList);
                                Intent intent = new Intent(StartUpPage.this, com.tinhat.ClubView.PersonalGroupList.class);
                                StartUpPage.this.startActivity(intent);
                            }
                            else {
                                Toast.makeText(StartUpPage.this, jsonResponse.toString(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };


                LoginRequest createRequest = new LoginRequest(loginEmail, loginPassword, responseListener);
                createRequest.setShouldCache(false);
                Volley.newRequestQueue(StartUpPage.this).add(createRequest);
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartUpPage.this, com.tinhat.ClubView.MakeAccount.class);
                StartUpPage.this.startActivity(intent);
            }
        });
    }

    public String[][] SplitGroupsArray(JSONArray GroupListData) {
        String[][] GroupList = new String[GroupListData.length()][]; // Makes an array based on the JSONArray
        for (int i = 0; i < GroupListData.length(); i++) { // Cycles through each group info set
            try {
                JSONObject NewGroup = GroupListData.getJSONObject(i); // Grabs group info of array id "i"
                GroupList[i] = new String[NewGroup.length()]; // Creates array for group data in spot "i" (array[i][])
                GroupList[i][0] = NewGroup.getString("ID");
                GroupList[i][1] = NewGroup.getString("UserID");
                GroupList[i][2] = NewGroup.getString("GroupID");
                GroupList[i][3] = NewGroup.getString("GroupName");
                GroupList[i][4] = NewGroup.getString("GroupDescription");
                GroupList[i][5] = NewGroup.getString("GroupPosition");
                GroupList[i][6] = NewGroup.getString("Points");
                GroupList[i][7] = NewGroup.getString("AdminPriv");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return GroupList;
    }

    public void SaveAllData(String UserID, String FirstName, String LastName, String Password,
                            String PrivateMode, String PersonalNumber, String PersonalEmail, String EmergencyRelation,
                            String EmergencyName, String EmergencyNumber, String EmergencyEmail, String[][] GroupList) {
        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
        personalInfo.edit().putString("userID", UserID).apply();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < GroupList.length; i++) {
            for (int index = 0; index < GroupList[i].length; index++) {
                stringBuilder.append(GroupList[i][index]).append("@object");
            }
            stringBuilder.append("@array");
        }

        personalInfo.edit().putString("clubsList", stringBuilder.toString()).apply();

        personalInfo.edit().putString("firstName", FirstName).apply();
        personalInfo.edit().putString("lastName", LastName).apply();
        personalInfo.edit().putString("password", Password).apply();
        personalInfo.edit().putString("privateMode", PrivateMode).apply();
        personalInfo.edit().putString("personalNumber", PersonalNumber).apply();
        personalInfo.edit().putString("personalEmail", PersonalEmail).apply();
        personalInfo.edit().putString("emergencyRelation", EmergencyRelation).apply();
        personalInfo.edit().putString("emergencyName", EmergencyName).apply();
        personalInfo.edit().putString("emergencyNumber", EmergencyNumber).apply();
        personalInfo.edit().putString("emergencyEmail", EmergencyEmail).apply();
    }
}
