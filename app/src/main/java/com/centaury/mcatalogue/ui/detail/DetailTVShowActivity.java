package com.centaury.mcatalogue.ui.detail;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.centaury.mcatalogue.BuildConfig;
import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.ViewModelFactory;
import com.centaury.mcatalogue.data.local.db.entity.TVShowEntity;
import com.centaury.mcatalogue.data.remote.model.genre.GenresItem;
import com.centaury.mcatalogue.data.remote.model.tvshow.TVShowResultsItem;
import com.centaury.mcatalogue.ui.base.BaseActivity;
import com.centaury.mcatalogue.ui.detail.viewmodel.DetailViewModel;
import com.centaury.mcatalogue.utils.AppConstants;
import com.centaury.mcatalogue.utils.Helper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailTVShowActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_cover_tv_detail)
    ImageView mIvCovertvdetail;
    @BindView(R.id.btn_favorite)
    LottieAnimationView mBtnFavorite;
    @BindView(R.id.iv_img_tv_detail)
    ImageView mIvImgtvdetail;
    @BindView(R.id.txt_title_tv_detail)
    TextView mTxtTitletvdetail;
    @BindView(R.id.txt_genre_tv_detail)
    TextView mTxtGenretvdetail;
    @BindView(R.id.rb_rating_tv_detail)
    RatingBar mRbRatingtvdetail;
    @BindView(R.id.txt_rate_tv_movie)
    TextView mTxtRatetvmovie;
    @BindView(R.id.txt_date_tv_detail)
    TextView mTxtDatetvdetail;
    @BindView(R.id.txt_desc_tv_detail)
    TextView mTxtDesctvdetail;

    private List<Integer> genreData = new ArrayList<>();
    private TVShowResultsItem tvShow;
    private TVShowEntity entity;
    private DetailViewModel detailViewModel;
    private String language;
    private Boolean isFavorite = false;
    private Observer<List<GenresItem>> getGenre = genresItems -> {
        if (genresItems != null) {
            showDialogLoading();
            Helper.getGenresString(genresItems, genreData, mTxtGenretvdetail);
            dismissDialogLoading();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tvshow);
        ButterKnife.bind(this);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
        setUpToolbar(mToolbar);

        language = String.valueOf(Locale.getDefault().toLanguageTag());

        ViewModelFactory factory = ViewModelFactory.getInstance(this);
        detailViewModel = new ViewModelProvider(this, factory).get(DetailViewModel.class);
        detailViewModel.getGenresTVShow(language).observe(this, getGenre);

        checkConnection(this);
        setUp();
        setFavorite();
        mBtnFavorite.setScale(2.481f);
    }

    private void setUp() {
        tvShow = getIntent().getParcelableExtra(AppConstants.EXTRA_TVSHOW);
        if (tvShow != null) {
            itemTVShow(tvShow);
            stateFavoriteDB(tvShow.getId());
        } else {
            entity = getIntent().getParcelableExtra(AppConstants.EXTRA_FAV_TVSHOW);
            itemTVShowDB(entity);
            stateFavoriteDB(entity.getId());
        }
    }

    private void itemTVShow(TVShowResultsItem tvShow) {
        mTxtTitletvdetail.setText(tvShow.getName());
        if (tvShow.getGenreIds().size() == 0) {
            mTxtGenretvdetail.setText(getResources().getString(R.string.txt_no_genre));
        } else {
            genreData.clear();
            genreData.addAll(tvShow.getGenreIds());
        }
        mTxtRatetvmovie.setText(String.valueOf(tvShow.getVoteAverage()));
        float movieRating = (float) (tvShow.getVoteAverage() / 2);
        mRbRatingtvdetail.setRating(movieRating);
        Glide.with(this)
                .load(BuildConfig.IMAGE_URL + tvShow.getPosterPath())
                .placeholder(R.drawable.noimage)
                .into(mIvImgtvdetail);
        if (tvShow.getBackdropPath() != null) {
            Glide.with(this)
                    .load(BuildConfig.IMAGE_URL + tvShow.getBackdropPath())
                    .placeholder(R.drawable.noimage)
                    .into(mIvCovertvdetail);
        } else {
            Glide.with(this)
                    .load(BuildConfig.IMAGE_URL + tvShow.getPosterPath())
                    .placeholder(R.drawable.noimage)
                    .into(mIvCovertvdetail);
        }

        if (tvShow.getOverview() == null || tvShow.getOverview().equals("")) {
            mTxtDesctvdetail.setText(getResources().getString(R.string.txt_no_desc));
        } else {
            mTxtDesctvdetail.setText(tvShow.getOverview());
        }

        try {
            Date date = Helper.inputDate().parse(tvShow.getFirstAirDate());
            String releaseDate = Helper.outputDate().format(date);
            mTxtDatetvdetail.setText(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void itemTVShowDB(TVShowEntity entity) {
        mTxtTitletvdetail.setText(entity.getName());
        if (entity.getGenreIds() == null || entity.getGenreIds().equals("")) {
            mTxtGenretvdetail.setText(getResources().getString(R.string.txt_no_genre));
        } else {
            mTxtGenretvdetail.setText(entity.getGenreIds());
        }
        mTxtRatetvmovie.setText(entity.getVoteAverage());
        int rate = (int) Double.parseDouble(entity.getVoteAverage());
        float movieRating = (float) (rate / 2);
        mRbRatingtvdetail.setRating(movieRating);
        Glide.with(this)
                .load(BuildConfig.IMAGE_URL + entity.getPosterPath())
                .placeholder(R.drawable.noimage)
                .into(mIvImgtvdetail);
        if (entity.getBackdropPath() != null) {
            Glide.with(this)
                    .load(BuildConfig.IMAGE_URL + entity.getBackdropPath())
                    .placeholder(R.drawable.noimage)
                    .into(mIvCovertvdetail);
        } else {
            Glide.with(this)
                    .load(BuildConfig.IMAGE_URL + entity.getPosterPath())
                    .placeholder(R.drawable.noimage)
                    .into(mIvCovertvdetail);
        }

        if (entity.getOverview() == null || entity.getOverview().equals("")) {
            mTxtDesctvdetail.setText(getResources().getString(R.string.txt_no_desc));
        } else {
            mTxtDesctvdetail.setText(entity.getOverview());
        }

        try {
            Date date = Helper.inputDate().parse(entity.getFirstAirDate());
            String releaseDate = Helper.outputDate().format(date);
            mTxtDatetvdetail.setText(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkConnection(Context context) {
        if (Helper.isNetworkConnected(context)) {
            detailViewModel.getGenresTVShow(language).observe(this, getGenre);
        } else {
            showNoInternet();
        }
    }

    private void showNoInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.item_alert_dialog);
        builder.setPositiveButton(getString(R.string.btn_ok), (dialog, which) -> {
            dialog.dismiss();
            checkConnection(this);
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void stateFavoriteDB(int id) {
        final TVShowEntity tvShowEntity;
        try {
            tvShowEntity = detailViewModel.getFavoriteTVShow(id);
            if (tvShowEntity != null) {
                isFavorite = true;
            }
            setFavorite();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setFavorite() {
        if (isFavorite) {
            mBtnFavorite.setSpeed(1f);
            mBtnFavorite.setProgress(1f);
        } else {
            mBtnFavorite.setProgress(0f);
            mBtnFavorite.setSpeed(-2f);
        }
    }

    private void addFavorite() {
        if (tvShow != null) {
            TVShowEntity tvShowEntity = new TVShowEntity(tvShow.getId(), mTxtTitletvdetail.getText().toString(), tvShow.getOriginalName(),
                    mTxtDesctvdetail.getText().toString(), tvShow.getPosterPath(), tvShow.getBackdropPath(),
                    mTxtRatetvmovie.getText().toString(), tvShow.getFirstAirDate(), mTxtGenretvdetail.getText().toString());
            detailViewModel.insertFavoriteTVShow(tvShowEntity);
        } else {
            TVShowEntity tvShowEntity = new TVShowEntity(entity.getId(), mTxtTitletvdetail.getText().toString(), entity.getOriginalName(),
                    mTxtDesctvdetail.getText().toString(), entity.getPosterPath(), entity.getBackdropPath(),
                    mTxtRatetvmovie.getText().toString(), entity.getFirstAirDate(), mTxtGenretvdetail.getText().toString());
            detailViewModel.insertFavoriteTVShow(tvShowEntity);
        }

        Toast.makeText(this, getString(R.string.txt_movie_add), Toast.LENGTH_SHORT).show();
    }

    private void removeFavorite(int id) {
        final TVShowEntity tvShowEntity;
        try {
            tvShowEntity = detailViewModel.getFavoriteTVShow(id);
            detailViewModel.deleteFavoriteTVShow(tvShowEntity);
            Toast.makeText(this, getString(R.string.txt_movie_remove), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_favorite)
    public void onClick(View v) {
        if (v.getId() == R.id.btn_favorite) {
            if (isFavorite) {
                if (tvShow != null) {
                    removeFavorite(tvShow.getId());
                } else {
                    removeFavorite(entity.getId());
                }
            } else {
                addFavorite();
            }

            isFavorite = !isFavorite;

            setFavorite();
            mBtnFavorite.playAnimation();
        }
    }
}
