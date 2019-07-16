package com.centaury.mcatalogue.ui.main.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.centaury.mcatalogue.data.model.Movie;
import com.centaury.mcatalogue.data.model.MovieResultsItem;
import com.centaury.mcatalogue.data.model.MoviesResponse;
import com.centaury.mcatalogue.utils.AppConstants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Centaury on 7/16/2019.
 */
public class MovieViewModel extends ViewModel {

    public static final String TAG = MovieViewModel.class.getSimpleName();
    private MutableLiveData<List<MovieResultsItem>> listMovieLiveData = new MutableLiveData<>();

    public LiveData<List<MovieResultsItem>> getMovies() {
        return listMovieLiveData;
    }

    public void setMovie() {
        List<MovieResultsItem> movieResultsItems = new ArrayList<>();
        AndroidNetworking.get(AppConstants.MOVIE_URL)
                .addPathParameter("api_key", AppConstants.API_KEY)
                .addPathParameter("language", "en-US")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);
                        MoviesResponse moviesResponse = new Gson().fromJson(response + "", MoviesResponse.class);
                        List<MovieResultsItem> resultsItems = moviesResponse.getResults();

                        movieResultsItems.addAll(resultsItems);
                        listMovieLiveData.postValue(movieResultsItems);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: " + anError.getMessage());
                    }
                });
    }
}
