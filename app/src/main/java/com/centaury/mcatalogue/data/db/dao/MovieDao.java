package com.centaury.mcatalogue.data.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.centaury.mcatalogue.data.db.entity.MovieEntity;

import java.util.List;

/**
 * Created by Centaury on 7/27/2019.
 */
@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies ORDER BY " + MovieEntity.COLUMN_ID + " ASC")
    LiveData<List<MovieEntity>> getAllMovies();

    @Query("SELECT * FROM movies ORDER BY " + MovieEntity.COLUMN_ID + " ASC")
    List<MovieEntity> getAllWidgetMovies();

    @Query("SELECT * FROM movies ORDER BY " + MovieEntity.COLUMN_ID + " ASC")
    Cursor selectAll();

    @Query("SELECT * FROM movies WHERE " + MovieEntity.COLUMN_ID + "=:id")
    MovieEntity getMovieById(int id);

    @Query("SELECT * FROM movies WHERE " + MovieEntity.COLUMN_ID + "=:id")
    Cursor selectedById(int id);

    @Query("DELETE FROM movies WHERE " + MovieEntity.COLUMN_ID + "=:id")
    int deleteById(int id);

    @Query("DELETE FROM movies")
    void deleteAll();

    @Insert
    long insert(MovieEntity movieEntity);

    @Update
    void update(MovieEntity movieEntity);

    @Delete
    void delete(MovieEntity movieEntity);
}
