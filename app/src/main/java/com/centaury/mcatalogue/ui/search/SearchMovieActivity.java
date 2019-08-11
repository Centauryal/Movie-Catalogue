package com.centaury.mcatalogue.ui.search;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.model.genre.GenresItem;
import com.centaury.mcatalogue.data.model.movie.MovieResultsItem;
import com.centaury.mcatalogue.ui.main.adapter.MovieAdapter;
import com.centaury.mcatalogue.ui.main.viewmodel.MovieViewModel;
import com.centaury.mcatalogue.utils.Helper;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchMovieActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_searchmovie)
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable arrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        if (arrow != null) {
            arrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
        getSupportActionBar().setHomeAsUpIndicator(arrow);

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getSearchMovie().observe(this, getSearchMovie);
        movieViewModel.getGenres().observe(this, getGenre);
        language = String.valueOf(Locale.getDefault().toLanguageTag());

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
            searchView.setQueryHint(getString(R.string.txt_hint_movie));
            searchView.setIconified(false);

            EditText editSearch = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            editSearch.setTextColor(Color.WHITE);
            editSearch.setHintTextColor(getResources().getColor(R.color.grey_20));

            ImageView imageSearch = searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
            imageSearch.setImageResource(R.drawable.ic_search_white_24dp);

            ImageView imageCloseSearch = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
            imageCloseSearch.setImageResource(R.drawable.ic_close);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    String query = s.toLowerCase();
                    movieViewModel.setSearchMovie(query, language);
                    movieViewModel.setGenre(language);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    String query = s.toLowerCase();
                    movieViewModel.setSearchMovie(query, language);
                    movieViewModel.setGenre(language);
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
        mRvSearchmovie.setItemAnimator(new DefaultItemAnimator());
        mRvSearchmovie.addItemDecoration(new Helper.TopItemDecoration(55));
    }
}
