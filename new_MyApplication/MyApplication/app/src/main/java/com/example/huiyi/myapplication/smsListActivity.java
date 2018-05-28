package com.example.huiyi.myapplication;

import android.arch.lifecycle.LiveData;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class smsListActivity extends AppCompatActivity {
    private SMSDatabase smsdb;
    private ListView list_show;

    public static final String SEE_SMS_DETAILS = "com.example.huiyi.myapplication";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        smsdb = SMSDatabase.getDatabase(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_list);

        Intent intent = getIntent();
        //final String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        final String status = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        // Capture the layout's TextView and set the string as its text



        TextView textView = findViewById(R.id.textView2);
        textView.setText(status);
        final Button see = (Button)findViewById(R.id.see_details);
        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDetails();
            }
        });

        list_show = (ListView) this.findViewById(R.id.list_show);
        list_show.setOnItemClickListener(new list_listener());

        final List<HashMap<String ,Object>> data = new ArrayList<HashMap<String , Object>>();

        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... types) {
                if(types[0] == "failed") {
                    List<Send> messages = smsdb.sendDao().getSMSByStatus(types[0]);
                }else{
                    List<Send> messages = smsdb.sendDao().getAllSMS();

                    for (Send sms : messages) {
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("id", sms.id);
                        //item.put("number", sms.number);
                        item.put("content", sms.content);
                        //item.put("date", sms.date);
                        data.add(item);
                    }
                }

                return null;
            }
        }.execute(status);

        SimpleAdapter spa = new SimpleAdapter(this, data, R.layout.list_item,
                new String[]{"id","content"}, new int[]{R.id.id,R.id.content});

        list_show.setAdapter(spa);
    }

    private void goToDetails(){
        final EditText smsId = (EditText)findViewById(R.id.sms_id);
        int mesId = Integer.parseInt(smsId.getText().toString());
        Intent intent = new Intent(this, smsDetailActivity.class);
        intent.putExtra(SEE_SMS_DETAILS, seeMessage(mesId).toString());
        startActivity(intent);

    }

    private StringBuilder seeMessage(long mid){
        /*new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... numbers) {
                //int sid = smsdb.sendDao().getId().get(0).content;
                String newcontent = "from database:" + smsdb.sendDao().getSentSMSById(send.id).get(0).content + "/?" + send.id;
                smsManager.sendTextMessage(numbers[0], null, newcontent, null, null);

                return null;
            }
        }.execute(number);*/

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

    private void viewDetails(String sid){
        int mesId = Integer.parseInt(sid);
        Intent intent = new Intent(this, smsDetailActivity.class);
        intent.putExtra(SEE_SMS_DETAILS, seeMessage(mesId).toString());
        startActivity(intent);
    }

    public final class list_listener implements AdapterView.OnItemClickListener {
        public String sid;

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {

            ListView lView = (ListView)arg0;
            HashMap sms = (HashMap)lView.getItemAtPosition(arg2);

            this.sid = sms.get("id").toString();
            viewDetails(sid);
        }
    }
}