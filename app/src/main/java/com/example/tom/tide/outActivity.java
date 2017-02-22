package com.example.tom.tide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.util.Log;
import android.widget.ArrayAdapter;
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
    OkHttpClient client = new OkHttpClient();
    //String listname1,listname2,listname3,listname;
    ArrayList trans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);

        new Thread() {
            @Override
            public void run() {
                postjson();
            }
        }.start();

        //final String[] trans = {"請選擇",trans};
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> list = new ArrayAdapter<>(outActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                trans);
        spinner.setAdapter(list);

    }

        private void postjson() {
        RequestBody body = new FormBody.Builder()
                .add("postdata", "{\"ApiName\":\"GetCustomer\",\"ApiID\":\"S000000001\"}")
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
                Log.e("OkHttp3", response.toString());
                Log.e("OkHttp4", json);
                parseJson(json);
            }
        });
    }



    private void parseJson(String json) {
        ArrayList<Transition> trans = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i<array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                //String id = obj.getString("cCustomerID");
                String listname = obj.getString("cCustomerName");
                Log.e("okHTTP5",listname);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }







            /*
            JSONArray array = new JSONArray(json);
            JSONObject obj = array.getJSONObject(0);
            listname1=obj.getString("cCustomerName");
            JSONObject obj2 = array.getJSONObject(1);
            listname2=obj2.getString("cCustomerName");
            JSONObject obj3 = array.getJSONObject(2);
            listname3=obj3.getString("cCustomerName");
            */







}
