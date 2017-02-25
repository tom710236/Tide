package com.example.tom.tide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    //宣告
    String cStatus, userName, passWord;
    String url = "http://demo.shinda.com.tw/ModernWebApi/WebApiLogin.aspx";
    Thread pass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //記住帳號
        SharedPreferences setting =
                getSharedPreferences("Login",MODE_PRIVATE);
        EditText uId = (EditText)findViewById(R.id.userName);
        uId.setText(setting.getString("userName",""));

    }
    //執行緒
    public void login(View v) {
        pass = new pass();
        pass.start();

    }
    //執行緒方法
    class pass extends Thread {
        @Override
        public void run() {
            final EditText uId = (EditText) findViewById(R.id.userName);
            EditText uPw = (EditText) findViewById(R.id.passWord);
            userName = uId.getText().toString();
            passWord = uPw.getText().toString();
            //使用OkHttp post
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    //.add("postdata", "{\"cAccount\":\"carlos\",\"cPassword\":\"123\"}")
                    .add("postdata", "{\"cAccount\":\"" + userName + "\",\"cPassword\":\"" + passWord + "\"}")
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Log.e("UP", body.toString());
            Call call = client.newCall(request);
            call.enqueue(new Callback() {

                //post 失敗後執行
                @Override
                public void onFailure(Call call, IOException e) {

                }
                //post 成功後執行
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    Log.e("OkHttp", response.toString());
                    Log.e("OkHttp2", json);
                    //所要執行的方法
                    parseJson(json);
                }

                //方法
                public void parseJson(String json) {


                    try {
                        cStatus = new JSONObject(json).getString("cStatus");
                        Log.e("JSOM", cStatus);
                        if(cStatus.equals("1")){
                            //到另一頁
                            Intent intent = new Intent(MainActivity.this,systemActivity.class);
                            startActivity(intent);
                            //記住帳號
                            SharedPreferences setting =
                                    getSharedPreferences("Login",MODE_PRIVATE);
                            setting.edit()
                                    .putString("userName",userName)
                                    .commit();


                        }
                        else{
                            //非主執行緒顯示UI
                            Looper.prepare();
                            Toast.makeText(MainActivity.this,"登入失敗 請重新輸入", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }


                    } catch (JSONException e) {

                        e.printStackTrace();
                    }


                }


            });

        }
    }

}


