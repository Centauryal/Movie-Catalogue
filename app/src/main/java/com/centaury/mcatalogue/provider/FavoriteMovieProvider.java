package com.centaury.mcatalogue.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.centaury.mcatalogue.data.local.db.AppDatabase;
import com.centaury.mcatalogue.data.local.db.dao.MovieDao;
import com.centaury.mcatalogue.data.local.db.dao.TVShowDao;
import com.centaury.mcatalogue.data.local.db.entity.MovieEntity;
import com.centaury.mcatalogue.data.local.db.entity.TVShowEntity;
import com.centaury.mcatalogue.ui.favorite.fragment.FavoriteMovieFragment;
import com.centaury.mcatalogue.ui.favorite.fragment.FavoriteTVShowFragment;
import com.centaury.mcatalogue.utils.AppConstants;

import java.util.Objects;

import static com.centaury.mcatalogue.data.local.db.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.centaury.mcatalogue.data.local.db.DatabaseContract.MovieColumns.TABLE_NAME;
import static com.centaury.mcatalogue.data.local.db.DatabaseContract.TVShowColumns;

/**
 * Created by Centaury on 8/19/2019.
 */
public class FavoriteMovieProvider extends ContentProvider {


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(AppConstants.DATABASE_AUTHORITY, TABLE_NAME, AppConstants.MOVIE);

        sUriMatcher.addURI(AppConstants.DATABASE_AUTHORITY, TABLE_NAME + "/#", AppConstants.MOVIE_ID);

        sUriMatcher.addURI(AppConstants.DATABASE_AUTHORITY, TVShowColumns.TABLE_NAME, AppConstants.TVSHOW);

        sUriMatcher.addURI(AppConstants.DATABASE_AUTHORITY, TVShowColumns.TABLE_NAME + "/#", AppConstants.TVSHOW_ID);
    }

    private MovieDao movieDao;
    private TVShowDao tvShowDao;

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
            case AppConstants.MOVIE:
                cursor = movieDao.selectAll();
                break;
            case AppConstants.MOVIE_ID:
                cursor = movieDao.selectedById((int) ContentUris.parseId(uri));
                break;
            case AppConstants.TVSHOW:
                cursor = tvShowDao.selectAll();
                break;
            case AppConstants.TVSHOW_ID:
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
            case AppConstants.MOVIE:
                assert values != null;
                added = movieDao.insert(MovieEntity.fromContentValues(values));
                uriReturn = Uri.parse(CONTENT_URI + "/" + added);
                Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI,
                        new FavoriteMovieFragment.DataObserver(new Handler(getContext().getMainLooper()), getContext()));
                break;
            case AppConstants.TVSHOW:
                assert values != null;
                added = tvShowDao.insert(TVShowEntity.fromContentValues(values));
                uriReturn = Uri.parse(TVShowColumns.CONTENT_URI + "/" + added);
                Objects.requireNonNull(getContext()).getContentResolver().notifyChange(TVShowColumns.CONTENT_URI,
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
            case AppConstants.MOVIE_ID:
                deleted = movieDao.deleteById((int) ContentUris.parseId(uri));
                Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI,
                        new FavoriteMovieFragment.DataObserver(new Handler(getContext().getMainLooper()), getContext()));
                break;
            case AppConstants.TVSHOW_ID:
                deleted = tvShowDao.deleteById((int) ContentUris.parseId(uri));
                Objects.requireNonNull(getContext()).getContentResolver().notifyChange(TVShowColumns.CONTENT_URI,
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
