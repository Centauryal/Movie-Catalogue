package com.centaury.mcatalogue.data.local.db.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.centaury.mcatalogue.data.local.db.entity.TVShowEntity;

import java.util.List;

/**
 * Created by Centaury on 7/28/2019.
 */
@Dao
public interface TVShowDao {
    @Query("SELECT * FROM tvshows ORDER BY " + TVShowEntity.COLUMN_ID + " ASC")
    LiveData<List<TVShowEntity>> getAllTVShows();

    @Query("SELECT * FROM tvshows ORDER BY " + TVShowEntity.COLUMN_ID + " ASC")
    Cursor selectAll();

    @Query("SELECT * FROM tvshows WHERE " + TVShowEntity.COLUMN_ID + "=:id")
    TVShowEntity getTVShowById(int id);

    @Query("SELECT * FROM tvshows WHERE " + TVShowEntity.COLUMN_ID + "=:id")
    Cursor selectedById(int id);

    @Query("DELETE FROM tvshows WHERE " + TVShowEntity.COLUMN_ID + "=:id")
    int deleteById(int id);

    @Query("DELETE FROM tvshows")
    void deleteAll();

    @Insert
    long insert(TVShowEntity tvShowEntity);

    @Update
    void update(TVShowEntity tvShowEntity);

    @Delete
    void delete(TVShowEntity tvShowEntity);
}
