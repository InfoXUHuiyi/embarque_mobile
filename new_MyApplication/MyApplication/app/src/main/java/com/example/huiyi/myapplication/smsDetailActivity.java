package com.example.huiyi.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class smsDetailActivity extends AppCompatActivity {
    private SMSDatabase smsdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        smsdb = SMSDatabase.getDatabase(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_detail);

        Intent intent = getIntent();
        String string = intent.getStringExtra(smsListActivity.SEE_SMS_DETAILS);
        String[] strs = string.split("\r\n");
        final String message = strs[0];
        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.sms_detail);
        textView.setText(message);

        final String phone = strs[1];
        // Capture the layout's TextView and set the string as its text
        TextView textView1 = findViewById(R.id.number);
        textView1.setText(phone);

        final String adr = strs[2];
        final String sid = strs[3];
//        String key = strs[4];

        final Button deleteButton = (Button)findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* delete message here*/
                new AsyncTask<String, Void, Void>() {
                    @Override
                    protected Void doInBackground(String... sids) {
                        long smsId = Integer.parseInt(sids[0]);
                        Send deleteSMS = smsdb.sendDao().getSentSMSById(smsId).get(0);
                        smsdb.sendDao().deleteSMS(deleteSMS);

                        return null;
                    }
                }.execute(sid);

            }
        });

        final Button reSentButton = (Button)findViewById(R.id.resent);
        reSentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                re-sent message
                 */
                String newmessage = "from database:" + message +"/?" + sid;
                SmsManager smsManager =SmsManager.getDefault();
                smsManager.sendTextMessage(phone,null,newmessage,null,null);
                Toast.makeText(getApplicationContext(), "message has been sent", Toast.LENGTH_SHORT).show();


            }
        });

        final Button locateButton = (Button)findViewById(R.id.locate);
        locateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent geoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + adr));

                if (geoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(geoIntent);
                }
            }
        });
    }


}
