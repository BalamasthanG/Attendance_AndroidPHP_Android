package com.example.balamasthang.smartattendance;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Balamasthan G on 14-12-2015.
 */
public class Next extends Activity {


     TextView v1,v2;
        Button at,bt;
    String s;
        @SuppressLint("SimpleDateFormat")
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.next);



            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy  hh:mm:ss E");
           // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String crdt = df.format(c.getTime());
            bt = (Button)findViewById(R.id.button);

            v1=(TextView)findViewById(R.id.textView4);

            v1.setText(crdt);

            v2=(TextView)findViewById(R.id.textView3);

            Intent intent = getIntent();
            final String receivedName = (String) intent.getSerializableExtra("USERNAME");
            v2.setText("Welcome " + receivedName);
           final  String cs1 = (String) intent.getSerializableExtra("cs1");
          final   String cs2 = (String) intent.getSerializableExtra("cs2");
            at=(Button)findViewById(R.id.button2);
            at.setText(cs1);
            bt.setText(cs2);
             s = (String)intent.getSerializableExtra("USERNAME");
            at.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {

                    Intent i= new Intent(Next.this,Clas.class);
                    i.putExtra("class", cs1);
                    i.putExtra("name",s+cs1);

                    startActivity(i);
                    Toast.makeText(getApplication(),s,Toast.LENGTH_LONG).show();

                }
            });
            bt.setOnClickListener(new View.OnClickListener(){
                public void onClick(View arg0){
                    Intent i= new Intent(Next.this,Clas.class);
                    i.putExtra("class",cs2);
                    i.putExtra("name",s+cs2);

                    startActivity(i);
                    Toast.makeText(getApplication(),s,Toast.LENGTH_LONG).show();

                }
            });



        }


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you want to log out ")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                               // System.exit(0);

                                Intent intent = new Intent(Intent.ACTION_MAIN);
                               // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                              //  intent.addCategory(Intent.CATEGORY_HOME);
                                startActivity(intent);
                                /*Intent in = new Intent(Next.this,MainActivity.class);
                                startActivity(in);*/


                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                })
                .show();

    }






}
