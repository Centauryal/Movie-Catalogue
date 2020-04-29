package com.centaury.mcatalogue.data.local;

import android.os.AsyncTask;

import com.centaury.mcatalogue.data.local.db.dao.MovieDao;
import com.centaury.mcatalogue.data.local.db.dao.TVShowDao;
import com.centaury.mcatalogue.data.local.db.entity.MovieEntity;
import com.centaury.mcatalogue.data.local.db.entity.TVShowEntity;

import java.util.concurrent.ExecutionException;

/**
 * Created by Centaury on 4/28/2020.
 */
public class LocalRepository {

    private static LocalRepository INSTANCE;
    private final MovieDao movieDao;
    private final TVShowDao tvShowDao;

    private LocalRepository(MovieDao movieDao, TVShowDao tvShowDao) {
        this.movieDao = movieDao;
        this.tvShowDao = tvShowDao;
    }

    public static LocalRepository getInstance(MovieDao movieDao, TVShowDao tvShowDao) {
        if (INSTANCE == null) {
            INSTANCE = new LocalRepository(movieDao, tvShowDao);
        }
        return INSTANCE;
    }

    public MovieEntity getMovie(int movieId) throws ExecutionException, InterruptedException {
        return new getMovieAsync(movieDao).execute(movieId).get();
    }

    public void insertMovie(MovieEntity movie) {
        new insertMoviesAsync(movieDao).execute(movie);
    }

    public void deleteMovie(MovieEntity movie) {
        new deleteMoviesAsync(movieDao).execute(movie);
    }

    public TVShowEntity getTVShow(int tvshowId) throws ExecutionException, InterruptedException {
        return new getTVShowAsync(tvShowDao).execute(tvshowId).get();
    }

    public void insertTVShow(TVShowEntity tvshow) {
        new insertTVShowsAsync(tvShowDao).execute(tvshow);
    }

    public void deleteTVShow(TVShowEntity tvshow) {
        new deleteTVShowsAsync(tvShowDao).execute(tvshow);
    }

    private static class getMovieAsync extends AsyncTask<Integer, Void, MovieEntity> {

        private MovieDao mMovieDaoAsync;

        getMovieAsync(MovieDao movieDao) {
            mMovieDaoAsync = movieDao;
        }

        @Override
        protected MovieEntity doInBackground(Integer... integers) {
            return mMovieDaoAsync.getMovieById(integers[0]);
        }
    }

    private static class insertMoviesAsync extends AsyncTask<MovieEntity, Void, Long> {

        private MovieDao mMovieDaoAsync;

        insertMoviesAsync(MovieDao movieDao) {
            mMovieDaoAsync = movieDao;
        }

        @Override
        protected Long doInBackground(MovieEntity... movieEntities) {
            return mMovieDaoAsync.insert(movieEntities[0]);
        }
    }

    private static class deleteMoviesAsync extends AsyncTask<MovieEntity, Void, Void> {

        private MovieDao mMovieDaoAsync;

        deleteMoviesAsync(MovieDao movieDao) {
            mMovieDaoAsync = movieDao;
        }

        @Override
        protected Void doInBackground(MovieEntity... movieEntities) {
            mMovieDaoAsync.delete(movieEntities[0]);
            return null;
        }
    }

    private static class getTVShowAsync extends AsyncTask<Integer, Void, TVShowEntity> {

        private TVShowDao mTVShowDaoAsync;

        getTVShowAsync(TVShowDao movieDao) {
            mTVShowDaoAsync = movieDao;
        }

        @Override
        protected TVShowEntity doInBackground(Integer... integers) {
            return mTVShowDaoAsync.getTVShowById(integers[0]);
        }
    }

    private static class insertTVShowsAsync extends AsyncTask<TVShowEntity, Void, Long> {

        private TVShowDao mTVShowDaoAsync;

        insertTVShowsAsync(TVShowDao tvShowDao) {
            mTVShowDaoAsync = tvShowDao;
        }

        @Override
        protected Long doInBackground(TVShowEntity... tvShowEntities) {
            return mTVShowDaoAsync.insert(tvShowEntities[0]);
        }
    }

    private static class deleteTVShowsAsync extends AsyncTask<TVShowEntity, Void, Void> {

        private TVShowDao mTVShowDaoAsync;

        deleteTVShowsAsync(TVShowDao tvShowDao) {
            mTVShowDaoAsync = tvShowDao;
        }

        @Override
        protected Void doInBackground(TVShowEntity... tvShowEntities) {
            mTVShowDaoAsync.delete(tvShowEntities[0]);
            return null;
        }
    }
}
