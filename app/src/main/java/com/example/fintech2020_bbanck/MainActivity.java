package com.example.fintech2020_bbanck;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fintech2020_bbanck.function.Alert;
import com.example.fintech2020_bbanck.function.Register_fingerprint;
import com.example.fintech2020_bbanck.function.Transfer;
import com.example.fintech2020_bbanck.model.User;
import com.example.fintech2020_bbanck.network.SSL_Connection;
import com.example.fintech2020_bbanck.network.SendRequest;

import java.util.HashMap;

public class MainActivity extends Activity {

    private Button btn_register;
    private Button btn_transfer;
    private TextView tv_balance;
    private long backKeyPressedTime;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toast =  Toast.makeText(MainActivity.this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);

        init();
        setOnBtnClickListener();
    }

    private void setOnBtnClickListener() {
        //송금하기
        btn_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_balance.getText().toString() == "0"){
                    Alert.alert_function(MainActivity.this, "no_money");
                }else{
                    Intent intent = new Intent(getApplicationContext(), Transfer.class);
                    startActivityForResult(intent,1004);
                }
            }
        });

        //지문등록하기
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register_fingerprint.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        btn_register = (Button)findViewById(R.id.btn_register);
        btn_transfer= (Button)findViewById(R.id.btn_transfer);
        tv_balance =(TextView) findViewById(R.id.tv_balance);
    }

    public void get_balance() {
        int balance = Integer.parseInt(tv_balance.getText().toString());
        tv_balance.setText(String.valueOf(balance-1000));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            if(resultCode == 1004) {
                Alert.alert_function(MainActivity.this, "transfer");
                get_balance();
            } else {}
        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast.show();
        }
        else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            // send(String url, int method, final HashMap<String, String> hashMap, Context context)
            SendRequest sendRequest = new SendRequest();
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("session_key", User.getInstance().getSessionKey());
            System.out.println("CHECK : "+hashMap);
            sendRequest.send("https://"+ SSL_Connection.getSSLConnection().getUrl()+"/logout",
                    1, hashMap, MainActivity.this);
        }
    }

    public void onFinish() {
        finish();
        toast.cancel();
    }
}
