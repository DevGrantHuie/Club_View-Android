package com.tinhat.ClubView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.signin.SignIn;
import com.tinhat.ClubView.R;
import com.tinhat.ClubView.StartUpPage;
import com.tinhat.ClubView.UpdateInformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class Settings extends AppCompatActivity {

    Button TempSave;

    Button logOut;

    public EditText Email;
    public EditText Password;
    public EditText FirstName;
    public EditText LastName;
    public EditText MobileNumber;
    //public View PrivateMode;
    //public Spinner ECRelation;
    public EditText ECName;
    public EditText ECNumber;
    public EditText ECEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        showCurrent();
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clears saved data
                SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
                personalInfo.edit().clear().apply();
                // Sends user to the login/create account screen
                Intent intent = new Intent(Settings.this, StartUpPage.class);
                Settings.this.startActivity(intent);
            }
        });

        TempSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);

                final String UserID = personalInfo.getString("userID", "");
                final String Email = findViewById(R.id.personalEmail).toString();
                final String Password = findViewById(R.id.Password).toString();
                final String FirstName = findViewById(R.id.FirstName).toString();
                final String LastName = findViewById(R.id.LastName).toString();
                final String MobileNumber = findViewById(R.id.personalNumber).toString();
                //final String PrivateMode = findViewById(R.id.privateModeSetting).toString();
                //final String ECRelation = findViewById(R.id.emergencyRelation).toString();
                final String ECName = findViewById(R.id.emergencyName).toString();
                final String ECNumber = findViewById(R.id.emergencyNumber).toString();
                final String ECEmail = findViewById(R.id.emergencyEmail).toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("Success");

                            if (success) {
                                Toast.makeText(Settings.this, "success.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };


                UpdateInformation updateInformation = new UpdateInformation( UserID, FirstName, LastName, Password,
                        MobileNumber, Email, ECName, ECNumber, ECEmail, responseListener);
                Volley.newRequestQueue(Settings.this).add(updateInformation);
            }
        });
    }

    void showCurrent() {

        //Initialization
        TempSave = findViewById(R.id.tempSave);
        logOut = findViewById(R.id.logoutButton);
        Email = findViewById(R.id.personalEmail);
        Password = findViewById(R.id.Password);
        FirstName = findViewById(R.id.FirstName);
        LastName = findViewById(R.id.LastName);
        MobileNumber = findViewById(R.id.personalNumber);
        //PrivateMode = findViewById(R.id.privateModeSetting);
        //ECRelation = findViewById(R.id.emergencyRelation);
        ECName = findViewById(R.id.emergencyName);
        ECNumber = findViewById(R.id.emergencyNumber);
        ECEmail = findViewById(R.id.emergencyEmail);

        //Initialization
        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
        String personalEmail = personalInfo.getString("personalEmail", "");
        String password = personalInfo.getString("password", "");
        String firstName = personalInfo.getString("firstName", "");
        String lastName = personalInfo.getString("lastName", "");
        //String privateMode = personalInfo.getString("privateMode", "");
        String personalNumber = personalInfo.getString("personalNumber", "");
        //String emergencyRelation = personalInfo.getString("emergencyRelation", ");
        String emergencyName = personalInfo.getString("emergencyName", "");
        String emergencyNumber = personalInfo.getString("emergencyNumber", "");
        String emergencyEmail = personalInfo.getString("emergencyEmail", "");

        //Execution
        Email.setText(personalEmail);
        Password.setText(password);
        FirstName.setText(firstName);
        LastName.setText(lastName);
        //PrivateMode.setText(privateMode);
        MobileNumber.setText(personalNumber);
        //ECRelation.setText(emergencyRelation);
        ECName.setText(emergencyName);
        ECNumber.setText(emergencyNumber);
        ECEmail.setText(emergencyEmail);
    }




   /* public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
        builder.setMessage("Would you like to save changes to your account?");
        builder.setCancelable(true);
        builder.setNegativeButton("Implement Changes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveChanges();
                finish();
            }
        });
        builder.setPositiveButton("Undo changes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Settings.this, "Changes not saved.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.create().show();
    }

    void saveChanges() {
        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);
        String UserID = personalInfo.getString("userID", "");

        final String Email = findViewById(R.id.personalEmail).toString();
        final String Password = findViewById(R.id.Password).toString();
        final String FirstName = findViewById(R.id.FirstName).toString();
        final String LastName = findViewById(R.id.LastName).toString();
        final String MobileNumber = findViewById(R.id.personalNumber).toString();
        //final String PrivateMode = findViewById(R.id.privateModeSetting).toString();
        //final String ECRelation = findViewById(R.id.emergencyRelation).toString();
        final String ECName = findViewById(R.id.emergencyName).toString();
        final String ECNumber = findViewById(R.id.emergencyNumber).toString();
        final String ECEmail = findViewById(R.id.emergencyEmail).toString();

        UpdateInformation createRequest = new UpdateInformation( UserID, FirstName, LastName, Password,
                MobileNumber, Email, ECName, ECNumber, ECEmail, responseListener);
        Volley.newRequestQueue(Settings.this).add(createRequest);
    }
    */

}
