package com.inhatc.android_vision.util;

import android.app.Application;
import android.content.Intent;

import com.inhatc.android_vision.MainActivity;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class KakaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        KakaoSdk.init(this, "95106610c6d143563112cf2541e4b51c");
    }


    public static void KakaoLogout(){
        UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
            @Override
            public Unit invoke(Throwable throwable) {
                FirebaseLogin.FirebaseLogout();
                return null;
            }
        });
    }
}
