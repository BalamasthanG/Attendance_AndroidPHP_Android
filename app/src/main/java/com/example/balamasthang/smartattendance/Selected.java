package com.example.balamasthang.smartattendance;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Balamasthan  G on 03-02-2016.
 */
public class Selected extends Activity {
   // ArrayList arrayList = new ArrayList(100);
  ListView l;
  // TextView t;
    Button btn;
    String css;
 ProgressDialog prgDialog;
    DBController selist  = new DBController(this);
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
     setContentView(R.layout.selected);
       l=(ListView)findViewById(R.id.list);
       // t=(TextView)findViewById(R.id.textView);

        //t.setText(selist.getAllsel());

        btn = (Button)findViewById(R.id.send);
        /*Intent in = getIntent();
        int[] i = in.getIntArrayExtra("stu");*/

        Intent i = getIntent();
        css = (String)i.getSerializableExtra("uname");
        ArrayList<HashMap<String, String>>  userList = selist.getAllsel();

        Integer b = selist.dbSyncCount();
        String s = b.toString();

        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
     try {
         if (userList.size() != 0) {
             ListAdapter adapter = new SimpleAdapter(Selected.this, userList, R.layout.view_user_entry, new String[]{
                     "stuId", "stuName"}, new int[]{R.id.userId, R.id.userName});
            l.setAdapter(adapter);
            // setListAdapter(adapter);
             // Toast.makeText(getApplicationContext(),"yess",Toast.LENGTH_LONG).show();
         }

     }
     catch (Exception  e)
     {
         Toast.makeText(getApplicationContext(),"HELLO",Toast.LENGTH_LONG);
     }

      // l.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, st));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncSQLiteMySQLDB();
                Toast.makeText(getApplicationContext(),"This",Toast.LENGTH_LONG).show();
            }
        });

    }
    //@Override
    /*public void onDestroy(){
        super.onDestroy();
        selist.updateall(0,1);
    }*/






   public void syncSQLiteMySQLDB(){

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        ArrayList<HashMap<String, String>> userList =  selist.getAllsel();
        if(userList.size()!=0){
            if(selist.dbSyncCount() != 0){
              //  prgDialog.show();
                params.put("usersJSON", selist.send());
                params.put("class",css);
                client.post("http://192.168.173.1/sqlitemysqlsync/getfrmand.php",params ,new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        //prgDialog.hide();
                        try {
                            //JSONArray arr = new JSONArray(response);
                            JSONObject rs = new JSONObject(response);
                           // System.out.println(arr.length());
                           // for(int i=0; i<arr.length();i++){
                               // JSONObject obj = (JSONObject)arr.get(i);
                               /* System.out.println(obj.get("id"));
                                System.out.println(obj.get("status"));*/
                               // controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());
                           // }
                            Toast.makeText(getApplicationContext(), "DB Sync completed!"+response.toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // TODO Auto-generated method stub
                       // prgDialog.hide();
                        if(statusCode == 404){
                            Toast.makeText(getApplicationContext(), "Requested resource not found ", Toast.LENGTH_LONG).show();
                        }else if(statusCode == 500){
                            Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]"+css, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else{
                Toast.makeText(getApplicationContext(), "SQLite and Remote MySQL DBs are in Sync!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "No data in SQLite DB, please do enter User name to perform Sync action", Toast.LENGTH_LONG).show();
        }
    }


}
