package com.centaury.mcatalogue.ui.detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.db.entity.MovieEntity;
import com.centaury.mcatalogue.data.model.genre.GenresItem;
import com.centaury.mcatalogue.data.model.movie.MovieResultsItem;
import com.centaury.mcatalogue.ui.detail.viewmodel.DetailViewModel;
import com.centaury.mcatalogue.ui.favorite.viewmodel.FavoriteMovieViewModel;
import com.centaury.mcatalogue.utils.AppConstants;
import com.centaury.mcatalogue.utils.Helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailMovieActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_FAV_MOVIE = "extra_favmovie";
    @BindView(R.id.iv_coverdetail)
    ImageView mIvCoverdetail;
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.iv_imgdetail)
    ImageView mIvImgdetail;
    @BindView(R.id.txt_titledetail)
    TextView mTxtTitledetail;
    @BindView(R.id.txt_genredetail)
    TextView mTxtGenredetail;
    @BindView(R.id.rb_ratingdetail)
    RatingBar mRbRatingdetail;
    @BindView(R.id.txt_ratemovie)
    TextView mTxtRatemovie;
    @BindView(R.id.txt_datedetail)
    TextView mTxtDatedetail;
    @BindView(R.id.txt_descdetail)
    TextView mTxtDescdetail;
    @BindView(R.id.btn_favorite)
    LottieAnimationView mBtnFavorite;

    private DateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat outputDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    private List<Integer> genreData = new ArrayList<>();
    private MovieResultsItem movie;
    private MovieEntity entity;
    private DetailViewModel detailViewModel;
    private FavoriteMovieViewModel favoriteMovieViewModel;
    private AlertDialog alertDialog;
    private String language;
    private Boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = getWindow().getDecorView();
            view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        language = String.valueOf(Locale.getDefault().toLanguageTag());
        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        favoriteMovieViewModel = ViewModelProviders.of(this).get(FavoriteMovieViewModel.class);
        detailViewModel.getGenresDetail().observe(this, getGenre);

        checkConnection(this);

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        if (movie != null) {
            itemMovie(movie);
            stateFavoriteDB(movie.getId());
        } else {
            int entityId = getIntent().getIntExtra(EXTRA_FAV_MOVIE, 0);
            try {
                entity = favoriteMovieViewModel.getMovie(entityId);
                itemMovieDB(entity);
                stateFavoriteDB(entity.getId());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        setFavorite();
        mBtnFavorite.setScale(2.481f);

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
        Glide.with(this).load(AppConstants.IMAGE_URL + movie.getPosterPath()).placeholder(R.drawable.noimage).into(mIvImgdetail);
        if (movie.getBackdropPath() != null) {
            Glide.with(this).load(AppConstants.IMAGE_URL + movie.getBackdropPath()).placeholder(R.drawable.noimage).into(mIvCoverdetail);
        } else {
            Glide.with(this).load(AppConstants.IMAGE_URL + movie.getPosterPath()).placeholder(R.drawable.noimage).into(mIvCoverdetail);
        }

        if (movie.getOverview() == null || movie.getOverview().equals("")) {
            mTxtDescdetail.setText(getResources().getString(R.string.txt_nodesc));
        } else {
            mTxtDescdetail.setText(movie.getOverview());
        }

        try {
            Date date = inputDate.parse(movie.getReleaseDate());
            String releaseDate = outputDate.format(date);
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
        Glide.with(this).load(AppConstants.IMAGE_URL + entity.getPosterPath()).placeholder(R.drawable.noimage).into(mIvImgdetail);
        if (entity.getBackdropPath() != null) {
            Glide.with(this).load(AppConstants.IMAGE_URL + entity.getBackdropPath()).placeholder(R.drawable.noimage).into(mIvCoverdetail);
        } else {
            Glide.with(this).load(AppConstants.IMAGE_URL + entity.getPosterPath()).placeholder(R.drawable.noimage).into(mIvCoverdetail);
        }

        if (entity.getOverview() == null || entity.getOverview().equals("")) {
            mTxtDescdetail.setText(getResources().getString(R.string.txt_nodesc));
        } else {
            mTxtDescdetail.setText(entity.getOverview());
        }

        try {
            Date date = inputDate.parse(entity.getReleaseDate());
            String releaseDate = outputDate.format(date);
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

    private Observer<List<GenresItem>> getGenre = genresItems -> {
        if (genresItems != null) {
            showDialogLoading();
            getGenresString(genresItems);
        }
    };

    public void checkConnection(Context context) {
        if (Helper.isNetworkConnected(context)) {
            detailViewModel.setGenreMovieDetail(language);
            detailViewModel.setGenreTVShowDetail(language);
        } else {
            showNoInternet();
        }
    }

    private void getGenresString(List<GenresItem> itemList) {
        List<String> genreMovies = new ArrayList<>();
        try {
            if (genreData.size() == 1) {
                for (Integer genreId : genreData) {
                    for (GenresItem genresItem : itemList) {
                        if (genresItem.getId() == genreId) {
                            genreMovies.add(genresItem.getName());
                        }
                    }
                    mTxtGenredetail.setText(TextUtils.join(", ", genreMovies));
                }
            } else {
                List<Integer> integers = genreData.subList(0, 2);
                for (Integer genreId : integers) {
                    for (GenresItem genresItem : itemList) {
                        if (genresItem.getId() == genreId) {
                            genreMovies.add(genresItem.getName());
                        }
                    }
                    mTxtGenredetail.setText(TextUtils.join(", ", genreMovies));
                }
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            System.out.println("Exception thrown : " + e);
        }
        alertDialog.dismiss();
    }

    private void showDialogLoading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setView(R.layout.item_loading_dialog);
        alertDialog = builder.create();
        alertDialog.show();
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
            movieEntity = favoriteMovieViewModel.getMovie(id);
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
            favoriteMovieViewModel.insertMovie(movieEntity);
        } else {
            MovieEntity movieEntity = new MovieEntity(entity.getId(), mTxtTitledetail.getText().toString(), entity.getOriginalTitle(),
                    mTxtDescdetail.getText().toString(), entity.getPosterPath(), entity.getBackdropPath(),
                    mTxtRatemovie.getText().toString(), entity.getReleaseDate(), mTxtGenredetail.getText().toString());
            favoriteMovieViewModel.insertMovie(movieEntity);
        }

        Helper.updateWidget(this);

        Toast.makeText(this, getString(R.string.txt_add_movie), Toast.LENGTH_SHORT).show();
    }

    private void removeFavorite(int id) {
        final MovieEntity movieEntity;
        try {
            movieEntity = favoriteMovieViewModel.getMovie(id);
            favoriteMovieViewModel.deleteMovie(movieEntity);

            Helper.updateWidget(this);
            Toast.makeText(this, getString(R.string.txt_remove_movie), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_back, R.id.btn_favorite})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_favorite:
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
                break;
        }
    }
}
