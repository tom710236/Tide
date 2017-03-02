package com.example.tom.tide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class orderActivity extends AppCompatActivity {
    String check, door1, cUserName;
    String url = "http://demo.shinda.com.tw/ModernWebApi/WebApi.aspx";
    OkHttpClient client = new OkHttpClient();
    String check2 = null;
    String check3 = null;
    String cProductName,cProductID,cShippersCount,cShippersCountEd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //接收上一頁的資料
        Intent intent = getIntent();
        Bundle bag = intent.getExtras();
        check = bag.getString("checked", null);
        cUserName = bag.getString("cUserName", null);
        door1 = bag.getString("order", null);
        //把check去中框號和中間空白處
        //先取得字串的長度
        int i = check.length();
        //再取字串範圍 (0和最後是[])
        check2 = check.substring(1, i - 1);
        check3 = check2.replaceAll(", ", ",");

        TextView orderName = (TextView)findViewById(R.id.textView11);
        //把上一頁傳過來的door用TextView顯示
        orderName.setText(door1);
        Log.e("CHECK11", check2);
        Log.e("DOOR", door1);
        //設定Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //設定回到上一頁
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

        //執行緒
        new Thread(new Runnable() {
            @Override
            public void run() {
                postjson();
            }
        }).start();
    }
    //執行執行緒的方法
    private void postjson() {
        //post
        RequestBody body = new FormBody.Builder()
                .add("postdata", "{ ApiName: \"GetShippersD\", ApiID: \"" + check3 + "\"}")
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
                //POST後 回傳的JSON檔
                String json = response.body().string();
                Log.e("OkHttp10", response.toString());
                Log.e("OkHttp11", json);
                parseJson2(json);

            }
        });
    }
    //取出回傳後JSON的值
    private void parseJson2(String json) {
        try {
            final ArrayList<String> trans = new ArrayList<String>();
            final JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                cProductName = obj.getString("cProductName");
                cProductID = obj.getString("cProductID");
                cShippersCount = obj.getString("cShippersCount");
                cShippersCountEd=obj.getString("cShippersCountEd");
                Log.e("okHTTP15", cProductName+cProductID+cShippersCount);
                trans.add(cProductName+"("+cProductID+")"+cShippersCount+"("+cShippersCountEd+")");
            }
            //設定ListView
            final ListView listView = (ListView) findViewById(R.id.list);
            listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
            final ArrayAdapter<String> list = new ArrayAdapter<>(
                    orderActivity.this,
                    android.R.layout.simple_list_item_activated_1,
                    trans);

            //顯示ListView 非主執行緒的UI
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //listView.setVisibility(View.VISIBLE);
                    listView.setAdapter(list);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
