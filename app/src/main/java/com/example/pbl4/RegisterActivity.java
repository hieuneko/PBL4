package com.example.pbl4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pbl4.model.register.RegisterRequest;
import com.example.pbl4.model.register.RegisterResponse;
import com.example.pbl4.viewmodel.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    Button btnSignUp;
    EditText edUsername, edEmail, edPassword, edCpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        btnSignUp = findViewById(R.id.bntSignup);
        edUsername = findViewById(R.id.edUsername);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        edCpassword = findViewById(R.id.edPasswordConfirm);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(edUsername.getText().toString())||TextUtils.isEmpty(edEmail.getText().toString())||TextUtils.isEmpty(edPassword.getText().toString())||TextUtils.isEmpty(edEmail.getText().toString())||TextUtils.isEmpty(edCpassword.getText().toString()))
                {
                    String message = "Điền đầy đủ thông tin...";
                    Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_LONG).show();
                }else{
                    RegisterRequest registerRequest = new RegisterRequest();
                    registerRequest.setUsername(edUsername.getText().toString());
                    registerRequest.setEmail(edEmail.getText().toString());
                    registerRequest.setPassword(edPassword.getText().toString());
                    registerRequest.setActive(false);
                    registerRequest.setHistory("");
                    registerUser(registerRequest);
                }
            }
        });
    }

    public void registerUser(RegisterRequest registerRequest){
        Call<RegisterResponse> registerResponseCall = UserClient.getApi().registerUser(registerRequest);
        registerResponseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if(response.isSuccessful()){
                    String message = "Thành công...";
                    Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    finish();
                }else{
                    String message = "Có lỗi xảy ra...";
                    Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_LONG).show();
            }
        });
    }
}