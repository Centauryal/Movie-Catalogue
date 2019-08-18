package com.centaury.mcatalogue.ui.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Centaury on 8/12/2019.
 */
public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }
}
