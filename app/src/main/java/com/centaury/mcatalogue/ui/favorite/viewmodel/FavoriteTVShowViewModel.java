package com.centaury.mcatalogue.ui.favorite.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.centaury.mcatalogue.data.db.TVShowRepository;
import com.centaury.mcatalogue.data.db.entity.TVShowEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Centaury on 7/28/2019.
 */
public class FavoriteTVShowViewModel extends AndroidViewModel {

    private TVShowRepository tvShowRepository;
    private LiveData<List<TVShowEntity>> tvshows;

    public FavoriteTVShowViewModel(@NonNull Application application) {
        super(application);
        tvShowRepository = new TVShowRepository(application);
    }

    public LiveData<List<TVShowEntity>> getTvshows() {
        if (tvshows == null) {
            tvshows = tvShowRepository.getmAllTVShows();
        }

        return tvshows;
    }

    public TVShowEntity getTVShow(int id) throws ExecutionException, InterruptedException {
        return tvShowRepository.getTVShow(id);
    }

    public void insertMovie(TVShowEntity tvshow) {
        tvShowRepository.insertTVShow(tvshow);
    }

    public void updateMovie(TVShowEntity tvshow) {
        tvShowRepository.updateTVShow(tvshow);
    }

    public void deleteMovie(TVShowEntity tvshow) {
        tvShowRepository.deleteTVShow(tvshow);
    }

    public void deleteAllMovies() {
        tvShowRepository.deleteAllTVShows();
    }
}
