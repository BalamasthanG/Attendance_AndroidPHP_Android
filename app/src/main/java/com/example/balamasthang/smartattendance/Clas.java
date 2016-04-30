package com.example.balamasthang.smartattendance;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Clas extends ListActivity {



    public int[] arr= new int[100];
    public int[] snt= new int[100];
    Button btn;
    ArrayList arrayList = new ArrayList(100);
    // DB Class to perform DB related operations
    DBController controller = new DBController(this);
    // Progress Dialog Object
    ProgressDialog prgDialog;
    HashMap<String, String> queryValues;

   // public String[] name = new String[100];
   static String Uname;

    Button next;
    int i=0;

   static String clas;

    ArrayList<HashMap<String, String>> userList;
    String flag = "fal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.clas);
        // Get User records from SQLite DB
         userList = controller.getAllUsers();

        Intent i = getIntent();
        clas = (String)i.getSerializableExtra("class");
        Uname = (String)i.getSerializableExtra("name");


        // If users exists in SQLite DB
        if (userList.size() != 0) {
            // Set the User Array list in ListView
                flag="tru";

            ListAdapter adapter = new SimpleAdapter(Clas.this, userList, R.layout.view_user_entry, new String[]{
                    "stuId", "stuName"}, new int[]{R.id.userId, R.id.userName,});






            setListAdapter(adapter);

        }

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Transferring Data from Remote MySQL DB and Syncing SQLite. Please wait...");
        prgDialog.setCancelable(false);

        /*Intent alarmIntent = new Intent(getApplicationContext(), SampleBC.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        // Alarm Manager calls BroadCast for every Ten seconds (10 * 1000), BroadCase further calls service to check if new records are inserted in
        // Remote MySQL DB
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 5000, 10 * 1000, pendingIntent);*/

        btn = (Button) findViewById(R.id.refresh);

        syncSQLiteMySQLDB();

       /* btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                syncSQLiteMySQLDB();
            }
        });*/

        next = (Button)findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*controller.Delete();
                reloadActivity();*/

                Intent sel = new Intent(Clas.this,Selected.class);

                sel.putExtra("uname",Uname);


                startActivity(sel);
                Toast.makeText(getApplicationContext(),Uname+" "+clas,Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        // super.onListItemClick(l, v, position, id);

      /* new AlertDialog.Builder(this)
                .setTitle("Student")
                .setMessage("from " + getListView().getItemAtPosition(position))
               .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                .show();*/



        if(arr[position]==0)
        {
            l.getChildAt(position).setBackgroundColor(Color.rgb(204,255,153));
            arr[position]=1;
            controller.update(1, (position + 1));
            snt[i]=position;
            i++;
            getListView().getItemAtPosition(position);

            Toast.makeText(Clas.this,
                    "YoU hAVe SeLEcted "  + "\n" +

                            "Roll NO: " + String.valueOf(position)+ "\n" +
                            "Name " + getListView().getItemAtPosition(position),
                    Toast.LENGTH_LONG).show();
        }
        else{

            l.getChildAt(position).setBackgroundColor(Color.WHITE);
            arr[position]=0;
            controller.update(0, (position + 1));
            i--;
            snt[i]= 0;
            getListView().getItemAtPosition(position).toString();



            Toast.makeText(Clas.this,
                    "YoU clr SeLEcted "  + "\n" +

                            "Roll NO: " + String.valueOf(position)+ "\n" +
                            "Name " + getListView().getItemAtPosition(position),
                    Toast.LENGTH_LONG).show();
        }





    }





    // Options Menu (ActionBar Menu)
    @Override
  /*  public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    // When Options Menu is selected


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        // When Sync action button is clicked
        if (id == R.id.refresh) {
            // Transfer data from remote MySQL DB to SQLite on Android and perform Sync
            syncSQLiteMySQLDB();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to Sync MySQL to SQLite DB
    public void syncSQLiteMySQLDB() {
        // Create AsycHttpClient object
        Toast.makeText(getApplicationContext(),clas+" "+Uname,Toast.LENGTH_LONG).show();
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        params.put("flag",flag);
        params.put("class",clas);
        // Show ProgressBar
        prgDialog.show();
        // Make Http call to getusers.php
        //noinspection deprecation
        client.post("http://192.168.173.1/sqlitemysqlsync/getusers.php", params, new AsyncHttpResponseHandler() {

            public void onSuccess(String response) {
                // Hide ProgressBar
                prgDialog.hide();
                // Update SQLite DB with response sent by getusers.php
                updateSQLite(response);
            }

            // When error occured
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // TODO Auto-generated method stub
                // Hide ProgressBar
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]" + statusCode,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void updateSQLite(String response) {
        ArrayList<HashMap<String, String>> usersynclist;
        usersynclist = new ArrayList<HashMap<String, String>>();
        // Create GSON object
        Gson gson = new GsonBuilder().create();
        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            // If no of array elements is not zero
            if (arr.length() != 0) {
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object

                    JSONObject obj = (JSONObject) arr.get(i);
                    System.out.println(obj.get("userId"));
                    System.out.println(obj.get("userName"));
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<String, String>();
                    // Add userID extracted from Object

                    queryValues.put("userId", obj.get("userId").toString());
                    // Add userName extracted from Object
                    queryValues.put("userName", obj.get("userName").toString());
                    // Insert User into SQLite DB
                    controller.insertUser(queryValues);
                    HashMap<String, String> map = new HashMap<String, String>();
                    // Add status for each User in Hashmap
                    map.put("Id", obj.get("userId").toString());
                    map.put("status", "1");
                    usersynclist.add(map);
                }
                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
                updateMySQLSyncSts(gson.toJson(usersynclist));
                // Reload the Main Activity
               reloadActivity();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Method to inform remote MySQL DB about completion of Sync activity
    public void updateMySQLSyncSts(String json) {
        System.out.println(json);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("syncsts", json);
        // Make Http call to updatesyncsts.php with JSON parameter which has Sync statuses of Users
        client.post("http://192.168.173.1/sqlitemysqlsync/updatesyncsts.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(), "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Reload MainActivity
    public void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), Clas.class);
        objIntent.putExtra("name",Uname);
        startActivity(objIntent);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        controller.updateall(0,1);
        controller.Delete();
    }



}
