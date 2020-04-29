package com.centaury.mcatalogue.ui.main.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.ViewModelFactory;
import com.centaury.mcatalogue.data.remote.model.genre.GenresItem;
import com.centaury.mcatalogue.data.remote.model.tvshow.TVShowResultsItem;
import com.centaury.mcatalogue.ui.base.BaseFragment;
import com.centaury.mcatalogue.ui.main.adapter.TVShowAdapter;
import com.centaury.mcatalogue.ui.main.viewmodel.TVShowViewModel;
import com.centaury.mcatalogue.utils.Helper;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TVShowFragment extends BaseFragment {

    @BindView(R.id.rv_tvshow)
    RecyclerView mRvTvshow;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;
    @BindView(R.id.empty_state)
    LinearLayout mEmptyState;
    private Unbinder unbinder;

    private TVShowAdapter tvShowAdapter;
    private TVShowViewModel tvShowViewModel;
    private String language;
    private Observer<List<TVShowResultsItem>> getTVShow = tvshowResultsItems -> {
        if (tvshowResultsItems != null) {
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
            toggleEmptyState(tvshowResultsItems.size(), mEmptyState, mRvTvshow);
            tvShowAdapter.setTVShowData(tvshowResultsItems);
        }
    };
    private Observer<List<GenresItem>> getGenre = genresItems -> {
        if (genresItems != null) {
            tvShowAdapter.setGenreTVShow(genresItems);
        }
    };

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

        language = String.valueOf(Locale.getDefault().toLanguageTag());

        ViewModelFactory factory = ViewModelFactory.getInstance(getActivity());
        tvShowViewModel = new ViewModelProvider(this, factory).get(TVShowViewModel.class);
        tvShowViewModel.getTVShows(language).observe(getViewLifecycleOwner(), getTVShow);
        tvShowViewModel.getGenres(language).observe(getViewLifecycleOwner(), getGenre);

        showRecyclerList();
        checkConnection(getContext());

    }

    private Observer<List<TVShowResultsItem>> getTVShow = tvshowResultsItems -> {
        if (tvshowResultsItems != null) {
            toggleEmptyTVShows(tvshowResultsItems.size());
            tvShowAdapter.setTVShowData(tvshowResultsItems);
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
        }
    };

    private Observer<List<GenresItem>> getGenre = genresItems -> {
        if (genresItems != null) {
            tvShowAdapter.setGenreTVShow(genresItems);
        }
    };

    private void toggleEmptyTVShows(int size) {
        if (size > 0) {
            mEmptyState.setVisibility(View.GONE);
            mRvTvshow.setVisibility(View.VISIBLE);
        } else {
            mRvTvshow.setVisibility(View.GONE);
            mEmptyState.setVisibility(View.VISIBLE);
        }
    }

    private void checkConnection(Context context) {
        if (Helper.isNetworkConnected(context)) {
            mShimmerViewContainer.startShimmer();
            tvShowViewModel.getTVShows(language).observe(getViewLifecycleOwner(), getTVShow);
            tvShowViewModel.getGenres(language).observe(getViewLifecycleOwner(), getGenre);
        } else {
            showNoInternet();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();
    }

    private void showRecyclerList() {
        tvShowAdapter = new TVShowAdapter(getContext());
        tvShowAdapter.notifyDataSetChanged();

        mRvTvshow.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvTvshow.setAdapter(tvShowAdapter);
        mRvTvshow.addItemDecoration(new Helper.TopItemDecoration(55));
    }

    private void showNoInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setView(R.layout.item_alert_dialog);
        builder.setPositiveButton(getString(R.string.btn_ok), (dialog, which) -> {
            dialog.dismiss();
            checkConnection(getContext());
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_try_again)
    public void onClick(View v) {
        if (v.getId() == R.id.btn_try_again) {
            mShimmerViewContainer.startShimmer();
            tvShowViewModel.getTVShows(language).observe(getViewLifecycleOwner(), getTVShow);
            tvShowViewModel.getGenres(language).observe(getViewLifecycleOwner(), getGenre);
        }
    }
}
