package com.centaury.mcatalogue.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.centaury.mcatalogue.ui.widget.FavoriteWidget;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Centaury on 6/29/2019.
 */
public class Helper {

    public static boolean isEmpty(EditText ed) {
        String temp = ed.getText().toString().replaceAll("\\s", "");
        return TextUtils.isEmpty(temp);
    }

    public static String toRupiah(long value) {
        Locale locale = new Locale("in", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        return numberFormat.format(value).replace("Rp", "Rp. ");
    }

    public static String toRupiahNoSymbol(long value) {
        Locale locale = new Locale("in", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        return numberFormat.format(value).replace("Rp", "");
    }

    public static class LeftItemDecotaion extends RecyclerView.ItemDecoration {
        private int spacing;

        public LeftItemDecotaion(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            if (position == 0) {
                outRect.left = spacing;
            }
        }
    }

    public static class TopItemDecoration extends RecyclerView.ItemDecoration {
        private int spacing;

        public TopItemDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            if (position == 0) {
                outRect.top = spacing;
            }
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void updateWidget(Context context) {
        Intent intent = new Intent(context, FavoriteWidget.class);
        intent.setAction(FavoriteWidget.UPDATE_WIDGET);
        context.sendBroadcast(intent);
    }
}
