package com.centaury.mcatalogue.ui.favorite.fragment;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.local.db.entity.MovieEntity;
import com.centaury.mcatalogue.ui.favorite.adapter.FavoriteMovieAdapter;
import com.centaury.mcatalogue.ui.favorite.viewmodel.FavoriteMovieViewModel;
import com.centaury.mcatalogue.utils.AppConstants;
import com.centaury.mcatalogue.utils.Helper;
import com.centaury.mcatalogue.utils.Mapping;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.centaury.mcatalogue.data.local.db.DatabaseContract.MovieColumns.CONTENT_URI;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovieFragment extends Fragment implements LoadMovieCallback {

    @BindView(R.id.rv_favmovie)
    RecyclerView mRvFavmovie;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;
    @BindView(R.id.empty_state)
    LinearLayout mEmptyState;
    private Unbinder unbinder;

    private FavoriteMovieAdapter favoriteMovieAdapter;
    private FavoriteMovieViewModel favoriteMovieViewModel;

    public FavoriteMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_movie, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        favoriteMovieViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(FavoriteMovieViewModel.class);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver dataObserver = new DataObserver(handler, getContext());
        getContext().getContentResolver().registerContentObserver(CONTENT_URI, true, dataObserver);

        showRecyclerList();

        if (savedInstanceState == null) {
            new LoadMovieAsync(getContext(), this).execute();
        } else {
            ArrayList<MovieEntity> list = savedInstanceState.getParcelableArrayList(AppConstants.EXTRA_STATE_MOVIE);
            if (list != null) {
                mShimmerViewContainer.stopShimmer();
                favoriteMovieAdapter.setMovies(list);
            }
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(AppConstants.EXTRA_STATE_MOVIE, favoriteMovieAdapter.getListMovies());
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
        new LoadMovieAsync(getContext(), this).execute();
        Helper.updateWidget(getContext());
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();
    }

    private void showRecyclerList() {
        favoriteMovieAdapter = new FavoriteMovieAdapter(getContext());

        mRvFavmovie.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvFavmovie.setAdapter(favoriteMovieAdapter);
        mRvFavmovie.setItemAnimator(new DefaultItemAnimator());
        mRvFavmovie.addItemDecoration(new Helper.TopItemDecoration(55));

        favoriteMovieAdapter.setOnDeleteItemClickCallback(this::showDialogDeleteFavorite);
    }

    private void showDialogDeleteFavorite(int movieId) {
        MovieEntity movieEntity;
        try {
            movieEntity = favoriteMovieViewModel.getMovie(movieId);

            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.item_alert_dialog, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            builder.setView(view);

            TextView title = view.findViewById(R.id.alerttitle);
            title.setText(getString(R.string.txt_title_delete_dialog));

            builder.setCancelable(false)
                    .setPositiveButton(getString(R.string.btn_delete), (dialog, which) -> {
                        favoriteMovieViewModel.deleteMovie(movieEntity);
                        dialog.dismiss();
                        Helper.updateWidget(getContext());
                        new LoadMovieAsync(getContext(), this).execute();
                        Toast.makeText(getContext(), getString(R.string.txt_remove_movie), Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(getString(R.string.btn_cancel), (dialog, which) -> dialog.dismiss());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void preExecute() {
        getActivity().runOnUiThread(() -> mShimmerViewContainer.startShimmer());

    }

    @Override
    public void postExecute(Cursor movies) {
        mShimmerViewContainer.stopShimmer();

        ArrayList<MovieEntity> movieEntities = Mapping.mapMovieCursorToArrayList(movies);
        if (movieEntities.size() > 0) {
            mEmptyState.setVisibility(View.GONE);
            mRvFavmovie.setVisibility(View.VISIBLE);
            mShimmerViewContainer.setVisibility(View.GONE);
            favoriteMovieAdapter.setMovies(movieEntities);
        } else {
            mRvFavmovie.setVisibility(View.GONE);
            mEmptyState.setVisibility(View.VISIBLE);
            mShimmerViewContainer.setVisibility(View.GONE);
            favoriteMovieAdapter.setMovies(new ArrayList<>());
        }
    }

    private static class LoadMovieAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMovieCallback> weakCallback;

        private LoadMovieAsync(Context context, LoadMovieCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecute(cursor);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        }
    }

    public static class DataObserver extends ContentObserver {

        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadMovieAsync(context, (LoadMovieCallback) context).execute();

        }
    }
}
