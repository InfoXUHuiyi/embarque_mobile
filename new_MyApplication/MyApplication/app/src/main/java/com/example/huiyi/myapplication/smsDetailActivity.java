package com.example.huiyi.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class smsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        String adr = strs[2];

        final Button deleteButton = (Button)findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                delete message here
                 */







            }
        });

        final Button reSentButton = (Button)findViewById(R.id.resent);
        reSentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                re-sent message
                 */
                SmsManager smsManager =SmsManager.getDefault();
                smsManager.sendTextMessage(phone,null,message,null,null);
                Toast.makeText(getApplicationContext(), "message has been sent", Toast.LENGTH_SHORT).show();


            }
        });

        final Button locateButton = (Button)findViewById(R.id.locate);
        locateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String adr = "";
                Intent geoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + adr));

                if (geoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(geoIntent);
                }
            }
        });
    }
}
