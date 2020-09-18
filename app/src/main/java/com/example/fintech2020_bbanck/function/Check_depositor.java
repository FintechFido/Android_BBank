package com.example.fintech2020_bbanck.function;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fintech2020_bbanck.R;
import com.example.fintech2020_bbanck.model.User;
import com.example.fintech2020_bbanck.network.CooconConnection;
import com.example.fintech2020_bbanck.network.SSL_Connection;
import com.example.fintech2020_bbanck.network.SendRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Check_depositor extends Activity {

    private String depositor_bank;
    private String price;
    private String account;
    private String name;
    private ProgressBar progressBar;
    private Button btn;
    private Spinner spinner;

    private String url = "dev.checkpay.co.kr/HKT_API_301.jct";

    private long backKeyPressedTime = 0;
    private Toast toast;

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
            sendRequest.send("https://"+ SSL_Connection.getSSLConnection().getUrl()+"/logout",
                    1, hashMap, Check_depositor.this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_depositor);

        btn = (Button)findViewById(R.id.btn_transfer_auth);

        depositor_bank = getIntent().getExtras().getString("depositor_bank").toString();
        price = getIntent().getExtras().getString("price").toString();
        account = getIntent().getExtras().getString("account").toString();
        name = getIntent().getExtras().getString("name").toString();
        progressBar = (ProgressBar)findViewById(R.id.check_depositor_progressBar);
        spinner = (Spinner)findViewById(R.id.spinner_saved_select);

        confirm();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transfer_auth();
            }
        });
    }

    public void confirm()
    {
        // 입금자가 존재하는지 확인
        // 서버에 확인 요청하기. 없으면 알람뜨고 뒤로가게 만들어야 함
        // send(String url, int method, final HashMap<String, String> hashMap, Context context)
        SendRequest sendRequest = new SendRequest();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("name", name);
        hashMap.put("bank_number", "1"); // 강제로 넣어주었음
        hashMap.put("account_number", account);
        System.out.println("CHECK : "+hashMap);
        sendRequest.send("https://"+ SSL_Connection.getSSLConnection().getUrl()+"/user/valid",
                1, hashMap, Check_depositor.this);
    }

    public void onResult(boolean result)
    {
        progressBar.setVisibility(View.GONE);

        if(result) {
            TextView textView = (TextView)findViewById(R.id.tv_confirm);
            textView.setText(name+" 님에게 송금하겠습니다");
            btn.setClickable(true);
        }
        else {
            Intent intent = new Intent();
            setResult(3000,intent);
            finish();
        }
    }

    public void onDepositResult(boolean result)
    {
        if(result) {
            SSL_Connection ssl_connection = SSL_Connection.getSSLConnection();
            ssl_connection.postHttps(1000, 1000);
            Intent intent = new Intent();
            setResult(1004,intent);
            finish();
        }
        else {
            Intent intent = new Intent();
            setResult(3000,intent);
            finish();
        }
    }

    public void transfer_auth() {
        // 송금 및 인증

        // 지문 정보가 존재하는지 확인
        Call_HIDO call_hido = new Call_HIDO();
        if(call_hido.exist_check(Check_depositor.this)) {
            Intent intent = new Intent();
            intent.setClassName("com.example.fintech_hido","com.example.fintech_hido.function.Fingerprint_function");
            intent.putExtra("mode", "auth_check");
            intent.putExtra("session_key", User.getInstance().getSessionKey());
            intent.putExtra("imei",User.getInstance().getImei());
            intent.putExtra("running",User.getInstance().getRunningCode());
            intent.putExtra("saved",spinner.getSelectedItem().toString()+"");
            startActivityForResult(intent,4000);
        }
        else{
            Alert.alert_function(Check_depositor.this, "exist");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.hasExtra("result")) {
            if (resultCode == 4000) {
                if (data.getExtras().getString("result").equals("true")) {
                    Date date = new Date();
                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
                    String today = sdfDate. format(date);
                    SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss",Locale.KOREA);
                    String now = sdfTime.format(date);

                    java.util.Random generator = new java.util.Random();
                    generator.setSeed(System.currentTimeMillis());
                    int randomInt = generator.nextInt(1000000) % 1000000;

                    CooconConnection cooconConnection = CooconConnection.getSsl_connection();
                    cooconConnection.postHttps(1000, 1000);

                    SendRequest sendRequest = new SendRequest();
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("OGN_CD", "00000000");
                    hashMap.put("CUST_ID", name);
                    hashMap.put("TRAN_DT", today);
                    hashMap.put("TRAN_TM", now);
                    hashMap.put("TRAN_DIV", "301");
                    hashMap.put("TRAN_SEQ", String.valueOf(randomInt));
                    hashMap.put("BNK_CD", "003");
                    hashMap.put("ACCT_NO", account);
                    hashMap.put("TRAN_AMT", price);
                    System.out.println("CHECK : "+hashMap);
                    sendRequest.send("https://"+ url,
                            1, hashMap, Check_depositor.this);
                } else {
                    Alert.alert_function(Check_depositor.this, "fail");
                }
            } else {
                Alert.alert_function(Check_depositor.this, "fail");
            }
        }
        else {
            Alert.alert_function(Check_depositor.this, "fail");
        }
    }

}
