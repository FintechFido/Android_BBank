package com.example.fintech2020_bbanck;

import android.app.Activity;
import android.os.Bundle;

import com.android.volley.toolbox.Volley;
import com.example.fintech2020_bbanck.network.AppHelper;
import com.example.fintech2020_bbanck.network.SSL_Connection;
import com.example.fintech2020_bbanck.network.Server_Connection;

/*
최초 실행시 Loading으로 이동
서버 상태 확인 후 연결 가능한 경우 onSuccess / 불가능한 경우 onFail (앱 종료)
Intent로부터 mode가 register(지문등록)인지 auth(사용자인증)인지 확인
Intent로부터 result가 TRUE라면 지문 등록 또는 사용자 인증에 성공한 것.
 */
public class Loading extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());

        start_connection();
    }

    private void start_connection()
    {
        SSL_Connection sslConnection = SSL_Connection.getSSLConnection();
        sslConnection.postHttps(1000, 1000);

        Server_Connection server_connection = new Server_Connection(sslConnection.getUrl(),Loading.this);
        server_connection.connection();
    }

}
