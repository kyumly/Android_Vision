package com.inhatc.android_vision;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.inhatc.android_vision.View.visionView;
import com.inhatc.android_vision.View.MemberJoin;
import com.inhatc.android_vision.util.DBConn;
import com.inhatc.android_vision.util.FirebaseLogin;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;


public class MainActivity extends AppCompatActivity {

    private Button btnMember;
    private Button btnLogin;
    private View kakaoLoginBtn;
    public static FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    EditText edtUserId;
    EditText edtPassword;

    String userId = null;
    String password = null;

    DBConn conn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        btnMember=(Button) findViewById(R.id.btnMemberJoin);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        edtUserId = (EditText)findViewById(R.id.userId);
        edtPassword = (EditText)findViewById(R.id.passwd);

        btnMember.setOnClickListener(memberJoin);
        btnLogin.setOnClickListener(login);

        kakaoLoginBtn = findViewById(R.id.kakaoLogin);

        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>(){
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if(oAuthToken != null){
                    //로그인이 된다

                }
                if(throwable != null){
                    //오류값
                }
                updateKakaoLoginUi();
                return null;
            }
        };

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(MainActivity.this, visionView.class);
                    startActivity(intent);
                    finish();
                } else {
                }
            }
        };


        kakaoLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserApiClient .getInstance().isKakaoTalkLoginAvailable(MainActivity.this)){
                    UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this, callback);
                }else{
                    UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this, callback);
                }
            }
        });

        updateKakaoLoginUi();
    }

    private void updateKakaoLoginUi() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if(user != null){
                    Intent imageIntent = new Intent(MainActivity.this, visionView.class);
                    startActivity(imageIntent);
                }else{
                    kakaoLoginBtn.setVisibility(View.VISIBLE);
                }
                return null;
            }
        });
    }


    View.OnClickListener memberJoin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(MainActivity.this, MemberJoin.class);
            startActivityForResult(i, 1);
        }
    };

    View.OnClickListener login = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            userId = edtUserId.getText().toString();
            password = edtPassword.getText().toString();

            System.out.println(userId);
            loginUser(userId, password);


//            conn = new DBConn("memberInfo");
//            conn.getMyDB_Reference().child(userId).child("password").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(null ==snapshot.getValue()) Toast.makeText(MainActivity.this, "존재하는 아이디가 없습니다.", Toast.LENGTH_LONG).show();
//                    else {
//                        String dbPassword = snapshot.getValue().toString();
//                        if(password.equals(dbPassword)){
//                            Intent imageIntent = new Intent(MainActivity.this, visionView.class);
//                            startActivity(imageIntent);
//
//                        }else Toast.makeText(MainActivity.this, "비밀번호가 틀렸습니다..", Toast.LENGTH_LONG).show();
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });

        }
    };

    private void loginUser(String userId, String password) {
        mAuth.signInWithEmailAndPassword(userId, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    mAuth.addAuthStateListener(firebaseAuthListener);
                }else{

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int req, int resultCode, Intent data){
        super.onActivityResult(req, resultCode, data);
        if(resultCode == 1){
            String result = data.getStringExtra("result");
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }


}

/*
        kakaoLoginBtn = findViewById(R.id.kakaoLogin);
        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>(){
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if(oAuthToken != null){
                    //로그인이 된다

                }
                if(throwable != null){
                    //오류값
                }
                updateKakaoLoginUi();
                return null;
            }
        };

        kakaoLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserApiClient .getInstance().isKakaoTalkLoginAvailable(MainActivity.this)){
                    UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this, callback);
                }else{
                    UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this, callback);
                }
            }
        });



    private void updateKakaoLoginUi() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if(user != null){
                    Intent imageIntent = new Intent(MainActivity.this, ImageView.class);
                    startActivity(imageIntent);
                }else{
                    kakaoLoginBtn.setVisibility(View.VISIBLE);
                }
                return null;
            }
        });
    }
 */