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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.ViewModelFactory;
import com.centaury.mcatalogue.data.local.db.entity.TVShowEntity;
import com.centaury.mcatalogue.ui.base.BaseFragment;
import com.centaury.mcatalogue.ui.favorite.adapter.FavoriteTVShowAdapter;
import com.centaury.mcatalogue.ui.main.viewmodel.TVShowViewModel;
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

import static com.centaury.mcatalogue.data.local.db.DatabaseContract.TVShowColumns.CONTENT_URI;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteTVShowFragment extends BaseFragment implements LoadTVShowCallback {

    @BindView(R.id.rv_favtvshow)
    RecyclerView mRvFavtvshow;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;
    @BindView(R.id.empty_state)
    LinearLayout mEmptyState;
    @BindView(R.id.btn_try_again)
    TextView mBtnTryAgain;
    private Unbinder unbinder;

    private FavoriteTVShowAdapter favoriteTVShowAdapter;
    private TVShowViewModel tvShowViewModel;

    public FavoriteTVShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_tvshow, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewModelFactory factory = ViewModelFactory.getInstance(getActivity());
        tvShowViewModel = new ViewModelProvider(this, factory).get(TVShowViewModel.class);

        HandlerThread handlerThread = new HandlerThread(AppConstants.OBSERVER_TVSHOW);
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver dataObserver = new DataObserver(handler, getContext());
        Objects.requireNonNull(getContext()).getContentResolver().registerContentObserver(CONTENT_URI, true, dataObserver);

        showRecyclerList();

        if (savedInstanceState == null) {
            new LoadTVShowAsync(getContext(), this).execute();
        } else {
            ArrayList<TVShowEntity> list = savedInstanceState.getParcelableArrayList(AppConstants.EXTRA_STATE_TVSHOW);
            if (list != null) {
                mShimmerViewContainer.stopShimmer();
                favoriteTVShowAdapter.setTVShows(list);
            }
        }

        mBtnTryAgain.setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(AppConstants.EXTRA_STATE_TVSHOW, favoriteTVShowAdapter.getListTVShows());
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
        new LoadTVShowAsync(getContext(), this).execute();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();
    }

    private void showRecyclerList() {
        favoriteTVShowAdapter = new FavoriteTVShowAdapter(getContext());

        mRvFavtvshow.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvFavtvshow.setAdapter(favoriteTVShowAdapter);
        mRvFavtvshow.addItemDecoration(new Helper.TopItemDecoration(55));

        favoriteTVShowAdapter.setOnDeleteItemClickCallback(this::showDialogDeleteFavorite);
    }

    private void showDialogDeleteFavorite(int tvshowId) {
        TVShowEntity tvShowEntity;
        try {
            tvShowEntity = tvShowViewModel.getFavoriteTVShow(tvshowId);

            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.item_alert_dialog, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            builder.setView(view);

            TextView title = view.findViewById(R.id.alerttitle);
            title.setText(getString(R.string.txt_dialog_title_delete));

            builder.setCancelable(false)
                    .setPositiveButton(getString(R.string.btn_delete), (dialog, which) -> {
                        tvShowViewModel.deleteFavoriteTVShow(tvShowEntity);
                        dialog.dismiss();
                        new LoadTVShowAsync(getContext(), this).execute();
                        Toast.makeText(getContext(), getString(R.string.txt_movie_remove), Toast.LENGTH_SHORT).show();
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
    public void preExecuteTVShow() {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> mShimmerViewContainer.startShimmer());
    }

    @Override
    public void postExecuteTVShow(Cursor tvshows) {
        mShimmerViewContainer.stopShimmer();

        Log.e("postExecuteTVShow: ", tvshows + "");

        ArrayList<TVShowEntity> tvShowEntities = Mapping.mapTVShowCursorToArrayList(tvshows);
        if (tvShowEntities.size() > 0) {
            mEmptyState.setVisibility(View.GONE);
            mRvFavtvshow.setVisibility(View.VISIBLE);
            mShimmerViewContainer.setVisibility(View.GONE);
            favoriteTVShowAdapter.setTVShows(tvShowEntities);
        } else {
            mRvFavtvshow.setVisibility(View.GONE);
            mEmptyState.setVisibility(View.VISIBLE);
            mShimmerViewContainer.setVisibility(View.GONE);
            favoriteTVShowAdapter.setTVShows(new ArrayList<>());
        }
    }

    private static class LoadTVShowAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadTVShowCallback> weakCallback;

        private LoadTVShowAsync(Context context, LoadTVShowCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecuteTVShow();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecuteTVShow(cursor);
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
            new LoadTVShowAsync(context, (LoadTVShowCallback) context).execute();

        }
    }
}
