package com.example.dell.Educators_App;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
//import static com.example.dell.demo.R.id.subject;

public class MainActivity extends AppCompatActivity {
    private static final String Login_REQUEST_URL ="https://shivanivalecha26.000webhostapp.com/web/insert.php";
    AlertDialog.Builder builder;
    EditText clas, subject,et1;
    TextToSpeech textToSpeech;
    String ssi;
    private Map<String,String> params;
    String c_class, s_subject;

    Handler handler;

    Button stop,create,start,manual;

     String w;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        clas = (EditText) findViewById(R.id.clas);
       // subject = (EditText) findViewById(R.id.subject);
        et1 = (EditText) findViewById(R.id.et1);
          create = (Button) findViewById(R.id.create);
         stop =(Button) findViewById(R.id.STOP);
         start = (Button) findViewById(R.id.start);
        manual = (Button) findViewById(R.id.manually);
        builder = new AlertDialog.Builder(MainActivity.this);
        WifiManager mWifiManager;
        final WifiConfiguration config;
        String pp=" ";
        String subject_name = "Enter Subject Name";
        String class_name = "Enter Class Name";


// textToSpeech helps the text to be spoken by Mobile itself
        textToSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status==TextToSpeech.SUCCESS){
                   int result = textToSpeech.setLanguage(Locale.ENGLISH);

                   String text1 = "Enter subject name";

                    textToSpeech.speak(text1, TextToSpeech.QUEUE_FLUSH, null);
                    //handler helps yo let the text be spoken first and then recording for input starts
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(intent, 10);
                            } else {
                                Toast.makeText(getApplicationContext(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, 1000);

                }else{
                    Toast.makeText(getApplicationContext(),"Feature not supported in your device",Toast.LENGTH_SHORT);
                }

            }
        });











        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        int s = (wifiConfiguration.status);
        final String w = wifiConfiguration.SSID;
        WifiConfiguration wifiConfiguration1 = new WifiConfiguration();
        // wifiConfiguration.status;
        WifiApManager wi = new WifiApManager(getApplicationContext());
        // boolean s = wi.isWifiApEnabled();
       // boolean s1 = wi.setWifiApEnabled(w, true);

        WifiManager wifimanager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Method[] methods = wifimanager.getClass().getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().equals("getWifiApConfiguration")) {
                try {
                    WifiConfiguration config1 = (WifiConfiguration) m.invoke(wifimanager);
                    pp = config1.SSID;
                   // Toast.makeText(MainActivity.this, "E1 " + "SSID is " + pp, Toast.LENGTH_LONG).show();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();

                }

            }
        }
        ssi = pp;
             //ssi.concat(pp);

        //final String c_class = clas.getText().toString();
        //final String s_subject = subject.getText().toString();

        create.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"CwC",Toast.LENGTH_LONG);
                final String c_class = clas.getText().toString();
                final String s_subject = subject.getText().toString();

                if (c_class.equals("") || s_subject.equals("")) {
                    builder.setTitle("something went wrong");
                    displayAlert("Enter details");
                } else {

                    //  JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST, Login_REQUEST_URL,null,
                    //  new Response.Listener<JSONArray>() {

                    Response.Listener<String> responseListener = new Response.Listener<String>() {

                        @Override
                        //  Toast.makeText(LoginActivity.this, "E1", Toast.LENGTH_LONG).show();

                        public void onResponse(String response) {
                            try {

                               // Toast.makeText(MainActivity.this, "s", Toast.LENGTH_LONG).show();
                                JSONObject jsonObject1 =new JSONObject(response);
                                //Toast.makeText(MainActivity.this, "y", Toast.LENGTH_LONG).show();
                                boolean success = jsonObject1.getBoolean("success");
                                //Toast.makeText(MainActivity.this, "8", Toast.LENGTH_LONG).show();
                                if (!success) {
                                    //Toast.makeText(MainActivity.this, "p", Toast.LENGTH_LONG).show();
                                    builder.setTitle("Error");
                                    displayAlert(response);
                                } else {
                                    //Toast.makeText(MainActivity.this, "suc", Toast.LENGTH_LONG).show();
                                    builder.setTitle("Attendance record created");
                                    displayAlert("Click on start attendance to start");
                                    start.setEnabled(true);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    //Toast.makeText(MainActivity.this, "s", Toast.LENGTH_LONG).show();
                    CreateRequest createRequest = new CreateRequest(c_class, s_subject,ssi,responseListener);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

                    queue.add(createRequest);
                                   }

            }
        });

        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddManually.class);
                startActivity(intent);
            }
        });



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAttendance();



                /*final String c_class = clas.getText().toString();
                final String s_subject = subject.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                          //  Toast.makeText(MainActivity.this, "s", Toast.LENGTH_LONG).show();
                            JSONObject jsonObject1 =new JSONObject(response);
                            //Toast.makeText(MainActivity.this, "y", Toast.LENGTH_LONG).show();
                            boolean success = jsonObject1.getBoolean("success");
                            //Toast.makeText(MainActivity.this, "8", Toast.LENGTH_LONG).show();
                            if (!success) {
                                //Toast.makeText(MainActivity.this, "not suceess", Toast.LENGTH_LONG).show();
                                builder.setTitle("Error");
                                displayAlert(response);
                            } else {
                                //Toast.makeText(MainActivity.this, "suc", Toast.LENGTH_LONG).show();
                                builder.setTitle("Attendance started");
                                 displayAlert("AP MODE CREATED");
                                //start.setEnabled(false);
                                WifiConfiguration wifiConfiguration = new WifiConfiguration();
                                // wifiConfiguration.status;
                                WifiApManager wi = new WifiApManager(getApplicationContext());
                                // boolean s = wi.isWifiApEnabled();
                                boolean s1 = wi.setWifiApEnabled(w, true);
                                stop.setEnabled(true);
                                create.setEnabled(false);
                                WifiManager wifimanager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                Method[] methods = wifimanager.getClass().getDeclaredMethods();
                                for (Method m : methods) {
                                    if (m.getName().equals("getWifiApConfiguration")) {
                                        try {
                                            WifiConfiguration config = (WifiConfiguration) m.invoke(wifimanager);
                                            String pp = config.SSID;
                                            //Toast.makeText(MainActivity.this, "E1 " + "SSID is " + pp, Toast.LENGTH_LONG).show();
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        } catch (InvocationTargetException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                };

                StopAttendance stopAttendance = new StopAttendance(c_class, s_subject,ssi,"1",responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(stopAttendance);*/



            }

        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                final String c_class = clas.getText().toString();
                final String s_subject = subject.getText().toString();

               // displayAlert("ok bye");
                WifiApManager wi = new WifiApManager(getApplicationContext());
                WifiManager wifi =(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                // boolean s = wi.isWifiApEnabled();
                boolean s1 = wi.setWifiApEnabled(w, false);
                wifi.setWifiEnabled(true);
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    //  Toast.makeText(LoginActivity.this, "E1", Toast.LENGTH_LONG).show();

                    public void onResponse(String response) {
                        try {

                             //Toast.makeText(MainActivity.this, "s", Toast.LENGTH_LONG).show();
                            JSONObject jsonObject1 =new JSONObject(response);
                            //Toast.makeText(MainActivity.this, "y", Toast.LENGTH_LONG).show();
                            boolean success = jsonObject1.getBoolean("success");
                            //Toast.makeText(MainActivity.this, "8", Toast.LENGTH_LONG).show();
                            if (!success) {
                                //Toast.makeText(MainActivity.this, "not suceess", Toast.LENGTH_LONG).show();
                                builder.setTitle("Error");
                                displayAlert(response);
                            } else {
                                //Toast.makeText(MainActivity.this, "suc", Toast.LENGTH_LONG).show();
                                builder.setTitle("Attendance stopped");
                                displayAlert("Attendance session stopped");
                                start.setEnabled(false);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //Toast.makeText(MainActivity.this, "s", Toast.LENGTH_LONG).show();
                //wifi.getConnectionInfo();
                StopAttendance stopAttendance = new StopAttendance(c_class, s_subject,ssi,"0",responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(stopAttendance);


            }

        }
        );

    }

    public void enterClass(){
       String text1 = "Enter class name";

        textToSpeech.speak(text1, TextToSpeech.QUEUE_FLUSH, null);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 11);
                } else {
                    Toast.makeText(getApplicationContext(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
                }

            }
        }, 1800);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    clas.setText(result.get(0));

                   // text1 = textView1.getText().toString();

                    //textToSpeech.speak(text1,TextToSpeech.QUEUE_FLUSH,null);
                    enterClass();
