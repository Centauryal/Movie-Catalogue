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
import com.centaury.mcatalogue.data.local.db.entity.MovieEntity;
import com.centaury.mcatalogue.data.remote.model.genre.GenresItem;
import com.centaury.mcatalogue.data.remote.model.movie.MovieResultsItem;
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

public class DetailMovieActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_cover_detail)
    ImageView mIvCoverdetail;
    @BindView(R.id.iv_img_detail)
    ImageView mIvImgdetail;
    @BindView(R.id.txt_title_detail)
    TextView mTxtTitledetail;
    @BindView(R.id.txt_genre_detail)
    TextView mTxtGenredetail;
    @BindView(R.id.rb_rating_detail)
    RatingBar mRbRatingdetail;
    @BindView(R.id.txt_rate_movie)
    TextView mTxtRatemovie;
    @BindView(R.id.txt_date_detail)
    TextView mTxtDatedetail;
    @BindView(R.id.txt_desc_detail)
    TextView mTxtDescdetail;
    @BindView(R.id.btn_favorite)
    LottieAnimationView mBtnFavorite;

    private List<Integer> genreData = new ArrayList<>();
    private MovieResultsItem movie;
    private MovieEntity entity;
    private DetailViewModel detailViewModel;
    private String language;
    private Boolean isFavorite = false;
    private Observer<List<GenresItem>> getGenre = genresItems -> {
        if (genresItems != null) {
            showDialogLoading();
            Helper.getGenresString(genresItems, genreData, mTxtGenredetail);
            dismissDialogLoading();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
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
        detailViewModel.getGenresMovie(language).observe(this, getGenre);

        checkConnection(this);
        setUp();
        setFavorite();
        mBtnFavorite.setScale(2.481f);

    }

    private void setUp() {
        movie = getIntent().getParcelableExtra(AppConstants.EXTRA_MOVIE);
        if (movie != null) {
            itemMovie(movie);
            stateFavoriteDB(movie.getId());
        } else {
            int entityId = getIntent().getIntExtra(AppConstants.EXTRA_FAV_MOVIE, 0);
            try {
                entity = detailViewModel.getFavoriteMovie(entityId);
                itemMovieDB(entity);
                stateFavoriteDB(entity.getId());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void itemMovie(MovieResultsItem movie) {
        mTxtTitledetail.setText(movie.getTitle());
        if (movie.getGenreIds().size() == 0) {
            mTxtGenredetail.setText(getResources().getString(R.string.txt_no_genre));
        } else {
            genreData.clear();
            genreData.addAll(movie.getGenreIds());
        }
        mTxtRatemovie.setText(String.valueOf(movie.getVoteAverage()));
        float movieRating = (float) (movie.getVoteAverage() / 2);
        mRbRatingdetail.setRating(movieRating);
        Glide.with(this)
                .load(BuildConfig.IMAGE_URL + movie.getPosterPath())
                .placeholder(R.drawable.noimage)
                .into(mIvImgdetail);
        if (movie.getBackdropPath() != null) {
            Glide.with(this)
                    .load(BuildConfig.IMAGE_URL + movie.getBackdropPath())
                    .placeholder(R.drawable.noimage)
                    .into(mIvCoverdetail);
        } else {
            Glide.with(this)
                    .load(BuildConfig.IMAGE_URL + movie.getPosterPath())
                    .placeholder(R.drawable.noimage)
                    .into(mIvCoverdetail);
        }

        if (movie.getOverview() == null || movie.getOverview().equals("")) {
            mTxtDescdetail.setText(getResources().getString(R.string.txt_no_desc));
        } else {
            mTxtDescdetail.setText(movie.getOverview());
        }

        try {
            Date date = Helper.inputDate().parse(movie.getReleaseDate());
            String releaseDate = Helper.outputDate().format(date);
            mTxtDatedetail.setText(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void itemMovieDB(MovieEntity entity) {
        mTxtTitledetail.setText(entity.getTitle());
        if (entity.getGenreIds() == null || entity.getGenreIds().equals("")) {
            mTxtGenredetail.setText(getResources().getString(R.string.txt_no_genre));
        } else {
            mTxtGenredetail.setText(entity.getGenreIds());
        }
        mTxtRatemovie.setText(entity.getVoteAverage());
        int rate = (int) Double.parseDouble(entity.getVoteAverage());
        float movieRating = (float) (rate / 2);
        mRbRatingdetail.setRating(movieRating);
        Glide.with(this)
                .load(BuildConfig.IMAGE_URL + entity.getPosterPath())
                .placeholder(R.drawable.noimage)
                .into(mIvImgdetail);
        if (entity.getBackdropPath() != null) {
            Glide.with(this)
                    .load(BuildConfig.IMAGE_URL + entity.getBackdropPath())
                    .placeholder(R.drawable.noimage)
                    .into(mIvCoverdetail);
        } else {
            Glide.with(this)
                    .load(BuildConfig.IMAGE_URL + entity.getPosterPath())
                    .placeholder(R.drawable.noimage)
                    .into(mIvCoverdetail);
        }

        if (entity.getOverview() == null || entity.getOverview().equals("")) {
            mTxtDescdetail.setText(getResources().getString(R.string.txt_no_desc));
        } else {
            mTxtDescdetail.setText(entity.getOverview());
        }

        try {
            Date date = Helper.inputDate().parse(entity.getReleaseDate());
            String releaseDate = Helper.outputDate().format(date);
            mTxtDatedetail.setText(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Helper.updateWidget(this);
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
            detailViewModel.getGenresMovie(language).observe(this, getGenre);
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
        final MovieEntity movieEntity;
        try {
            movieEntity = detailViewModel.getFavoriteMovie(id);
            if (movieEntity != null) {
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
        if (movie != null) {
            MovieEntity movieEntity = new MovieEntity(movie.getId(), mTxtTitledetail.getText().toString(), movie.getOriginalTitle(),
                    mTxtDescdetail.getText().toString(), movie.getPosterPath(), movie.getBackdropPath(),
                    mTxtRatemovie.getText().toString(), movie.getReleaseDate(), mTxtGenredetail.getText().toString());
            detailViewModel.insertFavoriteMovie(movieEntity);
        } else {
            MovieEntity movieEntity = new MovieEntity(entity.getId(), mTxtTitledetail.getText().toString(), entity.getOriginalTitle(),
                    mTxtDescdetail.getText().toString(), entity.getPosterPath(), entity.getBackdropPath(),
                    mTxtRatemovie.getText().toString(), entity.getReleaseDate(), mTxtGenredetail.getText().toString());
            detailViewModel.insertFavoriteMovie(movieEntity);
        }

        Helper.updateWidget(this);

        Toast.makeText(this, getString(R.string.txt_movie_add), Toast.LENGTH_SHORT).show();
    }

    private void removeFavorite(int id) {
        final MovieEntity movieEntity;
        try {
            movieEntity = detailViewModel.getFavoriteMovie(id);
            detailViewModel.deleteFavoriteMovie(movieEntity);

            Helper.updateWidget(this);
            Toast.makeText(this, getString(R.string.txt_movie_remove), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_favorite)
    public void onClick(View v) {
        if (v.getId() == R.id.btn_favorite) {
            if (isFavorite) {
                if (movie != null) {
                    removeFavorite(movie.getId());
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
