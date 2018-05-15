package com.example.huiyi.myapplication;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ReceiveDao {
    @Insert
    public void insertReceivedSMS(Receive... sms);

    @Update
    public void updateReceivedSMS(Receive... sms);

    @Query("SELECT * FROM receive WHERE receive.sid = :sid")
    List<Receive> getReceivedSMSById(int sid);
}
