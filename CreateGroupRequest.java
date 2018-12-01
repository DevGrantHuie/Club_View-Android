package com.tinhat.ClubView;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CreateGroupRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "https://computationalwizard.com/API/CreateGroup.php";
    private Map<String, String> params;

    public CreateGroupRequest(String groupTitle, String groupDescription, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("GroupName", groupTitle);
        params.put("GroupDescription", groupDescription);
        params.put("MemberList", "");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
