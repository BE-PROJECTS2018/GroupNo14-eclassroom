package com.zhaw.facerecognitionnew.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zhaw.facerecognitionnew.R;

public class VerifyingActivity extends AppCompatActivity implements View.OnClickListener
{
    WifiManager wifi;
    ListView lv;
    TextView textStatus;
    Button buttonScan;
    int size = 0;
    List<ScanResult> results;

    String ITEM_KEY = "key";
    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapter;
    RequestQueue mRequestQueue;
    Button markAttendance;

    /* Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifying);

        textStatus = (TextView) findViewById(R.id.textStatus);
        buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(this);
        lv = (ListView)findViewById(R.id.list);

        mRequestQueue = Volley.newRequestQueue(this);

        markAttendance = (Button) findViewById(R.id.markAttendance);
        markAttendance.setEnabled(false);
        markAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(VerifyingActivity.this,RecognitionActivity.class);
                startActivity(in);
            }
        });

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }
        this.adapter = new SimpleAdapter(VerifyingActivity.this, arraylist, R.layout.row, new String[] { ITEM_KEY }, new int[] { R.id.list_value });
        lv.setAdapter(this.adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                markAttendance.setEnabled(false);
                String ssid = results.get(position).SSID;
                String queryUrl = "http://pricyfy.esy.es/verify_ssid.php?ssid="+ssid;
                //String queryUrl = "http://pricyfy.esy.es/index.php";
                Log.d("query",queryUrl);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, queryUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        TextView tv = (TextView) findViewById(R.id.textStatus);
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();

                        if(response.equals("true")){
                            tv.setText("Verified");
                            tv.setTextColor(Color.GREEN);
                            markAttendance.setEnabled(true);
                        }else{
                            tv.setText("Not Verified");
                            tv.setTextColor(Color.RED);
                        }
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                mRequestQueue.add(stringRequest);
            }
        });

        registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context c, Intent intent)
            {
                results = wifi.getScanResults();
                size = results.size();
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    public void onClick(View view)
    {
        arraylist.clear();
        wifi.startScan();

        Toast.makeText(this, "Scanning...." + size, Toast.LENGTH_SHORT).show();
        try
        {
            int temp = 0;
            //size = size - 1;
            while (temp<size)
            {
                HashMap<String, String> item = new HashMap<String, String>();
                item.put(ITEM_KEY, results.get(temp).SSID + "  " + results.get(temp).capabilities);

                arraylist.add(item);
                //Log.d("array",arraylist.get(temp).toString());
                temp++;
                adapter.notifyDataSetChanged();
            }
        }
        catch (Exception e)
        { }
    }
}

