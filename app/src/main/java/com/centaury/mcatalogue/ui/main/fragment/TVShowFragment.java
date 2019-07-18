package com.centaury.mcatalogue.ui.main.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.model.TVShow;
import com.centaury.mcatalogue.data.model.genre.GenresItem;
import com.centaury.mcatalogue.data.model.tvshow.TVShowResultsItem;
import com.centaury.mcatalogue.ui.main.adapter.TVShowAdapter;
import com.centaury.mcatalogue.ui.main.viewmodel.TVShowViewModel;
import com.centaury.mcatalogue.utils.Helper;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TVShowFragment extends Fragment {

    @BindView(R.id.rv_tvshow)
    RecyclerView mRvTvshow;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;
    @BindView(R.id.btn_try_again)
    TextView mBtnTryAgain;
    @BindView(R.id.empty_state)
    LinearLayout mEmptyState;
    private Unbinder unbinder;

    private TVShowAdapter tvShowAdapter;
    private TVShowViewModel tvShowViewModel;

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

        tvShowViewModel = ViewModelProviders.of(this).get(TVShowViewModel.class);
        tvShowViewModel.getTVShows().observe(this, getTVShow);
        tvShowViewModel.getGenres().observe(this, getGenre);

        showRecyclerList();

        mShimmerViewContainer.startShimmer();
        tvShowViewModel.setTVShow();
        tvShowViewModel.setGenreTVShow();
        mShimmerViewContainer.stopShimmer();

    }

    private Observer<List<TVShowResultsItem>> getTVShow = new Observer<List<TVShowResultsItem>>() {
        @Override
        public void onChanged(@Nullable List<TVShowResultsItem> tvshowResultsItems) {
            if (tvshowResultsItems != null) {
                tvShowAdapter.setTVShowData(tvshowResultsItems);
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
            } else {
                mEmptyState.setVisibility(View.VISIBLE);
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
            }
        }
    };

    private Observer<List<GenresItem>> getGenre = new Observer<List<GenresItem>>() {
        @Override
        public void onChanged(@Nullable List<GenresItem> genresItems) {
            if (genresItems != null) {
                tvShowAdapter.setGenreTVShow(genresItems);
            }
        }
    };

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
        mRvTvshow.setItemAnimator(new DefaultItemAnimator());
        mRvTvshow.addItemDecoration(new Helper.TopItemDecoration(55));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_try_again)
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_try_again:
                mShimmerViewContainer.startShimmer();
                tvShowViewModel.setTVShow();
                tvShowViewModel.setGenreTVShow();
                mShimmerViewContainer.stopShimmer();
                break;
        }
    }
}
