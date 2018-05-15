package com.example.huiyi.myapplication;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface SendDao {
    @Insert
    public long[] insertSentSMS(Send... sms);

    @Update
    public void updateSentSMS(Send sms);

    @Query("SELECT * FROM send WHERE send.id = :id")
    List<Send> getSentSMSById(long id);

    @Query("SELECT * FROM send WHERE send.content = :content")
    List<Send> getId(String content);

    @Query("SELECT * FROM send")
    List<Send> getAllSMS();
}
