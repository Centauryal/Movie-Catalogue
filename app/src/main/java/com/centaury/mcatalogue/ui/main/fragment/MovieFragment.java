package com.centaury.mcatalogue.ui.main.fragment;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.model.Movie;
import com.centaury.mcatalogue.ui.main.adapter.MovieAdapter;
import com.centaury.mcatalogue.utils.Helper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    private RecyclerView rvMovies;

    private String[] movieName;
    private String[] movieDesc;
    private String[] movieDate;
    private TypedArray moviePhoto;

    private ArrayList<Movie> movieArrayList = new ArrayList<>();

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMovies = view.findViewById(R.id.rv_movie);

        prepare();
        addItem();

    }

    private void addItem() {

        for (int i = 0; i < movieName.length; i++) {
            Movie movie = new Movie();
            movie.setName(movieName[i]);
            movie.setDesc(movieDesc[i]);
            movie.setDate(movieDate[i]);
            movie.setPhoto(moviePhoto.getResourceId(i, -1));
            movieArrayList.add(movie);
        }

        showRecyclerList();
    }

    private void prepare() {
        movieName = getResources().getStringArray(R.array.movie_name);
        movieDesc = getResources().getStringArray(R.array.movie_desc);
        movieDate = getResources().getStringArray(R.array.movie_date);
        moviePhoto = getResources().obtainTypedArray(R.array.movie_photo);
    }

    private void showRecyclerList() {
        MovieAdapter movieAdapter = new MovieAdapter(getContext());
        movieAdapter.setMovieArrayList(movieArrayList);
        rvMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovies.setAdapter(movieAdapter);
        rvMovies.setItemAnimator(new DefaultItemAnimator());
        rvMovies.addItemDecoration(new Helper.TopItemDecoration(55));
    }

}
