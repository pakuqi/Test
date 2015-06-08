package jp.personal.pakuqi.chatworktrial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ChatRoom extends AppCompatActivity implements View.OnClickListener {
    static final String BASEURL = "https://api.chatwork.com/v1";
    Button mSubmitBtn;          //投稿ボタン
    Button mCanceBtn;           //キャンセルボタン
    TextView mRoomText;         //チャットルーム名
    EditText mChatEditText;     //投稿内容入力欄
    String roomId;
    String APITOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //レイアウトフィルの設定
        setContentView(R.layout.activity_chat);

        //チャットルーム一覧から選択したチャットルーム名とチャットルームIDを受け取る
        Intent intent = getIntent();
        roomId = intent.getStringExtra("chatroom.id");              //チャットルームID（投稿に必要）
        String roomName = intent.getStringExtra("chatroom.name");   //チャットルーム名（画面に表示）
        APITOKEN = intent.getStringExtra("apitoken");

        //IDを元にボタンのインスタンスを取得する
        mSubmitBtn = (Button) findViewById(R.id.submit_btn);        //投稿ボタン
        mCanceBtn = (Button) findViewById(R.id.cancel_btn);         //キャンセルボタン
        mRoomText = (TextView)findViewById(R.id.room_name_title);   //チャットルーム名
        mChatEditText = (EditText)findViewById(R.id.chat_text);     //投稿内容の入力欄

        //チャットルーム名を画面に設定する
        mRoomText.setText(roomName);

        //ボタンを押したときのコールバックを登録する
        mSubmitBtn.setOnClickListener(this);
        mCanceBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //メニューを押下したときの処理
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            //ログアウトボタンを押下

            //ログイン画面へ戻る
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            //Activityのスタック履歴をクリアする
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //ログイン画面呼び出し
            startActivity(intent);
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ボタンを押したときに呼ばれるコールバックメソッド
     *
     * @param v ボタンのインスタンス
     */
    public void onClick(View v) {


       if (v == mSubmitBtn) {
            //投稿ボタンを押下したときの処理

            //画面から入力内容を取得する
            final String messageText = mChatEditText.getText().toString();

            //RoomIDを指定して投稿する
            String roomListUrl = BASEURL + "/rooms/" + roomId + "/messages";

           //Volleyのリクエストキューを生成
            RequestQueue mQueue = Volley.newRequestQueue(this);

            //StringRequestを使ってメッセージをPOSTする
            StringRequest sr = new StringRequest(
                    Request.Method.POST,
                    roomListUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //正常終了に呼び出されるコールバック
                            Toast.makeText(getApplicationContext(), "投稿が成功しました", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //エラー発生時に呼び出されるコールバック
                            Toast.makeText(getApplicationContext(), "投稿が失敗しました", Toast.LENGTH_LONG).show();
                    }
                }){
                @Override
                protected Map<String,String> getParams(){
                    //getParams()メソッドをオーバーライドしてPOSTするデータを設定する

                    //HashMapを生成
                    Map<String,String> params = new HashMap<String, String>();
                    //パラメータ：bodyに画面で入力した文字列を設定する
                    params.put("body", messageText);

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //getHeaders()メソッドをオーバーライドしてHTTPヘッダにAPI TOKENを設定する
                    //HashMapを生成
                    Map<String,String> params = new HashMap<String, String>();
                    //パラメータ：X-ChatWorkTokenにログイン画面で入力したAPI TOKENを設定する
                    params.put("X-ChatWorkToken", APITOKEN);

                    return params;
                }
            };

           //リクエストキューにStringRequestを追加してPOSTを実行する
            mQueue.add(sr);


        } else if (v == mCanceBtn) {
            //画面終了
            this.finish();
        }
    }


}