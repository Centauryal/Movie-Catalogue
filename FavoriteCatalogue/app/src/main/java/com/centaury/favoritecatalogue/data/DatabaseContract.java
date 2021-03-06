package com.centaury.favoritecatalogue.data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.centaury.favoritecatalogue.data.entity.MovieEntity;
import com.centaury.favoritecatalogue.data.entity.TVShowEntity;
import com.centaury.favoritecatalogue.utils.AppConstants;

/**
 * Created by Centaury on 8/22/2019.
 */
public class DatabaseContract {

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static final class MovieColumns implements BaseColumns {
        public static final String ID = MovieEntity.COLUMN_ID;
        public static final String TITLE = MovieEntity.COLUMN_NAME;
        public static final String ORIGINAL_TITLE = MovieEntity.COLUMN_ORIGINAL;
        public static final String OVERVIEW = MovieEntity.COLUMN_DESC;
        public static final String POSTER_PATH = MovieEntity.COLUMN_PHOTO;
        public static final String BACKDROP_PATH = MovieEntity.COLUMN_BACKDROP;
        public static final String VOTE_AVERAGE = MovieEntity.COLUMN_VOTE;
        public static final String RELEASE_DATE = MovieEntity.COLUMN_DATE;
        public static final String GENRE = MovieEntity.COLUMN_GENRE;
        private static final String TABLE_NAME = MovieEntity.TABLE_NAME;
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(AppConstants.DATABASE_SCHEME)
                .authority(AppConstants.DATABASE_AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

    public static final class TVShowColumns implements BaseColumns {
        public static final String ID = TVShowEntity.COLUMN_ID;
        public static final String TITLE = TVShowEntity.COLUMN_NAME;
        public static final String ORIGINAL_TITLE = TVShowEntity.COLUMN_ORIGINAL;
        public static final String OVERVIEW = TVShowEntity.COLUMN_DESC;
        public static final String POSTER_PATH = TVShowEntity.COLUMN_PHOTO;
        public static final String BACKDROP_PATH = TVShowEntity.COLUMN_BACKDROP;
        public static final String VOTE_AVERAGE = TVShowEntity.COLUMN_VOTE;
        public static final String RELEASE_DATE = TVShowEntity.COLUMN_DATE;
        public static final String GENRE = TVShowEntity.COLUMN_GENRE;
        private static final String TABLE_NAME = TVShowEntity.TABLE_NAME;
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(AppConstants.DATABASE_SCHEME)
                .authority(AppConstants.DATABASE_AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
}
