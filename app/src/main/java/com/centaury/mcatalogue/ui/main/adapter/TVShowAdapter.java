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
import com.centaury.mcatalogue.data.model.TVShow;
import com.centaury.mcatalogue.ui.detail.DetailMovieActivity;

import java.util.ArrayList;

/**
 * Created by Centaury on 7/5/2019.
 */
public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.viewHolder> {

    private Context context;
    private ArrayList<TVShow> tvShowArrayList;

    public void setTvShowArrayList(ArrayList<TVShow> tvShowArrayList) {
        this.tvShowArrayList = tvShowArrayList;
    }

    public TVShowAdapter(Context context) {
        this.context = context;
        tvShowArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movielist, viewGroup, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {

        TVShow tvShow = tvShowArrayList.get(i);
        viewHolder.bind(tvShow);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailMovieActivity.class);
                intent.putExtra(DetailMovieActivity.EXTRA_TVSHOW, tvShowArrayList.get(viewHolder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tvShowArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private TextView txtNamebg;
        private TextView txtDescription;
        private TextView txtDate;
        private ImageView imgPhoto;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txt_titlemovielist);
            txtNamebg = itemView.findViewById(R.id.txt_titlebackground);
            txtDescription = itemView.findViewById(R.id.txt_descmovielist);
            txtDate = itemView.findViewById(R.id.txt_datemovielist);
            imgPhoto = itemView.findViewById(R.id.iv_movielist);
        }

        public void bind(TVShow tvShow) {
            txtName.setText(tvShow.getName());
            txtNamebg.setText(tvShow.getName());
            txtDescription.setText(tvShow.getDesc());
            txtDate.setText(tvShow.getDate());
            imgPhoto.setImageResource(tvShow.getPhoto());
        }
    }
}
