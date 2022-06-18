package com.inhatc.android_vision.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.inhatc.android_vision.R;
import com.inhatc.android_vision.util.DBConn;

public class EmailSearch extends AppCompatActivity implements View.OnClickListener {


    Button btnJoin;
    Button btnId;
    DBConn conn = null;
    EditText edtId;
    TextView textId;
    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_search);

        textId = (TextView) findViewById(R.id.txtIdSearch);
        btnId = (Button)findViewById(R.id.btnIdSearch) ;
        btnJoin = (Button) findViewById(R.id.btnReturn);
        txtResult = (TextView)findViewById(R.id.txtResult);

        edtId = (EditText)findViewById(R.id.edtIdsearch);
        btnJoin.setOnClickListener(this);
        btnId.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.btnIdSearch:
                String userId = edtId.getText().toString();
                conn = new DBConn("memberInfo");
                conn.getMyDB_Reference().child(userId).child("email").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(null ==snapshot.getValue()) Toast.makeText(EmailSearch.this, "존재하는 아이디가 없습니다.", Toast.LENGTH_LONG).show();
                        else {
                            String userEmail = snapshot.getValue().toString();
                            txtResult.setText("회원님에 이메일 입니다. : "+userEmail);
                            }
                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                break;
            case R.id.btnReturn:
                finish();
                break;
        }
    }
}