package com.centaury.mcatalogue.di;

import android.content.Context;

import com.centaury.mcatalogue.data.MovieCatalogueRepository;
import com.centaury.mcatalogue.data.local.LocalRepository;
import com.centaury.mcatalogue.data.local.db.AppDatabase;
import com.centaury.mcatalogue.data.remote.RemoteRepository;

/**
 * Created by Centaury on 4/27/2020.
 */
public class Injection {

    public static MovieCatalogueRepository movieCatalogueRepository(Context context) {
        AppDatabase appDatabase = AppDatabase.getDatabase(context);

        LocalRepository localRepository = LocalRepository.getInstance(appDatabase.movieDao(), appDatabase.tvShowDao());
        RemoteRepository remoteRepository = RemoteRepository.getInstance(context);

        return MovieCatalogueRepository.getInstance(localRepository, remoteRepository);
    }
}
