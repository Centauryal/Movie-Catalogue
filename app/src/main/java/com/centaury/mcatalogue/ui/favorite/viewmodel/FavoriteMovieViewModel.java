package com.centaury.mcatalogue.ui.favorite.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.centaury.mcatalogue.data.local.db.MovieRepository;
import com.centaury.mcatalogue.data.local.db.entity.MovieEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Centaury on 7/28/2019.
 */
public class FavoriteMovieViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;
    private LiveData<List<MovieEntity>> movies;

    public FavoriteMovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
    }

    public LiveData<List<MovieEntity>> getMovies() {
        if (movies == null) {
            movies = movieRepository.getmAllMovies();
        }

        return movies;
    }

    public MovieEntity getMovie(int id) throws ExecutionException, InterruptedException {
        return movieRepository.getMovie(id);
    }

    public void insertMovie(MovieEntity movie) {
        movieRepository.insertMovie(movie);
    }

    public void deleteMovie(MovieEntity movie) {
        movieRepository.deleteMovie(movie);
    }
}
