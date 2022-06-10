package com.inhatc.android_vision.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.inhatc.android_vision.Model.MemberVO;
import com.inhatc.android_vision.R;
import com.inhatc.android_vision.util.DBConn;

import java.sql.SQLOutput;
import java.util.regex.Pattern;

public class MemberJoin extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase myFirebase;
    DatabaseReference myDB_Reference = null;
    String strKeyValue = "Info";

    private boolean checkName;
    private boolean checkNumber;
    private boolean checkLength;


    Button btnJoin;
    Button btnIdTest;
    EditText edtUserId;
    EditText edtPassword;
    EditText edtEmail;
    EditText edtPasswordConfirm;
    EditText edtName;

    EditText edtNumber;

    DBConn Conn = null;

    MemberVO member;

    int valid = 0;
    String value  =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_join);

        myFirebase = FirebaseDatabase.getInstance();
        myDB_Reference = myFirebase.getReference();


        btnJoin = (Button) findViewById(R.id.btnJoin);
        btnIdTest = (Button) findViewById(R.id.btnIdConfirm);

        edtName = (EditText)findViewById(R.id.edtName);
        edtPasswordConfirm = (EditText)findViewById(R.id.edtPasswordConfirm);
        edtUserId = (EditText) findViewById(R.id.edtUserId);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtNumber = (EditText)findViewById(R.id.edtNumber);

        btnIdTest.setOnClickListener(this);
        btnJoin.setOnClickListener(this);



        Conn = new DBConn("memberInfo");
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnJoin:
                if(valid != 1){
                    Toast.makeText(this, "아이디 중복을 확인해 주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!edtPassword.getText().toString().equals(edtPasswordConfirm.getText().toString())){
                    Toast.makeText(this, "비밀번호가 다르다.", Toast.LENGTH_LONG).show();
                    return;
                }

                checkInfo();
                if(!checkLength){
                    Toast.makeText(this, "빈 공간이 있습니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!checkName){
                    Toast.makeText(this, "이름에 영어가 있습니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!checkNumber){
                    Toast.makeText(this, "번호 양식이 잘못되었습니다.(EX)010-XXXX-XXXX)", Toast.LENGTH_LONG).show();
                    return;
                }

                strKeyValue = edtUserId.getText().toString();
                member = new MemberVO(edtPassword.getText().toString(), edtEmail.getText().toString(), edtName.getText().toString());
                Conn.insertMember(true, member, strKeyValue);
                Intent call = getIntent();
                call.putExtra("result", "가입을 축하합니다.");
                setResult(RESULT_OK,call);
                finish();

                break;
            case R.id.btnIdConfirm:
                strKeyValue = edtUserId.getText().toString();
                Conn.getMyDB_Reference().child(strKeyValue).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                         if(null == snapshot.getValue()){
                             valid = 1;
                             Toast.makeText(MemberJoin.this, "사용가능한 아이디 입니다.", Toast.LENGTH_LONG).show();
                         }else Toast.makeText(MemberJoin.this, "중복되는 아이디 입니다.", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            default:
                break;
        }

    }

    private void checkInfo() {
        if(edtUserId.getText().length() == 0 || edtName.getText().length() == 0 || edtPassword.getText().length() == 0 ||
                edtEmail.getText().length() == 0|| edtPasswordConfirm.getText().length() == 0 || edtNumber.getText().length() == 0){
            checkLength =  false;
        }else {
            checkLength = true;
        }
        if(edtName.getText().toString().matches("\".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")){
            checkName = false;
        }else{
            checkName=true;
        }
        if(Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", edtNumber.getText().toString().trim())){
            checkNumber = true;
        }else{
            checkNumber = false;
        }
    }
}

