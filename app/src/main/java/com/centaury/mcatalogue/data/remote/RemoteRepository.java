package com.centaury.mcatalogue.data.remote;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.centaury.mcatalogue.BuildConfig;
import com.centaury.mcatalogue.data.remote.model.genre.GenreResponse;
import com.centaury.mcatalogue.data.remote.model.genre.GenresItem;
import com.centaury.mcatalogue.data.remote.model.movie.MovieResponse;
import com.centaury.mcatalogue.data.remote.model.tvshow.TVShowResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Centaury on 4/28/2020.
 */
public class RemoteRepository {

    private static RemoteRepository INSTANCE;

    private RemoteRepository(Context context) {
    }

    public static RemoteRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteRepository(context);
        }

        return INSTANCE;
    }

    public void getMovie(String language, GetMovieCallback callback) {
        AndroidNetworking.get(ApiEndPoint.ENDPOINT_DISCOVERY_MOVIE)
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .addQueryParameter("language", language)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MovieResponse movieResponse = new Gson().fromJson(response + "", MovieResponse.class);
                        callback.onResponse(movieResponse);
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onErrorResponse("onErrorMovie: " + anError.getErrorBody());
                    }
                });

    }

    public void getSearchMovie(String query, String language, GetMovieCallback callback) {
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
                        callback.onResponse(movieResponse);
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onErrorResponse("onErrorSearchMovie: " + anError.getErrorBody());
                    }
                });
    }

    public void getGenreMovie(String language, GetGenreCallback callback) {
        AndroidNetworking.get(ApiEndPoint.ENDPOINT_GENRE_MOVIE)
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .addQueryParameter("language", language)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        GenreResponse genreResponse = new Gson().fromJson(response + "", GenreResponse.class);
                        callback.onResponse(genreResponse.getGenres());
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onErrorResponse("onErrorGenreMovie: " + anError.getErrorBody());
                    }
                });
    }

    public void getTVShow(String language, GetTVShowCallback callback) {
        AndroidNetworking.get(ApiEndPoint.ENDPOINT_DISCOVERY_TVSHOW)
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .addQueryParameter("language", language)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        TVShowResponse tvShowResponse = new Gson().fromJson(response + "", TVShowResponse.class);
                        callback.onResponse(tvShowResponse);
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onErrorResponse("onErrorTVShow: " + anError.getErrorBody());
                    }
                });
    }

    public void getSearchTVShow(String query, String language, GetTVShowCallback callback) {
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
                        callback.onResponse(tvShowResponse);
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onErrorResponse("onErrorSearchTVShow: " + anError.getErrorBody());
                    }
                });
    }

    public void getGenreTVShow(String language, GetGenreCallback callback) {
        AndroidNetworking.get(ApiEndPoint.ENDPOINT_GENRE_TVSHOW)
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .addQueryParameter("language", language)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        GenreResponse genreResponse = new Gson().fromJson(response + "", GenreResponse.class);
                        callback.onResponse(genreResponse.getGenres());
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onErrorResponse("onErrorGenreTVShow: " + anError.getErrorBody());
                    }
                });
    }

    public interface GetMovieCallback {
        void onResponse(MovieResponse movieResponse);

        void onErrorResponse(String message);
    }

    public interface GetGenreCallback {
        void onResponse(List<GenresItem> genresItemList);

        void onErrorResponse(String message);
    }

    public interface GetTVShowCallback {
        void onResponse(TVShowResponse tvShowResponse);

        void onErrorResponse(String message);
    }

}
