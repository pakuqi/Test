package jp.personal.pakuqi.chatworktrial;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.personal.pakuqi.chatworktrial.db.ChatWorkOpenHelper;
import jp.personal.pakuqi.chatworktrial.javabeans.RoomBean;

public class LoginActivity extends AppCompatActivity {

    //ベースのURL
    static final String BASEURL = "https://api.chatwork.com/v1";
    ChatWorkOpenHelper chatOpenHelper;
    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //レイアウトフィルの設定
        setContentView(R.layout.activity_login_fragment);

        //DBの生成
        chatOpenHelper = new ChatWorkOpenHelper(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //メニューの生成
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //メニューを押した時の処理
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //ログインボタン押下時のコールバックルーチン
    public void onOkClicked(String apiToken) {

        //Fragmentでログインボタンが押された
        //自分のチャットルーム一覧を取得する

        //チャットルーム一覧データを格納するArrayList
        final ArrayList<RoomBean> countries = new ArrayList<RoomBean>();

        final String APITOKEN = apiToken;


        //URLを生成
        String roomListUrl = BASEURL + "/rooms";

        //Volleyのオブジェクト生成
        RequestQueue mQueue = Volley.newRequestQueue(this);

        //VolleyオブジェクトにJsonArrayRequestを追加してデータを取得する
        mQueue.add(new JsonArrayRequest(roomListUrl,
                           new Response.Listener<JSONArray>() {
                               @Override
                               public void onResponse(JSONArray response) {
                                    //正常終了時のコールバック

                                   //取得したJSONオブジェクトの取り出し
                                   for (int i = 0; i < response.length(); i++) {
                                       try {
                                           JSONObject jo = response.getJSONObject(i);
                                           int roomId = jo.getInt("room_id");           //部屋ID
                                           String name = jo.getString("name");          //部屋の名前
                                           String iConPath = jo.getString("icon_path"); //アイコンのパス

                                           //取り出したデータを保存
                                           countries.add(new RoomBean(roomId, name, iConPath));

                                       } catch (JSONException e) {
                                           e.printStackTrace();
                                       }
                                   }

                                   //取り出したデータをテーブルにキャッシュする
                                   try {
                                       //データベースを編集モードで取得
                                       mDatabase = chatOpenHelper.getWritableDatabase();

                                       //現在のデータを全削除
                                       mDatabase.delete("cash", null, null);

                                       //取得したデータ数だけループしてテーブルに挿入する
                                       for (int i = 0; i < countries.size(); i++){
                                           ContentValues values = new ContentValues();

                                           values.put("room_id", countries.get(i).getRoomId());
                                           values.put("room_name", countries.get(i).getName());

                                           long num = mDatabase.insert("cash", null, values);

                                           if(num > 0){
                                               Log.i("ChatWorkTrial", "キャッシュにデータ挿入成功");
                                           }
                                           else{
                                               Log.i("ChatWorkTrial", "キャッシュにデータ挿入失敗");
                                           }
                                       }
                                   }catch(SQLiteException e){
                                       //Exceptionを表示
                                       Log.e("ChatWorkTrial", "SQLiteException" + e.getMessage());
                                   }
                                   finally{
                                       //DBをクローズする
                                       if(mDatabase != null){
                                           mDatabase.close();
                                       }
                                   }

                                   //チャットルーム一覧を表示するActivityを起動する
                                   //取得したチャットルームの一覧データ配列を渡す
                                   Intent intent = new Intent(getApplicationContext(), ChatRoomList.class);
                                   intent.putExtra("jp.personal.pakuqi.chatworktrial.roomObject", countries);
                                   intent.putExtra("jp.personal.pakuqi.chatworktrial.APITOKEN", APITOKEN);
                                   startActivity(intent);
                               }
                           },

                           new Response.ErrorListener() {
                               @Override public void onErrorResponse(VolleyError error) {
                                   //エラーが発生した場合のコールバック

                                   if(error.networkResponse == null) {
                                       //コネクションなし

                                       //キャッシュしたデータがあれば取り出してチャットルーム一覧を表示する

                                       //データベースを編集モードで取得
                                       mDatabase = chatOpenHelper.getReadableDatabase();
                                       //検索結果を保持するカーソル
                                       SQLiteCursor mSelectCursor;

                                       //キャッシュテーブルからデータを取り出す
                                       mSelectCursor = (SQLiteCursor) mDatabase.query("cash", //table
                                               new String[]{"_id, room_id", "room_name"},  //columns
                                               null,        //selection WHERE条件
                                               null,        //selectionArgs WHERE条件で比較する値
                                               null,        //groupBy
                                               null,        //having
                                               null);        //orderBy

                                       if (mSelectCursor.getCount() == 0) {
                                           //キャッシュにデータがない
                                           Toast.makeText(getApplicationContext(), "チャットルームの取得ができませんでした", Toast.LENGTH_LONG).show();
                                       } else {
                                           //キャッシュからデータを読み出してチャットルームを表示する

                                           Toast.makeText(getApplicationContext(), "キャッシュから一覧表示します", Toast.LENGTH_LONG).show();
                                           int room_id;
                                           String room_name;
                                           ArrayList<RoomBean> cashdata = new ArrayList<RoomBean>();

                                           //カーソルの先頭へ移動
                                           mSelectCursor.moveToFirst();
                                           //データ数だけループする
                                           for (int i = 0; i < mSelectCursor.getCount(); i++) {
                                               //カーソルから値を取り出す
                                               room_id = mSelectCursor.getInt(1);       //room_id
                                               room_name = mSelectCursor.getString(2);   //room_name
                                               //カーソルを次に進める
                                               mSelectCursor.moveToNext();

                                               //取り出したデータを格納
                                               //パスは"no_data"で固定
                                               cashdata.add(new RoomBean(room_id, room_name, "no_data"));
                                           }

                                           //チャットルーム一覧を表示するActivityを起動する
                                           //取得したチャットルームの一覧データ配列を渡す
                                           Intent intent = new Intent(getApplicationContext(), ChatRoomList.class);
                                           intent.putExtra("jp.personal.pakuqi.chatworktrial.roomObject", cashdata);
                                           intent.putExtra("jp.personal.pakuqi.chatworktrial.APITOKEN", APITOKEN);
                                           startActivity(intent);
                                       }
                                   }
                                   else if(error.networkResponse.statusCode == 401){
                                       Toast.makeText(getApplicationContext(), "認証エラー", Toast.LENGTH_LONG).show();
                                   }

                               }
                           }){
                       @Override
                       public Map<String, String> getHeaders() throws AuthFailureError {
                           //HTTPヘッダにAPI Tokenを設定する
                           Map<String, String> headers = super.getHeaders();
                           Map<String, String> newHeaders = new HashMap<String, String>();
                           newHeaders.put("X-ChatWorkToken", APITOKEN);
                           return newHeaders;
                       }
                   }
        );

    }
}
