package com.centaury.favoritecatalogue.ui.tvshow;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.centaury.favoritecatalogue.R;
import com.centaury.favoritecatalogue.data.entity.TVShowEntity;
import com.centaury.favoritecatalogue.ui.tvshow.adapter.TVShowAdapter;
import com.centaury.favoritecatalogue.utils.Helper;
import com.centaury.favoritecatalogue.utils.Mapping;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.centaury.favoritecatalogue.data.DatabaseContract.TVShowColumns.CONTENT_URI;

/**
 * A simple {@link Fragment} subclass.
 */
public class TVShowFragment extends Fragment implements LoadTVShowCallback {

    private static final String EXTRA_STATE_TVSHOW = "EXTRA_STATE_TVSHOW";

    @BindView(R.id.rv_favtvshow)
    RecyclerView mRvFavtvshow;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;
    @BindView(R.id.empty_state)
    LinearLayout mEmptyState;
    private Unbinder unbinder;

    private TVShowAdapter tvShowAdapter;

    public TVShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tvshow, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HandlerThread handlerThread = new HandlerThread("Observer");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserverTVShow dataObserverTVShow = new DataObserverTVShow(handler, getContext());
        getActivity().getContentResolver().registerContentObserver(CONTENT_URI, true, dataObserverTVShow);
        new getDataTVShow(getContext(), this).execute();

        showRecyclerList();

        if (savedInstanceState == null) {
            new getDataTVShow(getContext(), this).execute();
        } else {
            ArrayList<TVShowEntity> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE_TVSHOW);
            if (list != null) {
                mShimmerViewContainer.stopShimmer();
                tvShowAdapter.setTVShows(list);
            }
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE_TVSHOW, tvShowAdapter.getListTVShows());
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
        new getDataTVShow(getContext(), this).execute();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();
    }

    private void showRecyclerList() {
        tvShowAdapter = new TVShowAdapter(getContext());

        mRvFavtvshow.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvFavtvshow.setAdapter(tvShowAdapter);
        mRvFavtvshow.setItemAnimator(new DefaultItemAnimator());
        mRvFavtvshow.addItemDecoration(new Helper.TopItemDecoration(55));

        tvShowAdapter.setOnDeleteItemClickCallback(this::showDialogDeleteFavorite);
    }

    private void showDialogDeleteFavorite(Uri uri) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.item_alert_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        TextView title = view.findViewById(R.id.alerttitle);
        title.setText(getString(R.string.txt_title_delete_dialog));

        builder.setCancelable(false)
                .setPositiveButton(getString(R.string.btn_delete), (dialog, which) -> {
                    getActivity().getContentResolver().delete(uri, null, null);
                    getActivity().getContentResolver().notifyChange(CONTENT_URI, new DataObserverTVShow(new Handler(), getContext()));
                    new getDataTVShow(getContext(), this).execute();
                    dialog.dismiss();
                    Toast.makeText(getContext(), getString(R.string.txt_remove_movie), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(getString(R.string.btn_cancel), (dialog, which) -> {
                    dialog.dismiss();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void preExecuteTVShow() {
        getActivity().runOnUiThread(() -> mShimmerViewContainer.startShimmer());
    }

    @Override
    public void postExecuteTVShow(Cursor tvshow) {
        ArrayList<TVShowEntity> tvShowEntities = Mapping.mapTVShowCursorToArrayList(tvshow);
        if (tvShowEntities.size() > 0) {
            mEmptyState.setVisibility(View.GONE);
            mRvFavtvshow.setVisibility(View.VISIBLE);
            mShimmerViewContainer.setVisibility(View.GONE);
            tvShowAdapter.setTVShows(tvShowEntities);
        } else {
            mRvFavtvshow.setVisibility(View.GONE);
            mEmptyState.setVisibility(View.VISIBLE);
            mShimmerViewContainer.setVisibility(View.GONE);
            tvShowAdapter.setTVShows(new ArrayList<>());
        }
    }

    public static class getDataTVShow extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadTVShowCallback> weakCallback;


        getDataTVShow(Context context, LoadTVShowCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecuteTVShow();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return weakContext.get().getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor data) {
            super.onPostExecute(data);
            weakCallback.get().postExecuteTVShow(data);
        }

    }

    public static class DataObserverTVShow extends ContentObserver {

        final Context context;

        public DataObserverTVShow(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new getDataTVShow(context, (LoadTVShowCallback) context).execute();
        }
    }
}
