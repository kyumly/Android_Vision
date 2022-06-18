package com.inhatc.android_vision;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.inhatc.android_vision.View.EmailSearch;
import com.inhatc.android_vision.View.PasswordSearch;
import com.inhatc.android_vision.View.visionView;
import com.inhatc.android_vision.View.MemberJoin;
import com.inhatc.android_vision.util.FirebaseLogin;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


        private Button btnMember;
        private Button btnLogin;
        private View kakaoLoginBtn;

        private TextView txtIdSearch;
        private TextView txtPasswordSearch;

        EditText edtEmail;
        EditText edtPassword;

        String email = null;
        String password = null;


        //화면 만들기
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            btnMember=(Button) findViewById(R.id.btnMemberJoin);
            btnLogin = (Button) findViewById(R.id.btnLogin);

            edtEmail = (EditText)findViewById(R.id.userId);
            edtPassword = (EditText)findViewById(R.id.passwd);
            btnMember.setOnClickListener(memberJoin);
            btnLogin.setOnClickListener(login);
            kakaoLoginBtn = findViewById(R.id.kakaoLogin);

            txtIdSearch = (TextView) findViewById(R.id.txtId);
            txtPasswordSearch = (TextView) findViewById(R.id.txtPassword);

            txtIdSearch.setOnClickListener(this);
            txtPasswordSearch.setOnClickListener(this);



            //카카오톡 로그인 토큰 확인하기
            Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>(){
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if(oAuthToken != null){
                    updateKakaoLoginUi();

                }
                if(throwable != null){
                    return null;
                }
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
            updateKakaoLoginUi();
        }

        private void updateKakaoLoginUi() {
            UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                @Override
                public Unit invoke(User user, Throwable throwable) {
                    if(user != null){
                        Intent imageIntent = new Intent(MainActivity.this, visionView.class);
                        FirebaseLogin.mAuth = FirebaseAuth.getInstance();
                        signInAnonymously();
                        Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                        startActivity(imageIntent);
                    }else{
                        kakaoLoginBtn.setVisibility(View.VISIBLE);
                    }
                    return null;
                }
            });
        }
    
        /*파이어베이스 익명 로그인*/
        private void signInAnonymously() {
            FirebaseLogin.mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseLogin.mAuth.getCurrentUser();
                    } else {
                    }
                }
            });
        }



        //회원가입
        View.OnClickListener memberJoin = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MemberJoin.class);
                startActivity(i);
            }
        };


        //로그인 버튼 이벤트
        View.OnClickListener login = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();

                loginUser(email, password);

            }
        };


        
        //로그인 하기
        public void loginUser(String email, String password){
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(MainActivity.this, "이메일 또는 비밀번호가 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseLogin.mAuth = FirebaseAuth.getInstance();
            FirebaseLogin.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this, visionView.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(MainActivity.this, "이메일 비밀번호가 틀렸습니다. ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }



        //이메일&비번찾기
        @Override
        public  void onClick(View v){
            Intent i = null;
            switch (v.getId()){

                case R.id.txtId:
                    i = new Intent(MainActivity.this, EmailSearch.class);
                    startActivity(i);
                    break;
                case R.id.txtPassword:
                    i = new Intent(MainActivity.this, PasswordSearch.class);
                    startActivity(i);
                    break;
            }
        }

}