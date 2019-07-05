package com.centaury.mcatalogue.ui.main;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.model.Movie;
import com.centaury.mcatalogue.ui.detail.DetailMovieActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] movieName;
    private String[] movieDesc;
    private String[] movieDate;
    private TypedArray moviePhoto;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = getWindow().getDecorView();
            view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            Window window = getWindow();
            window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
        }

        movieAdapter = new MovieAdapter(this);
        ListView listView = findViewById(R.id.rv_movie);
        listView.setAdapter(movieAdapter);

        prepare();
        addItem();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailMovieActivity.class);
                intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movieArrayList.get(position));
                startActivity(intent);
            }
        });
    }

    private void addItem() {
        movieArrayList = new ArrayList<>();

        for (int i = 0; i < movieName.length; i++) {
            Movie movie = new Movie();
            movie.setName(movieName[i]);
            movie.setDesc(movieDesc[i]);
            movie.setDate(movieDate[i]);
            movie.setPhoto(moviePhoto.getResourceId(i, -1));
            movieArrayList.add(movie);
        }
        movieAdapter.setMovieArrayList(movieArrayList);
    }

    private void prepare() {
        movieName = getResources().getStringArray(R.array.movie_name);
        movieDesc = getResources().getStringArray(R.array.movie_desc);
        movieDate = getResources().getStringArray(R.array.movie_date);
        moviePhoto = getResources().obtainTypedArray(R.array.movie_photo);
    }
}
