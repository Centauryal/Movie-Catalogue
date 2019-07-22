package com.centaury.mcatalogue.ui.detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.model.genre.GenresItem;
import com.centaury.mcatalogue.data.model.movie.MovieResultsItem;
import com.centaury.mcatalogue.data.model.tvshow.TVShowResultsItem;
import com.centaury.mcatalogue.utils.AppConstants;
import com.centaury.mcatalogue.utils.Helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailMovieActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_TVSHOW = "extra_tvshow";
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

    private List<Integer> genreData = new ArrayList<>();
    DetailViewModel detailViewModel;
    private AlertDialog alertDialog;
    private String language;
    DateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat outputDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

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
        detailViewModel.getGenresDetail().observe(this, getGenre);

        checkConnection(this);

        MovieResultsItem movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        if (movie != null) {
            itemMovie(movie);
        } else {
            TVShowResultsItem tvShow = getIntent().getParcelableExtra(EXTRA_TVSHOW);
            itemTVShow(tvShow);
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
        Glide.with(this).load(AppConstants.IMAGE_URL + movie.getPosterPath()).into(mIvImgdetail);
        if (movie.getBackdropPath() != null) {
            Glide.with(this).load(AppConstants.IMAGE_URL + movie.getBackdropPath()).into(mIvCoverdetail);
        } else {
            Glide.with(this).load(AppConstants.IMAGE_URL + movie.getPosterPath()).into(mIvCoverdetail);
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

    private void itemTVShow(TVShowResultsItem tvShow) {
        mTxtTitledetail.setText(tvShow.getName());
        if (tvShow.getGenreIds().size() == 0) {
            mTxtGenredetail.setText(getResources().getString(R.string.txt_no_genre));
        } else {
            genreData.clear();
            genreData.addAll(tvShow.getGenreIds());
        }
        mTxtRatemovie.setText(String.valueOf(tvShow.getVoteAverage()));
        float movieRating = (float) (tvShow.getVoteAverage() / 2);
        mRbRatingdetail.setRating(movieRating);
        Glide.with(this).load(AppConstants.IMAGE_URL + tvShow.getPosterPath()).into(mIvImgdetail);
        if (tvShow.getBackdropPath() != null) {
            Glide.with(this).load(AppConstants.IMAGE_URL + tvShow.getBackdropPath()).into(mIvCoverdetail);
        } else {
            Glide.with(this).load(AppConstants.IMAGE_URL + tvShow.getPosterPath()).into(mIvCoverdetail);
        }

        if (tvShow.getOverview() == null || tvShow.getOverview().equals("")) {
            mTxtDescdetail.setText(getResources().getString(R.string.txt_nodesc));
        } else {
            mTxtDescdetail.setText(tvShow.getOverview());
        }

        try {
            Date date = inputDate.parse(tvShow.getFirstAirDate());
            String releaseDate = outputDate.format(date);
            mTxtDatedetail.setText(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Observer<List<GenresItem>> getGenre = new Observer<List<GenresItem>>() {
        @Override
        public void onChanged(@Nullable List<GenresItem> genresItems) {
            if (genresItems != null) {
                showDialogLoading();
                getGenresString(genresItems);
            }
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
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Exception thrown : " + e);
        } catch (IllegalArgumentException e) {
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

    @OnClick(R.id.btn_back)
    public void onClick(View v) {
        if (v.getId() == R.id.btn_back) {
            onBackPressed();
        }
    }
}
