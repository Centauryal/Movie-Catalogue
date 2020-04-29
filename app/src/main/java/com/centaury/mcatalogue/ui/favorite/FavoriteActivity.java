package com.centaury.mcatalogue.ui.favorite;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.ui.base.BaseActivity;
import com.centaury.mcatalogue.ui.favorite.fragment.FavoriteMovieFragment;
import com.centaury.mcatalogue.ui.favorite.fragment.FavoriteTVShowFragment;
import com.centaury.mcatalogue.ui.favorite.fragment.LoadMovieCallback;
import com.centaury.mcatalogue.ui.favorite.fragment.LoadTVShowCallback;
import com.centaury.mcatalogue.utils.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends BaseActivity implements LoadMovieCallback, LoadTVShowCallback {

    @BindView(R.id.tab_favorite)
    TabLayout mTabFavorite;
    @BindView(R.id.vp_favorite)
    ViewPager mVpFavorite;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));

        setUpToolbar(mToolbar);
        setupViewPager(mVpFavorite);
        mTabFavorite.setupWithViewPager(mVpFavorite);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new FavoriteMovieFragment(), getString(R.string.title_movie));
        viewPagerAdapter.addFragment(new FavoriteTVShowFragment(), getString(R.string.title_tv_show));
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
    public void postExecuteTVShow(Cursor tvshows) {

    }
}
