package com.example.udacity_android_developer_nanodegree_project2.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.example.udacity_android_developer_nanodegree_project2.R;

/**
 * The type Settings activity.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionbar = getSupportActionBar();

        // Enable and display Up button
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Return to Movies Activity on Up button clicked
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * The type Movie preferences fragment.
     */
    public static class MoviePreferencesFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Add preferences custom xml
            addPreferencesFromResource(R.xml.settings_main);

            // Get Order by preference and bind it with OnPreferenceChangeListener
            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);

        }

        //Method to set OnPreferenceChangeListener
        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), getString(R.string.settings_order_by_most_popular));
            if (preferenceString == null) {
                preferenceString = getString(R.string.settings_order_by_most_popular);
            }
            onPreferenceChange(preference, preferenceString);
        }


        // Method called when a preference value has changed, then update its preference
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            preference.setSummary(stringValue);
            return true;
        }
    }


}
