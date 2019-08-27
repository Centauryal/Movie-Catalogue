package com.centaury.mcatalogue.ui.favorite;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.ui.favorite.fragment.FavoriteMovieFragment;
import com.centaury.mcatalogue.ui.favorite.fragment.FavoriteTVShowFragment;
import com.centaury.mcatalogue.ui.favorite.fragment.LoadMovieCallback;
import com.centaury.mcatalogue.ui.favorite.fragment.LoadTVShowCallback;
import com.centaury.mcatalogue.utils.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavoriteActivity extends AppCompatActivity implements LoadMovieCallback, LoadTVShowCallback {

    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tab_favorite)
    TabLayout mTabFavorite;
    @BindView(R.id.vp_favorite)
    ViewPager mVpFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
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
        viewPagerAdapter.addFragment(new FavoriteMovieFragment(), getString(R.string.title_movie));
        viewPagerAdapter.addFragment(new FavoriteTVShowFragment(), getString(R.string.title_tv_show));
        viewPager.setAdapter(viewPagerAdapter);
    }

    @OnClick(R.id.btn_back)
    public void onClick(View v) {
        if (v.getId() == R.id.btn_back) {
            onBackPressed();
        }
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