//                    record.setVisibility(View.GONE);
//                    speak.setVisibility(View.GONE);
//                    recordAgain.setVisibility(View.VISIBLE);

                }
                break;

            case 11:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    et1.setText(result.get(0));

                    askQuestion();

                    break;

                }

            case 12:

                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String question = result.get(0);

                    if(question.equals("yes")){
                        Log.d("Vijay1",question);
                        startAttendance();

                    }else{

                        String text1 = "You did not created Attendance";

                        textToSpeech.speak(text1, TextToSpeech.QUEUE_FLUSH, null);

                    }


                    break;

                }

        }

        //   @Override
//    protected void onStart() {
//        super.onStart();
////        text = textView.getText().toString();
////
////        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
//        Log.d("Vijay1","Inside onStart");
//        if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
//            Toast.makeText(getApplicationContext(),"Feature not supported in your device",Toast.LENGTH_SHORT);
//            Log.d("Vijay1","Inside if");
//        }else {
//            // text1 = et.getText().toString();
//            Log.d("Vijay1","Inside else");
//
//            text = textView.getText().toString();
//            Log.d("Vijay1",text);
//
//
//            textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
//        }
//    }

        //   @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d("Vijay1","Inside onResume");
//        if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
//            Toast.makeText(getApplicationContext(),"Feature not supported in your device",Toast.LENGTH_SHORT);
//            Log.d("Vijay1","Inside if");
//        }else {
//            // text1 = et.getText().toString();
//            Log.d("Vijay1","Inside else");
//
//            text = textView.getText().toString();
//            Log.d("Vijay1",text);
//
//
//            textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
//        }
//    }

    }

    public void askQuestion(){

        String text1 = "Do you want to create Attendance?";

        textToSpeech.speak(text1, TextToSpeech.QUEUE_FLUSH, null);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 12);
                } else {
                    Toast.makeText(getApplicationContext(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
                }

            }
        }, 1800);
    }

    public void startAttendance(){

        c_class = clas.getText().toString();
        s_subject = et1.getText().toString();
        Log.d("Vijay1",c_class);
        Log.d("Vijay1",s_subject);

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://pricyfy.esy.es/insert.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.d("Vijay1","here2");
                    Log.d("Vijay1",ssi);
                    //  Toast.makeText(MainActivity.this, "s", Toast.LENGTH_LONG).show();
                    JSONObject jsonObject1 =new JSONObject(response);
                    //Toast.makeText(MainActivity.this, "y", Toast.LENGTH_LONG).show();
                    boolean success = jsonObject1.getBoolean("success");
                    //Toast.makeText(MainActivity.this, "8", Toast.LENGTH_LONG).show();
                    if (!success) {
                        //Toast.makeText(MainActivity.this, "not suceess", Toast.LENGTH_LONG).show();
                        builder.setTitle("Error");
                        displayAlert(response);
                    } else {
                        //Toast.makeText(MainActivity.this, "suc", Toast.LENGTH_LONG).show();
                        builder.setTitle("Attendance started");
                        displayAlert("AP MODE CREATED");
                        //start.setEnabled(false);
                        WifiConfiguration wifiConfiguration = new WifiConfiguration();
                        // wifiConfiguration.status;
                        WifiApManager wi = new WifiApManager(getApplicationContext());
                        // boolean s = wi.isWifiApEnabled();
                        boolean s1 = wi.setWifiApEnabled(w, true);
                        stop.setEnabled(true);
                        create.setEnabled(false);
                        manual.setEnabled(true);

                        String text1 = "Your Attendance is created and your Hotspot name is "+ssi;

                        textToSpeech.speak(text1, TextToSpeech.QUEUE_FLUSH, null);


                        WifiManager wifimanager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        Method[] methods = wifimanager.getClass().getDeclaredMethods();
                        for (Method m : methods) {
                            if (m.getName().equals("getWifiApConfiguration")) {
                                try {
                                    WifiConfiguration config = (WifiConfiguration) m.invoke(wifimanager);
                                    String pp = config.SSID;
                                    handler = new Handler();
//                                    handler.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Log.d("Vijay1","Inside Handler");
//                                            stopAttendance();
//                                            String text = "Your Attendance has been stopped";
//                                            textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
//                                        }
//                                    },15000);

                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("err",error.toString());
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String, String>();
                hashMap.put("class",c_class);
                hashMap.put("subject",s_subject);
                hashMap.put("ssid",ssi);
                return hashMap;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(stringRequest);

    }




public void displayAlert(String message){
        builder.setMessage(message);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }

        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }


  }

