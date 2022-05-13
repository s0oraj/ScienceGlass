package blog.cosmos.home.scienceglass.navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import blog.cosmos.home.scienceglass.R;

/**
 * Settings activity of the app where user selects his/her preferences for the app.
 * **/

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set action bars title to "settings"
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");
    }

    // preference fragment which will show postcount value selected by the user
    public static class PostPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.settings_main);
            Preference minMagnitude = findPreference(getString(R.string.settings_postcount_key));
            bindPreferenceSummaryToValue(minMagnitude);

        }

        /**This method is triggered if the preference of postcount changes
         * then it updates the ui to show new postcount value.
         **/
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            preference.setSummary(stringValue);
            return true;
        }

        /**
         * Binds value of postcountin the preference fragment when the
         * fragment is created i.e at the start of SettingsActivity
         **/
        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), getString(R.string.settings_postcount_default));
            onPreferenceChange(preference, preferenceString);
        }


    }



    /** Works in conjunction with  this code written in onCreate method
     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     Calls onBackPressed() method when user presses backbutton
     This redirects user back to the mainActivity**/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()== android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}