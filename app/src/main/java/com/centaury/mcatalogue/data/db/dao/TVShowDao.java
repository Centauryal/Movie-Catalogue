package com.centaury.mcatalogue.data.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.centaury.mcatalogue.data.db.entity.TVShowEntity;

import java.util.List;

/**
 * Created by Centaury on 7/28/2019.
 */
@Dao
public interface TVShowDao {
    @Query("SELECT * FROM tvshows ORDER BY id ASC")
    LiveData<List<TVShowEntity>> getAllTVShows();

    @Query("SELECT * FROM tvshows WHERE id=:id")
    TVShowEntity getTVShowById(int id);

    @Query("DELETE FROM tvshows")
    void deleteAll();

    @Insert
    long insert(TVShowEntity tvShowEntity);

    @Update
    void update(TVShowEntity tvShowEntity);

    @Delete
    void delete(TVShowEntity tvShowEntity);
}
