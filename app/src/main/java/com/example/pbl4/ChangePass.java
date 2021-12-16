package com.example.pbl4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pbl4.model.login.LoginResponse;
import com.example.pbl4.viewmodel.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePass extends AppCompatActivity {

    EditText edCurrpass, edPass, edCpass;
    LoginResponse mAccount;
    Button bntSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        getSupportActionBar().hide();

        edCurrpass = findViewById(R.id.edCurrPass);
        edPass = findViewById(R.id.edPassword);
        edCpass = findViewById(R.id.edPasswordConfirm);
        bntSave = findViewById(R.id.bnt_change);

        Intent intent = getIntent();
        if(intent.getExtras()!=null){
            mAccount = (LoginResponse) intent.getSerializableExtra("data");
        }

        bntSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(edPass.getText().toString())||TextUtils.isEmpty(edCurrpass.getText().toString())||TextUtils.isEmpty(edCpass.getText().toString())){
                        String message = "Điền đầy đủ thông tin...";
                        Toast.makeText(ChangePass.this,message,Toast.LENGTH_LONG).show();
                }
                else {
                    if(TextUtils.isEmpty(edPass.getText().toString())!=TextUtils.isEmpty(edCpass.getText().toString())){
                        String message = "Xác nhận mật khẩu sai...";
                        Toast.makeText(ChangePass.this,message,Toast.LENGTH_LONG).show();
                    }
                    else{
                        mAccount.setPassword(edPass.getText().toString());
                        updatePassword(mAccount);
                    }
                }
            }
        });
    }
    public void updatePassword(LoginResponse loginResponse){
        Call<LoginResponse> loginResponseCall = UserClient.getApi().putAccount(loginResponse.getId(),loginResponse);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    String message = "Thay đổi thành công...";
                    Toast.makeText(ChangePass.this,message,Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    String message = "Có lỗi xảy ra...";
                    Toast.makeText(ChangePass.this,message,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                String message = "Có lỗi xảy ra...";
                Toast.makeText(ChangePass.this,message,Toast.LENGTH_LONG).show();
            }
        });
    }
}