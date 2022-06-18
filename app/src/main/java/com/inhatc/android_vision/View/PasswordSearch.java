package com.inhatc.android_vision.View;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.inhatc.android_vision.MainActivity;
import com.inhatc.android_vision.R;
import com.inhatc.android_vision.util.DBConn;
import com.inhatc.android_vision.util.FirebaseLogin;

public class PasswordSearch extends AppCompatActivity implements View.OnClickListener {
    Button btnSearch;
    Button btnReturn;

    EditText edtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_search);
        edtPassword = (EditText) findViewById(R.id.edtEmail);

        btnSearch = (Button) findViewById(R.id.btnPassword);
        btnReturn = (Button) findViewById(R.id.btnReturn);

        btnSearch.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.btnPassword:
                String email = edtPassword.getText().toString();
                FirebaseLogin.mAuth.sendPasswordResetEmail(email).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(PasswordSearch.this, "이메일을 전송 했습니다.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(PasswordSearch.this, "이메일이 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                break;
            case R.id.btnReturn:
                finish();
                break;
        }
    }
}