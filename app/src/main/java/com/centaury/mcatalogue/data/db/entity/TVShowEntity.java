package com.centaury.mcatalogue.data.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import com.centaury.mcatalogue.data.db.DatabaseContract.TVShowColumns;

import static com.centaury.mcatalogue.data.db.DatabaseContract.getColumnInt;
import static com.centaury.mcatalogue.data.db.DatabaseContract.getColumnString;

/**
 * Created by Centaury on 7/28/2019.
 */
@Entity(tableName = TVShowEntity.TABLE_NAME)
public class TVShowEntity implements Parcelable {

    public static final String TABLE_NAME = "tvshows";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ORIGINAL = "original";
    public static final String COLUMN_DESC = "desc";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_BACKDROP = "backdrop";
    public static final String COLUMN_VOTE = "vote";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_GENRE = "genre";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public int id;

    @ColumnInfo(name = COLUMN_NAME)
    private String name;

    @ColumnInfo(name = COLUMN_ORIGINAL)
    private String originalName;

    @ColumnInfo(name = COLUMN_DESC)
    private String overview;

    @ColumnInfo(name = COLUMN_PHOTO)
    private String posterPath;

    @ColumnInfo(name = COLUMN_BACKDROP)
    private String backdropPath;

    @ColumnInfo(name = COLUMN_VOTE)
    private String voteAverage;

    @ColumnInfo(name = COLUMN_DATE)
    private String firstAirDate;

    @ColumnInfo(name = COLUMN_GENRE)
    private String genreIds;

    public static TVShowEntity fromContentValues(ContentValues values) {
        final TVShowEntity tvShowEntity = new TVShowEntity();
        if (values.containsKey(COLUMN_ID)) {
            tvShowEntity.id = values.getAsInteger(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_NAME)) {
            tvShowEntity.name = values.getAsString(COLUMN_NAME);
        }
        if (values.containsKey(COLUMN_ORIGINAL)) {
            tvShowEntity.originalName = values.getAsString(COLUMN_ORIGINAL);
        }
        if (values.containsKey(COLUMN_DESC)) {
            tvShowEntity.overview = values.getAsString(COLUMN_DESC);
        }
        if (values.containsKey(COLUMN_PHOTO)) {
            tvShowEntity.posterPath = values.getAsString(COLUMN_PHOTO);
        }
        if (values.containsKey(COLUMN_BACKDROP)) {
            tvShowEntity.backdropPath = values.getAsString(COLUMN_BACKDROP);
        }
        if (values.containsKey(COLUMN_VOTE)) {
            tvShowEntity.voteAverage = values.getAsString(COLUMN_VOTE);
        }
        if (values.containsKey(COLUMN_DATE)) {
            tvShowEntity.firstAirDate = values.getAsString(COLUMN_DATE);
        }
        if (values.containsKey(COLUMN_GENRE)) {
            tvShowEntity.genreIds = values.getAsString(COLUMN_GENRE);
        }
        return tvShowEntity;
    }

    public TVShowEntity() {

    }

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

    public TVShowEntity(Cursor cursor) {
        this.id = getColumnInt(cursor, TVShowColumns.ID);
        this.name = getColumnString(cursor, TVShowColumns.TITLE);
        this.originalName = getColumnString(cursor, TVShowColumns.ORIGINAL_TITLE);
        this.overview = getColumnString(cursor, TVShowColumns.OVERVIEW);
        this.posterPath = getColumnString(cursor, TVShowColumns.POSTER_PATH);
        this.backdropPath = getColumnString(cursor, TVShowColumns.BACKDROP_PATH);
        this.voteAverage = getColumnString(cursor, TVShowColumns.VOTE_AVERAGE);
        this.firstAirDate = getColumnString(cursor, TVShowColumns.RELEASE_DATE);
        this.genreIds = getColumnString(cursor, TVShowColumns.GENRE);
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
