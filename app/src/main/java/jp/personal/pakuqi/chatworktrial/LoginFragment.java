package jp.personal.pakuqi.chatworktrial;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Fragment用のレイアウトを読み込み
        final View loginActivity = inflater.inflate(R.layout.activity_login, container, false);

        //ログインボタンのインスタンス生成
        Button logiBtn = (Button)loginActivity.findViewById(R.id.login_button);

        //ログインボタンを押下時のリスナーを登録
        logiBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    //画面からAPI TOKENを取得する
                    EditText apiToken = (EditText)loginActivity.findViewById(R.id.api_token_text);
                    String apiTokenString = apiToken.getText().toString();

                    //Activityのコールバックを呼び出す
                    LoginActivity activity = (LoginActivity) getActivity();
                    activity.onOkClicked(apiTokenString);

                } catch (ClassCastException e) {
                    e.getStackTrace();
                }
            }
        });

        //生成したレイアウトを返す
        return loginActivity;
    }


}
