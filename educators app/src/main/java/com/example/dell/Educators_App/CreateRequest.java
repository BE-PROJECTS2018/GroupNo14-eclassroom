package com.example.dell.Educators_App;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DELL on 25-06-2017.
 */

public class CreateRequest extends StringRequest {

    private static final String Login_REQUEST_URL ="http://pricyfy.esy.es/insert.php";
    /*private  static LoginRequest mInstance;
    private RequestQueue requestQueue;
    private  static Context mCtx;
    private  LoginRequest(Context context){
        mCtx= context;
        requestQueue = getRequestQueue();

    }
    public RequestQueue getRequestQueue()
    {
        if (requestQueue==null){
            requestQueue= Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized LoginRequest getmInstance(Context context){
        if (mInstance==null){
            mInstance= new LoginRequest(context);
        }
        return mInstance;
    }
   public <T>void  addToRequestQueue(Request<T> request){
       requestQueue.add(request);
   }*/
    private Map<String,String> params;
    public CreateRequest (String name, String password,String ssid, Response.Listener<String>listener)
    {
        super(Request.Method.POST,Login_REQUEST_URL, listener, null);
        params=new HashMap<>();
        params.put("class",name);
        params.put("subject",password);
        params.put("ssid",ssid);
    }

    public Map<String,String>getParams(){
        return params;
    }

}

