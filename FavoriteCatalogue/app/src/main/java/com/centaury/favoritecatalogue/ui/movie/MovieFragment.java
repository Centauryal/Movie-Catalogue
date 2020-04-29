package com.centaury.favoritecatalogue.ui.movie;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.centaury.favoritecatalogue.R;
import com.centaury.favoritecatalogue.data.entity.MovieEntity;
import com.centaury.favoritecatalogue.ui.movie.adapter.MovieAdapter;
import com.centaury.favoritecatalogue.utils.AppConstants;
import com.centaury.favoritecatalogue.utils.Helper;
import com.centaury.favoritecatalogue.utils.Mapping;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.centaury.favoritecatalogue.data.DatabaseContract.MovieColumns.CONTENT_URI;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment implements LoadMovieCallback {

    @BindView(R.id.rv_favMovie)
    RecyclerView mRvFavmovie;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;
    @BindView(R.id.empty_state)
    LinearLayout mEmptyState;
    private Unbinder unbinder;

    private MovieAdapter movieAdapter;

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HandlerThread handlerThread = new HandlerThread(AppConstants.OBSERVER_MOVIE);
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver dataObserver = new DataObserver(handler, getContext());
        Objects.requireNonNull(getActivity()).getContentResolver().registerContentObserver(CONTENT_URI, true, dataObserver);
        new getData(getContext(), this).execute();

        showRecyclerList();

        if (savedInstanceState == null) {
            new getData(getContext(), this).execute();
        } else {
            ArrayList<MovieEntity> list = savedInstanceState.getParcelableArrayList(AppConstants.EXTRA_STATE_MOVIE);
            if (list != null) {
                mShimmerViewContainer.stopShimmer();
                movieAdapter.setMovies(list);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(AppConstants.EXTRA_STATE_MOVIE, movieAdapter.getListMovies());
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
        new getData(getContext(), this).execute();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();
    }

    private void showRecyclerList() {
        movieAdapter = new MovieAdapter(getContext());

        mRvFavmovie.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvFavmovie.setAdapter(movieAdapter);
        mRvFavmovie.addItemDecoration(new Helper.TopItemDecoration(55));

        movieAdapter.setOnDeleteItemClickCallback(this::showDialogDeleteFavorite);
    }

    private void showDialogDeleteFavorite(Uri uri) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.item_alert_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setView(view);

        TextView title = view.findViewById(R.id.alerttitle);
        title.setText(getString(R.string.txt_dialog_title_delete));

        builder.setCancelable(false)
                .setPositiveButton(getString(R.string.btn_delete), (dialog, which) -> {
                    Objects.requireNonNull(getActivity()).getContentResolver().delete(uri, null, null);
                    getActivity().getContentResolver().notifyChange(CONTENT_URI, new DataObserver(new Handler(), getContext()));
                    new getData(getContext(), this).execute();
                    dialog.dismiss();
                    Toast.makeText(getContext(), getString(R.string.txt_movie_remove), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(getString(R.string.btn_cancel), (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void preExecute() {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> mShimmerViewContainer.startShimmer());
    }

    @Override
    public void postExecute(Cursor movies) {
        ArrayList<MovieEntity> movieEntities = Mapping.mapMovieCursorToArrayList(movies);
        if (movieEntities.size() > 0) {
            mEmptyState.setVisibility(View.GONE);
            mRvFavmovie.setVisibility(View.VISIBLE);
            mShimmerViewContainer.setVisibility(View.GONE);
            movieAdapter.setMovies(movieEntities);
        } else {
            mRvFavmovie.setVisibility(View.GONE);
            mEmptyState.setVisibility(View.VISIBLE);
            mShimmerViewContainer.setVisibility(View.GONE);
            movieAdapter.setMovies(new ArrayList<>());
        }
    }

    static class getData extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMovieCallback> weakCallback;


        getData(Context context, LoadMovieCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return weakContext.get().getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor data) {
            super.onPostExecute(data);
            weakCallback.get().postExecute(data);
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
            new getData(context, (LoadMovieCallback) context).execute();
        }
    }
}
