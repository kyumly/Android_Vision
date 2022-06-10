package com.inhatc.android_vision.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inhatc.android_vision.Model.MemberVO;

public class DBConn {
    private FirebaseDatabase myFirebase;
    private DatabaseReference myDB_Reference = null;

    public DBConn(String path){
        myFirebase = FirebaseDatabase.getInstance();
        myDB_Reference = myFirebase.getReference(path);
    }

    public DatabaseReference getMyDB_Reference() {
        return myDB_Reference;
    }

    public FirebaseDatabase getMyFirebase() {
        return myFirebase;
    }

    public void setMyDB_Reference(DatabaseReference myDB_Reference) {
        this.myDB_Reference = myDB_Reference;
    }

    public void setMyFirebase(FirebaseDatabase myFirebase) {
        this.myFirebase = myFirebase;
    }



    public void insertMember(boolean bFlag, MemberVO vo,  String strKeyValue) {
        if(bFlag){
            myDB_Reference.child(strKeyValue).setValue(vo);
        }
    }

}
