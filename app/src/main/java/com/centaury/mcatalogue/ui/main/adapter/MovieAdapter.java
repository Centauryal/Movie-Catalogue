package com.centaury.mcatalogue.ui.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.model.genre.GenresItem;
import com.centaury.mcatalogue.data.model.movie.MovieResultsItem;
import com.centaury.mcatalogue.utils.AppConstants;

import java.util.ArrayList;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movielist, viewGroup, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {

        MovieResultsItem movie = movieResultsList.get(i);
        viewHolder.bind(movie);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, DetailMovieActivity.class);
                intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movieResultsList.get(viewHolder.getAdapterPosition()));
                context.startActivity(intent);*/
            }
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

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(MovieResultsItem movie) {
            mTxtTitlemovielist.setText(movie.getTitle());
            mTxtTitlebackground.setText(movie.getOriginalTitle());
            mTxtDescmovielist.setText(movie.getOverview());
            mTxtDatemovielist.setText(movie.getReleaseDate());
            mTxtGenremovielist.setText(getGenres(movie.getGenreIds()));
            Glide.with(context).load(AppConstants.IMAGE_URL + movie.getPosterPath()).into(mIvMovielist);
        }

        private String getGenres(List<Integer> genreList) {
            List<String> genreMovies = new ArrayList<>();
            for (Integer genreId : genreList) {
                for (GenresItem genresItem : genresItemsList) {
                    if (genresItem.getId() == genreId) {
                        genreMovies.add(genresItem.getName());
                        return String.valueOf(Math.min(genreMovies.size(), 2));
                    }
                }
            }
            return TextUtils.join(", ", genreMovies);
        }
    }
}
