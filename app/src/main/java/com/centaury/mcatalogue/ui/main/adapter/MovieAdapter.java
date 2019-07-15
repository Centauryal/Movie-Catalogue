package com.centaury.mcatalogue.ui.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.model.Movie;
import com.centaury.mcatalogue.ui.detail.DetailMovieActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Centaury on 7/5/2019.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.viewHolder> {

    private Context context;
    private ArrayList<Movie> movieArrayList;

    public void setMovieArrayList(ArrayList<Movie> movieArrayList) {
        this.movieArrayList = movieArrayList;
    }

    public MovieAdapter(Context context) {
        this.context = context;
        movieArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movielist, viewGroup, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {

        Movie movie = movieArrayList.get(i);
        viewHolder.bind(movie);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailMovieActivity.class);
                intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movieArrayList.get(viewHolder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
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

        public void bind(Movie movie) {
            mTxtTitlemovielist.setText(movie.getName());
            mTxtTitlebackground.setText(movie.getName());
            mTxtDescmovielist.setText(movie.getDesc());
            mTxtDatemovielist.setText(movie.getDate());
            mIvMovielist.setImageResource(movie.getPhoto());
        }
    }
}
