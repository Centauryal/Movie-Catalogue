package com.centaury.mcatalogue.ui.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.centaury.mcatalogue.data.MovieCatalogueRepository;
import com.centaury.mcatalogue.data.local.db.entity.TVShowEntity;
import com.centaury.mcatalogue.data.remote.model.genre.GenresItem;
import com.centaury.mcatalogue.data.remote.model.tvshow.TVShowResultsItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Centaury on 7/18/2019.
 */
public class TVShowViewModel extends ViewModel {

    private MovieCatalogueRepository movieCatalogueRepository;

    public TVShowViewModel(MovieCatalogueRepository movieCatalogueRepository) {
        this.movieCatalogueRepository = movieCatalogueRepository;
    }

    public LiveData<List<TVShowResultsItem>> getTVShows(String language) {
        return movieCatalogueRepository.getTVShows(language);
    }

    public LiveData<List<TVShowResultsItem>> getSearchTVShow(String query, String language) {
        return movieCatalogueRepository.getSearchTVShows(query, language);
    }

    public LiveData<List<GenresItem>> getGenres(String language) {
        return movieCatalogueRepository.getGenresTVShows(language);
    }

    public TVShowEntity getFavoriteTVShow(int id) throws ExecutionException, InterruptedException {
        return movieCatalogueRepository.getFavTVShow(id);
    }

    public void deleteFavoriteTVShow(TVShowEntity tvshow) {
        movieCatalogueRepository.deleteFavTVShow(tvshow);
    }
}
