package com.centaury.mcatalogue.ui.base;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.centaury.mcatalogue.R;

import java.util.Objects;

/**
 * Created by Centaury on 4/27/2020.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private AlertDialog alertDialog;

    protected void setUpToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable arrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        if (arrow != null) {
            arrow.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
        getSupportActionBar().setHomeAsUpIndicator(arrow);
    }

    protected void toggleEmptyState(int size, View emptyState, View recyclerView) {
        if (size > 0) {
            emptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        }
    }

    protected void showDialogLoading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setView(R.layout.item_loading_dialog);
        alertDialog = builder.create();
        alertDialog.show();
    }

    protected void dismissDialogLoading() {
        alertDialog.dismiss();
    }
}
