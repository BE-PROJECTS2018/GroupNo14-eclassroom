package com.example.dell.Educators_App;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class AddManually extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    AlertDialog.Builder builder;
    EditText name,roll_no;
    Button addRow,submit;

    //for blind student this AddManually Class file helps teacher to manually enter the students attendance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manually);

        lvItems = (ListView)findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);



        name = (EditText)findViewById(R.id.name);
        roll_no = (EditText)findViewById(R.id.roll_no);
        addRow = (Button)findViewById(R.id.addRow);
        submit = (Button)findViewById(R.id.submit);
        builder = new AlertDialog.Builder(AddManually.this);

    }

    public void addRow(View view) {
        // this function helps teacher to add more than onr student
        String rollno = roll_no.getText().toString();
        String name1 = name.getText().toString();
        itemsAdapter.add(name1 +"   "+rollno);
        roll_no.setText("");
        name.setText("");

    }

    public void onSubmit(View view){

        //this function when executed submits manual attendance done by teacher to the server
        builder.setTitle("Attendance Result");
       // builder.setMessage("Attendance marked successfully");
       // builder.show();
        displayAlert("Attendance marked successfully");
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

//public void onSubmit(){
//    RequestQueue requestQueue = Volley.newRequestQueue(AddManually.this);
//    StringRequest stringRequest = new StringRequest(Request.Method.GET, "", new Response.Listener<String>() {
//        @Override
//        public void onResponse(String response) {
//
//        }
//    }, new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//
//            Toast.makeText(AddManually.this, "Error", Toast.LENGTH_SHORT).show();
//
//        }
//    }){
//        protected Map<String, String> getParams() throws AuthFailureError {
//            HashMap<String,String> hashMap = new HashMap<String, String>();
//            // hashMap.put("",);
//            //  hashMap.put("",);
//            return hashMap;
//        }
//    };
//
//    requestQueue.add(stringRequest);
//}
