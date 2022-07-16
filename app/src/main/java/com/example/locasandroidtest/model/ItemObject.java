
package com.example.locasandroidtest.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.Objects;

public class ItemObject implements Parcelable {

    @SerializedName("dataMap")
    private DataMap mDataMap;
    @SerializedName("id")
    private String mId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("type")
    private String mType;
    private Uri capturedImageFile;
    private int checkedButtonId=-1;
    private boolean isToggled;
    private String comment;

    protected ItemObject(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mType = in.readString();
        capturedImageFile = in.readParcelable(Uri.class.getClassLoader());
        checkedButtonId = in.readInt();
        isToggled = in.readByte() != 0;
        comment = in.readString();
    }

    public static final Creator<ItemObject> CREATOR = new Creator<ItemObject>() {
        @Override
        public ItemObject createFromParcel(Parcel in) {
            return new ItemObject(in);
        }

        @Override
        public ItemObject[] newArray(int size) {
            return new ItemObject[size];
        }
    };

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public boolean isToggled() {
        return isToggled;
    }

    public void setToggled(boolean toggled) {
        isToggled = toggled;
    }

    public int getCheckedButtonId() {
        return checkedButtonId;
    }

    public void setCheckedButtonId(int checkedButtonId) {
        this.checkedButtonId = checkedButtonId;
    }

    public void setCapturedImageFile(Uri capturedImageFile) {
        this.capturedImageFile = capturedImageFile;
    }

    public Uri getCapturedImageFile() {
        return capturedImageFile;
    }

    public DataMap getDataMap() {
        return mDataMap;
    }

    public void setDataMap(DataMap dataMap) {
        mDataMap = dataMap;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mType);
        parcel.writeParcelable(capturedImageFile, i);
        parcel.writeInt(checkedButtonId);
        parcel.writeByte((byte) (isToggled ? 1 : 0));
        parcel.writeString(comment);
    }

    @Override
    public String toString() {
        return "ItemObject{" +
                "mDataMap=" + mDataMap +
                ", mId='" + mId + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mType='" + mType + '\'' +
                ", capturedImageFile=" + capturedImageFile +
                ", checkedButtonId=" + checkedButtonId +
                ", isToggled=" + isToggled +
                ", comment='" + comment + '\'' +
                '}';
    }
}
