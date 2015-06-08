package jp.personal.pakuqi.chatworktrial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import jp.personal.pakuqi.chatworktrial.javabeans.RoomBean;

/**
 * Created by oono on 2015/06/06.
 */
public class CustomAdapter extends ArrayAdapter<RoomBean> {

    private LayoutInflater layoutInflater_;
    private ImageLoader mImageLoader;

    public CustomAdapter(Context context, int textViewResourceId, ArrayList<RoomBean> objects, ImageLoader imageloader) {
        super(context, textViewResourceId, objects);
        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageLoader = imageloader;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 特定の行(position)のデータを得る
        RoomBean item = (RoomBean)getItem(position);

        // convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
        if (null == convertView) {
            convertView = layoutInflater_.inflate(R.layout.room_list_row, null);
        }

        //RoomBeanのデータをViewの各Widgetにセットする

        //画像
        if(item.getiConPath().equals("no_data")) {
            //データなし
        }
        else{
            NetworkImageView image = (NetworkImageView) convertView.findViewById(R.id.image);
            image.setImageUrl(item.getiConPath(), mImageLoader);
        }

        //部屋の名前
        TextView nameTextView;
        nameTextView = (TextView)convertView.findViewById(R.id.room_name);
        nameTextView.setText(item.getName());

        //部屋のID
        TextView idTextView;
        idTextView = (TextView)convertView.findViewById(R.id.room_id);
        idTextView.setText(Integer.toString(item.getRoomId()));

        return convertView;
    }


}
