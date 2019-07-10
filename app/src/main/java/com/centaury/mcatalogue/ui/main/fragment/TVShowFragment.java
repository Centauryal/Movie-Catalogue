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
import com.centaury.mcatalogue.data.model.TVShow;
import com.centaury.mcatalogue.ui.main.adapter.TVShowAdapter;
import com.centaury.mcatalogue.utils.Helper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TVShowFragment extends Fragment {

    private RecyclerView rvTVShows;

    private String[] tvshowName;
    private String[] tvshowDesc;
    private String[] tvshowDate;
    private TypedArray tvshowPhoto;

    private ArrayList<TVShow> tvShowArrayList = new ArrayList<>();


    public TVShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tvshow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvTVShows = view.findViewById(R.id.rv_tvshow);

        prepare();
        addItem();

    }

    private void addItem() {

        for (int i = 0; i < tvshowName.length; i++) {
            TVShow tvShow = new TVShow();
            tvShow.setName(tvshowName[i]);
            tvShow.setDesc(tvshowDesc[i]);
            tvShow.setDate(tvshowDate[i]);
            tvShow.setPhoto(tvshowPhoto.getResourceId(i, -1));
            tvShowArrayList.add(tvShow);
        }

        showRecyclerList();
    }

    private void prepare() {
        tvshowName = getResources().getStringArray(R.array.tvshow_name);
        tvshowDesc = getResources().getStringArray(R.array.tvshow_desc);
        tvshowDate = getResources().getStringArray(R.array.tvshow_date);
        tvshowPhoto = getResources().obtainTypedArray(R.array.tvshow_photo);
    }

    private void showRecyclerList() {
        TVShowAdapter tvShowAdapter = new TVShowAdapter(getContext());
        tvShowAdapter.setTvShowArrayList(tvShowArrayList);
        rvTVShows.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTVShows.setAdapter(tvShowAdapter);
        rvTVShows.setItemAnimator(new DefaultItemAnimator());
        rvTVShows.addItemDecoration(new Helper.TopItemDecoration(55));
    }
}
