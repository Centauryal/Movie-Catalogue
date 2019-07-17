package com.centaury.mcatalogue.ui.main.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.centaury.mcatalogue.data.model.genre.GenreResponse;
import com.centaury.mcatalogue.data.model.genre.GenresItem;
import com.centaury.mcatalogue.data.model.movie.MovieResponse;
import com.centaury.mcatalogue.data.model.movie.MovieResultsItem;
import com.centaury.mcatalogue.utils.AppConstants;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Centaury on 7/16/2019.
 */
public class MovieViewModel extends ViewModel {

    public static final String TAG = MovieViewModel.class.getSimpleName();

    private MutableLiveData<List<MovieResultsItem>> listMovieLiveData = new MutableLiveData<>();
    private MutableLiveData<List<GenresItem>> listGenreLiveData = new MutableLiveData<>();

    public LiveData<List<MovieResultsItem>> getMovies() {
        return listMovieLiveData;
    }

    public LiveData<List<GenresItem>> getGenres() {
        return listGenreLiveData;
    }

    public void setMovie() {
        AndroidNetworking.get(AppConstants.BASE_URL + "discover/movie")
                .addQueryParameter("api_key", AppConstants.API_KEY)
                .addQueryParameter("language", "en-US")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponseMovie: " + response);
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

    public void setGenre() {
        AndroidNetworking.get(AppConstants.BASE_URL + "genre/movie/list")
                .addQueryParameter("api_key", AppConstants.API_KEY)
                .addQueryParameter("language", "en-US")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponseGenre: " + response);
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
