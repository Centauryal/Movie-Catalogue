package com.centaury.favoritecatalogue.ui.detail;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.centaury.favoritecatalogue.R;
import com.centaury.favoritecatalogue.data.entity.TVShowEntity;
import com.centaury.favoritecatalogue.ui.tvshow.TVShowFragment;
import com.centaury.favoritecatalogue.utils.AppConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

public class DetailTVShowActivity extends AppCompatActivity {

    @BindView(R.id.iv_covertvdetail)
    ImageView mIvCovertvdetail;
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.btn_favorite)
    LottieAnimationView mBtnFavorite;
    @BindView(R.id.iv_imgtvdetail)
    ImageView mIvImgtvdetail;
    @BindView(R.id.txt_titletvdetail)
    TextView mTxtTitletvdetail;
    @BindView(R.id.txt_genretvdetail)
    TextView mTxtGenretvdetail;
    @BindView(R.id.rb_ratingtvdetail)
    RatingBar mRbRatingtvdetail;
    @BindView(R.id.txt_ratetvmovie)
    TextView mTxtRatetvmovie;
    @BindView(R.id.txt_datetvdetail)
    TextView mTxtDatetvdetail;
    @BindView(R.id.txt_desctvdetail)
    TextView mTxtDesctvdetail;

    private DateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat outputDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = getWindow().getDecorView();
            view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

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
        Glide.with(this).load(AppConstants.IMAGE_URL + entity.getPosterPath()).placeholder(R.drawable.noimage).into(mIvImgtvdetail);
        if (entity.getBackdropPath() != null) {
            Glide.with(this).load(AppConstants.IMAGE_URL + entity.getBackdropPath()).placeholder(R.drawable.noimage).into(mIvCovertvdetail);
        } else {
            Glide.with(this).load(AppConstants.IMAGE_URL + entity.getPosterPath()).placeholder(R.drawable.noimage).into(mIvCovertvdetail);
        }

        if (entity.getOverview() == null || entity.getOverview().equals("")) {
            mTxtDesctvdetail.setText(getResources().getString(R.string.txt_nodesc));
        } else {
            mTxtDesctvdetail.setText(entity.getOverview());
        }

        try {
            Date date = inputDate.parse(entity.getFirstAirDate());
            String releaseDate = outputDate.format(date);
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

        Toast.makeText(this, getString(R.string.txt_add_movie), Toast.LENGTH_SHORT).show();
    }

    private void removeFavorite(Uri uri) {
        contentResolver.delete(uri, null, null);
        contentResolver.notifyChange(CONTENT_URI, new TVShowFragment.DataObserverTVShow(new Handler(), DetailTVShowActivity.this));
        Toast.makeText(this, getString(R.string.txt_remove_movie), Toast.LENGTH_SHORT).show();
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
                    removeFavorite(uri);
                } else {
                    addFavorite(entity);
                }

                isFavorite = !isFavorite;

                setFavorite();
                mBtnFavorite.playAnimation();
                break;
        }
    }
}
