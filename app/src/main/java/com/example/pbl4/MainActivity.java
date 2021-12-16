package com.example.pbl4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.pbl4.helper.RecordWavMaster;
import com.example.pbl4.model.login.LoginResponse;
import com.example.pbl4.utils.PreferenceUtils;
import com.example.pbl4.viewmodel.UserClient;


import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    final int REQUEST_PERMISSION_CODE=1000;
    ImageView imgAccount, imgHistory, imgMic, imgLog;
    LoginResponse mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        imgAccount = findViewById(R.id.img_account);
        imgHistory = findViewById(R.id.img_history);
        imgMic = findViewById(R.id.img_mic);
        imgLog = findViewById(R.id.img_log);

        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }
        Python py = Python.getInstance();
        final PyObject pyobj = py.getModule("xuli");

        if(CheckPermissionFromDevice()){
        }else{
            requestPermissions();
        }

        Intent intent = getIntent();
        if(intent.getExtras()!=null) {
            mAccount = (LoginResponse) intent.getSerializableExtra("data");
            mAccount.setActive(true);
            updateActive(mAccount);
        }
        imgAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ChangePass.class).putExtra("data",mAccount));
            }
        });
        imgHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,HistoryActivity.class).putExtra("data",mAccount));
            }
        });
        imgLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAccount.setActive(false);
                updateActive(mAccount);
                MainActivity.this.finish();
                System.exit(0);
            }
        });
        imgMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT).show();
                Record();
                Toast.makeText(getApplicationContext(), "end", Toast.LENGTH_SHORT).show();
                String path = getExternalFilesDir(null).getAbsolutePath()+"/myrecording2/test.wav";
                PyObject obj = pyobj.callAttr("predict",path);
                String activity = obj.toString();
                Toast.makeText(getApplicationContext(), activity, Toast.LENGTH_SHORT).show();
                Date currentTime = (Date) Calendar.getInstance().getTime();
                String date = DateFormat.format("EEE, d MMM yyyy HH:mm", currentTime).toString();
                String history = date+": "+activity+"\n";
                openApp(activity);
                if(activity.equals("gmail")||activity.equals("image")||activity.equals("mess")||activity.equals("phone")){
                    updateHistory(history);
                }
            }
        });
    }
    private void openApp(String activity) {
        if(activity.equals("gmail")){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            startActivity(intent);
        }
        else if(activity.equals("mess")){
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:"));
            sendIntent.putExtra("sms_body", "Body of Message");
            startActivity(sendIntent);
        }
        else if(activity.equals("phone")){
            Intent intent = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
            if(intent!=null) startActivity(intent);
        }
        else if(activity.equals("image")){
            Intent intent = new Intent(Intent.ACTION_VIEW, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if(intent!=null) startActivity(intent);
        }
    }

    public void updateActive(LoginResponse loginResponse){
        Call<LoginResponse> loginResponseCall = UserClient.getApi().putAccount(loginResponse.getId(),loginResponse);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                String message;
                if(response.isSuccessful()){
                    message = "Account is active...";
                }else{
                    message = "Something wrong...";
                }
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                String message = "Something wrong...";
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
            }
        });
    }
    public void updateHistory(String history){
        String now = mAccount.getHistory();
        mAccount.setHistory(now+history);
        Call<LoginResponse> loginResponseCall = UserClient.getApi().putAccount(mAccount.getId(),mAccount);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                String message;
                if(response.isSuccessful()){
                    message = "Success add new history...";
                }else{
                    message = "Something wrong...";
                }
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                String message = "Something wrong...";
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
            }
        });
    }
    public void Record(){
        String path = getExternalFilesDir(null).getAbsolutePath()+"/myrecording2";
        RecordWavMaster record = new RecordWavMaster(getApplicationContext(),path);
        record.recordWavStart();
        Handler handler=new Handler(Looper.getMainLooper());
        Runnable r=new Runnable()
        {
            public void run()
            {
                record.recordWavStop();
                record.releaseRecord();
            }
        };
        handler.postDelayed(r, 1000);
    }
    private void requestPermissions(){
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        },REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permisson Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permisson Deny", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }
    private boolean CheckPermissionFromDevice(){
        int write_external_storage = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        return write_external_storage == PackageManager.PERMISSION_GRANTED && record_audio_result == PackageManager.PERMISSION_GRANTED;
    }
}