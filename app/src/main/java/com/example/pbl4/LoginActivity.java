package com.example.pbl4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pbl4.model.login.LoginResponse;
import com.example.pbl4.utils.PreferenceUtils;
import com.example.pbl4.viewmodel.UserClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText edUsername, edPassword;
    private TextView noAccount;
    private List<LoginResponse> listLogin;
    public LoginResponse mUser;
    public CheckBox mCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        btnLogin = findViewById(R.id.bntLogin);
        edUsername = findViewById(R.id.et_user);
        edPassword = findViewById(R.id.et_password);
        noAccount = findViewById(R.id.tvCreateAccount);
        mCheck = findViewById(R.id.check_remember);

        listLogin = new ArrayList<>();

        mCheck.setChecked(PreferenceUtils.getCheck(this));
        edUsername.setText(PreferenceUtils.getEmail(this));
        edPassword.setText(PreferenceUtils.getPassword(this));


        getListUser();
        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(edUsername.getText().toString())||TextUtils.isEmpty(edPassword.getText().toString()))
                {
                    String message = "Điền đầy đủ thông tin...";
                    Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();
                }else{
                    loginUser();
                }
            }
        });

    }
    public void loginUser(){
        String strUsername = edUsername.getText().toString().trim();
        String strPassword = edPassword.getText().toString().trim();
        boolean isHasUser=false;
        for(LoginResponse user: listLogin){
            if(strUsername.equals(user.getEmail())&&strPassword.equals(user.getPassword())){
                isHasUser=true;
                mUser = user;
                break;
            }
        }
        if(isHasUser){
            if(mCheck.isChecked()){
                PreferenceUtils.saveCheck(true,this);
                PreferenceUtils.saveEmail(strUsername,this);
                PreferenceUtils.savePassword(strPassword,this);
            }else{
                PreferenceUtils.saveCheck(false,this);
                PreferenceUtils.saveEmail("",this);
                PreferenceUtils.savePassword("",this);
            }
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("data",mUser);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(LoginActivity.this,"user name or password isvalid",Toast.LENGTH_LONG).show();
        }
    }
    public void getListUser(){
        Call<List<LoginResponse>> list =UserClient.getApi().loginUser();
        list.enqueue(new Callback<List<LoginResponse>>() {
            @Override
            public void onResponse(Call<List<LoginResponse>> call, Response<List<LoginResponse>> response) {
                listLogin = response.body();
            }

            @Override
            public void onFailure(Call<List<LoginResponse>> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"error",Toast.LENGTH_LONG).show();
            }
        });
    }
}