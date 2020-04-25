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
import com.centaury.mcatalogue.data.remote.model.tvshow.TVShowResponse;
import com.centaury.mcatalogue.data.remote.model.tvshow.TVShowResultsItem;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Centaury on 7/18/2019.
 */
public class TVShowViewModel extends ViewModel {

    private static final String TAG = TVShowViewModel.class.getSimpleName();

    private MutableLiveData<List<TVShowResultsItem>> listTVShowLiveData = new MutableLiveData<>();
    private MutableLiveData<List<TVShowResultsItem>> listSearchTVShowData = new MutableLiveData<>();
    private MutableLiveData<List<GenresItem>> listGenreLiveData = new MutableLiveData<>();

    public LiveData<List<TVShowResultsItem>> getTVShows() {
        return listTVShowLiveData;
    }

    public LiveData<List<TVShowResultsItem>> getSearchTVShow() {
        return listSearchTVShowData;
    }

    public LiveData<List<GenresItem>> getGenres() {
        return listGenreLiveData;
    }

    public void setTVShow(String language) {
        AndroidNetworking.get(ApiEndPoint.ENDPOINT_DISCOVERY_TVSHOW)
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .addQueryParameter("language", language)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
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

    public void setSearchTVShow(String query, String language) {
        AndroidNetworking.get(ApiEndPoint.ENDPOINT_SEARCH_TVSHOW)
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .addQueryParameter("language", language)
                .addQueryParameter("query", query)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        TVShowResponse tvShowResponse = new Gson().fromJson(response + "", TVShowResponse.class);
                        List<TVShowResultsItem> resultsSearch = tvShowResponse.getResults();

                        listSearchTVShowData.setValue(resultsSearch);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: " + anError.getErrorBody());
                    }
                });
    }

    public void setGenreTVShow(String language) {
        AndroidNetworking.get(ApiEndPoint.ENDPOINT_GENRE_TVSHOW)
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
