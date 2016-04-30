package com.example.balamasthang.smartattendance;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

     AutoCompleteTextView Ename;
    EditText passwrd;
    int rslt;

    Button sign;
   // public static final String Attn = "Attn";
    public static final String name = "nameKey";
 //   public static final String pass = "passwordKey";
    //SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Ename = (AutoCompleteTextView)findViewById(R.id.email);

        passwrd =(EditText)findViewById(R.id.password);

        sign = (Button)findViewById(R.id.email_sign_in_button);




    }



    private boolean checkNetwork(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnected()){
            return true;
        }else {
            return false;
        }
    }

    public void login(View view) {
        Toast.makeText(getApplicationContext(),"Clicked ",Toast.LENGTH_LONG).show();

        if(checkNetwork()) {
            // SharedPreferences.Editor editor = sharedPreferences.edit();
            String username = Ename.getText().toString().trim();
            String password = passwrd.getText().toString();

            /* editor.putString(name, username);
            editor.putString(pass, password);
            editor.commit();*/

            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();





            if(!(username.trim().equals(""))){
                if(!(password.trim().equals(""))){
                    //  prgDialog.show();

                    params.put("username",username);
                    params.put("password",password);
                    client.post("http://192.168.173.1/sqlitemysqlsync/android_sign.php",params ,new AsyncHttpResponseHandler() {



                        @Override
                        public void onSuccess(String response) {
                            System.out.println(response);
                            //prgDialog.hide();
                            try {

                                 JSONArray arr = new JSONArray(response);
                                // JSONObject obj = new JSONObject(response);

                                   JSONObject obj = (JSONObject)arr.get(0);
                                  System.out.println(obj.get("result"));
                                  System.out.println(obj.get("cls1"));
                                    System.out.println(obj.get("cls2"));

                                // controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());
                               // rslt = obj.get("result").toString();
                                rslt = Integer.parseInt(obj.get("result").toString());
                                String cs1 = obj.getString("cls1").toString();
                                String cs2 = obj.getString("cls2").toString();
                                if(rslt == 1){
                                    Toast.makeText(getApplicationContext(),rslt+"  Success login"+cs1+" "+cs2,Toast.LENGTH_LONG).show();
                                    Intent next = new Intent(MainActivity.this, Next.class);
                                    startActivity(next);
                                    Intent intObj = new Intent(MainActivity.this,Next.class);
                                    String s=Ename.getText().toString();
                                    intObj.putExtra("USERNAME",s);
                                    intObj.putExtra("cs1",cs1);
                                    intObj.putExtra("cs2", cs2);
                                    intObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                    startActivity(intObj);
                                    finish();
                                }else if (rslt == 0){
                                    Toast.makeText(getApplicationContext(),rslt+"  Not Registered user "+cs1+" "+cs2, Toast.LENGTH_LONG).show();
                                }


                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                Toast.makeText(getApplicationContext(), rslt+" Error Occured [Server's JSON response might be invalid]! ", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Throwable error,
                                              String content) {
                            // TODO Auto-generated method stub
                            // prgDialog.hide();
                            if(statusCode == 404){
                                Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                            }else if(statusCode == 500){
                                Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Please enter Username", Toast.LENGTH_SHORT).show();
            }







        }
        else {
            Toast.makeText(this, "No network available", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onBackPressed() {
      // super.onDestroy();
        finish();
       // System.exit(0);
        Intent intent = new Intent(Intent.ACTION_MAIN);
       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


}
