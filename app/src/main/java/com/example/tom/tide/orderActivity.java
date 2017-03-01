package com.example.tom.tide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class orderActivity extends AppCompatActivity {
    String check,door1,cUserName;
    String url = "http://demo.shinda.com.tw/ModernWebApi/WebApi.aspx";
    OkHttpClient client = new OkHttpClient();
    String check2 = null;
    String check3 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Intent intent = getIntent();
        //取得Bundle物件後 再一一取得資料
        Bundle bag = intent.getExtras();
        check = bag.getString("checked", null);
        //先取得字串的長度
        int i =check.length();
        //
        check2=check.substring(1,i-1);


        door1 = bag.getString("order",null);
        Log.e("CHECK11",check2);
        Log.e("DOOR",door1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(orderActivity.this, outActivity.class);
                Bundle bag = new Bundle();
                bag.putString("cUserName", cUserName);
                intent.putExtras(bag);
                startActivity(intent);
                orderActivity.this.finish();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                postjson();
            }
        }).start();
    }
    private void postjson() {
        //post
        RequestBody body = new FormBody.Builder()
                .add("postdata", "{ ApiName: \"GetShippersD\", ApiID: \""+check2+"\"}")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e("OkHttp10", response.toString());
                Log.e("OkHttp11", json);

            }
        });
    }



}
