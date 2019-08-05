package com.centaury.mcatalogue.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.centaury.mcatalogue.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.setting_change_language)
    LinearLayout mSettingChangeLanguage;
    @BindView(R.id.sw_release_reminder)
    Switch mSwReleaseReminder;
    @BindView(R.id.sw_daily_reminder)
    Switch mSwDailyReminder;
    @BindView(R.id.settings_language)
    TextView mSettingsLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        mSettingsLanguage.setText(Locale.getDefault().getDisplayCountry());
    }

    @OnClick({R.id.btn_back, R.id.setting_change_language, R.id.sw_release_reminder, R.id.sw_daily_reminder})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.setting_change_language:
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
                break;
            case R.id.sw_release_reminder:
                break;
            case R.id.sw_daily_reminder:
                break;
        }
    }
}
