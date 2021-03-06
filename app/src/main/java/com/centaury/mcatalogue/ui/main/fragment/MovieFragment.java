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
import com.centaury.mcatalogue.data.remote.model.movie.MovieResultsItem;
import com.centaury.mcatalogue.ui.base.BaseFragment;
import com.centaury.mcatalogue.ui.main.adapter.MovieAdapter;
import com.centaury.mcatalogue.ui.main.viewmodel.MovieViewModel;
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
public class MovieFragment extends BaseFragment {

    @BindView(R.id.rv_movie)
    RecyclerView mRvMovie;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;
    @BindView(R.id.empty_state)
    LinearLayout mEmptyState;
    private Unbinder unbinder;

    private MovieAdapter movieAdapter;
    private MovieViewModel movieViewModel;
    private String language;
    private Observer<List<MovieResultsItem>> getMovie = movieResultsItems -> {
        if (movieResultsItems != null) {
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
            toggleEmptyState(movieResultsItems.size(), mEmptyState, mRvMovie);
            movieAdapter.setMovieData(movieResultsItems);
        }
    };
    private Observer<List<GenresItem>> getGenre = genresItems -> {
        if (genresItems != null) {
            movieAdapter.setGenreData(genresItems);
        }
    };

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

        language = String.valueOf(Locale.getDefault().toLanguageTag());

        ViewModelFactory factory = ViewModelFactory.getInstance(getActivity());
        movieViewModel = new ViewModelProvider(this, factory).get(MovieViewModel.class);
        movieViewModel.getMovies(language).observe(getViewLifecycleOwner(), getMovie);
        movieViewModel.getGenres(language).observe(getViewLifecycleOwner(), getGenre);


        showRecyclerList();
        checkConnection(getContext());

    }

    private Observer<List<MovieResultsItem>> getMovie = movieResultsItems -> {
        if (movieResultsItems != null) {
            toggleEmptyMovies(movieResultsItems.size());
            movieAdapter.setMovieData(movieResultsItems);
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
        }
    };

    private Observer<List<GenresItem>> getGenre = genresItems -> {
        if (genresItems != null) {
            movieAdapter.setGenreData(genresItems);
        }
    };

    private void toggleEmptyMovies(int size) {
        if (size > 0) {
            mEmptyState.setVisibility(View.GONE);
            mRvMovie.setVisibility(View.VISIBLE);
        } else {
            mRvMovie.setVisibility(View.GONE);
            mEmptyState.setVisibility(View.VISIBLE);
        }
    }

    private void checkConnection(Context context) {
        if (Helper.isNetworkConnected(context)) {
            mShimmerViewContainer.startShimmer();
            movieViewModel.getMovies(language).observe(getViewLifecycleOwner(), getMovie);
            movieViewModel.getGenres(language).observe(getViewLifecycleOwner(), getGenre);
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
        movieAdapter = new MovieAdapter(getContext());
        movieAdapter.notifyDataSetChanged();

        mRvMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvMovie.setAdapter(movieAdapter);
        mRvMovie.addItemDecoration(new Helper.TopItemDecoration(55));
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
            movieViewModel.getMovies(language).observe(getViewLifecycleOwner(), getMovie);
            movieViewModel.getGenres(language).observe(getViewLifecycleOwner(), getGenre);
        }
    }
}
