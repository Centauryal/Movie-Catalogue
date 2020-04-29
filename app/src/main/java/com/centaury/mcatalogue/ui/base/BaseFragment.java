package com.centaury.mcatalogue.ui.base;

import android.view.View;

import androidx.fragment.app.Fragment;

/**
 * Created by Centaury on 4/27/2020.
 */
public abstract class BaseFragment extends Fragment {

    protected void toggleEmptyState(int size, View emptyState, View recyclerView) {
        if (size > 0) {
            emptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        }
    }
}
