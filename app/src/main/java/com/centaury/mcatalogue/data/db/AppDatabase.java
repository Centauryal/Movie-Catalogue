package com.centaury.mcatalogue.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.centaury.mcatalogue.data.db.dao.MovieDao;
import com.centaury.mcatalogue.data.db.dao.TVShowDao;
import com.centaury.mcatalogue.data.db.entity.MovieEntity;
import com.centaury.mcatalogue.data.db.entity.TVShowEntity;

/**
 * Created by Centaury on 7/28/2019.
 */
@Database(entities = {MovieEntity.class, TVShowEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();

    public abstract TVShowDao tvShowDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "movie_database")
                            .fallbackToDestructiveMigration()
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}
