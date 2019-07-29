package com.centaury.mcatalogue.data.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Centaury on 7/28/2019.
 */
@Entity(tableName = "tvshows")
public class TVShowEntity implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "original")
    private String originalName;

    @ColumnInfo(name = "desc")
    private String overview;

    @ColumnInfo(name = "photo")
    private String posterPath;

    @ColumnInfo(name = "backdrop")
    private String backdropPath;

    @ColumnInfo(name = "vote")
    private String voteAverage;

    @ColumnInfo(name = "date")
    private String firstAirDate;

    @ColumnInfo(name = "genre")
    private String genreIds;

    public TVShowEntity(int id, String name, String originalName, String overview, String posterPath, String backdropPath, String voteAverage, String firstAirDate, String genreIds) {
        this.id = id;
        this.name = name;
        this.originalName = originalName;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
        this.firstAirDate = firstAirDate;
        this.genreIds = genreIds;
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

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(String genreIds) {
        this.genreIds = genreIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.originalName);
        dest.writeString(this.overview);
        dest.writeString(this.posterPath);
        dest.writeString(this.backdropPath);
        dest.writeString(this.voteAverage);
        dest.writeString(this.firstAirDate);
        dest.writeString(this.genreIds);
    }

    protected TVShowEntity(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.originalName = in.readString();
        this.overview = in.readString();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.voteAverage = in.readString();
        this.firstAirDate = in.readString();
        this.genreIds = in.readString();
    }

    public static final Parcelable.Creator<TVShowEntity> CREATOR = new Parcelable.Creator<TVShowEntity>() {
        @Override
        public TVShowEntity createFromParcel(Parcel source) {
            return new TVShowEntity(source);
        }

        @Override
        public TVShowEntity[] newArray(int size) {
            return new TVShowEntity[size];
        }
    };
}
