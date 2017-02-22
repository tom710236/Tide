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
    String cStatus, userName, passWord;
    String url = "http://demo.shinda.com.tw/ModernWebApi/WebApiLogin.aspx";
    Thread pass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences setting =
                getSharedPreferences("Login",MODE_PRIVATE);
        EditText uId = (EditText)findViewById(R.id.userName);
        uId.setText(setting.getString("userName",""));

    }

    public void login(View v) {

        pass = new pass();
        pass.start();

    }
    class pass extends Thread {
        @Override
        public void run() {
            final EditText uId = (EditText) findViewById(R.id.userName);
            EditText uPw = (EditText) findViewById(R.id.passWord);
            userName = uId.getText().toString();
            passWord = uPw.getText().toString();
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


                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    Log.e("OkHttp", response.toString());
                    Log.e("OkHttp2", json);

                    parseJson(json);


                }


                public void parseJson(String json) {


                    try {
                        cStatus = new JSONObject(json).getString("cStatus");
                        Log.e("JSOM", cStatus);
                        if(cStatus.equals("1")){
                            Intent intent = new Intent(MainActivity.this,systemActivity.class);
                            startActivity(intent);
                            Looper.prepare();
                            SharedPreferences setting =
                                    getSharedPreferences("Login",MODE_PRIVATE);
                            setting.edit()
                                    .putString("userName",userName)
                                    .commit();
                            Looper.loop();


                        }
                        else{
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


