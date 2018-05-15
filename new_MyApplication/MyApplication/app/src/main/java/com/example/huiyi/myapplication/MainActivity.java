package com.example.huiyi.myapplication;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.SEND_SMS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {
    private SMSDatabase smsdb;

    private static final String TAG = "MyApplication";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smsdb = SMSDatabase.getDatabase(getApplicationContext());

        final EditText addrText = (EditText) findViewById(R.id.location);
        final Button openSMSButton = (Button) findViewById(R.id.open_sms);
        openSMSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = addrText.getText().toString();

                Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:5554"));
                smsIntent.putExtra("sms_body", content);

                if (smsIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(smsIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "INTENT NOT RESOLVED", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final Button sendSMSButton = (Button) findViewById(R.id.send_sms);
        sendSMSButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                smsButtonClicked();
            }
        });
    }

    private void smsButtonClicked() {
        if ((ContextCompat.checkSelfPermission(MainActivity.this, SEND_SMS) != PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(MainActivity.this, READ_CONTACTS) != PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { SEND_SMS, READ_CONTACTS },
                    REQUEST_SEND_SMS);
        } else {
            pickContact();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            try {
                ContentResolver cr = getContentResolver();
                Uri dataUri = data.getData();
                String[] projection = { ContactsContract.Contacts._ID };
                Cursor cursor = cr.query(dataUri, projection, null, null, null);

                if (null != cursor && cursor.moveToFirst()) {
                    String id = cursor
                            .getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String number = getPhoneNumber(id);
                    if (number == null) {
                        Toast.makeText(getApplicationContext(), "No number in contact", Toast.LENGTH_SHORT).show();
                    } else {
                        final EditText addrText = (EditText) findViewById(R.id.location);
                        sendSMS(addrText.getText().toString(), number);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Permission denied " + requestCode, Toast.LENGTH_SHORT).show();
            return;
        }

        switch(requestCode) {
            case REQUEST_SEND_SMS:
                pickContact();
                break;
            default:
                Toast.makeText(getApplicationContext(), "WRONG REQUEST CODE in Permissions", Toast.LENGTH_SHORT).show();
        }
    }

    private static final int REQUEST_SEND_SMS = 10;
    private static final int PICK_CONTACT_REQUEST = 20;

    private void pickContact() {
        Intent i = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, PICK_CONTACT_REQUEST);
    }

    private String getPhoneNumber(String id) {
        ContentResolver cr = getContentResolver();
        String where = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id;
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, where, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        return null;
    }

    private void sendSMS(String content, String number) {
        final Send send = new Send();
        send.content = content;

        new AsyncTask<Send, Void, Void>() {
            @Override
            protected Void doInBackground(Send... sends) {
                long[] result = smsdb.sendDao().insertSentSMS(sends);
                send.id = result[0];

                return null;
            }
        }.execute(send);


        final SmsManager smsManager = SmsManager.getDefault();
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... numbers) {
                //int sid = smsdb.sendDao().getId().get(0).content;
                String newcontent = "from database:" + smsdb.sendDao().getSentSMSById(send.id).get(0).content;

                smsManager.sendTextMessage(numbers[0], null, newcontent, null, null);

                return null;
            }
        }.execute(number);
    }
}
