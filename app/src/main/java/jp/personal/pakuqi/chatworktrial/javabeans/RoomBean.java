package jp.personal.pakuqi.chatworktrial.javabeans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by oono on 2015/06/06.
 */
public class RoomBean implements Parcelable {
    private int _roomId;
    private String _name;
    private String _iConPath;

    public RoomBean(int roomId, String name, String iConPath){
        this._roomId = roomId;
        this._name = name;
        this._iConPath = iConPath;
    }

    public int getRoomId() {
        return _roomId;
    }

    public void setRoomId(int roomId) {
        this._roomId = roomId;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getiConPath() {
        return _iConPath;
    }

    public void setiConPath(String iConPath) {
        this._iConPath = iConPath;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    private RoomBean(Parcel in) {
        _roomId = in.readInt();
        _name = in.readString();
        _iConPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        // TODO Auto-generated method stub
        out.writeInt(_roomId);
        out.writeString(_name);
        out.writeString(_iConPath);
    }

    public static final Parcelable.Creator<RoomBean> CREATOR
            = new Parcelable.Creator<RoomBean>() {
        public RoomBean createFromParcel(Parcel in) {
            return new RoomBean(in);
        }
        public RoomBean[] newArray(int size) {
            return new RoomBean[size];
        }
    };

}
