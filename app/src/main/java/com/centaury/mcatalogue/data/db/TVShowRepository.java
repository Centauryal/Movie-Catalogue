package com.centaury.mcatalogue.data.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.centaury.mcatalogue.data.db.dao.TVShowDao;
import com.centaury.mcatalogue.data.db.entity.TVShowEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Centaury on 7/28/2019.
 */
public class TVShowRepository {
    private TVShowDao tvShowDao;
    private LiveData<List<TVShowEntity>> mAllTVShows;

    public TVShowRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        tvShowDao = database.tvShowDao();
        mAllTVShows = tvShowDao.getAllTVShows();
    }

    public LiveData<List<TVShowEntity>> getmAllTVShows() {
        return mAllTVShows;
    }

    public TVShowEntity getTVShow(int tvshowId) throws ExecutionException, InterruptedException {
        return new getTVShowAsync(tvShowDao).execute(tvshowId).get();
    }

    public void insertTVShow(TVShowEntity tvshow) {
        new insertTVShowsAsync(tvShowDao).execute(tvshow);
    }

    public void updateTVShow(TVShowEntity tvshow) {
        new updateTVShowsAsync(tvShowDao).execute(tvshow);
    }

    public void deleteTVShow(TVShowEntity tvshow) {
        new deleteTVShowsAsync(tvShowDao).execute(tvshow);
    }

    public void deleteAllTVShows() {
        new deleteAllTVShowsAsync(tvShowDao).execute();
    }

    /**
     * NOTE: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */

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

    private static class updateTVShowsAsync extends AsyncTask<TVShowEntity, Void, Void> {

        private TVShowDao mTVShowDaoAsync;

        updateTVShowsAsync(TVShowDao tvShowDao) {
            mTVShowDaoAsync = tvShowDao;
        }

        @Override
        protected Void doInBackground(TVShowEntity... tvShowEntities) {
            mTVShowDaoAsync.update(tvShowEntities[0]);
            return null;
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

    private static class deleteAllTVShowsAsync extends AsyncTask<TVShowEntity, Void, Void> {

        private TVShowDao mTVShowDaoAsync;

        deleteAllTVShowsAsync(TVShowDao tvShowDao) {
            mTVShowDaoAsync = tvShowDao;
        }

        @Override
        protected Void doInBackground(TVShowEntity... tvShowEntities) {
            mTVShowDaoAsync.deleteAll();
            return null;
        }
    }
}
