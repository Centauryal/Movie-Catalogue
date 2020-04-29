package com.centaury.mcatalogue.ui.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.centaury.mcatalogue.data.MovieCatalogueRepository;
import com.centaury.mcatalogue.data.local.db.entity.MovieEntity;
import com.centaury.mcatalogue.data.remote.model.genre.GenresItem;
import com.centaury.mcatalogue.data.remote.model.movie.MovieResultsItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Centaury on 7/16/2019.
 */
public class MovieViewModel extends ViewModel {

    private MovieCatalogueRepository movieCatalogueRepository;

    public MovieViewModel(MovieCatalogueRepository movieCatalogueRepository) {
        this.movieCatalogueRepository = movieCatalogueRepository;
    }

    public LiveData<List<MovieResultsItem>> getMovies(String language) {
        return movieCatalogueRepository.getMovies(language);
    }

    public LiveData<List<MovieResultsItem>> getSearchMovie(String query, String language) {
        return movieCatalogueRepository.getSearchMovies(query, language);
    }

    public LiveData<List<GenresItem>> getGenres(String language) {
        return movieCatalogueRepository.getGenresMovies(language);
    }

    public MovieEntity getFavoriteMovie(int id) throws ExecutionException, InterruptedException {
        return movieCatalogueRepository.getFavMovie(id);
    }

    public void deleteFavoriteMovie(MovieEntity movie) {
        movieCatalogueRepository.deleteFavMovie(movie);
    }
}
