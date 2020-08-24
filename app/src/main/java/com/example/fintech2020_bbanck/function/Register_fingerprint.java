package com.example.fintech2020_bbanck.function;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.fintech2020_bbanck.R;
import com.example.fintech2020_bbanck.model.User;
import com.example.fintech2020_bbanck.network.SSL_Connection;
import com.example.fintech2020_bbanck.network.SendRequest;

import java.util.HashMap;

public class Register_fingerprint extends Activity {

    private long backKeyPressedTime = 0;
    private Toast toast = Toast.makeText(Register_fingerprint.this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);

    public Register_fingerprint()
    {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        register_function();
    }

    public void register_function()
    {
        Call_HIDO call_hido = new Call_HIDO();
        if(call_hido.exist_check(Register_fingerprint.this)) {
            Intent intent = new Intent();
            intent.setClassName("com.example.fintech_hido","com.example.fintech_hido.function.Fingerprint_function");
            intent.putExtra("mode", "register");
            intent.putExtra("session_key", User.getInstance().getSessionKey());
            intent.putExtra("imei",User.getInstance().getImei());
            intent.putExtra("running",User.getInstance().getRunningCode());
            startActivityForResult(intent,1000);
        }
        else{
            Alert.alert_function(Register_fingerprint.this, "exist");
        }
    }

    public void onFinish() {
        finish();
        toast.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.hasExtra("result")){
            if(data.getExtras().getString("result").toString().equals("true")) {
                Alert.alert_function(Register_fingerprint.this, "register");
            }
            else {
                Alert.alert_function(Register_fingerprint.this, "main");
            }
        }
        else {
            Alert.alert_function(Register_fingerprint.this, "main");
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
            sendRequest.send("https://"+ SSL_Connection.getSSLConnection().getUrl()+"/logout",
                    1, hashMap, Register_fingerprint.this);
        }
    }
}
