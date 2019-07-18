package com.centaury.mcatalogue.ui.detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.centaury.mcatalogue.data.model.TVShow;
import com.centaury.mcatalogue.data.model.genre.GenresItem;
import com.centaury.mcatalogue.data.model.movie.MovieResultsItem;
import com.centaury.mcatalogue.data.model.tvshow.TVShowResultsItem;
import com.centaury.mcatalogue.ui.main.adapter.MovieAdapter;
import com.centaury.mcatalogue.ui.main.viewmodel.MovieViewModel;
import com.centaury.mcatalogue.utils.AppConstants;

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

    private List<GenresItem> genresItemList = new ArrayList<>();
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

        DetailViewModel detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        detailViewModel.getGenresDetail().observe(this, getGenre);

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
        mTxtGenredetail.setText(getGenresData(movie.getGenreIds()));
        mTxtRatemovie.setText(String.valueOf(movie.getVoteAverage()));
        Glide.with(this).load(AppConstants.IMAGE_URL + movie.getPosterPath()).into(mIvImgdetail);
        Glide.with(this).load(AppConstants.IMAGE_URL + movie.getBackdropPath()).into(mIvCoverdetail);

        if (movie.getOverview() != null) {
            mTxtDescdetail.setText(movie.getOverview());
        } else {
            mTxtDescdetail.setText(getResources().getString(R.string.txt_nodesc));
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
        mTxtGenredetail.setText(getGenresData(tvShow.getGenreIds()));
        mTxtRatemovie.setText(String.valueOf(tvShow.getVoteAverage()));
        Glide.with(this).load(AppConstants.IMAGE_URL + tvShow.getPosterPath()).into(mIvImgdetail);
        Glide.with(this).load(AppConstants.IMAGE_URL + tvShow.getBackdropPath()).into(mIvCoverdetail);

        if (tvShow.getOverview() != null) {
            mTxtDescdetail.setText(tvShow.getOverview());
        } else {
            mTxtDescdetail.setText(getResources().getString(R.string.txt_nodesc));
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
                setGenreData(genresItems);
            }
        }
    };

    private void setGenreData(List<GenresItem> genreData) {
        genresItemList.clear();
        genresItemList.addAll(genreData);
    }

    private String getGenresData(List<Integer> genreList) {
        List<String> genreMovies = new ArrayList<>();
        try {
            if (genreList.size() > 2) {
                List<Integer> integers = genreList.subList(0, 2);
                for (Integer genreId : integers) {
                    for (GenresItem genresItem : genresItemList) {
                        if (genresItem.getId() == genreId) {
                            genreMovies.add(genresItem.getName());
                        }
                    }
                }
            } else {
                for (Integer genreId : genreList) {
                    for (GenresItem genresItem : genresItemList) {
                        if (genresItem.getId() == genreId) {
                            genreMovies.add(genresItem.getName());
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Exception thrown : " + e);
        }

        catch (IllegalArgumentException e) {
            System.out.println("Exception thrown : " + e);
        }
        return TextUtils.join(", ", genreMovies);
    }

    @OnClick(R.id.btn_back)
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }
}
