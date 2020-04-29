package com.centaury.mcatalogue.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.centaury.mcatalogue.data.local.LocalRepository;
import com.centaury.mcatalogue.data.local.db.entity.MovieEntity;
import com.centaury.mcatalogue.data.local.db.entity.TVShowEntity;
import com.centaury.mcatalogue.data.remote.RemoteRepository;
import com.centaury.mcatalogue.data.remote.model.genre.GenresItem;
import com.centaury.mcatalogue.data.remote.model.movie.MovieResponse;
import com.centaury.mcatalogue.data.remote.model.movie.MovieResultsItem;
import com.centaury.mcatalogue.data.remote.model.tvshow.TVShowResponse;
import com.centaury.mcatalogue.data.remote.model.tvshow.TVShowResultsItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Centaury on 4/27/2020.
 */
public class MovieCatalogueRepository implements MovieCatalogueDataSource {

    private volatile static MovieCatalogueRepository INSTANCE = null;

    private final LocalRepository localRepository;
    private final RemoteRepository remoteRepository;

    private MovieCatalogueRepository(LocalRepository localRepository, RemoteRepository remoteRepository) {
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
    }

    public static MovieCatalogueRepository getInstance(LocalRepository localRepository, RemoteRepository remoteRepository) {
        if (INSTANCE == null) {
            synchronized (RemoteRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovieCatalogueRepository(localRepository, remoteRepository);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public LiveData<List<MovieResultsItem>> getMovies(String language) {
        MutableLiveData<List<MovieResultsItem>> movieResults = new MutableLiveData<>();

        remoteRepository.getMovie(language, new RemoteRepository.GetMovieCallback() {
            @Override
            public void onResponse(MovieResponse movieResponse) {
                movieResults.postValue(movieResponse.getResults());
            }

            @Override
            public void onErrorResponse(String message) {
                Log.e("onErrorResponse: ", message);
            }
        });
        return movieResults;
    }

    @Override
    public LiveData<List<MovieResultsItem>> getSearchMovies(String query, String language) {
        MutableLiveData<List<MovieResultsItem>> searchMovieResults = new MutableLiveData<>();

        remoteRepository.getSearchMovie(query, language, new RemoteRepository.GetMovieCallback() {
            @Override
            public void onResponse(MovieResponse movieResponse) {
                searchMovieResults.postValue(movieResponse.getResults());
            }

            @Override
            public void onErrorResponse(String message) {
                Log.e("onErrorResponse: ", message);
            }
        });
        return searchMovieResults;
    }

    @Override
    public LiveData<List<GenresItem>> getGenresMovies(String language) {
        MutableLiveData<List<GenresItem>> genreMovieResults = new MutableLiveData<>();

        remoteRepository.getGenreMovie(language, new RemoteRepository.GetGenreCallback() {
            @Override
            public void onResponse(List<GenresItem> genresItemList) {
                genreMovieResults.postValue(genresItemList);
            }

            @Override
            public void onErrorResponse(String message) {
                Log.e("onErrorResponse: ", message);
            }
        });
        return genreMovieResults;
    }

    @Override
    public LiveData<List<TVShowResultsItem>> getTVShows(String language) {
        MutableLiveData<List<TVShowResultsItem>> tvShowResults = new MutableLiveData<>();

        remoteRepository.getTVShow(language, new RemoteRepository.GetTVShowCallback() {
            @Override
            public void onResponse(TVShowResponse tvShowResponse) {
                tvShowResults.postValue(tvShowResponse.getResults());
            }

            @Override
            public void onErrorResponse(String message) {
                Log.e("onErrorResponse: ", message);
            }
        });
        return tvShowResults;
    }

    @Override
    public LiveData<List<TVShowResultsItem>> getSearchTVShows(String query, String language) {
        MutableLiveData<List<TVShowResultsItem>> searchTVShowResults = new MutableLiveData<>();

        remoteRepository.getSearchTVShow(query, language, new RemoteRepository.GetTVShowCallback() {
            @Override
            public void onResponse(TVShowResponse tvShowResponse) {
                searchTVShowResults.postValue(tvShowResponse.getResults());
            }

            @Override
            public void onErrorResponse(String message) {
                Log.e("onErrorResponse: ", message);
            }
        });
        return searchTVShowResults;
    }

    @Override
    public LiveData<List<GenresItem>> getGenresTVShows(String language) {
        MutableLiveData<List<GenresItem>> genreTVShowResults = new MutableLiveData<>();

        remoteRepository.getGenreTVShow(language, new RemoteRepository.GetGenreCallback() {
            @Override
            public void onResponse(List<GenresItem> genresItemList) {
                genreTVShowResults.postValue(genresItemList);
            }

            @Override
            public void onErrorResponse(String message) {
                Log.e("onErrorResponse: ", message);
            }
        });
        return genreTVShowResults;
    }

    @Override
    public MovieEntity getFavMovie(int id) throws ExecutionException, InterruptedException {
        return localRepository.getMovie(id);
    }

    @Override
    public void insertFavMovie(MovieEntity movieEntity) {
        localRepository.insertMovie(movieEntity);
    }

    @Override
    public void deleteFavMovie(MovieEntity movieEntity) {
        localRepository.deleteMovie(movieEntity);
    }

    @Override
    public TVShowEntity getFavTVShow(int tvShowId) throws ExecutionException, InterruptedException {
        return localRepository.getTVShow(tvShowId);
    }

    @Override
    public void insertFavTVShow(TVShowEntity tvShowEntity) {
        localRepository.insertTVShow(tvShowEntity);
    }

    @Override
    public void deleteFavTVShow(TVShowEntity tvShowEntity) {
        localRepository.deleteTVShow(tvShowEntity);
    }
}
