package com.tinhat.ClubView;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MakeAccountRequest extends StringRequest {
    private static final String USER_REQUEST_URL="https://computationalwizard.com/API/CreateUser.php";
    private Map<String, String> params;

    public MakeAccountRequest(String FirstName,
                              String LastName,
                              String Password,
                              String PersonalNumber,
                              String PersonalEmail,
                              //String EmergencyRelation,
                              String EmergencyName,
                              String EmergencyNumber,
                              String EmergencyEmail,
                              Response.Listener<String> listener){

        super(Method.POST, USER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("FirstName", FirstName);
        params.put("LastName", LastName);
        params.put("Password", Password);
        params.put("PersonalNumber", PersonalNumber);
        params.put("PersonalEmail", PersonalEmail);
        //params.put("EmergencyRelation", EmergencyRelation);
        params.put("EmergencyName", EmergencyName);
        params.put("EmergencyNumber", EmergencyNumber);
        params.put("EmergencyEmail", EmergencyEmail);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
