package com.centaury.mcatalogue.ui.detail;

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
import com.centaury.mcatalogue.utils.AppConstants;
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

    public void setGenreDetail() {
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
