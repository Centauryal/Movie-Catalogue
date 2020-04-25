package com.centaury.mcatalogue.ui.detail.viewmodel;

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
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Centaury on 7/18/2019.
 */
public class DetailViewModel extends ViewModel {

    private static final String TAG = DetailViewModel.class.getSimpleName();

    private MutableLiveData<List<GenresItem>> listGenreLiveData = new MutableLiveData<>();

    public LiveData<List<GenresItem>> getGenresDetail() {
        return listGenreLiveData;
    }

    public void setGenreMovieDetail(String language) {
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

    public void setGenreTVShowDetail(String language) {
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
