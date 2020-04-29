package com.centaury.favoritecatalogue.ui.base;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.centaury.favoritecatalogue.R;

import java.util.Objects;

/**
 * Created by Centaury on 4/28/2020.
 */
public abstract class BaseActivity extends AppCompatActivity {

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
}
