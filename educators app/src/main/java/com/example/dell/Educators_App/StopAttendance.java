package com.example.dell.Educators_App;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DELL on 01-11-2017.
 */

public class StopAttendance extends StringRequest {
    private static final String Login_REQUEST_URL ="http://pricyfy.esy.es/updatesession.php";
    private Map<String,String> params;
    public StopAttendance (String name, String password,String ssid,String session, Response.Listener<String>listener)
    {
        super(Request.Method.POST,Login_REQUEST_URL, listener, null);
        params=new HashMap<>();
        params.put("class",name);
        params.put("subject",password);
        params.put("ssid",ssid);
        params.put("attendance_live",session);
    }

    public Map<String,String>getParams(){
        return params;
    }

}
