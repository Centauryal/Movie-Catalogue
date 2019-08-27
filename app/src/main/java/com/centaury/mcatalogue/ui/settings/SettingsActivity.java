package com.centaury.mcatalogue.ui.settings;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.centaury.mcatalogue.R;
import com.centaury.mcatalogue.data.prefs.ReminderPreference;
import com.centaury.mcatalogue.services.reminder.DailyReminder;
import com.centaury.mcatalogue.services.reminder.ReleaseReminder;

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
    @BindView(R.id.ln_release)
    LinearLayout mLnRelease;
    @BindView(R.id.ln_daily)
    LinearLayout mLnDaily;

    private ReminderPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
        }

        mSettingsLanguage.setText(Locale.getDefault().getDisplayLanguage());

        preference = new ReminderPreference(this);
        DailyReminder dailyReminder = new DailyReminder();
        String timeDaily = "07:00";
        mSwDailyReminder.setChecked(preference.getDailyReminder());
        mSwDailyReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                preference.setDailyReminder(true);
                preference.setTimeDaily(timeDaily);
                dailyReminder.setDailyReminder(this, timeDaily);
            } else {
                preference.setDailyReminder(false);
                dailyReminder.cancelDailyReminder(this);
            }
        });

        ReleaseReminder releaseReminder = new ReleaseReminder();
        String timeRelease = "08:00";
        mSwReleaseReminder.setChecked(preference.getReleaseReminder());
        mSwReleaseReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                preference.setReleaseReminder(true);
                preference.setTimeRelease(timeRelease);
                releaseReminder.setReleaseReminder(this, timeRelease);
            } else {
                preference.setReleaseReminder(false);
                releaseReminder.cancelReleaseReminder(this);
            }
        });

    }

    @OnClick({R.id.btn_back, R.id.setting_change_language, R.id.ln_release, R.id.ln_daily})
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
            case R.id.ln_release:
                if (mSwReleaseReminder.isChecked()) {
                    mSwReleaseReminder.setChecked(false);
                } else {
                    mSwReleaseReminder.setChecked(true);
                }
                break;
            case R.id.ln_daily:
                if (mSwDailyReminder.isChecked()) {
                    mSwDailyReminder.setChecked(false);
                } else {
                    mSwDailyReminder.setChecked(true);
                }
                break;
        }
    }
}
