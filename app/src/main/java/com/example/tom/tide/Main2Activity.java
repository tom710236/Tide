package com.example.tom.tide;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity {
    String url = "http://demo.shinda.com.tw/ModernWebApi/getProduct.aspx";
    ArrayList<ProductInfo> trans;
    private MyDBHelper helper;
    SQLiteDatabase db;
    final String DB_NAME = "tblTable";
    ListView listView;
    ContentValues addbase;
    int DB_NUM = 0;
    final String tableName = "tblOrder";//資料表名稱
    String ID,name,NO,DT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);

        helper = new MyDBHelper(this, DB_NAME, null, 1);
        //實做 db(繼承SQLiteDatabase)類別 getWritableDatabase用來更新 新增修改刪除
        db = helper.getWritableDatabase();


    }
    public class ProductInfo {
        private String cProductID;
        private String cProductName;
        private String cGoodsNo;
        private String cUpdateDT;

        //建構子
        ProductInfo(final String ProductID, final String ProductName, final String GoodsNo,final String UpdateDT) {
            this.cProductID = ProductID;
            this.cProductName = ProductName;
            this.cGoodsNo = GoodsNo;
            this.cUpdateDT = UpdateDT;

        }
        //方法
        @Override
        public String toString() {
            return this.cProductID +  this.cProductName  + this.cGoodsNo + this.cUpdateDT;
        }
    }
    class Get extends Thread {
        @Override
        public void run() {
            okHttpGet();
        }
    }
    private void okHttpGet(){
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback(){

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e("json",json);
                parseJson(json);
            }

        });

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void parseJson(String json) {

        try {
            //解析JSON資料
            trans = new ArrayList<ProductInfo>();
            final JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                trans.add(new ProductInfo(obj.optString("cProductID"), obj.optString("cProductName"),obj.optString("cGoodsNo"),obj.optString("cUpdateDT")));
                ID = obj.optString("cProductID");
                name = obj.optString("cProductName");
                NO = obj.optString("cGoodsNo");
                DT = obj.optString("cUpdateDT");
                //放入SQL
                addbase = new ContentValues();
                addbase.put("cProductID", ID);
                addbase.put("cProductName", name);
                addbase.put("cGoodsNo", NO);
                addbase.put("cUpdateDT", DT);
                db.insert(DB_NAME, null, addbase);
                makeNotification();
            }



        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onClick(View v){

        db.delete(DB_NAME, null, null);
        Get get = new Get();
        get.start();

        //Intent intent = new Intent(this, Delay.class);
        //startService(intent);
    }
    //推播
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void makeNotification() {
        int i= 1;
        Bitmap bmp = BitmapFactory
                .decodeResource(getResources(), R.drawable.pig64);
        Notification.BigPictureStyle big =
                new Notification.BigPictureStyle();
        big.bigPicture(
                BitmapFactory.decodeResource(getResources(), R.drawable.pig256))
                .setSummaryText("bla bla bla");
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.pig32)
                .setContentTitle("新增")
                .setContentText("新增一筆資料")
                .setContentInfo("第"+i+"資料")
                .setWhen(System.currentTimeMillis())
                .build();
        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(i, notification);
        Log.e("INT", String.valueOf(i));
    }
}
