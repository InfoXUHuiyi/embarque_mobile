package com.example.huiyi.myapplication;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface StatusDao {
    @Insert
    public void insertStatus(Status status);

    @Update
    public void updateStatus(Status status);

    @Query("SELECT * FROM status WHERE status.sid = :sid")
    List<Status> getStatusById(int sid);
}