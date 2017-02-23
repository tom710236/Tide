package com.example.tom.tide;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

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

public class outActivity extends AppCompatActivity {

    String url = "http://demo.shinda.com.tw/ModernWebApi/WebApi.aspx";
    String url2 = "http://demo.shinda.com.tw/ModernWebApi/GetShippersByCustomerID.aspx";
    OkHttpClient client = new OkHttpClient();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);


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
                .add("postdata", "{\"ApiName\":\"GetCustomer\",\"ApiID\":\"S000000001\"}")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            //post 失敗後
            @Override
            public void onFailure(Call call, IOException e) {

            }
            //POST 成功後
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e("OkHttp3", response.toString());
                Log.e("OkHttp4", json);
                parseJson(json);
            }
        });
    }


    //POST成功後回傳的值(陣列)取出來 用spinner顯示
    private void parseJson(String json) {
        //取值
        try {

            ArrayList<String> trans = new ArrayList<String>();
            JSONArray array = new JSONArray(json);
            trans.add("請選擇");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                //String id = obj.getString("cCustomerID");
                String listname = obj.getString("cCustomerName");
                Log.e("okHTTP5", listname);
                trans.add(listname);

            }
            //spinner 顯示


            final Spinner spinner = (Spinner) findViewById(R.id.spinner);
            final ArrayAdapter<String> list = new ArrayAdapter<>(outActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    trans);


            Looper.prepare();
            spinner.setAdapter(list);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            break;
                        case 1:
                            postjson2();
                            break;
                        case 2:
                            postjson2();
                            break;
                        case 3:
                            postjson2();
                            break;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                    // sometimes you need nothing here
                }
            }
            );



        } catch (JSONException e) {
            e.printStackTrace();
        }Looper.loop();
    }
    //點擊 spinner項目後 所要執行的方法
    private void postjson2() {
        RequestBody body = new FormBody.Builder()
                .add("postdata", "{ cCustomerID: \"C000000001\", cUserID: \"S000000001\" }")

                .build();
        Request request = new Request.Builder()
                .url(url2)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json2 = response.body().string();
                Log.e("OkHttp6", response.toString());
                Log.e("OkHttp7", json2);
                parseJson2(json2);
            }
        });
    }
    //POST成功後回傳的值(陣列)取出來 用listView顯示
    private void parseJson2(String json) {


        try {

            final ArrayList<String> trans = new ArrayList<String>();
            final JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String listname = obj.getString("cShippersID");
                Log.e("okHTTP8", listname);
                trans.add(listname);


            }




            final ListView listView = (ListView)findViewById(R.id.listView);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            final ArrayAdapter<String> list = new ArrayAdapter<>(outActivity.this,
                    android.R.layout.simple_list_item_multiple_choice,
                    trans);


            runOnUiThread(new Runnable() {

                              @Override
                              public void run() {
                                  listView.setVisibility(View.VISIBLE);
                                  listView.setAdapter(list);
                              }
                          });



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}