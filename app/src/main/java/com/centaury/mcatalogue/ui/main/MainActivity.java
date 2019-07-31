package com.centaury.mcatalogue.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.ui.favorite.FavoriteActivity;
import com.centaury.mcatalogue.ui.main.fragment.MovieFragment;
import com.centaury.mcatalogue.ui.main.fragment.TVShowFragment;
import com.centaury.mcatalogue.utils.ViewPagerAdapter;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_FIRST_RUN = "first_run";
    @BindView(R.id.settings)
    ImageView mSettings;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
        }

        setupViewPager(mViewPager);
        mTabs.setupWithViewPager(mViewPager);
        showcaseGuide();

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new MovieFragment(), getString(R.string.title_movie));
        viewPagerAdapter.addFragment(new TVShowFragment(), getString(R.string.title_tv_show));
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void menuSettings() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, mSettings);
        popupMenu.getMenuInflater().inflate(R.menu.menu_settings, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.change_language) {
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void showcaseGuide() {
        BubbleShowCaseBuilder builder = new BubbleShowCaseBuilder(this)
                .title(getString(R.string.title_favorite))
                .description(getString(R.string.txt_showcase_one))
                .backgroundColor(Color.WHITE)
                .textColor(Color.BLACK)
                .closeActionImage(ContextCompat.getDrawable(this, R.drawable.ic_close_black_24dp))
                .targetView(mFavorite)
                .showOnce(EXTRA_FIRST_RUN);
        builder.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick({R.id.settings, R.id.favorite})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.settings:
                menuSettings();
                break;
            case R.id.favorite:
                startActivity(new Intent(this, FavoriteActivity.class));
                break;
        }
    }
}
