package com.centaury.favoritecatalogue.ui.detail;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.centaury.favoritecatalogue.BuildConfig;
import com.centaury.favoritecatalogue.R;
import com.centaury.favoritecatalogue.data.entity.MovieEntity;
import com.centaury.favoritecatalogue.ui.base.BaseActivity;
import com.centaury.favoritecatalogue.ui.movie.MovieFragment;
import com.centaury.favoritecatalogue.utils.Helper;

import java.text.ParseException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.centaury.favoritecatalogue.data.DatabaseContract.MovieColumns.BACKDROP_PATH;
import static com.centaury.favoritecatalogue.data.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.centaury.favoritecatalogue.data.DatabaseContract.MovieColumns.GENRE;
import static com.centaury.favoritecatalogue.data.DatabaseContract.MovieColumns.ID;
import static com.centaury.favoritecatalogue.data.DatabaseContract.MovieColumns.ORIGINAL_TITLE;
import static com.centaury.favoritecatalogue.data.DatabaseContract.MovieColumns.OVERVIEW;
import static com.centaury.favoritecatalogue.data.DatabaseContract.MovieColumns.POSTER_PATH;
import static com.centaury.favoritecatalogue.data.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.centaury.favoritecatalogue.data.DatabaseContract.MovieColumns.TITLE;
import static com.centaury.favoritecatalogue.data.DatabaseContract.MovieColumns.VOTE_AVERAGE;

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

    private MovieEntity entity = null;
    private Boolean isFavorite = false;
    private ContentResolver contentResolver;
    private Uri uri;

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

        contentResolver = getContentResolver();

        uri = getIntent().getData();
        if (uri != null) {
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    entity = new MovieEntity(cursor);
                    itemMovieDB(entity);
                    stateFavoriteDB(entity.getId());
                    cursor.close();
                }
            }
        }

        setFavorite();
        mBtnFavorite.setScale(2.481f);
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

    private void stateFavoriteDB(int id) {
        if (id != 0) {
            isFavorite = true;
        }
        setFavorite();
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

    private void addFavorite(MovieEntity entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, entity.getId());
        contentValues.put(TITLE, entity.getTitle());
        contentValues.put(ORIGINAL_TITLE, entity.getOriginalTitle());
        contentValues.put(OVERVIEW, entity.getOverview());
        contentValues.put(POSTER_PATH, entity.getPosterPath());
        contentValues.put(BACKDROP_PATH, entity.getBackdropPath());
        contentValues.put(VOTE_AVERAGE, entity.getVoteAverage());
        contentValues.put(RELEASE_DATE, entity.getReleaseDate());
        contentValues.put(GENRE, entity.getGenreIds());

        contentResolver.insert(CONTENT_URI, contentValues);
        contentResolver.notifyChange(CONTENT_URI, new MovieFragment.DataObserver(new Handler(), DetailMovieActivity.this));

        Toast.makeText(this, getString(R.string.txt_movie_add), Toast.LENGTH_SHORT).show();
    }

    private void removeFavorite(Uri uri) {
        contentResolver.delete(uri, null, null);
        contentResolver.notifyChange(CONTENT_URI, new MovieFragment.DataObserver(new Handler(), DetailMovieActivity.this));
        Toast.makeText(this, getString(R.string.txt_movie_remove), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_favorite)
    public void onClick(View v) {
        if (v.getId() == R.id.btn_favorite) {
            if (isFavorite) {
                removeFavorite(uri);
            } else {
                addFavorite(entity);
            }

            isFavorite = !isFavorite;

            setFavorite();
            mBtnFavorite.playAnimation();
        }
    }
}
