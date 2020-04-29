package com.centaury.mcatalogue.ui.main.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.centaury.mcatalogue.data.remote.model.movie.MovieResultsItem;
import com.centaury.mcatalogue.ui.detail.DetailMovieActivity;
import com.centaury.mcatalogue.utils.AppConstants;
import com.centaury.mcatalogue.utils.Helper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Centaury on 7/5/2019.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.viewHolder> {

    private Context context;
    private List<MovieResultsItem> movieResultsList = new ArrayList<>();
    private List<GenresItem> genresItemsList = new ArrayList<>();

    public MovieAdapter(Context context) {
        this.context = context;
    }

    public void setMovieData(List<MovieResultsItem> movieData) {
        movieResultsList.clear();
        movieResultsList.addAll(movieData);
        notifyDataSetChanged();
    }

    public void setGenreData(List<GenresItem> genreData) {
        genresItemsList.clear();
        genresItemsList.addAll(genreData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie_list, viewGroup, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {

        MovieResultsItem movie = movieResultsList.get(i);
        viewHolder.bind(movie);
        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailMovieActivity.class);
            intent.putExtra(AppConstants.EXTRA_MOVIE, movieResultsList.get(viewHolder.getAdapterPosition()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieResultsList.size();
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

        void bind(MovieResultsItem movie) {
            mTxtTitlemovielist.setText(movie.getTitle());
            mTxtTitlebackground.setText(movie.getOriginalTitle());
            if (movie.getGenreIds().size() == 0) {
                mTxtGenremovielist.setText(context.getString(R.string.txt_no_genre));
            } else {
                mTxtGenremovielist.setText(Helper.getGenresString(genresItemsList, movie.getGenreIds()));
            }
            Glide.with(context)
                    .load(BuildConfig.IMAGE_URL + movie.getPosterPath())
                    .placeholder(R.drawable.noimage)
                    .into(mIvMovielist);

            if (movie.getOverview() == null || movie.getOverview().equals("")) {
                mTxtDescmovielist.setText(context.getString(R.string.txt_no_desc));
            } else {
                mTxtDescmovielist.setText(movie.getOverview());
            }

            try {
                Date date = Helper.inputDate().parse(movie.getReleaseDate());
                String releaseDate = Helper.outputDate().format(date);
                mTxtDatemovielist.setText(releaseDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
