package com.inhatc.android_vision.util;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class KakaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        KakaoSdk.init(this, "95106610c6d143563112cf2541e4b51c");
    }
}
