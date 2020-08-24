package com.example.fintech2020_bbanck.function;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.fintech2020_bbanck.R;

public class Transfer extends Activity {

    private Spinner spinner;
    private EditText account;
    private EditText price;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer);

        init();
        setBtnClickListener();
    }

    private void init() {
        spinner = (Spinner)findViewById(R.id.spinner_bank);
        account = (EditText)findViewById(R.id.et_account);
        price = (EditText)findViewById(R.id.et_price);
        name = (EditText)findViewById(R.id.et_name);
    }

    private void setBtnClickListener() {
        Button btn = (Button)findViewById(R.id.btn_check);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Transfer.this, Check_depositor.class);
                intent.putExtra("depositor_bank", spinner.getSelectedItem().toString());
                intent.putExtra("account", ""+account.getText());
                intent.putExtra("price",""+price.getText());
                intent.putExtra("name", ""+name.getText());
                startActivityForResult(intent, 3000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            if (resultCode == 3000) {
                // 입금자 정보 확인 실패한 경우 리턴됨
                //
            }
            else {

            }
        }
    }

}
