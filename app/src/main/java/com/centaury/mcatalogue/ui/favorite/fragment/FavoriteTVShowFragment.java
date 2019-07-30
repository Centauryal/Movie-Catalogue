package com.centaury.mcatalogue.ui.favorite.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.db.entity.TVShowEntity;
import com.centaury.mcatalogue.ui.favorite.adapter.FavoriteTVShowAdapter;
import com.centaury.mcatalogue.ui.favorite.viewmodel.FavoriteTVShowViewModel;
import com.centaury.mcatalogue.utils.Helper;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteTVShowFragment extends Fragment {

    @BindView(R.id.rv_favtvshow)
    RecyclerView mRvFavtvshow;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;
    @BindView(R.id.btn_try_again)
    TextView mBtnTryAgain;
    @BindView(R.id.empty_state)
    LinearLayout mEmptyState;
    private View view;
    private Unbinder unbinder;

    private FavoriteTVShowAdapter favoriteTVShowAdapter;
    private FavoriteTVShowViewModel favoriteTVShowViewModel;

    public FavoriteTVShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_tvshow, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        favoriteTVShowViewModel = ViewModelProviders.of(this).get(FavoriteTVShowViewModel.class);
        favoriteTVShowViewModel.getTvshows().observe(this, getFavTVShow);

        showRecyclerList();
    }

    private Observer<List<TVShowEntity>> getFavTVShow = new Observer<List<TVShowEntity>>() {
        @Override
        public void onChanged(@Nullable List<TVShowEntity> tvShowEntities) {
            if (tvShowEntities != null) {
                favoriteTVShowAdapter.setTVShows(tvShowEntities);
                toggleEmptyTVShows(tvShowEntities.size());
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
            }
        }
    };

    private void toggleEmptyTVShows(int size) {
        if (size > 0) {
            mEmptyState.setVisibility(View.GONE);
        } else {
            mRvFavtvshow.setVisibility(View.GONE);
            mEmptyState.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();
    }

    private void showRecyclerList() {
        favoriteTVShowAdapter = new FavoriteTVShowAdapter(getContext());

        mRvFavtvshow.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvFavtvshow.setAdapter(favoriteTVShowAdapter);
        mRvFavtvshow.setItemAnimator(new DefaultItemAnimator());
        mRvFavtvshow.addItemDecoration(new Helper.TopItemDecoration(55));

        favoriteTVShowAdapter.setOnDeleteItemClickCallback(new FavoriteTVShowAdapter.OnDeleteItemClickCallback() {
            @Override
            public void onDeleteClicked(int tvshowId) {
                showDialogDeleteFavorite(tvshowId);
            }
        });
    }

    private void showDialogDeleteFavorite(int tvshowId) {
        TVShowEntity tvShowEntity;
        try {
            tvShowEntity = favoriteTVShowViewModel.getTVShow(tvshowId);

            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.item_alert_dialog, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(view);

            TextView title = view.findViewById(R.id.alerttitle);
            title.setText(getString(R.string.txt_title_delete_dialog));

            builder.setCancelable(false)
                    .setPositiveButton(getString(R.string.btn_delete), (dialog, which) -> {
                        favoriteTVShowViewModel.deleteMovie(tvShowEntity);
                        dialog.dismiss();
                        Toast.makeText(getContext(), getString(R.string.txt_remove_movie), Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(getString(R.string.btn_cancel), (dialog, which) -> {
                        dialog.dismiss();
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } catch (ExecutionException e) {
            // TODO - handle error
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO - handle error
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_try_again)
    public void onClick(View v) {
        if (v.getId() == R.id.btn_try_again) {
            mShimmerViewContainer.startShimmer();
            favoriteTVShowViewModel.getTvshows().observe(this, getFavTVShow);
        }
    }
}
