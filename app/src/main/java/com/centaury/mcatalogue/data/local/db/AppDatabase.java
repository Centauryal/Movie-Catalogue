package com.centaury.mcatalogue.data.local.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.centaury.mcatalogue.data.local.db.dao.MovieDao;
import com.centaury.mcatalogue.data.local.db.dao.TVShowDao;
import com.centaury.mcatalogue.data.local.db.entity.MovieEntity;
import com.centaury.mcatalogue.data.local.db.entity.TVShowEntity;
import com.centaury.mcatalogue.utils.AppConstants;

/**
 * Created by Centaury on 7/28/2019.
 */
@Database(entities = {MovieEntity.class, TVShowEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, AppConstants.DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    public abstract MovieDao movieDao();

    public abstract TVShowDao tvShowDao();
}
