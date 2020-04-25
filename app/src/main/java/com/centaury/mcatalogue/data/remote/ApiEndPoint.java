package com.centaury.mcatalogue.data.remote;

import com.centaury.mcatalogue.BuildConfig;

/**
 * Created by Centaury on 4/21/2020.
 */
public final class ApiEndPoint {

    public static final String ENDPOINT_DISCOVERY_MOVIE = BuildConfig.BASE_URL + "discover/movie";
    public static final String ENDPOINT_GENRE_MOVIE = BuildConfig.BASE_URL + "genre/movie/list";
    public static final String ENDPOINT_SEARCH_MOVIE = BuildConfig.BASE_URL + "search/movie";
    public static final String ENDPOINT_DISCOVERY_TVSHOW = BuildConfig.BASE_URL + "discover/tv";
    public static final String ENDPOINT_GENRE_TVSHOW = BuildConfig.BASE_URL + "genre/tv/list";
    public static final String ENDPOINT_SEARCH_TVSHOW = BuildConfig.BASE_URL + "search/tv";

}
