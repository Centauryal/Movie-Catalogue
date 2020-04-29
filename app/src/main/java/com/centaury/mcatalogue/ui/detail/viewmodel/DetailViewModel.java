package com.centaury.mcatalogue.ui.detail.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.centaury.mcatalogue.data.MovieCatalogueRepository;
import com.centaury.mcatalogue.data.local.db.entity.MovieEntity;
import com.centaury.mcatalogue.data.local.db.entity.TVShowEntity;
import com.centaury.mcatalogue.data.remote.model.genre.GenresItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Centaury on 7/18/2019.
 */
public class DetailViewModel extends ViewModel {

    private MovieCatalogueRepository movieCatalogueRepository;

    public DetailViewModel(MovieCatalogueRepository movieCatalogueRepository) {
        this.movieCatalogueRepository = movieCatalogueRepository;
    }

    public LiveData<List<GenresItem>> getGenresMovie(String language) {
        return movieCatalogueRepository.getGenresMovies(language);
    }

    public LiveData<List<GenresItem>> getGenresTVShow(String language) {
        return movieCatalogueRepository.getGenresTVShows(language);
    }

    public MovieEntity getFavoriteMovie(int id) throws ExecutionException, InterruptedException {
        return movieCatalogueRepository.getFavMovie(id);
    }

    public void insertFavoriteMovie(MovieEntity movie) {
        movieCatalogueRepository.insertFavMovie(movie);
    }

    public void deleteFavoriteMovie(MovieEntity movie) {
        movieCatalogueRepository.deleteFavMovie(movie);
    }

    public TVShowEntity getFavoriteTVShow(int id) throws ExecutionException, InterruptedException {
        return movieCatalogueRepository.getFavTVShow(id);
    }

    public void insertFavoriteTVShow(TVShowEntity tvshow) {
        movieCatalogueRepository.insertFavTVShow(tvshow);
    }

    public void deleteFavoriteTVShow(TVShowEntity tvshow) {
        movieCatalogueRepository.deleteFavTVShow(tvshow);
    }
}
