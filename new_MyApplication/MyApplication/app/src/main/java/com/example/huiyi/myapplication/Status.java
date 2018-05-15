package com.example.huiyi.myapplication;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = {@Index(value = "sid", unique = true)})
public class Status {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int sid;
    public String sms;
    public String status;
}
