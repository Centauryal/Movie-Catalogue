package com.centaury.mcatalogue.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.ui.base.BaseActivity;
import com.centaury.mcatalogue.ui.favorite.FavoriteActivity;
import com.centaury.mcatalogue.ui.main.fragment.MovieFragment;
import com.centaury.mcatalogue.ui.main.fragment.TVShowFragment;
import com.centaury.mcatalogue.ui.search.SearchMovieActivity;
import com.centaury.mcatalogue.ui.search.SearchTVShowActivity;
import com.centaury.mcatalogue.ui.settings.SettingsActivity;
import com.centaury.mcatalogue.utils.AppConstants;
import com.centaury.mcatalogue.utils.Helper;
import com.centaury.mcatalogue.utils.ViewPagerAdapter;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.favorite)
    ImageView mFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));

        setupViewPager(mViewPager);
        mTabs.setupWithViewPager(mViewPager);
        Helper.updateWidget(this);
        showcaseGuide();

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new MovieFragment(), getString(R.string.title_movie));
        viewPagerAdapter.addFragment(new TVShowFragment(), getString(R.string.title_tv_show));
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Helper.updateWidget(this);
    }

    private void showcaseGuide() {
        BubbleShowCaseBuilder builder = new BubbleShowCaseBuilder(this)
                .title(getString(R.string.title_favorite))
                .description(getString(R.string.txt_showcase_one))
                .backgroundColor(Color.WHITE)
                .textColor(Color.BLACK)
                .closeActionImage(ContextCompat.getDrawable(this, R.drawable.ic_close_black_24dp))
                .targetView(mFavorite)
                .showOnce(AppConstants.EXTRA_FIRST_RUN);
        builder.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick({R.id.search, R.id.favorite, R.id.settings})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.search:
                switch (mTabs.getSelectedTabPosition()) {
                    case 0:
                        startActivity(new Intent(this, SearchMovieActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(this, SearchTVShowActivity.class));
                        break;
                }
                break;
            case R.id.favorite:
                startActivity(new Intent(this, FavoriteActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
    }
}
