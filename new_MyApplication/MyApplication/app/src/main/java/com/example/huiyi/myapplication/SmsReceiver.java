package com.example.huiyi.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    private SMSDatabase smsdb;
    private static final String TAG ="SmsService";
    private String content;
    private String sender;
    public static String adrSender;

    @Override
    public void onReceive(Context context, Intent intent) {
//            Log.i(TAG, "new messege");
//        Toast.makeText(context,"new sms", Toast.LENGTH_LONG).show();
        Bundle bundle = intent.getExtras();//get content of ntent
        SmsMessage msg = null;
        smsdb = SMSDatabase.getDatabase(context);

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
                    adrSender = sender;

                        if(!content.contains("ACK:messege received") && !content.contains("KEY:")){
                            sendSMS("ACK:messege received",sender, 0);
                        }else if(content.contains("ACK:messege received")){
                            //将10换成我们加密的密钥
                            String info = "KEY:";
                            int sid = 1;
                            sendSMS(info,sender, sid);
                        }else if(content.contains("KEY:")){
                            //解密方法写在这里
                            /**
                             * 解密
                             * @param input 数据源（被加密后的数据）
                             * @param key 秘钥，即偏移量
                             * @return 返回解密后的数据
                             */



                            Toast.makeText(context,"new sms", Toast.LENGTH_LONG).show();
                        }
                    abortBroadcast();
                }
            }
        }

    }

    public static String decrypt(String input, int key) {
        //得到字符串里的每一个字符
        char[] array = input.toCharArray();
        for (int i = 0; i < array.length; ++i) {
            //字符转换成ASCII 码值
            int ascii = array[i];
            //恢复字符偏移，例如b->a
            ascii = ascii - key;
            //ASCII 码值转换为char
            char newChar = (char) ascii;
            //替换原有字符
            array[i] = newChar;
        }

        //字符数组转换成String
        return new String(array);
    }

    private void sendSMS(String content, String number, int sid) {
        final SmsManager smsManager = SmsManager.getDefault();
        if(sid > 0){
            final Send send = new Send();
            send.content = "KEY:";
            send.id = sid;
            new AsyncTask<String, Void, Void>() {
                @Override
                protected Void doInBackground(String... numbers) {
                    //int sid = smsdb.sendDao().getId().get(0).content;
                    String key = send.content + String.valueOf(smsdb.sendDao().getSentSMSById(send.id).get(0).cle1);
                    smsManager.sendTextMessage(numbers[0], null, key, null, null);

                    return null;
                }
            }.execute(number);
        }
        else{
            smsManager.sendTextMessage(number, null, content, null, null);
        }
    }

}


