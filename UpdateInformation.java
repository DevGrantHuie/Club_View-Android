package com.tinhat.ClubView;

import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateInformation extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://computationalwizard.com/API/UpdateInformation.php";
    private Map<String, String> params;

    public UpdateInformation(String userID, String firstName, String lastName, String password,
                             String personalNumber, String personalEmail, String emergencyName,
                             String emergencyNumber, String emergencyEmail, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("UserID", userID);
        params.put("FirstName", firstName);
        params.put("LastName", lastName);
        params.put("Password", password);
        params.put("PersonalNumber", personalNumber);
        params.put("PersonalEmail", personalEmail);
        params.put("EmergencyName", emergencyName);
        params.put("EmergencyNumber", emergencyNumber);
        params.put("EmergencyEmail", emergencyEmail);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
