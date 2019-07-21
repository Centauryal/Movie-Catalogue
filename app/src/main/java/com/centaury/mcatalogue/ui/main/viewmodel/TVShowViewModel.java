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
import com.centaury.mcatalogue.data.model.tvshow.TVShowResponse;
import com.centaury.mcatalogue.data.model.tvshow.TVShowResultsItem;
import com.centaury.mcatalogue.utils.AppConstants;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Centaury on 7/18/2019.
 */
public class TVShowViewModel extends ViewModel {

    private static final String TAG = TVShowViewModel.class.getSimpleName();

    private MutableLiveData<List<TVShowResultsItem>> listTVShowLiveData = new MutableLiveData<>();
    private MutableLiveData<List<GenresItem>> listGenreLiveData = new MutableLiveData<>();

    public LiveData<List<TVShowResultsItem>> getTVShows() {
        return listTVShowLiveData;
    }

    public LiveData<List<GenresItem>> getGenres() {
        return listGenreLiveData;
    }

    public void setTVShow(String language) {
        AndroidNetworking.get(AppConstants.BASE_URL + "discover/tv")
                .addQueryParameter("api_key", AppConstants.API_KEY)
                .addQueryParameter("language", language)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponseTVShow: " + response);
                        TVShowResponse tvShowResponse = new Gson().fromJson(response + "", TVShowResponse.class);
                        List<TVShowResultsItem> resultsItems = tvShowResponse.getResults();

                        listTVShowLiveData.setValue(resultsItems);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: " + anError.getErrorBody());
                    }
                });
    }

    public void setGenreTVShow(String language) {
        AndroidNetworking.get(AppConstants.BASE_URL + "genre/tv/list")
                .addQueryParameter("api_key", AppConstants.API_KEY)
                .addQueryParameter("language", language)
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
