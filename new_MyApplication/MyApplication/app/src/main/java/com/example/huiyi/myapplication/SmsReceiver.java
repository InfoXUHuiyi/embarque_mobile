package com.example.huiyi.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    private SMSDatabase smsdb;// = SMSDatabase.getDatabase(context);
    private static final String TAG ="SmsService";
    private String content;
    private String sender;
    public static String adrSender;
    private static String messagecode = "";
//    public static String smsRecu = "";

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

                    //接收到信息的联系人号码
                    sender = msg.getDisplayOriginatingAddress();
                    //接收到的信息内容
                    content = msg.getDisplayMessageBody();
                    adrSender = sender;

                    //如果收到密文，则发送一个带有相同id的ACK
                        if(!content.contains("ACK:messege received /?") && !content.contains("KEY /?")){
                            messagecode = content;
                            int begin = content.indexOf("?")+1;
                            String subContent = content.substring(begin);
                            String ack = "ACK:messege received /?"+subContent;
                            sendSMS(ack,sender, 0);

                            /*
                            *   待完成：将密文存进数据库
                            */




                    //如果收到和自己发送的密文的id一样的ACK，那就发送相同id的密钥KEY
                        }else if(content.contains("ACK:messege received /?") && !content.contains("KEY /?")){
                            int begin = content.indexOf("?")+1;
                            String subContent = content.substring(begin);
                            int sid = Integer.parseInt(subContent);
                            String info = "KEY /?"+subContent+":";
//                            System.out.println(subContent);
//                            System.out.println(sid);
                            sendSMS(info,sender, sid);

                            /*
                             *   待完成：将接收到的ACK存进数据库
                             */




                    //如果收到对方发来的密钥，并且密钥id和收到的密文id一样，那就解密
                        }else if(content.contains("KEY") && !content.contains("ACK:messege received")){
                            /*
                             *   待完成：将接收到的KEY存进数据库
                             */




                            /**
                             * 解密
                             * @param input 数据源（被加密后的数据）
                             * @param key 秘钥，即偏移量
                             * @return 返回解密后的数据
                             */
//                            System.out.println(messagecode);

                            int begin = content.indexOf("?")+1;
                            int end = content.indexOf(":");
                            String subContent1 = content.substring(begin,end);
                            String subContent2 = content.substring(end+1);
                            int sid = Integer.parseInt(subContent1);
                            int clef = Integer.parseInt(subContent2);
//                            System.out.println("{"+sid+"}");
//                            System.out.println("{"+clef+"}");
                            int mesbegin = messagecode.indexOf(":")+1;
                            int mesend = messagecode.indexOf("/");
//                            int meskey = messagecode.indexOf("?");
                            String messageWorld = messagecode.substring(mesbegin,mesend);
//                            int messageKey = Integer.parseInt(messagecode.substring(meskey)+1);
//                            if(clef == messageKey){
                                Toast.makeText(context,decrypt(messageWorld,clef), Toast.LENGTH_LONG).show();
//                            }


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
//            send.content = "KEY:";
            send.content = content;
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


