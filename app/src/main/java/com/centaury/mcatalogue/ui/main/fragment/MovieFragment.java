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
import android.widget.TextView;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.model.Movie;
import com.centaury.mcatalogue.ui.main.adapter.MovieAdapter;
import com.centaury.mcatalogue.utils.Helper;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    @BindView(R.id.rv_movie)
    RecyclerView mRvMovie;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;
    @BindView(R.id.btn_try_again)
    TextView mBtnTryAgain;
    private View view;
    private Unbinder unbinder;

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
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        mRvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvMovie.setAdapter(movieAdapter);
        mRvMovie.setItemAnimator(new DefaultItemAnimator());
        mRvMovie.addItemDecoration(new Helper.TopItemDecoration(55));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
