package com.centaury.mcatalogue.ui.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.centaury.mcatalogue.BuildConfig;
import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.remote.model.genre.GenresItem;
import com.centaury.mcatalogue.data.remote.model.tvshow.TVShowResultsItem;
import com.centaury.mcatalogue.ui.detail.DetailTVShowActivity;
import com.centaury.mcatalogue.utils.AppConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Centaury on 7/5/2019.
 */
public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.viewHolder> {

    private Context context;
    private List<TVShowResultsItem> tvShowResultsList = new ArrayList<>();
    private List<GenresItem> genresItemsList = new ArrayList<>();

    public TVShowAdapter(Context context) {
        this.context = context;
    }

    public void setTVShowData(List<TVShowResultsItem> tvShowData) {
        tvShowResultsList.clear();
        tvShowResultsList.addAll(tvShowData);
        notifyDataSetChanged();
    }

    public void setGenreTVShow(List<GenresItem> genreData) {
        genresItemsList.clear();
        genresItemsList.addAll(genreData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movielist, viewGroup, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {

        TVShowResultsItem tvShow = tvShowResultsList.get(i);
        viewHolder.bind(tvShow);
        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailTVShowActivity.class);
            intent.putExtra(AppConstants.EXTRA_TVSHOW, tvShowResultsList.get(viewHolder.getAdapterPosition()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tvShowResultsList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_titlebackground)
        TextView mTxtTitlebackground;
        @BindView(R.id.iv_movielist)
        ImageView mIvMovielist;
        @BindView(R.id.txt_genremovielist)
        TextView mTxtGenremovielist;
        @BindView(R.id.txt_titlemovielist)
        TextView mTxtTitlemovielist;
        @BindView(R.id.txt_descmovielist)
        TextView mTxtDescmovielist;
        @BindView(R.id.txt_datemovielist)
        TextView mTxtDatemovielist;

        viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(TVShowResultsItem tvShow) {
            mTxtTitlemovielist.setText(tvShow.getName());
            mTxtTitlebackground.setText(tvShow.getOriginalName());
            if (tvShow.getGenreIds().size() == 0) {
                mTxtGenremovielist.setText(context.getString(R.string.txt_no_genre));
            } else {
                mTxtGenremovielist.setText(getGenres(tvShow.getGenreIds()));
            }
            Glide.with(context).load(BuildConfig.IMAGE_URL + tvShow.getPosterPath()).placeholder(R.drawable.noimage).into(mIvMovielist);

            if (tvShow.getOverview() == null || tvShow.getOverview().equals("")) {
                mTxtDescmovielist.setText(context.getString(R.string.txt_nodesc));
            } else {
                mTxtDescmovielist.setText(tvShow.getOverview());
            }

            DateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            DateFormat outputDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            try {
                Date date = inputDate.parse(tvShow.getFirstAirDate());
                String releaseDate = outputDate.format(date);
                mTxtDatemovielist.setText(releaseDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        private String getGenres(List<Integer> genreList) {
            List<String> genreMovies = new ArrayList<>();
            try {
                if (genreList.size() == 1) {
                    for (Integer genreId : genreList) {
                        for (GenresItem genresItem : genresItemsList) {
                            if (genresItem.getId() == genreId) {
                                genreMovies.add(genresItem.getName());
                            }
                        }
                    }
                } else {
                    List<Integer> integers = genreList.subList(0, 2);
                    for (Integer genreId : integers) {
                        for (GenresItem genresItem : genresItemsList) {
                            if (genresItem.getId() == genreId) {
                                genreMovies.add(genresItem.getName());
                            }
                        }
                    }
                }
            } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
                System.out.println("Exception thrown : " + e);
            }
            return TextUtils.join(", ", genreMovies);
        }
    }
}
