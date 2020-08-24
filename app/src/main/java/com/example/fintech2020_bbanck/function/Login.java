package com.example.fintech2020_bbanck.function;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.fintech2020_bbanck.R;
import com.example.fintech2020_bbanck.network.SSL_Connection;
import com.example.fintech2020_bbanck.network.SendRequest;

import java.util.HashMap;

public class Login extends Activity
{
    private EditText et_id;
    private EditText et_passwd;
    private Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // UI 초기화
        et_id = (EditText)findViewById(R.id.et_id);
        et_passwd = (EditText)findViewById(R.id.et_passwd);
        btn_login = (Button)findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 데이터 전송하고 콜백 함수 대기하기
                login_function();
            }
        });
    }

    private void login_function()
    {
        final String url = "https://"+ SSL_Connection.getSSLConnection().getUrl()+"/login";
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("id",et_id.getText().toString());
        hashMap.put("passwd",et_passwd.getText().toString());

        SendRequest sendRequest = new SendRequest();
        sendRequest.send(url,1,hashMap, Login.this);

    }
}

