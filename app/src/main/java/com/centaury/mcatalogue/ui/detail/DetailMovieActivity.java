package com.centaury.mcatalogue.ui.detail;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.model.Movie;
import com.centaury.mcatalogue.data.model.TVShow;

public class DetailMovieActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_TVSHOW = "extra_tvshow";
    private TextView detailName;
    private TextView detailDesc;
    private TextView detailDate;
    private ImageView detailPhoto;
    private ImageView detailCover;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = getWindow().getDecorView();
            view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        detailName = findViewById(R.id.txt_titledetail);
        detailDesc = findViewById(R.id.txt_descdetail);
        detailDate = findViewById(R.id.txt_datedetail);
        detailPhoto = findViewById(R.id.iv_imgdetail);
        detailCover = findViewById(R.id.iv_coverdetail);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        if (movie != null) {
            detailName.setText(movie.getName());
            detailDesc.setText(movie.getDesc());
            detailDate.setText(movie.getDate());
            Glide.with(this).load(movie.getPhoto()).into(detailPhoto);
            Glide.with(this).load(movie.getPhoto()).into(detailCover);
        } else {
            TVShow tvShow = getIntent().getParcelableExtra(EXTRA_TVSHOW);
            detailName.setText(tvShow.getName());
            detailDesc.setText(tvShow.getDesc());
            detailDate.setText(tvShow.getDate());
            Glide.with(this).load(tvShow.getPhoto()).into(detailPhoto);
            Glide.with(this).load(tvShow.getPhoto()).into(detailCover);
        }

    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}
