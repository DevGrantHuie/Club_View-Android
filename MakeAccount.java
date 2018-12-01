package com.tinhat.ClubView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.tinhat.ClubView.MakeAccountRequest;
import com.tinhat.ClubView.PersonalGroupList;
import com.tinhat.ClubView.SetUpPage;
import com.tinhat.ClubView.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class MakeAccount extends AppCompatActivity {

    Button CAB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_account);

        //Declarations
        CAB = findViewById(R.id.CreateAccountButton);
        final TextView firstNameInput = findViewById(R.id.firstNameInput);
        final TextView lastNameInput = findViewById(R.id.lastNameInput);
        final TextView passwordInput = findViewById(R.id.passwordInput);
        final TextView personalNumberInput = findViewById(R.id.personalNumberInput);
        final TextView personalEmailInput = findViewById(R.id.personalEmailInput);
        //final Spinner emergencyRelationInput = findViewById(R.id.emergencyRelationInput);
        final TextView emergencyNameInput = findViewById(R.id.emergencyNameInput);
        final TextView emergencyNumberInput = findViewById(R.id.emergencyNumberInput);
        final TextView emergencyEmailInput = findViewById(R.id.emergencyEmailInput);

        CAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstName = firstNameInput.getText().toString();
                final String lastName = lastNameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                final String personalNumber = personalNumberInput.getText().toString();
                final String personalEmail = personalEmailInput.getText().toString();
                //final String emergencyRelation = emergencyRelationInput.getText().toString();
                final String emergencyName = emergencyNameInput.getText().toString();
                final String emergencyNumber = emergencyNumberInput.getText().toString();
                final String emergencyEmail = emergencyEmailInput.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getBoolean("success")) {

                                // Clear all saved data from possible previous accounts
                                SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
                                personalInfo.edit().clear().apply();

                                // Go to PGL
                                Intent intent = new Intent(MakeAccount.this, PersonalGroupList.class);
                                MakeAccount.this.startActivity(intent);

                                // Save data
                                personalInfo.edit().putString("firstName", firstName).apply();
                                personalInfo.edit().putString("lastName", lastName).apply();
                                personalInfo.edit().putString("password", password).apply();
                                personalInfo.edit().putString("privateMode", null).apply();
                                personalInfo.edit().putString("personalNumber", personalNumber).apply();
                                personalInfo.edit().putString("personalEmail", personalEmail).apply();
                              //personalInfo.edit().putString("emergencyRelation", emergencyRelation).apply();
                                personalInfo.edit().putString("emergencyName", emergencyName).apply();
                                personalInfo.edit().putString("emergencyNumber", emergencyNumber).apply();
                                personalInfo.edit().putString("emergencyEmail", emergencyEmail).apply();
                                personalInfo.edit().putString("clubPositions", null).apply();
                            }
                        } catch (JSONException e) {

                        }
                    }
                };

                MakeAccountRequest createRequest = new MakeAccountRequest(
                        firstName, lastName, password, personalNumber, personalEmail,
 /*emergencyRelation,*/ emergencyName, emergencyNumber, emergencyEmail, responseListener);
                Volley.newRequestQueue(MakeAccount.this).add(createRequest);
            }
        });
    }
}
