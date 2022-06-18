package com.inhatc.android_vision.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.inhatc.android_vision.BuildConfig;
import com.inhatc.android_vision.R;
import com.inhatc.android_vision.util.FirebaseLogin;
import com.inhatc.android_vision.util.KakaApplication;
import com.inhatc.android_vision.util.VisionApi;
import com.inhatc.android_vision.View.visionView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class visionViewMap extends AppCompatActivity {

    double lat = VisionApi.getLatitude();
    double lng = VisionApi.getLongitude();
    String lm_name = VisionApi.getLandmark_name();
    Toolbar myToolbar;

    String StaticMap = "https://maps.googleapis.com/maps/api/staticmap?center="+
            lat + "," + lng + "&zoom=17&size=400x400&key=";
    String ApiKey = BuildConfig.MAPS_API_KEY;;

    ImageView mapView;
    TextView txtLandmarkName;
    Button btnOpenMap;
    Button btnBackToView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision_view_map);

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");


        mapView = (ImageView) findViewById(R.id.mapView);

        String imageStr = StaticMap+ApiKey;
        Glide.with(this).load(imageStr).into(mapView);

        // 레이블
        txtLandmarkName = (TextView) findViewById(R.id.txtLandmarkName);
        txtLandmarkName.setText(lm_name);

        // 버튼
        btnOpenMap = (Button) findViewById(R.id.btnOpenMap);
        btnBackToView = (Button) findViewById(R.id.btnBackToView);

        btnOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("DefaultLocale")
                Uri geouri = Uri.parse(String.format("geo:%f,%f?q=%f,%f (%s)", lat, lng, lat, lng, lm_name));
                System.out.println(geouri);
                Intent geomap = new Intent(Intent.ACTION_VIEW, geouri);
                geomap.setPackage("com.google.android.apps.maps");  // 구글맵으로 열기
                startActivity(geomap);
            }
        });

        btnBackToView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

}