package com.centaury.mcatalogue.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Centaury on 7/5/2019.
 */
public class TVShow implements Parcelable {

    private int photo;
    private String name;
    private String date;
    private String desc;

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.photo);
        dest.writeString(this.name);
        dest.writeString(this.date);
        dest.writeString(this.desc);
    }

    public TVShow() {
    }

    protected TVShow(Parcel in) {
        this.photo = in.readInt();
        this.name = in.readString();
        this.date = in.readString();
        this.desc = in.readString();
    }

    public static final Parcelable.Creator<TVShow> CREATOR = new Parcelable.Creator<TVShow>() {
        @Override
        public TVShow createFromParcel(Parcel source) {
            return new TVShow(source);
        }

        @Override
        public TVShow[] newArray(int size) {
            return new TVShow[size];
        }
    };
}
