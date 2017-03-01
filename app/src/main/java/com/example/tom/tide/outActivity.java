package com.example.tom.tide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class outActivity extends AppCompatActivity {
    // 宣告
    String url = "http://demo.shinda.com.tw/ModernWebApi/WebApi.aspx";
    String url2 = "http://demo.shinda.com.tw/ModernWebApi/GetShippersByCustomerID.aspx";
    OkHttpClient client = new OkHttpClient();
    String cUserName;
    List<String> checked;
    String door1 = null;
    int index;
    String listname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(outActivity.this, systemActivity.class);
                Bundle bag = new Bundle();
                bag.putString("cUserName", cUserName);
                intent.putExtras(bag);
                startActivity(intent);
                outActivity.this.finish();
            }
        });

        Intent intent = getIntent();
        //取得Bundle物件後 再一一取得資料
        Bundle bag = intent.getExtras();
        cUserName = bag.getString("cUserName", null);
        TextView textView = (TextView) findViewById(R.id.textView3);
        textView.setText(cUserName + "您好");


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
    private void parseJson(final String json) {
        //取值
        try {

            final ArrayList<String> trans = new ArrayList<String>();
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


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    spinner.setAdapter(list);
                }
            });

            //spinner 點擊事件
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    index = spinner.getSelectedItemPosition();
                    Log.e("index", String.valueOf(index));
                    postjson2(json, index);

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // sometimes you need nothing here
                }
            }
            );


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //點擊 spinner項目後 所要執行的方法
    private void postjson2(String json, int index) {

        try {
            door1 = new JSONArray(json).getJSONObject(index - 1).getString("cCustomerID");
            Log.e("ARRAY", door1);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody body = new FormBody.Builder()
                .add("postdata", "{ cCustomerID:\"" + door1 + "\", cUserID: \"S000000001\" }")
                //.add("postdata", "{\"cAccount\":\"" + userName + "\",\"cPassword\":\"" + passWord + "\"}")
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

    //POST成功後把回傳的值(陣列)取出來 用listView顯示
    private void parseJson2(String json) {
        try {
            final ArrayList<String> trans = new ArrayList<String>();
                final JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    listname = obj.getString("cShippersID");
                    Log.e("okHTTP8", listname);
                    trans.add(listname);
            }
            final ListView listView = (ListView) findViewById(R.id.listView);
            listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
            final ArrayAdapter<String> list = new ArrayAdapter<>(
                    outActivity.this,
                    android.R.layout.simple_list_item_multiple_choice,
                    trans);
            //非主執行緒顯示UI
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    listView.setVisibility(View.VISIBLE);
                    listView.setAdapter(list);
                    //假如選到請選擇 list將不會出現
                    if (index==0){
                        listView.setVisibility(View.GONE);
                    }

                }
            });
            /*
            listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int index = listView.getSelectedItemPosition();
                    Log.e("index2", String.valueOf(index));
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            */


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    AbsListView list = (AbsListView)adapterView;
                    Adapter adapter = list.getAdapter();
                    SparseBooleanArray array = list.getCheckedItemPositions();
                    checked = new ArrayList<>(list.getCheckedItemCount());
                    for (int i = 0; i < array.size(); i++) {
                        int key = array.keyAt(i);
                        if (array.get(key)) {
                            checked.add((String) adapter.getItem(key));
                            Log.e("CHECK", String.valueOf(checked));

                        }

                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void enter (View v){
        Intent intent = new Intent(outActivity.this, orderActivity.class);
        Bundle bag = new Bundle();
        bag.putString("checked", String.valueOf(checked));
        bag.putString("order",door1);
        intent.putExtras(bag);
        startActivity(intent);
        outActivity.this.finish();
    }
}







