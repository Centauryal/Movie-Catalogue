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
import com.centaury.mcatalogue.data.model.TVShow;
import com.centaury.mcatalogue.ui.main.adapter.TVShowAdapter;
import com.centaury.mcatalogue.utils.Helper;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TVShowFragment extends Fragment {

    @BindView(R.id.rv_tvshow)
    RecyclerView mRvTvshow;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;
    @BindView(R.id.btn_try_again)
    TextView mBtnTryAgain;
    private View view;
    private Unbinder unbinder;

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
        View view = inflater.inflate(R.layout.fragment_tvshow, container, false);

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
        mRvTvshow.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvTvshow.setAdapter(tvShowAdapter);
        mRvTvshow.setItemAnimator(new DefaultItemAnimator());
        mRvTvshow.addItemDecoration(new Helper.TopItemDecoration(55));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
