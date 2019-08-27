package com.centaury.mcatalogue.utils;

import android.database.Cursor;

import com.centaury.mcatalogue.data.db.DatabaseContract;
import com.centaury.mcatalogue.data.db.entity.MovieEntity;
import com.centaury.mcatalogue.data.db.entity.TVShowEntity;

import java.util.ArrayList;

import static com.centaury.mcatalogue.data.db.DatabaseContract.MovieColumns.BACKDROP_PATH;
import static com.centaury.mcatalogue.data.db.DatabaseContract.MovieColumns.GENRE;
import static com.centaury.mcatalogue.data.db.DatabaseContract.MovieColumns.ID;
import static com.centaury.mcatalogue.data.db.DatabaseContract.MovieColumns.ORIGINAL_TITLE;
import static com.centaury.mcatalogue.data.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.centaury.mcatalogue.data.db.DatabaseContract.MovieColumns.POSTER_PATH;
import static com.centaury.mcatalogue.data.db.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.centaury.mcatalogue.data.db.DatabaseContract.MovieColumns.TITLE;
import static com.centaury.mcatalogue.data.db.DatabaseContract.MovieColumns.VOTE_AVERAGE;


/**
 * Created by Centaury on 8/22/2019.
 */
public class Mapping {

    public static ArrayList<MovieEntity> mapMovieCursorToArrayList(Cursor cursor) {
        ArrayList<MovieEntity> movieEntities = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String originalTitle = cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_TITLE));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW));
            String posterPath = cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH));
            String backdropPath = cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP_PATH));
            String voteAverage = cursor.getString(cursor.getColumnIndexOrThrow(VOTE_AVERAGE));
            String releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE));
            String genreIds = cursor.getString(cursor.getColumnIndexOrThrow(GENRE));

            movieEntities.add(new MovieEntity(id, title, originalTitle, overview, posterPath, backdropPath,
                    voteAverage, releaseDate, genreIds));
        }
        return movieEntities;
    }

    public static ArrayList<TVShowEntity> mapTVShowCursorToArrayList(Cursor cursortv) {
        ArrayList<TVShowEntity> tvShowEntities = new ArrayList<>();

        while (cursortv.moveToNext()) {
            int id = cursortv.getInt(cursortv.getColumnIndexOrThrow(DatabaseContract.TVShowColumns.ID));
            String title = cursortv.getString(cursortv.getColumnIndexOrThrow(DatabaseContract.TVShowColumns.TITLE));
            String originalTitle = cursortv.getString(cursortv.getColumnIndexOrThrow(DatabaseContract.TVShowColumns.ORIGINAL_TITLE));
            String overview = cursortv.getString(cursortv.getColumnIndexOrThrow(DatabaseContract.TVShowColumns.OVERVIEW));
            String posterPath = cursortv.getString(cursortv.getColumnIndexOrThrow(DatabaseContract.TVShowColumns.POSTER_PATH));
            String backdropPath = cursortv.getString(cursortv.getColumnIndexOrThrow(DatabaseContract.TVShowColumns.BACKDROP_PATH));
            String voteAverage = cursortv.getString(cursortv.getColumnIndexOrThrow(DatabaseContract.TVShowColumns.VOTE_AVERAGE));
            String releaseDate = cursortv.getString(cursortv.getColumnIndexOrThrow(DatabaseContract.TVShowColumns.RELEASE_DATE));
            String genreIds = cursortv.getString(cursortv.getColumnIndexOrThrow(DatabaseContract.TVShowColumns.GENRE));

            tvShowEntities.add(new TVShowEntity(id, title, originalTitle, overview, posterPath, backdropPath,
                    voteAverage, releaseDate, genreIds));
        }

        return tvShowEntities;
    }
}
