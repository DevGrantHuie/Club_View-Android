package com.tinhat.ClubView;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GrabAdminPrivRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "https://computationalwizard.com/API/GrabAdminPriv.php";
    private Map<String, String> params;

    public GrabAdminPrivRequest(String UserID, String GroupID, Response.Listener<String> listener){
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("UserID", UserID);
        params.put("GroupID", GroupID);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
