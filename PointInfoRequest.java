package com.tinhat.ClubView;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PointInfoRequest extends StringRequest {
    private static final String USER_REQUEST_URL="https://computationalwizard.com/API/GrabPointLog.php";
    private Map<String, String> params;

    public PointInfoRequest(String GroupID, String UserID, Response.Listener<String> listener){

        super(Method.POST, USER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("GroupID", GroupID);
        params.put("UserID", UserID);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
