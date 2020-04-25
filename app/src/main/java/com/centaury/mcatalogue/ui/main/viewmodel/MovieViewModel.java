package com.centaury.mcatalogue.ui.main.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.centaury.mcatalogue.BuildConfig;
import com.centaury.mcatalogue.data.remote.ApiEndPoint;
import com.centaury.mcatalogue.data.remote.model.genre.GenreResponse;
import com.centaury.mcatalogue.data.remote.model.genre.GenresItem;
import com.centaury.mcatalogue.data.remote.model.movie.MovieResponse;
import com.centaury.mcatalogue.data.remote.model.movie.MovieResultsItem;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Centaury on 7/16/2019.
 */
public class MovieViewModel extends ViewModel {

    private static final String TAG = MovieViewModel.class.getSimpleName();

    private MutableLiveData<List<MovieResultsItem>> listMovieLiveData = new MutableLiveData<>();
    private MutableLiveData<List<MovieResultsItem>> listSearchMovieData = new MutableLiveData<>();
    private MutableLiveData<List<GenresItem>> listGenreLiveData = new MutableLiveData<>();

    public LiveData<List<MovieResultsItem>> getMovies() {
        return listMovieLiveData;
    }

    public LiveData<List<MovieResultsItem>> getSearchMovie() {
        return listSearchMovieData;
    }

    public LiveData<List<GenresItem>> getGenres() {
        return listGenreLiveData;
    }

    public void setMovie(String language) {
        AndroidNetworking.get(ApiEndPoint.ENDPOINT_DISCOVERY_MOVIE)
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .addQueryParameter("language", language)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MovieResponse movieResponse = new Gson().fromJson(response + "", MovieResponse.class);
                        List<MovieResultsItem> resultsItems = movieResponse.getResults();

                        listMovieLiveData.setValue(resultsItems);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: " + anError.getErrorBody());
                    }
                });
    }

    public void setSearchMovie(String query, String language) {
        AndroidNetworking.get(ApiEndPoint.ENDPOINT_SEARCH_MOVIE)
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .addQueryParameter("language", language)
                .addQueryParameter("query", query)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MovieResponse movieResponse = new Gson().fromJson(response + "", MovieResponse.class);
                        List<MovieResultsItem> resultsSearch = movieResponse.getResults();

                        listSearchMovieData.setValue(resultsSearch);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: " + anError.getErrorBody());
                    }
                });
    }

    public void setGenre(String language) {
        AndroidNetworking.get(ApiEndPoint.ENDPOINT_GENRE_MOVIE)
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .addQueryParameter("language", language)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        GenreResponse genreResponse = new Gson().fromJson(response + "", GenreResponse.class);
                        List<GenresItem> genresItems = genreResponse.getGenres();

                        listGenreLiveData.setValue(genresItems);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: " + anError.getErrorBody());
                    }
                });
    }
}
