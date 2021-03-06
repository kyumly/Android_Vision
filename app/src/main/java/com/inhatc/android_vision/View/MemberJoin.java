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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inhatc.android_vision.MainActivity;
import com.inhatc.android_vision.Model.MemberVO;
import com.inhatc.android_vision.R;
import com.inhatc.android_vision.util.DBConn;
import com.inhatc.android_vision.util.FirebaseLogin;

import java.util.regex.Pattern;

public class MemberJoin extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase myFirebase;

    DatabaseReference myDB_Reference = null;
    String strKeyValue = "Info";

    Object chkId;


    private boolean checkName;
    private boolean checkNumber;
    private boolean checkLength;
    private boolean checkPassword;
    private boolean checkEamil;

    boolean result = false;


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

    int valid_id = 0;
    boolean chkOneTime = false;

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
                if(valid_id != 1){
                    Toast.makeText(this, "????????? ????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!edtPassword.getText().toString().equals(edtPasswordConfirm.getText().toString())){
                    Toast.makeText(this, "??????????????? ?????????.", Toast.LENGTH_SHORT).show();
                    return;
                }

                checkInfo();
                if(!checkLength){
                    Toast.makeText(this, "??? ????????? ????????????.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!checkName){
                    Toast.makeText(this, "????????? ???????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!checkNumber){
                    Toast.makeText(this, "????????? ????????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!checkPassword){
                    Toast.makeText(this, "???????????? (??????, ????????????, ?????? ?????? 8??? ??????)", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!checkEamil){
                    Toast.makeText(this, "????????? ????????? ???????????????:)", Toast.LENGTH_LONG).show();
                    return;
                }

                createUser(edtEmail.getText().toString(), edtPassword.getText().toString());

                if(result){
                    Intent i  = new Intent(this, MainActivity.class);
                    strKeyValue = edtUserId.getText().toString();
                    member = new MemberVO(edtPassword.getText().toString(), edtEmail.getText().toString(), edtName.getText().toString());
                    Conn.insertMember(true, member, strKeyValue);
                    startActivity(i);

                    Toast.makeText(this, "??????????????? ?????????????????????.", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "?????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                }
                break;
                
            case R.id.btnIdConfirm:
                strKeyValue = edtUserId.getText().toString();
                if(chkOneTime){
                    return;
                }else{
                    Conn.getMyDB_Reference().child(strKeyValue).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            chkId = snapshot.getValue();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                valiId(chkId);
                break;

            default:
                break;
        }

    }

    private void valiId(Object id){
        try{
            Thread.sleep(1000);
            if(id == null){
                valid_id = 1;
                Toast.makeText(this, "?????? ????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                return;
            }else{
                valid_id = 0;
                Toast.makeText(this, "???????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                return;
            }
        }catch (Exception ex){
            System.out.println(ex);
        }

    }

    private void checkInfo() {
        if(edtUserId.getText().length() == 0 || edtName.getText().length() == 0 || edtPassword.getText().length() == 0 ||
                edtEmail.getText().length() == 0|| edtPasswordConfirm.getText().length() == 0 || edtNumber.getText().length() == 0){
            checkLength =  false;
        }else {
            checkLength = true;
        }
        if(edtName.getText().toString().matches(".*[???-??????-??????-???]+.*")){
            checkName = true;
        }else{
            checkName=false;
        }
        if(Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", edtNumber.getText().toString().trim())){
            checkNumber = true;
        }else{
            checkNumber = false;
        }

        if(Pattern.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", edtPassword.getText().toString())){
            checkPassword = true;
        }else{
            checkPassword = false;
        }
        if(Pattern.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", edtEmail.getText().toString()))
        {
            checkEamil = true;
        }else{
            checkEamil=false;
        }
    }


    private void createUser(String email, String password) {
        FirebaseLogin.mAuth = FirebaseAuth.getInstance();
        FirebaseLogin.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // ???????????? ??????
                            result = true;
                        } else {
                            // ???????????? ??????
                            result = false;
                        }
                    }
                });
    }
}