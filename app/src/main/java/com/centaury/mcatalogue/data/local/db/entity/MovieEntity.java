package com.centaury.mcatalogue.data.local.db.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.centaury.mcatalogue.data.local.db.DatabaseContract.MovieColumns;

import static com.centaury.mcatalogue.data.local.db.DatabaseContract.getColumnInt;
import static com.centaury.mcatalogue.data.local.db.DatabaseContract.getColumnString;

/**
 * Created by Centaury on 7/27/2019.
 */
@Entity(tableName = MovieEntity.TABLE_NAME)
public class MovieEntity implements Parcelable {

    public static final String TABLE_NAME = "movies";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ORIGINAL = "original";
    public static final String COLUMN_DESC = "desc";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_BACKDROP = "backdrop";
    public static final String COLUMN_VOTE = "vote";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_GENRE = "genre";
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
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public int id;
    @ColumnInfo(name = COLUMN_NAME)
    private String title;
    @ColumnInfo(name = COLUMN_ORIGINAL)
    private String originalTitle;
    @ColumnInfo(name = COLUMN_DESC)
    private String overview;
    @ColumnInfo(name = COLUMN_PHOTO)
    private String posterPath;
    @ColumnInfo(name = COLUMN_BACKDROP)
    private String backdropPath;
    @ColumnInfo(name = COLUMN_VOTE)
    private String voteAverage;
    @ColumnInfo(name = COLUMN_DATE)
    private String releaseDate;
    @ColumnInfo(name = COLUMN_GENRE)
    private String genreIds;

    public MovieEntity() {

    }

    public MovieEntity(int id, String title, String originalTitle, String overview, String posterPath, String backdropPath, String voteAverage, String releaseDate, String genreIds) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.genreIds = genreIds;
    }

    public MovieEntity(Cursor cursor) {
        this.id = getColumnInt(cursor, MovieColumns.ID);
        this.title = getColumnString(cursor, MovieColumns.TITLE);
        this.originalTitle = getColumnString(cursor, MovieColumns.ORIGINAL_TITLE);
        this.overview = getColumnString(cursor, MovieColumns.OVERVIEW);
        this.posterPath = getColumnString(cursor, MovieColumns.POSTER_PATH);
        this.backdropPath = getColumnString(cursor, MovieColumns.BACKDROP_PATH);
        this.voteAverage = getColumnString(cursor, MovieColumns.VOTE_AVERAGE);
        this.releaseDate = getColumnString(cursor, MovieColumns.RELEASE_DATE);
        this.genreIds = getColumnString(cursor, MovieColumns.GENRE);
    }

    protected MovieEntity(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.voteAverage = in.readString();
        this.releaseDate = in.readString();
        this.genreIds = in.readString();
    }

    public static MovieEntity fromContentValues(ContentValues values) {
        final MovieEntity movieEntity = new MovieEntity();
        if (values.containsKey(COLUMN_ID)) {
            movieEntity.id = values.getAsInteger(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_NAME)) {
            movieEntity.title = values.getAsString(COLUMN_NAME);
        }
        if (values.containsKey(COLUMN_ORIGINAL)) {
            movieEntity.originalTitle = values.getAsString(COLUMN_ORIGINAL);
        }
        if (values.containsKey(COLUMN_DESC)) {
            movieEntity.overview = values.getAsString(COLUMN_DESC);
        }
        if (values.containsKey(COLUMN_PHOTO)) {
            movieEntity.posterPath = values.getAsString(COLUMN_PHOTO);
        }
        if (values.containsKey(COLUMN_BACKDROP)) {
            movieEntity.backdropPath = values.getAsString(COLUMN_BACKDROP);
        }
        if (values.containsKey(COLUMN_VOTE)) {
            movieEntity.voteAverage = values.getAsString(COLUMN_VOTE);
        }
        if (values.containsKey(COLUMN_DATE)) {
            movieEntity.releaseDate = values.getAsString(COLUMN_DATE);
        }
        if (values.containsKey(COLUMN_GENRE)) {
            movieEntity.genreIds = values.getAsString(COLUMN_GENRE);
        }
        return movieEntity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
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
        dest.writeString(this.title);
        dest.writeString(this.originalTitle);
        dest.writeString(this.overview);
        dest.writeString(this.posterPath);
        dest.writeString(this.backdropPath);
        dest.writeString(this.voteAverage);
        dest.writeString(this.releaseDate);
        dest.writeString(this.genreIds);
    }
}
