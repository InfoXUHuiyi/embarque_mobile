package com.example.huiyi.myapplication;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = {@Index(value = "id", unique = true)})
public class Send {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String content;
    public int cle1;
//    public String envoye;
//    public String ack_recu;
//    public String code_recu;

}
