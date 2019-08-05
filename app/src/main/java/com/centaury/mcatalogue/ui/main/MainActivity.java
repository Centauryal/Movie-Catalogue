package com.centaury.mcatalogue.ui.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.ui.favorite.FavoriteActivity;
import com.centaury.mcatalogue.ui.main.fragment.MovieFragment;
import com.centaury.mcatalogue.ui.main.fragment.TVShowFragment;
import com.centaury.mcatalogue.utils.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_FIRST_RUN = "first_run";
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private SearchView searchView;

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
        //showcaseGuide();

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new MovieFragment(), getString(R.string.title_movie));
        viewPagerAdapter.addFragment(new TVShowFragment(), getString(R.string.title_tv_show));
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            searchView = (SearchView) (menu.findItem(R.id.action_search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setQueryHint(getString(R.string.txt_hint_movie));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.action_favorite:
                startActivity(new Intent(this, FavoriteActivity.class));
                return true;
            case R.id.action_settings:
                return true;
            default:
                return true;
        }
    }

    private void searchItem() {

    }

    /*private void showcaseGuide() {
        BubbleShowCaseBuilder builder = new BubbleShowCaseBuilder(this)
                .title(getString(R.string.title_favorite))
                .description(getString(R.string.txt_showcase_one))
                .backgroundColor(Color.WHITE)
                .textColor(Color.BLACK)
                .closeActionImage(ContextCompat.getDrawable(this, R.drawable.ic_close_black_24dp))
                .targetView(mFavorite)
                .showOnce(EXTRA_FIRST_RUN);
        builder.show();
    }*/

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            finish();
        }
    }

    /*@OnClick({R.id.settings, R.id.favorite})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.settings:
                break;
            case R.id.favorite:

                break;
        }
    }*/
}
