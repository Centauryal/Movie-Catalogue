package com.centaury.favoritecatalogue.ui.main;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;

import androidx.viewpager.widget.ViewPager;

import com.centaury.favoritecatalogue.R;
import com.centaury.favoritecatalogue.ui.base.BaseActivity;
import com.centaury.favoritecatalogue.ui.movie.LoadMovieCallback;
import com.centaury.favoritecatalogue.ui.movie.MovieFragment;
import com.centaury.favoritecatalogue.ui.tvshow.LoadTVShowCallback;
import com.centaury.favoritecatalogue.ui.tvshow.TVShowFragment;
import com.centaury.favoritecatalogue.utils.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements LoadMovieCallback, LoadTVShowCallback {

    @BindView(R.id.tab_favorite)
    TabLayout mTabFavorite;
    @BindView(R.id.vp_favorite)
    ViewPager mVpFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));

        setupViewPager(mVpFavorite);
        mTabFavorite.setupWithViewPager(mVpFavorite);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new MovieFragment(), getString(R.string.title_movie));
        viewPagerAdapter.addFragment(new TVShowFragment(), getString(R.string.title_tv_show));
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(Cursor movies) {
    }

    @Override
    public void preExecuteTVShow() {

    }

    @Override
    public void postExecuteTVShow(Cursor tvshow) {

    }
}
