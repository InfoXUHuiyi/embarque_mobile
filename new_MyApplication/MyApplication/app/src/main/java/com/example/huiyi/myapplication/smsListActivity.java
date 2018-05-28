package com.example.huiyi.myapplication;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class smsListActivity extends AppCompatActivity {

    public static final String SEE_SMS_DETAILS = "com.example.huiyi.myapplication";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_list);

        Intent intent = getIntent();
        final String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView2);
        textView.setText(message);


        final Button see = (Button)findViewById(R.id.see_details);
        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDetails();
            }
        });

    }

    private void goToDetails(){
        final EditText smsId = (EditText)findViewById(R.id.sms_id);
        int mesId = Integer.parseInt(smsId.getText().toString());

        Intent intent = new Intent(this, smsDetailActivity.class);
        intent.putExtra(SEE_SMS_DETAILS, seeMessage(mesId).toString());
        startActivity(intent);

    }
    private StringBuilder seeMessage(int mid){
        /*
        search message from database by its id
         */
        StringBuilder s = new StringBuilder();
        String msg = "content:blablabla";
        String pho = "0600006666";
        String adr = "930 Route des Colles,06410 Biot";
        s.append(msg);
        s.append("\r\n");
        s.append(pho);
        s.append("\r\n");
        s.append(adr);


        return s;
    }

}
