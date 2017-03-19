package com.example.tom.tide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import static com.example.tom.tide.R.layout.lview;

public class systemActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {
    //字串陣列方式將功能儲存在func陣列
    String[] func = {"出貨單檢貨", "採購單點貨", "庫存調整", "系統管理",
            "產品資訊撈取"};
    //int陣列方式將功能儲存在icons陣列
    int[] icons = {R.drawable.ic_keyboard_arrow_right_black_24dp, R.drawable.ic_keyboard_arrow_right_black_24dp, R.drawable.ic_keyboard_arrow_right_black_24dp
            , R.drawable.ic_keyboard_arrow_right_black_24dp, R.drawable.ic_keyboard_arrow_right_black_24dp};
    String cUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);

        //Toolbar 設定
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //回到上一頁的圖示
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        //回到上一頁按鍵設定
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(systemActivity.this,MainActivity.class);
                startActivity(intent);
                systemActivity.this.finish();
            }
        });

        //取得另一頁傳過來的cUserName並顯示在TextView
        Intent intent = getIntent();
        Bundle bag = intent.getExtras();
        cUserName = bag.getString("cUserName",null);
        TextView textView = (TextView)findViewById(R.id.textView3);
        textView.setText(cUserName+"您好");

        //listView自訂
        ListView list = (ListView) findViewById(R.id.list);
        IconAdapter gAdapter = new IconAdapter();
        list.setAdapter(gAdapter);
        list.setOnItemClickListener(this);

    }
    //ListView 點擊觸發
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                //點擊後到另一頁 並把cUserName帶到另一頁
                Intent intent = new Intent(systemActivity.this,outActivity.class);
                Bundle bag = new Bundle();
                bag.putString("cUserName",cUserName);
                intent.putExtras(bag);
                startActivity(intent);
                systemActivity.this.finish();
                break;
            case 1:
                Intent intent1 = new Intent(this,Main2Activity.class);
                startActivity(intent1);
                systemActivity.this.finish();
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }

    class IconAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return func.length;
        }
        @Override
        public Object getItem(int position) {
            return func[position];
        }
        @Override
        public long getItemId(int position) {
            return icons[position];
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //設定listView
            View v = convertView;
            if(v == null){
                v =getLayoutInflater().inflate(lview,null);
                ImageView image = (ImageView)v.findViewById(R.id.img);
                TextView text = (TextView)v.findViewById(R.id.textView);
                //呼叫setImageResource方法設定圖示的圖檔資源
                image.setImageResource(icons[position]);
                //呼叫setText方法設定圖示上的文字
                text.setText(func[position]);
            }
            return v;

    }


    }
}