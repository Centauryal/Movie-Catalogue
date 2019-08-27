package com.centaury.favoritecatalogue.ui.main;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.centaury.favoritecatalogue.R;
import com.centaury.favoritecatalogue.ui.movie.LoadMovieCallback;
import com.centaury.favoritecatalogue.ui.movie.MovieFragment;
import com.centaury.favoritecatalogue.ui.tvshow.LoadTVShowCallback;
import com.centaury.favoritecatalogue.ui.tvshow.TVShowFragment;
import com.centaury.favoritecatalogue.utils.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoadMovieCallback, LoadTVShowCallback {

    @BindView(R.id.tab_favorite)
    TabLayout mTabFavorite;
    @BindView(R.id.vp_favorite)
    ViewPager mVpFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
        }

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
