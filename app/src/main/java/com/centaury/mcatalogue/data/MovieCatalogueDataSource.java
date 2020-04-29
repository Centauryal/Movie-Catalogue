package com.centaury.mcatalogue.data;

import androidx.lifecycle.LiveData;

import com.centaury.mcatalogue.data.local.db.entity.MovieEntity;
import com.centaury.mcatalogue.data.local.db.entity.TVShowEntity;
import com.centaury.mcatalogue.data.remote.model.genre.GenresItem;
import com.centaury.mcatalogue.data.remote.model.movie.MovieResultsItem;
import com.centaury.mcatalogue.data.remote.model.tvshow.TVShowResultsItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Centaury on 4/28/2020.
 */
public interface MovieCatalogueDataSource {

    LiveData<List<MovieResultsItem>> getMovies(String language);

    LiveData<List<MovieResultsItem>> getSearchMovies(String query, String language);

    LiveData<List<GenresItem>> getGenresMovies(String language);

    LiveData<List<TVShowResultsItem>> getTVShows(String language);

    LiveData<List<TVShowResultsItem>> getSearchTVShows(String query, String language);

    LiveData<List<GenresItem>> getGenresTVShows(String language);

    MovieEntity getFavMovie(int id) throws ExecutionException, InterruptedException;

    void insertFavMovie(MovieEntity movieEntity);

    void deleteFavMovie(MovieEntity movieEntity);

    TVShowEntity getFavTVShow(int tvShowId) throws ExecutionException, InterruptedException;

    void insertFavTVShow(TVShowEntity tvShowEntity);

    void deleteFavTVShow(TVShowEntity tvShowEntity);
}
