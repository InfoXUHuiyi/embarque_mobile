package com.example.huiyi.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG ="SmsService";
    private String content;
    private String sender;

    @Override
    public void onReceive(Context context, Intent intent) {
//            Log.i(TAG, "new messege");
//        Toast.makeText(context,"new sms", Toast.LENGTH_LONG).show();
        Bundle bundle = intent.getExtras();//get content of ntent
        SmsMessage msg = null;

        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)){
            if (bundle != null) {
                Object[] objects = (Object[]) bundle.get("pdus");//get content of bundle
                for (Object object : objects) {
                    msg = SmsMessage.createFromPdu((byte[]) object);
//                    Date date = new Date(msg.getTimestampMillis());//time
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String receiveTime = format.format(date);

                    sender = msg.getDisplayOriginatingAddress();
                    content = msg.getDisplayMessageBody();

                        if(!content.contains("ACK:messege received") && !content.contains("KEY:")){
                            sendSMS("ACK:messege received",sender);
                        }else if(content.contains("ACK:messege received")){
                            //将10换成我们加密的密钥
                            String info = "KEY:"+"10";
                            sendSMS(info,sender);
                        }else if(content.contains("KEY:")){
                            //解密方法写在这里


                            Toast.makeText(context,"new sms", Toast.LENGTH_LONG).show();
                        }
                    abortBroadcast();
                }
            }
        }

    }

    private void sendSMS(String content, String number) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, content, null, null);
    }

}


