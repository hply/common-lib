package com.gogoal.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author wangjd on 2017/12/19 15:42.
 * @description :${annotated}.
 */
public class BottomGridData implements Parcelable{
    private String itemText;
    private String imageUrl;
    private int imageRes;

    public BottomGridData(String itemText, int imageRes) {
        this.itemText = itemText;
        this.imageRes = imageRes;
    }

    public BottomGridData(String itemText, String imageUrl) {
        this.itemText = itemText;
        this.imageUrl = imageUrl;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemText);
        dest.writeString(this.imageUrl);
        dest.writeInt(this.imageRes);
    }

    protected BottomGridData(Parcel in) {
        this.itemText = in.readString();
        this.imageUrl = in.readString();
        this.imageRes = in.readInt();
    }

    public static final Creator<BottomGridData> CREATOR = new Creator<BottomGridData>() {
        @Override
        public BottomGridData createFromParcel(Parcel source) {
            return new BottomGridData(source);
        }

        @Override
        public BottomGridData[] newArray(int size) {
            return new BottomGridData[size];
        }
    };
}
