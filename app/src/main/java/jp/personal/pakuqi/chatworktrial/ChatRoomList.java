package jp.personal.pakuqi.chatworktrial;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import jp.personal.pakuqi.chatworktrial.javabeans.RoomBean;

public class ChatRoomList extends ListActivity  {
    String APITOKEN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_room_list);

        //Intent経由でChartRoomの一覧を取得する
        Intent intent = getIntent();
        if(intent != null) {

            RequestQueue mRequestQueue;
            ImageLoader mImageLoader;
            ImageLoader.ImageCache mCache;

            mCache = getCacheNone();
            //RequestQueueのインスタンス用を取得
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            //ImageLoaderの生成
            mImageLoader = new ImageLoader(mRequestQueue, mCache);

            //ログイン画面からListに表示するデータのArrayListを受け取る
            ArrayList<RoomBean> roomObject = intent.getParcelableArrayListExtra("jp.personal.pakuqi.chatworktrial.roomObject");
            APITOKEN = intent.getStringExtra("jp.personal.pakuqi.chatworktrial.APITOKEN");

            //カスタマイズしたAdapterを生成する
            CustomAdapter customAdapater = new CustomAdapter(this, 0, roomObject, mImageLoader);

            //ListView listView = (ListView) findViewById(R.id.list);
            //生成したAdapterをListに設定する
            setListAdapter(customAdapater);
        }
    }


    protected void onListItemClick(ListView listView, View v, int position,long id) {
        super.onListItemClick(listView, v, position, id);

        //String str = (String) listView.getItemAtPosition(position);
        //Log.v("TAG", position + "　行目の　" + str + "　がクリックされた");
        //クリックした行の値を取り出す

        //タップした行のISBN用TextViewのインスタンスを取得する
        TextView roomId = (TextView)v.findViewById(R.id.room_id);

        //タップした行のISBN用TextViewのインスタンスを取得する
        TextView roomName = (TextView)v.findViewById(R.id.room_name);

        //ChatRoomアクティビティを呼び出す
        Intent intent =new Intent(getApplicationContext(), ChatRoom.class);

        //パラメータにタップした行のISBN番号を設定する
        intent.putExtra("chatroom.id", roomId.getText().toString());
        intent.putExtra("chatroom.name", roomName.getText().toString());
        intent.putExtra("apitoken", APITOKEN);

        //Activityを開始する
        startActivity(intent);

    }

    private ImageLoader.ImageCache getCacheNone(){

        return new ImageLoader.ImageCache(){
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }
            @Override
            public void putBitmap(String url, Bitmap bitmap) {
            }
        };

    }

}
