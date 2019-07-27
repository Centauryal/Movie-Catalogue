package com.centaury.mcatalogue.data.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Centaury on 7/27/2019.
 */
@Entity(tableName = "movies")
public class MovieEntity implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "desc")
    String desc;

    @ColumnInfo(name = "photo")
    String photo;

    @ColumnInfo(name = "vote")
    String vote;

    @ColumnInfo(name = "date")
    String date;

    @ColumnInfo(name = "genre")
    List<Integer> genre;

    public MovieEntity(int id, String name, String desc, String photo, String vote, String date, List<Integer> genre) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.photo = photo;
        this.vote = vote;
        this.date = date;
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Integer> getGenre() {
        return genre;
    }

    public void setGenre(List<Integer> genre) {
        this.genre = genre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.desc);
        dest.writeString(this.photo);
        dest.writeString(this.vote);
        dest.writeString(this.date);
        dest.writeList(this.genre);
    }

    protected MovieEntity(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.desc = in.readString();
        this.photo = in.readString();
        this.vote = in.readString();
        this.date = in.readString();
        this.genre = new ArrayList<Integer>();
        in.readList(this.genre, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<MovieEntity> CREATOR = new Parcelable.Creator<MovieEntity>() {
        @Override
        public MovieEntity createFromParcel(Parcel source) {
            return new MovieEntity(source);
        }

        @Override
        public MovieEntity[] newArray(int size) {
            return new MovieEntity[size];
        }
    };
}
