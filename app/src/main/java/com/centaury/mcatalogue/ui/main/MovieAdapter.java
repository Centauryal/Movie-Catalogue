package com.centaury.mcatalogue.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.model.Movie;

import java.util.ArrayList;

/**
 * Created by Centaury on 6/29/2019.
 */
public class MovieAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Movie> movieArrayList;

    public void setMovieArrayList(ArrayList<Movie> movieArrayList) {
        this.movieArrayList = movieArrayList;
    }

    public MovieAdapter(Context context) {
        this.context = context;
        movieArrayList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return movieArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_movielist, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(convertView);
        Movie movie = (Movie) getItem(position);
        viewHolder.bind(movie);
        return convertView;
    }

    private class ViewHolder {
        private TextView txtName;
        private TextView txtNamebg;
        private TextView txtDescription;
        private TextView txtDate;
        private ImageView imgPhoto;

        ViewHolder(View view) {
            txtName = view.findViewById(R.id.txt_titlemovielist);
            txtNamebg = view.findViewById(R.id.txt_titlebackground);
            txtDescription = view.findViewById(R.id.txt_descmovielist);
            txtDate = view.findViewById(R.id.txt_datemovielist);
            imgPhoto = view.findViewById(R.id.iv_movielist);
        }

        void bind(Movie movie) {
            txtName.setText(movie.getName());
            txtNamebg.setText(movie.getName());
            txtDescription.setText(movie.getDesc());
            txtDate.setText(movie.getDate());
            imgPhoto.setImageResource(movie.getPhoto());
        }
    }
}
