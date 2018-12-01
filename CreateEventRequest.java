package com.tinhat.ClubView;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CreateEventRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://computationalwizard.com/API/CreateEvent.php";
    private Map<String, String> params;

    public CreateEventRequest(String GroupID, String EventName, String Description, String EventDate,
                              String StartTime, String EndTime, String Location, String Other,
                              String TotalPositions, String PointValue, String SignedBy,
                              Response.Listener<String> listener){

        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("GroupID", GroupID);
        params.put("EventName", EventName);
        params.put("Description", Description);
        params.put("EventDate", EventDate);
        params.put("StartTime", StartTime);
        params.put("EndTime", EndTime);
        params.put("Location", Location);
        params.put("Other", Other);
        params.put("TotalPositions", TotalPositions);
        params.put("PointValue", PointValue);
        params.put("SignedBy", SignedBy);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
