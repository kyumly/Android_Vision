package com.inhatc.android_vision.View;

import static android.os.SystemClock.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.inhatc.android_vision.MainActivity;
import com.inhatc.android_vision.R;
import com.inhatc.android_vision.util.FirebaseLogin;
import com.inhatc.android_vision.util.KakaApplication;
import com.inhatc.android_vision.util.VisionApi;

import java.io.InputStream;

public class visionView extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CODE = 101;
    private static final int GALLERY_IMAGE_CODE = 1;


//    Thread thread = new Thread();


    VisionApi vision;
    ImageView imageView;
    Bitmap img;
    Toolbar myToolbar;


    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision_view);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");


        imageView = (ImageView) findViewById(R.id.imageView);
        Button btnGallery = (Button) findViewById(R.id.btnGallery);
        Button btnCamera = (Button) findViewById(R.id.btnCamera);
        Button btnOpenMap = (Button) findViewById(R.id.btnMoveToMap);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GALLERY_IMAGE_CODE);
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CODE);
            }
        });

        btnOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(VisionApi.chkApi) {
                    if (!VisionApi.confirm) {
                        Toast.makeText(visionView.this, "해당 랜다마크는 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        VisionApi.confirm = false;
                        VisionApi.chkApi = false;
                        Intent intent = new Intent(visionView.this, visionViewMap.class);
                        startActivity(intent);
                    }
                }
                else{
                    Toast.makeText(visionView.this, "검색 중 입니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    
    /*로그아웃 세션 처리 완료*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        boolean result = false;
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Toast.makeText(this, "로그아웃", Toast.LENGTH_LONG).show();
                KakaApplication.KakaoLogout();
                FirebaseLogin.FirebaseLogout();
                finish();
                result =  true;
                break;
        }
        return result;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_IMAGE_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    vision = new VisionApi();
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);


                    // 이미지 표시
                    imageView.setImageBitmap(img);
                    in.close();

                    vision.visionApi(img);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == REQUEST_IMAGE_CODE) {
            if (resultCode == RESULT_OK) {
                try{
                    Bundle extras = data.getExtras();
                    img = (Bitmap) extras.get("data");
                    vision.visionApi(img);
                    imageView.setImageBitmap(img);
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

}