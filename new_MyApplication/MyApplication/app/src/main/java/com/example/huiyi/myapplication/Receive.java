package com.example.huiyi.myapplication;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = {@Index(value = "sid", unique = true)})
public class Receive {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int sid;
    public String number;
    public String content;
    public String recu;
    public String ack;
    public String cle2;
    public String code_envoye;
}
