/*
 * Copyright © 2018-2019 Syberia Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.syberia.settings.fragments;

import android.os.Bundle;
import android.content.Context;
import android.content.res.Resources;
import android.provider.SearchIndexableResource;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import android.content.ContentResolver;


import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;


import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.syberia.settings.preference.CustomSeekBarPreference;
import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.logging.nano.MetricsProto;

import android.content.ContentResolver;


import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;


import java.util.ArrayList;
import java.util.List;

public class LockScreenSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener, Indexable {


    private static final String FOD_ICON_PICKER_CATEGORY = "fod_icon_picker";
    private PreferenceCategory mFODIconPickerCategory;
    private static final String LOCK_CLOCK_FONTS = "lock_clock_fonts";
    private static final String CUSTOM_TEXT_CLOCK_FONTS = "custom_text_clock_fonts";
    private static final String LOCK_DATE_FONTS = "lock_date_fonts";
    private static final String CLOCK_FONT_SIZE  = "lockclock_font_size";
    private static final String CUSTOM_TEXT_CLOCK_FONT_SIZE  = "custom_text_clock_font_size";
    private static final String DATE_FONT_SIZE  = "lockdate_font_size";
    private static final String LOCK_OWNERINFO_FONTS = "lock_ownerinfo_fonts";
    private static final String LOCKOWNER_FONT_SIZE = "lockowner_font_size";

    private ListPreference mLockClockFonts;
    private ListPreference mTextClockFonts;
    private ListPreference mLockDateFonts;
    private ListPreference mLockOwnerInfoFonts;
    private CustomSeekBarPreference mClockFontSize;
    private CustomSeekBarPreference mCustomTextClockFontSize;
    private CustomSeekBarPreference mDateFontSize;
    private CustomSeekBarPreference mOwnerInfoFontSize;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.lockscreen_settings);
        ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();
        Resources resources = getResources();

        // Lockscren Clock Fonts
        mLockClockFonts = (ListPreference) findPreference(LOCK_CLOCK_FONTS);
        mLockClockFonts.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.LOCK_CLOCK_FONTS, 34)));
        mLockClockFonts.setSummary(mLockClockFonts.getEntry());
        mLockClockFonts.setOnPreferenceChangeListener(this);

        // Lockscren Text Clock Fonts
        mTextClockFonts = (ListPreference) findPreference(CUSTOM_TEXT_CLOCK_FONTS);
        mTextClockFonts.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.CUSTOM_TEXT_CLOCK_FONTS, 32)));
        mTextClockFonts.setSummary(mTextClockFonts.getEntry());
        mTextClockFonts.setOnPreferenceChangeListener(this);

        // Lockscren Date Fonts
        mLockDateFonts = (ListPreference) findPreference(LOCK_DATE_FONTS);
        mLockDateFonts.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.LOCK_DATE_FONTS, 32)));
        mLockDateFonts.setSummary(mLockDateFonts.getEntry());
        mLockDateFonts.setOnPreferenceChangeListener(this);

        mFODIconPickerCategory = (PreferenceCategory) findPreference(FOD_ICON_PICKER_CATEGORY);
        if (mFODIconPickerCategory != null
                && !getResources().getBoolean(com.android.internal.R.bool.config_needCustomFODView)) {
            prefScreen.removePreference(mFODIconPickerCategory);
        }

        // Lock Clock Size
        mClockFontSize = (CustomSeekBarPreference) findPreference(CLOCK_FONT_SIZE);
        mClockFontSize.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKCLOCK_FONT_SIZE, 78));
        mClockFontSize.setOnPreferenceChangeListener(this);

        // Custom Text Clock Size
        mCustomTextClockFontSize = (CustomSeekBarPreference) findPreference(CUSTOM_TEXT_CLOCK_FONT_SIZE);
        mCustomTextClockFontSize.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.CUSTOM_TEXT_CLOCK_FONT_SIZE, 40));
        mCustomTextClockFontSize.setOnPreferenceChangeListener(this);

        // Lock Date Size
        mDateFontSize = (CustomSeekBarPreference) findPreference(DATE_FONT_SIZE);
        mDateFontSize.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKDATE_FONT_SIZE, 18));
        mDateFontSize.setOnPreferenceChangeListener(this);

        // Lockscren OwnerInfo Fonts
        mLockOwnerInfoFonts = (ListPreference) findPreference(LOCK_OWNERINFO_FONTS);
        mLockOwnerInfoFonts.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.LOCK_OWNERINFO_FONTS, 0)));
        mLockOwnerInfoFonts.setSummary(mLockOwnerInfoFonts.getEntry());
        mLockOwnerInfoFonts.setOnPreferenceChangeListener(this);

        // Lockscren OwnerInfo Size
        mOwnerInfoFontSize = (CustomSeekBarPreference) findPreference(LOCKOWNER_FONT_SIZE);
        mOwnerInfoFontSize.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKOWNER_FONT_SIZE,21));
        mOwnerInfoFontSize.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();

        if (preference == mLockClockFonts) {
            Settings.System.putInt(getContentResolver(), Settings.System.LOCK_CLOCK_FONTS,
                    Integer.valueOf((String) newValue));
            mLockClockFonts.setValue(String.valueOf(newValue));
            mLockClockFonts.setSummary(mLockClockFonts.getEntry());
            return true;
        } else if (preference == mTextClockFonts) {
            Settings.System.putInt(getContentResolver(), Settings.System.CUSTOM_TEXT_CLOCK_FONTS,
                    Integer.valueOf((String) newValue));
            mTextClockFonts.setValue(String.valueOf(newValue));
            mTextClockFonts.setSummary(mTextClockFonts.getEntry());
            return true;
        } else if (preference == mLockDateFonts) {
            Settings.System.putInt(getContentResolver(), Settings.System.LOCK_DATE_FONTS,
                    Integer.valueOf((String) newValue));
            mLockDateFonts.setValue(String.valueOf(newValue));
            mLockDateFonts.setSummary(mLockDateFonts.getEntry());
            return true;
        } else if (preference == mClockFontSize) {
            int top = (Integer) newValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKCLOCK_FONT_SIZE, top*1);
            return true;
        } else if (preference == mCustomTextClockFontSize) {
            int top = (Integer) newValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.CUSTOM_TEXT_CLOCK_FONT_SIZE, top*1);
            return true;
        } else if (preference == mDateFontSize) {
            int top = (Integer) newValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKDATE_FONT_SIZE, top*1);
            return true;
       } else if (preference == mLockOwnerInfoFonts) {
            Settings.System.putInt(getContentResolver(), Settings.System.LOCK_OWNERINFO_FONTS,
                    Integer.valueOf((String) newValue));
            mLockOwnerInfoFonts.setValue(String.valueOf(newValue));
            mLockOwnerInfoFonts.setSummary(mLockOwnerInfoFonts.getEntry());
            return true;
        } else if (preference == mOwnerInfoFontSize) {
            int top = (Integer) newValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKOWNER_FONT_SIZE, top*1);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.SYBERIA;
    }

    /**
     * For Search.
     */
    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {

                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    final ArrayList<SearchIndexableResource> result = new ArrayList<>();

                    final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.lockscreen_settings;
                    result.add(sir);
                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    final List<String> keys = super.getNonIndexableKeys(context);
                    return keys;
                }
    };
}
