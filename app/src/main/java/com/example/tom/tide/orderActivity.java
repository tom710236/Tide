package com.example.tom.tide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
    String cProductName;
    String cProductID;
    String cShippersCount;
    int cShippersCountEd;
    String json;
    EditText editText;
    String order;
    ArrayAdapter<String> list, list2;
    int a = 0;
    int myint = 0;
    ArrayList<String> trans, trans2;
    ListView listView;
    int number;


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
        //取代
        check2 = check.substring(1, i - 1);
        check3 = check2.replaceAll(", ", ",");

        TextView orderName = (TextView) findViewById(R.id.textView11);
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
                json = response.body().string();
                Log.e("json",json);
                Log.e("OkHttp10", response.toString());
                Log.e("OkHttp11", json);
                parseJson2(json);

            }
        });
    }

    //取出回傳後JSON的值
    private void parseJson2(String json) {
        try {
            trans = new ArrayList<String>();
            final JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                cProductName = obj.getString("cProductName");
                cProductID = obj.getString("cProductID");
                cShippersCount = obj.getString("cShippersCount");
                int cShippersCountEd = obj.getInt("cShippersCountEd");
                Log.e("okHTTP15", cProductName + cProductID + cShippersCount);
                trans.add(cProductName + "(" + cProductID + ")" + cShippersCount + "(" + cShippersCountEd + ")");


            }
//設定ListView
            listView = (ListView) findViewById(R.id.list);
            listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
            list = new ArrayAdapter<>(
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

    public void enter(View v) throws JSONException {

        editText = (EditText) findViewById(R.id.editText);
        order = editText.getText().toString();
        trans2 = new ArrayList<String>();
        final JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            Log.e("OBJ", String.valueOf(obj));
            cProductName = obj.getString("cProductName");
            cProductID = obj.getString("cProductID");
            cShippersCount = obj.getString("cShippersCount");
            int cShippersCountEd2 = obj.getInt("cShippersCountEd");
            Log.e("cShippersCountEd3", String.valueOf(cShippersCountEd2));
            Log.e("trans2之前", String.valueOf(trans2));
            trans2.add(cProductName + "(" + cProductID + ")" + cShippersCount + "(" + cShippersCountEd + ")");
            Log.e("trans2之後", String.valueOf(trans2));
            if (order.equals(cProductID)) {
                Log.e("成功", String.valueOf(array));
                //第二次cShippersCountEd 因為數量改變 所以找不到索引值
                int k = trans2.indexOf(cProductName + "(" + cProductID + ")" + cShippersCount + "(" + cShippersCountEd + ")");
                //int k = trans.indexOf(cProductID);
                //Log.e("trans2對比後", String.valueOf(trans2));
                Log.e("比對後索引", String.valueOf(k));
                //這邊加數字 有問題
                cShippersCountEd2++;
                Log.e("cShippersCountEd", String.valueOf(cShippersCountEd));
                Log.e("cShippersCountEd2", String.valueOf(cShippersCountEd2));
                //trans.remove(k);
                trans.set(k, cProductName + "(" + cProductID + ")" + cShippersCount + "(" + cShippersCountEd2 + ")");
                trans2.set(k, cProductName + "(" + cProductID + ")" + cShippersCount + "(" + cShippersCountEd2 + ")");
                //Log.e("trans", String.valueOf(trans));
                //k = trans.indexOf(cProductName + "(" + cProductID + ")" + cShippersCount + "(" + cShippersCountEd + ")");

                //Log.e("int2", String.valueOf(k));
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {


                        //list.notifyDataSetChanged();
                        listView.setAdapter(list);
                        Log.e("listview", String.valueOf(listView));
                    }
                });





            } else {

                Log.e("比對失敗", String.valueOf(trans));
            }



        }



    }
}


