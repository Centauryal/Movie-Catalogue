package com.centaury.favoritecatalogue.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
