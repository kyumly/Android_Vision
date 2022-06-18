package com.inhatc.android_vision.util;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.inhatc.android_vision.MainActivity;

public class FirebaseLogin extends Activity {
    public static FirebaseAuth mAuth;
    //public static FirebaseAuth KakaomAuth;

    public static void FirebaseLogout(){
        mAuth.signOut();
    }
}
