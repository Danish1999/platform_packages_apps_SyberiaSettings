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

import android.provider.SearchIndexableResource;

import android.os.Bundle;
import android.content.Context;
import com.android.settings.R;
import com.syberia.settings.preference.CustomSeekBarPreference;
import com.syberia.settings.preference.SystemSettingSwitchPreference;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;

import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.logging.nano.MetricsProto;

import java.util.ArrayList;
import java.util.List;

public class StatusBarSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener, Indexable {

    private static final String SMS_BREATH = "sms_breath";
    private static final String MISSED_CALL_BREATH = "missed_call_breath";
    private static final String VOICEMAIL_BREATH = "voicemail_breath";

    private SwitchPreference mSmsBreath;
    private SwitchPreference mMissedCallBreath;
    private SwitchPreference mVoicemailBreath;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.statusbar_settings);

        PreferenceScreen prefSet = getPreferenceScreen();

        ContentResolver resolver = getActivity().getContentResolver();

            // Breathing Notifications
            mSmsBreath = (SwitchPreference) findPreference(SMS_BREATH);
            mMissedCallBreath = (SwitchPreference) findPreference(MISSED_CALL_BREATH);
            mVoicemailBreath = (SwitchPreference) findPreference(VOICEMAIL_BREATH);

            ConnectivityManager cm = (ConnectivityManager)
                    getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cm.isNetworkSupported(ConnectivityManager.TYPE_MOBILE)) {
                mSmsBreath.setChecked(Settings.Global.getInt(resolver,
                        Settings.Global.KEY_SMS_BREATH, 0) == 1);
                mSmsBreath.setOnPreferenceChangeListener(this);

                mMissedCallBreath.setChecked(Settings.Global.getInt(resolver,
                        Settings.Global.KEY_MISSED_CALL_BREATH, 0) == 1);
                mMissedCallBreath.setOnPreferenceChangeListener(this);

                mVoicemailBreath.setChecked(Settings.System.getInt(resolver,
                        Settings.System.KEY_VOICEMAIL_BREATH, 0) == 1);
                mVoicemailBreath.setOnPreferenceChangeListener(this);
            } else {
                prefSet.removePreference(mSmsBreath);
                prefSet.removePreference(mMissedCallBreath);
                prefSet.removePreference(mVoicemailBreath);
            }

    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
	    ContentResolver resolver = getActivity().getContentResolver();

            if (preference == mSmsBreath) {
                boolean value = (Boolean) objValue;
                Settings.Global.putInt(getContentResolver(), SMS_BREATH, value ? 1 : 0);
                return true;
            } else if (preference == mMissedCallBreath) {
                boolean value = (Boolean) objValue;
                Settings.Global.putInt(getContentResolver(), MISSED_CALL_BREATH, value ? 1 : 0);
                return true;
            } else if (preference == mVoicemailBreath) {
                boolean value = (Boolean) objValue;
                Settings.System.putInt(getContentResolver(), VOICEMAIL_BREATH, value ? 1 : 0);
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
                    sir.xmlResId = R.xml.statusbar_settings;
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
