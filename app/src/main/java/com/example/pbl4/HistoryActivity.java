package com.example.pbl4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.pbl4.model.login.LoginResponse;

public class HistoryActivity extends AppCompatActivity {

    TextView tvHistory;
    LoginResponse mAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        if(intent.getExtras()!=null){
            mAccount = (LoginResponse) intent.getSerializableExtra("data");
        }

        tvHistory = findViewById(R.id.tv_history);
        tvHistory.setText(mAccount.getHistory());
    }
}