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
import com.centaury.mcatalogue.data.db.entity.TVShowEntity;
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
 * Created by Centaury on 7/28/2019.
 */
public class FavoriteTVShowAdapter extends RecyclerView.Adapter<FavoriteTVShowAdapter.viewHolder> {

    private Context context;
    private List<TVShowEntity> tvShowEntityList = new ArrayList<>();
    private OnDeleteItemClickCallback onDeleteItemClickCallback;

    public interface OnDeleteItemClickCallback {
        void onDeleteClicked(int tvshowId);
    }

    public void setOnDeleteItemClickCallback(OnDeleteItemClickCallback onDeleteItemClickCallback) {
        this.onDeleteItemClickCallback = onDeleteItemClickCallback;
    }

    public FavoriteTVShowAdapter(Context context) {
        this.context = context;
    }

    public void setTVShows(List<TVShowEntity> tvShows) {
        this.tvShowEntityList.clear();
        this.tvShowEntityList.addAll(tvShows);
        notifyDataSetChanged();
    }

    public ArrayList<TVShowEntity> getListTVShows() {
        return (ArrayList<TVShowEntity>) tvShowEntityList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_favorite_movielist, viewGroup, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {

        if (tvShowEntityList != null) {
            TVShowEntity tvShow = tvShowEntityList.get(i);
            viewHolder.bind(tvShow);

            viewHolder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailTVShowActivity.class);
                intent.putExtra(DetailTVShowActivity.EXTRA_FAV_TVSHOW, tvShowEntityList.get(viewHolder.getAdapterPosition()));
                context.startActivity(intent);
            });
            viewHolder.mBtnDelete.setOnClickListener(v ->
                    onDeleteItemClickCallback.onDeleteClicked(tvShowEntityList.get(viewHolder.getAdapterPosition()).getId()));
        }
    }

    @Override
    public int getItemCount() {
        if (tvShowEntityList != null) {
            return tvShowEntityList.size();
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

        public void bind(TVShowEntity tvShow) {
            mTxtTitlemovielist.setText(tvShow.getName());
            mTxtTitlebackground.setText(tvShow.getOriginalName());
            if (tvShow.getGenreIds() == null || tvShow.getGenreIds().equals("")) {
                mTxtGenremovielist.setText(context.getResources().getString(R.string.txt_no_genre));
            } else {
                mTxtGenremovielist.setText(tvShow.getGenreIds());
            }
            Glide.with(context).load(AppConstants.IMAGE_URL + tvShow.getPosterPath()).placeholder(R.drawable.noimage).into(mIvMovielist);

            if (tvShow.getOverview() == null || tvShow.getOverview().equals("")) {
                mTxtDescmovielist.setText(context.getString(R.string.txt_nodesc));
            } else {
                mTxtDescmovielist.setText(tvShow.getOverview());
            }

            DateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            try {
                Date date = inputDate.parse(tvShow.getFirstAirDate());
                String releaseDate = outputDate.format(date);
                mTxtDatemovielist.setText(releaseDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
