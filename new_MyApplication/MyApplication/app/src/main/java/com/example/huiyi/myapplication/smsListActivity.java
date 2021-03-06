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
                        item.put("number", sms.number);
                        item.put("content", sms.content);
                        item.put("address", sms.address);
                        item.put("date", sms.date);
                        item.put("cle1", sms.cle1);
                        data.add(item);
                    }
                }

                return null;
            }
        }.execute(status);

        SimpleAdapter spa = new SimpleAdapter(this, data, R.layout.list_item,
                new String[]{"id","number","content","date"}, new int[]{R.id.id,R.id.number,R.id.content,R.id.date});

        list_show.setAdapter(spa);
    }

    private void getDetials(String sid){
        String number = sid;

        final Intent intent = new Intent(this, smsDetailActivity.class);

        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... sids) {
                long sid = Integer.parseInt(sids[0]);
                Send sms = smsdb.sendDao().getSentSMSById(sid).get(0);

                final StringBuilder s = new StringBuilder();

                s.append(sms.content);
                s.append("\r\n");
                s.append(sms.number);
                s.append("\r\n");
                //s.append(sms.address);
                s.append("1047 chemin des ames du purgatoire");
                s.append("\r\n");
                s.append(sms.id);
                s.append("\r\n");
                s.append(sms.cle1);

                intent.putExtra(SEE_SMS_DETAILS, s.toString());
                startActivity(intent);

                return null;
            }
        }.execute(sid);

    }

    public final class list_listener implements AdapterView.OnItemClickListener {
        public String sid;

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {

            ListView lView = (ListView)arg0;
            HashMap sms = (HashMap)lView.getItemAtPosition(arg2);

            this.sid = sms.get("id").toString();
            getDetials(sid);
        }
    }
}
