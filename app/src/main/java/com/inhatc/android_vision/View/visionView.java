package com.inhatc.android_vision.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.inhatc.android_vision.MainActivity;
import com.inhatc.android_vision.R;
import com.inhatc.android_vision.util.VisionApi;

import java.io.ByteArrayOutputStream;

public class visionView extends AppCompatActivity {

    private FirebaseAuth auth;
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision_view);
        //img = (ImageView) findViewById(R.id.imageView);
        auth = MainActivity.mAuth;
        VisionApi a=new VisionApi();
        Resources res = getResources();
        a.visionApi(res);

    }

}
