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

    private void sendSMS(String content, String number) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, content, null, null);
    }

}


