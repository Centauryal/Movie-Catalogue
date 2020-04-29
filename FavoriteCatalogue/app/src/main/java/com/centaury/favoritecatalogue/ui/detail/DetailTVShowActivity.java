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
import com.centaury.favoritecatalogue.data.entity.TVShowEntity;
import com.centaury.favoritecatalogue.ui.base.BaseActivity;
import com.centaury.favoritecatalogue.ui.tvshow.TVShowFragment;
import com.centaury.favoritecatalogue.utils.Helper;

import java.text.ParseException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.centaury.favoritecatalogue.data.DatabaseContract.TVShowColumns.BACKDROP_PATH;
import static com.centaury.favoritecatalogue.data.DatabaseContract.TVShowColumns.CONTENT_URI;
import static com.centaury.favoritecatalogue.data.DatabaseContract.TVShowColumns.GENRE;
import static com.centaury.favoritecatalogue.data.DatabaseContract.TVShowColumns.ID;
import static com.centaury.favoritecatalogue.data.DatabaseContract.TVShowColumns.ORIGINAL_TITLE;
import static com.centaury.favoritecatalogue.data.DatabaseContract.TVShowColumns.OVERVIEW;
import static com.centaury.favoritecatalogue.data.DatabaseContract.TVShowColumns.POSTER_PATH;
import static com.centaury.favoritecatalogue.data.DatabaseContract.TVShowColumns.RELEASE_DATE;
import static com.centaury.favoritecatalogue.data.DatabaseContract.TVShowColumns.TITLE;
import static com.centaury.favoritecatalogue.data.DatabaseContract.TVShowColumns.VOTE_AVERAGE;

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

    private TVShowEntity entity = null;
    private Boolean isFavorite = false;
    private ContentResolver contentResolver;
    private Uri uri;

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

        contentResolver = getContentResolver();

        uri = getIntent().getData();
        if (uri != null) {
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    entity = new TVShowEntity(cursor);
                    itemTVShowDB(entity);
                    stateFavoriteDB(entity.getId());
                    cursor.close();
                }
            }
        }

        setFavorite();
        mBtnFavorite.setScale(2.481f);
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

    private void addFavorite(TVShowEntity entity) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, entity.getId());
        contentValues.put(TITLE, entity.getName());
        contentValues.put(ORIGINAL_TITLE, entity.getOriginalName());
        contentValues.put(OVERVIEW, entity.getOverview());
        contentValues.put(POSTER_PATH, entity.getPosterPath());
        contentValues.put(BACKDROP_PATH, entity.getBackdropPath());
        contentValues.put(VOTE_AVERAGE, entity.getVoteAverage());
        contentValues.put(RELEASE_DATE, entity.getFirstAirDate());
        contentValues.put(GENRE, entity.getGenreIds());

        contentResolver.insert(CONTENT_URI, contentValues);
        contentResolver.notifyChange(CONTENT_URI, new TVShowFragment.DataObserverTVShow(new Handler(), DetailTVShowActivity.this));

        Toast.makeText(this, getString(R.string.txt_movie_add), Toast.LENGTH_SHORT).show();
    }

    private void removeFavorite(Uri uri) {
        contentResolver.delete(uri, null, null);
        contentResolver.notifyChange(CONTENT_URI, new TVShowFragment.DataObserverTVShow(new Handler(), DetailTVShowActivity.this));
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
