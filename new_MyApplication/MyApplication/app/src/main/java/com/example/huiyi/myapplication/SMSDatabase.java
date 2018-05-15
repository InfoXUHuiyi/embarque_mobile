package com.example.huiyi.myapplication;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Send.class, Receive.class, Status.class}, version = 3, exportSchema = false)
public abstract class SMSDatabase extends RoomDatabase{
    public abstract SendDao sendDao();
    public abstract ReceiveDao receiveDao();
    public abstract StatusDao statusDao();

    private static SMSDatabase INSTANCE;

    public static SMSDatabase getDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SMSDatabase.class, "sms_db").fallbackToDestructiveMigration().build();
        }
        return  INSTANCE;
    }

}
