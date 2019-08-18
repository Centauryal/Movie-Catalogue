package com.centaury.mcatalogue.ui.favorite.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.db.entity.MovieEntity;
import com.centaury.mcatalogue.ui.detail.DetailMovieActivity;
import com.centaury.mcatalogue.utils.AppConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Centaury on 7/28/2019.
 */
public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.viewHolder> {

    private Context context;
    private List<MovieEntity> movieEntityList;
    private OnDeleteItemClickCallback onDeleteItemClickCallback;

    public interface OnDeleteItemClickCallback {
        void onDeleteClicked(int movieId);
    }

    public void setOnDeleteItemClickCallback(OnDeleteItemClickCallback onDeleteItemClickCallback) {
        this.onDeleteItemClickCallback = onDeleteItemClickCallback;
    }

    public FavoriteMovieAdapter(Context context) {
        this.context = context;
    }

    public void setMovies(List<MovieEntity> movies) {
        movieEntityList = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_favorite_movielist, viewGroup, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {

        if (movieEntityList != null) {
            MovieEntity entity = movieEntityList.get(i);
            viewHolder.bind(entity);

            viewHolder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailMovieActivity.class);
                intent.putExtra(DetailMovieActivity.EXTRA_FAV_MOVIE, movieEntityList.get(viewHolder.getAdapterPosition()).getId());
                context.startActivity(intent);
            });
            viewHolder.mBtnDelete.setOnClickListener(v ->
                    onDeleteItemClickCallback.onDeleteClicked(movieEntityList.get(viewHolder.getAdapterPosition()).getId()));
        }
    }

    @Override
    public int getItemCount() {
        if (movieEntityList != null) {
            return movieEntityList.size();
        } else {
            return 0;
        }
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_titlefavbackground)
        TextView mTxtTitlebackground;
        @BindView(R.id.iv_moviefavlist)
        ImageView mIvMovielist;
        @BindView(R.id.txt_genremoviefavlist)
        TextView mTxtGenremovielist;
        @BindView(R.id.txt_titlemoviefavlist)
        TextView mTxtTitlemovielist;
        @BindView(R.id.txt_descmoviefavlist)
        TextView mTxtDescmovielist;
        @BindView(R.id.txt_datemoviefavlist)
        TextView mTxtDatemovielist;
        @BindView(R.id.btn_delete)
        ImageView mBtnDelete;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(MovieEntity movie) {
            mTxtTitlemovielist.setText(movie.getTitle());
            mTxtTitlebackground.setText(movie.getOriginalTitle());
            if (movie.getGenreIds() == null || movie.getGenreIds().equals("")) {
                mTxtGenremovielist.setText(context.getResources().getString(R.string.txt_no_genre));
            } else {
                mTxtGenremovielist.setText(movie.getGenreIds());
            }
            Glide.with(context).load(AppConstants.IMAGE_URL + movie.getPosterPath()).placeholder(R.drawable.noimage).into(mIvMovielist);

            if (movie.getOverview() == null || movie.getOverview().equals("")) {
                mTxtDescmovielist.setText(context.getString(R.string.txt_nodesc));
            } else {
                mTxtDescmovielist.setText(movie.getOverview());
            }

            DateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            try {
                Date date = inputDate.parse(movie.getReleaseDate());
                String releaseDate = outputDate.format(date);
                mTxtDatemovielist.setText(releaseDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    }
}
