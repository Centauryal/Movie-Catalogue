package com.centaury.mcatalogue.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.centaury.mcatalogue.data.remote.model.genre.GenresItem;
import com.centaury.mcatalogue.ui.widget.FavoriteWidget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Centaury on 6/29/2019.
 */
public class Helper {

    public static DateFormat inputDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    public static DateFormat outputDate() {
        return new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void updateWidget(Context context) {
        Intent intent = new Intent(context, FavoriteWidget.class);
        intent.setAction(AppConstants.WIDGET_UPDATE_WIDGET);
        context.sendBroadcast(intent);
    }

    public static void getGenresString(List<GenresItem> itemList, List<Integer> genreData, TextView txtGenre) {
        List<String> genreMovies = new ArrayList<>();
        try {
            if (genreData.size() == 1) {
                for (Integer genreId : genreData) {
                    for (GenresItem genresItem : itemList) {
                        if (genresItem.getId() == genreId) {
                            genreMovies.add(genresItem.getName());
                        }
                    }
                    txtGenre.setText(TextUtils.join(", ", genreMovies));
                }
            } else {
                List<Integer> integers = genreData.subList(0, 2);
                for (Integer genreId : integers) {
                    for (GenresItem genresItem : itemList) {
                        if (genresItem.getId() == genreId) {
                            genreMovies.add(genresItem.getName());
                        }
                    }
                    txtGenre.setText(TextUtils.join(", ", genreMovies));
                }
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static String getGenresString(List<GenresItem> itemList, List<Integer> genreList) {
        List<String> genreMovies = new ArrayList<>();
        try {
            if (genreList.size() == 1) {
                for (Integer genreId : genreList) {
                    for (GenresItem genresItem : itemList) {
                        if (genresItem.getId() == genreId) {
                            genreMovies.add(genresItem.getName());
                        }
                    }
                }
            } else {
                List<Integer> integers = genreList.subList(0, 2);
                for (Integer genreId : integers) {
                    for (GenresItem genresItem : itemList) {
                        if (genresItem.getId() == genreId) {
                            genreMovies.add(genresItem.getName());
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return TextUtils.join(", ", genreMovies);
    }

    public static class TopItemDecoration extends RecyclerView.ItemDecoration {
        private int spacing;

        public TopItemDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            if (position == 0) {
                outRect.top = spacing;
            }
        }
    }
}
