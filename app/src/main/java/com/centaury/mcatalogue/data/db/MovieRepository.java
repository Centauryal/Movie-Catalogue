package com.centaury.mcatalogue.data.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.centaury.mcatalogue.data.db.dao.MovieDao;
import com.centaury.mcatalogue.data.db.entity.MovieEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Centaury on 7/28/2019.
 */
public class MovieRepository {

    private MovieDao movieDao;
    private LiveData<List<MovieEntity>> mAllMovies;

    public MovieRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        movieDao = database.movieDao();
        mAllMovies = movieDao.getAllMovies();
    }

    public LiveData<List<MovieEntity>> getmAllMovies() {
        return mAllMovies;
    }

    public MovieEntity getMovie(int movieId) throws ExecutionException, InterruptedException {
        return new getMovieAsync(movieDao).execute(movieId).get();
    }

    public void insertMovie(MovieEntity movie) {
        new insertMoviesAsync(movieDao).execute(movie);
    }

    public void updateMovie(MovieEntity movie) {
        new updateMoviesAsync(movieDao).execute(movie);
    }

    public void deleteMovie(MovieEntity movie) {
        new deleteMoviesAsync(movieDao).execute(movie);
    }

    public void deleteAllMovies() {
        new deleteAllMoviesAsync(movieDao).execute();
    }

    /**
     * NOTE: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */

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

    private static class updateMoviesAsync extends AsyncTask<MovieEntity, Void, Void> {

        private MovieDao mMovieDaoAsync;

        updateMoviesAsync(MovieDao movieDao) {
            mMovieDaoAsync = movieDao;
        }

        @Override
        protected Void doInBackground(MovieEntity... movieEntities) {
            mMovieDaoAsync.update(movieEntities[0]);
            return null;
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

    private static class deleteAllMoviesAsync extends AsyncTask<MovieEntity, Void, Void> {

        private MovieDao mMovieDaoAsync;

        deleteAllMoviesAsync(MovieDao movieDao) {
            mMovieDaoAsync = movieDao;
        }

        @Override
        protected Void doInBackground(MovieEntity... movieEntities) {
            mMovieDaoAsync.deleteAll();
            return null;
        }
    }
}
