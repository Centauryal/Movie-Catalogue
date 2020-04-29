package com.centaury.mcatalogue.ui.search;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.ViewModelFactory;
import com.centaury.mcatalogue.data.remote.model.genre.GenresItem;
import com.centaury.mcatalogue.data.remote.model.movie.MovieResultsItem;
import com.centaury.mcatalogue.ui.base.BaseActivity;
import com.centaury.mcatalogue.ui.main.adapter.MovieAdapter;
import com.centaury.mcatalogue.ui.main.viewmodel.MovieViewModel;
import com.centaury.mcatalogue.utils.Helper;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchMovieActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_search_movie)
    RecyclerView mRvSearchmovie;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;
    @BindView(R.id.btn_try_again)
    TextView mBtnTryAgain;
    @BindView(R.id.empty_state)
    LinearLayout mEmptyState;

    private MovieAdapter movieAdapter;
    private MovieViewModel movieViewModel;
    private String language;
    private Observer<List<MovieResultsItem>> getSearchMovie = movieResultsItems -> {
        if (movieResultsItems != null) {
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
            toggleEmptyState(movieResultsItems.size(), mEmptyState, mRvSearchmovie);
            movieAdapter.setMovieData(movieResultsItems);
        }
    };
    private Observer<List<GenresItem>> getGenre = genresItems -> {
        if (genresItems != null) {
            movieAdapter.setGenreData(genresItems);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);
        ButterKnife.bind(this);
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));

        setUpToolbar(mToolbar);

        language = String.valueOf(Locale.getDefault().toLanguageTag());

        ViewModelFactory factory = ViewModelFactory.getInstance(this);
        movieViewModel = new ViewModelProvider(this, factory).get(MovieViewModel.class);
        movieViewModel.getGenres(language).observe(this, getGenre);

        showRecyclerList();
        mBtnTryAgain.setVisibility(View.GONE);
    }

    private Observer<List<MovieResultsItem>> getSearchMovie = movieResultsItems -> {
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
            mRvSearchmovie.setVisibility(View.VISIBLE);
        } else {
            mRvSearchmovie.setVisibility(View.GONE);
            mEmptyState.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setQueryHint(getString(R.string.txt_movie_hint));
            searchView.setIconified(false);

            EditText editSearch = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
            editSearch.setTextColor(Color.WHITE);
            editSearch.setHintTextColor(ContextCompat.getColor(this, R.color.grey_20));

            ImageView imageSearch = searchView.findViewById(androidx.appcompat.R.id.search_button);
            imageSearch.setImageResource(R.drawable.ic_search_white_24dp);

            ImageView imageCloseSearch = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
            imageCloseSearch.setImageResource(R.drawable.ic_close);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    String query = s.toLowerCase();
                    movieViewModel.getSearchMovie(query, language)
                            .observe(SearchMovieActivity.this, getSearchMovie);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    String query = s.toLowerCase();
                    movieViewModel.getSearchMovie(query, language)
                            .observe(SearchMovieActivity.this, getSearchMovie);
                    return false;
                }
            });

            searchView.setOnCloseListener(() -> {
                onBackPressed();
                return false;
            });

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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
        movieAdapter = new MovieAdapter(this);
        movieAdapter.notifyDataSetChanged();

        mRvSearchmovie.setLayoutManager(new LinearLayoutManager(this));
        mRvSearchmovie.setAdapter(movieAdapter);
        mRvSearchmovie.addItemDecoration(new Helper.TopItemDecoration(55));
    }
}
