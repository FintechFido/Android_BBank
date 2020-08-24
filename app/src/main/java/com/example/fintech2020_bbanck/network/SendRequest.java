package com.example.fintech2020_bbanck.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.fintech2020_bbanck.MainActivity;
import com.example.fintech2020_bbanck.function.Alert;
import com.example.fintech2020_bbanck.function.Check_depositor;
import com.example.fintech2020_bbanck.function.Login;
import com.example.fintech2020_bbanck.model.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*
Request.Method.~
GET = 0
POST = 1
DELETE = 3
PATCH = 7
 */

public class SendRequest extends Activity {

    public JSONObject jsonObject;
    public Context context;
    public void send(String url, int method, final HashMap<String, String> hashMap, Context context)
    {
        this.context = context;
        StringRequest request = new StringRequest(
                method,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("SUCCESS : " + response);
                        try {
                            jsonObject = new JSONObject(response);
                            find_mode();
                        }catch (Exception e){
                            e.printStackTrace();
                            find_mode();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR : "+ error.getMessage());
                        find_mode();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return hashMap;
            }
        };
        request.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue.add(request);
    }


    // 결과 처리
    public void find_mode()
    {
        try {
            // 접속 //
            if(jsonObject.has("mode")) {
                if (jsonObject.getString("mode").toString().equals("access")) {
                    if (jsonObject.getString("result").toString().equals("true")) {
                        // 접속 성공
                        Handler handler = new Handler();
                        final Intent next_intent = new Intent(context, Login.class);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                next_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                next_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(next_intent);
                            }
                        }, 1000);
                    } else {
                        // 접속 실패
                        Alert.alert_function(context, "loading");
                    }
                }
                // 로그인 //
                else if (jsonObject.getString("mode").toString().equals("login")) {
                    if (jsonObject.getString("result").toString().equals("true")) {
                        // 로그인 성공
                        User.getInstance().setInfo(jsonObject.getString("session_key").toString(),
                                Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                        final Intent next_intent = new Intent(context, MainActivity.class);
                        next_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        next_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(next_intent);
                    } else {
                        // 접속 실패
                        Alert.alert_function(context, "login");
                    }
                }
                // 로그아웃 //
                else if (jsonObject.getString("mode").toString().equals("logout")) {
                    if (jsonObject.getString("result").toString().equals("true")) {
                        MainActivity mainActivity = (MainActivity)context;
                        mainActivity.onFinish();
                    }
                    else {
                        Alert.alert_function(context, "logout"); //시스템 종료에 사용
                    }
                }
                // 입금자 정보 확인
                else if (jsonObject.getString("mode").toString().equals("depositor_valid")) {
                    if (jsonObject.getString("result").toString().equals("true")) {
                        Check_depositor check_depositor = (Check_depositor) context;
                        check_depositor.onResult(true);
                    }
                    else {
                        Alert.alert_function(context, "valid");
                    }
                }
                // 잔액 갱신
                else if(jsonObject.getString("mode").toString().equals("balance")) {
                    if(jsonObject.getString("result").toString().equals("true")) {
                        MainActivity mainActivity = (MainActivity)context;
                        mainActivity.update_balance(jsonObject.getString("balance").toString());
                    }
                    else {
                        MainActivity mainActivity = (MainActivity)context;
                        mainActivity.update_balance("");
                    }
                }
                else {
                    Alert.alert_function(context, "valid");
                }
            }
            else {
                Alert.alert_function(context, "main");
            }
        }catch (Exception e) {
            // mode가 없거나 연결이 불가능한 경우
            e.printStackTrace();
            Alert.alert_function(context, "main");
        }

    }
}
