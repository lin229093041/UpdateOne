package com.oracle.oaec.androidproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oracle.oaec.androidproject.publicmethod.ToUrl;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

public class IPActivity extends AppCompatActivity {
    private Button btn_ip;
    private EditText  edt_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);
        btn_ip= (Button) findViewById(R.id.btn_ip);
        edt_ip= (EditText) findViewById(R.id.edt_ip);
        btn_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToUrl.Url="http://"+edt_ip.getText().toString()+":8093/OneAndroidDemo/";
                FinalHttp http =new FinalHttp();
                http.get(ToUrl.Url, new AjaxCallBack<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        Intent intent=new Intent(IPActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                        super.onSuccess(o);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        Toast.makeText(IPActivity.this,"访问该地址失败，请检查服务器是否已开启或该IP地址是否正确",Toast.LENGTH_SHORT).show();
                        super.onFailure(t, errorNo, strMsg);
                    }
                });
            }
        });
    }
}
