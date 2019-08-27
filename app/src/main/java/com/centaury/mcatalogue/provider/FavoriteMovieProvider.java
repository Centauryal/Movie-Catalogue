package com.centaury.mcatalogue.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.centaury.mcatalogue.data.db.AppDatabase;
import com.centaury.mcatalogue.data.db.DatabaseContract;
import com.centaury.mcatalogue.data.db.dao.MovieDao;
import com.centaury.mcatalogue.data.db.dao.TVShowDao;
import com.centaury.mcatalogue.data.db.entity.MovieEntity;
import com.centaury.mcatalogue.data.db.entity.TVShowEntity;
import com.centaury.mcatalogue.ui.favorite.fragment.FavoriteMovieFragment;
import com.centaury.mcatalogue.ui.favorite.fragment.FavoriteTVShowFragment;

import static com.centaury.mcatalogue.data.db.DatabaseContract.AUTHORITY;
import static com.centaury.mcatalogue.data.db.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.centaury.mcatalogue.data.db.DatabaseContract.MovieColumns.TABLE_NAME;

/**
 * Created by Centaury on 8/19/2019.
 */
public class FavoriteMovieProvider extends ContentProvider {

    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final int TVSHOW = 3;
    private static final int TVSHOW_ID = 4;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private MovieDao movieDao;
    private TVShowDao tvShowDao;

    static {

        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIE);

        sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", MOVIE_ID);

        sUriMatcher.addURI(AUTHORITY, DatabaseContract.TVShowColumns.TABLE_NAME, TVSHOW);

        sUriMatcher.addURI(AUTHORITY, DatabaseContract.TVShowColumns.TABLE_NAME + "/#", TVSHOW_ID);
    }

    @Override
    public boolean onCreate() {
        movieDao = AppDatabase.getDatabase(getContext()).movieDao();
        tvShowDao = AppDatabase.getDatabase(getContext()).tvShowDao();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                cursor = movieDao.selectAll();
                break;
            case MOVIE_ID:
                cursor = movieDao.selectedById((int) ContentUris.parseId(uri));
                break;
            case TVSHOW:
                cursor = tvShowDao.selectAll();
                break;
            case TVSHOW_ID:
                cursor = tvShowDao.selectedById((int) ContentUris.parseId(uri));
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long added;
        Uri uriReturn;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                added = movieDao.insert(MovieEntity.fromContentValues(values));
                uriReturn = Uri.parse(CONTENT_URI + "/" + added);
                getContext().getContentResolver().notifyChange(CONTENT_URI,
                        new FavoriteMovieFragment.DataObserver(new Handler(getContext().getMainLooper()), getContext()));
                break;
            case TVSHOW:
                added = tvShowDao.insert(TVShowEntity.fromContentValues(values));
                uriReturn = Uri.parse(DatabaseContract.TVShowColumns.CONTENT_URI + "/" + added);
                getContext().getContentResolver().notifyChange(DatabaseContract.TVShowColumns.CONTENT_URI,
                        new FavoriteTVShowFragment.DataObserver(new Handler(getContext().getMainLooper()), getContext()));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }


        return ContentUris.withAppendedId(uriReturn, added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted = movieDao.deleteById((int) ContentUris.parseId(uri));
                getContext().getContentResolver().notifyChange(CONTENT_URI,
                        new FavoriteMovieFragment.DataObserver(new Handler(getContext().getMainLooper()), getContext()));
                break;
            case TVSHOW_ID:
                deleted = tvShowDao.deleteById((int) ContentUris.parseId(uri));
                getContext().getContentResolver().notifyChange(DatabaseContract.TVShowColumns.CONTENT_URI,
                        new FavoriteTVShowFragment.DataObserver(new Handler(getContext().getMainLooper()), getContext()));
                break;
            default:
                deleted = 0;
                break;
        }

        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
