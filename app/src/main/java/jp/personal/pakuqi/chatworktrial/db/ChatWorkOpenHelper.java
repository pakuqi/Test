package jp.personal.pakuqi.chatworktrial.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by oono on 2015/06/07.
 */
public class ChatWorkOpenHelper extends SQLiteOpenHelper {

    //DB名
    static final String dbName = "chatworktrial.db";
    //DBのバージョン
    static final int dbVersion = 1;

    //Create Table
    static final String CREATE_CASH_TBL = "create table cash(" +
            "room_id integer primary key," +
            "room_name text not null," +
            "_id integer" +
            ");";

    public ChatWorkOpenHelper(Context context, String name,
                             SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    public ChatWorkOpenHelper(Context context) {
        super(context, dbName, null, dbVersion);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //データベースを新規に作成した後で呼ばれる
        Log.i("ChatWorkTrial", CREATE_CASH_TBL);
        //テーブルを作成する
        db.execSQL(CREATE_CASH_TBL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //コンストラクタで指定したバージョンと、参照先のDBのバージョンに差異があるときにコールされる
        //今回は特に処理なし

    }
}
