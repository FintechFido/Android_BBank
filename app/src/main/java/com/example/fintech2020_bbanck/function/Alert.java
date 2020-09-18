package com.example.fintech2020_bbanck.function;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fintech2020_bbanck.MainActivity;


public class Alert extends AppCompatActivity {

    static AlertDialog.Builder builder;
    public static void alert_function(Context context, String mode) {
        builder = new AlertDialog.Builder(context);

        switch (mode) {
            case "login":
                builder.setTitle("알림").setMessage("ID 또는 비밀번호를 확인해주세요");
                setneutralButton("normal", context);
                break;
            case "exist":
                builder.setTitle("알림").setMessage("HIDO 앱이 없습니다");
                setneutralButton("normal", context);
                break;
            case "logout":
                builder.setTitle("알림").setMessage("서버의 응답이 없습니다");
                setneutralButton("finish", context);
                break;
            case "loading":
                builder.setTitle("알림").setMessage("서버의 응답이 없습니다");
                setneutralButton("exit", context);
                break;
            case "register":
                builder.setTitle("알림").setMessage("지문이 등록되었습니다");
                setneutralButton("main", context);
                break;
            case "transfer":
                builder.setTitle("알림").setMessage("송금이 완료되었습니다");
                setneutralButton("transfer", context);
                // 송금 완료 페이지던 상대 확인 페이지던 이동해야할 것 같음
                break;
            case "fail":
                builder.setTitle("알림").setMessage("요청이 실패되었습니다");
                setneutralButton("normal", context);
                break;
            case "main":
                builder.setTitle("알림").setMessage("요청이 실패되었습니다");
                setneutralButton("main", context);
                break;
            case "valid":
                builder.setTitle("알림").setMessage("입금자 정보를 다시 확인해주세요");
                setneutralButton("return", context);
                break;
            case "depositor_fail":
                builder.setTitle("알림").setMessage("송금이 실패하였습니다");
                setneutralButton("return", context);
                break;
            case "no_money":
                builder.setTitle("알림").setMessage("잔액이 0원입니다");
                setneutralButton("main", context);
                break;
        }
    }

    private static void setneutralButton(String mode, Context para_context)
    {
        final Context context = para_context;
        switch (mode) {
            case "exit":
                builder.setNeutralButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.runFinalization();
                        System.exit(0);
                    }
                });
                break;
            case "normal":
                builder.setNeutralButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                break;
            case "logout":
                builder.setNeutralButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity mainActivity = (MainActivity) context;
                        mainActivity.onFinish();
                    }
                });
                break;
            case "main":
                builder.setNeutralButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
                break;
            case "transfer":
                builder.setNeutralButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                break;
            case "return":
                builder.setNeutralButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Check_depositor check_depositor = (Check_depositor) context;
                        check_depositor.onResult(false);
                    }
                });
                break;
        }
        alert_show();
    }

    private static void alert_show(){
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}