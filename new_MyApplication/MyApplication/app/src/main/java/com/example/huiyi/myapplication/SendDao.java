package com.example.huiyi.myapplication;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface SendDao {
    @Insert
    public long[] insertSentSMS(Send... sms);

    @Delete
    public void deleteSMS(Send sms);

    @Update
    public void updateSentSMS(Send sms);

    @Query("SELECT * FROM send WHERE send.id = :id")
    List<Send> getSentSMSById(long id);

    @Query("SELECT * FROM send")
    List<Send> getAllSMS();

    @Query("SELECT * FROM send,status WHERE send.id = status.sid and status.status = :status")
    List<Send> getSMSByStatus(String status);
}
