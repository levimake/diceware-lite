package com.levimake.dicewarelite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ${USER} on ${DATE}.
 */

public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Add 'general' preferences, defined in the XML file
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
        // updated when the preference changes

    }


    public static class MyPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            bindPreferenceSummaryToValue(findPreference("prefNumberOfWords"));
            bindPreferenceSummaryToValue(findPreference("prefSource"));
            bindPreferenceSummaryToValue(findPreference("prefLanguage"));
            bindPreferenceSummaryToValue(findPreference("prefType"));

            Preference creditsPreference = findPreference("prefCredits");
            creditsPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    //open browser or intent here
                    startActivity(new Intent(getActivity(), credits.class));
                    return true;
                }
            });

            ListPreference listPreference = (ListPreference) findPreference("prefType");
            ListPreference listPreference1 = (ListPreference) findPreference("prefLanguage");

            String value;

            if (listPreference.getValue() != null) {

                value = listPreference.getValue().toString();
                if (value.equals("passphrase")) {
                    listPreference1.setEnabled(true);

                } else {
                    listPreference1.setEnabled(false);
                }
            }

        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            // Set the listener to watch for value changes.
            preference.setOnPreferenceChangeListener(this);

            // Trigger the listener immediately with the preference's
            // current value.
            onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }

        public boolean onPreferenceChange(Preference preference, Object value) {

            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list (since they have separate labels/values).
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    preference.setSummary(listPreference.getEntries()[prefIndex]);
                }

            } else {
                // For other preferences, set the summary to the value's simple string representation.
                preference.setSummary(stringValue);
                Log.v("onpreferencechange", stringValue);
            }

            final SwitchPreference switchPreference1=(SwitchPreference) findPreference("prefAssistant");
            final SwitchPreference switchPreference = (SwitchPreference) findPreference("prefSpaces");
            final EditTextPreference editTextPreference = (EditTextPreference) findPreference("prefNumberOfWords");
            final ListPreference listPreference = (ListPreference) findPreference("prefType");
            final ListPreference listPreference1 = (ListPreference) findPreference("prefLanguage");

            listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String value = newValue.toString();
                    Log.v("TYPE VALUE", value);
                    int prefIndex = listPreference.findIndexOfValue(value);
                    if (prefIndex >= 0) {
                        preference.setSummary(listPreference.getEntries()[prefIndex]);
                    }
                    if (value.equals("passphrase")) {
                        listPreference1.setEnabled(true);

                        if(switchPreference1.isChecked()==true) {
                            switchPreference.setChecked(true);
                            if (listPreference1.getValue().toString().equals("eff_short_1") || listPreference1.getValue().toString().equals("eff_short_2")) {
                                editTextPreference.setSummary("" + 8);
                                editTextPreference.setText("" + 8);
                            } else {
                                editTextPreference.setSummary("" + 6);
                                editTextPreference.setText("" + 6);
                            }
                        }

                        return true;
                    } else {
                        if(switchPreference1.isChecked()==true) {
                            if (value.equals("number")) {
                                editTextPreference.setSummary("" + 4);
                                editTextPreference.setText("" + 4);
                            } else {
                                editTextPreference.setText("" + 8);
                                editTextPreference.setSummary("" + 8);
                            }
                            switchPreference.setChecked(false);
                        }
                        listPreference1.setEnabled(false);
                        return true;
                    }
                }
            });

            listPreference1.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String value = newValue.toString();
                    Log.v("TYPE VALUE", value);
                    int prefIndex = listPreference1.findIndexOfValue(value);
                    if (prefIndex >= 0) {
                        preference.setSummary(listPreference1.getEntries()[prefIndex]);
                    }
                    if(switchPreference1.isChecked()==true) {
                        if (value.equals("eff_short_1") || value.equals("eff_short_2")) {
                            editTextPreference.setText("" + 8);
                            editTextPreference.setSummary("" + 8);
                        } else {
                            editTextPreference.setText("" + 6);
                            editTextPreference.setSummary("" + 6);
                        }
                    }
                    return true;
                }
            });

            editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(final Preference preference, Object newValue) {
                    final int[] val = {Integer.parseInt(newValue.toString())};
                    int minVal = 7;
                    if (listPreference.getValue().toString().equals("number")) {
                        minVal = 4;
                    } else if (listPreference.getValue().toString().equals("alphanumeric") || listPreference.getValue().toString().equals("ascii")) {
                        minVal = 8;
                    } else {
                        minVal = 6;
                        if (listPreference1.getValue().toString().equals("eff_short_1") || listPreference1.getValue().toString().equals("eff_short_2")) {
                            minVal = 8;
                        }
                    }
                    if ((val[0] > 1) && (val[0] < 101)) {
                        Log.d("Preference ", "Value saved: " + val[0]);

                        //building alert
                        if (val[0] < minVal) {
                            final int finalMinVal = minVal;
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Low Security Warning")
                                    .setMessage("These settings will result in a password not strong enough to be secure. " +
                                            "Would you like to use the suggested minimum instead?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            val[0] = finalMinVal;
                                            preference.setSummary("" + val[0]);
                                            editTextPreference.setText(""+val[0]);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            preference.setSummary("" + val[0]);
                                            editTextPreference.setText(""+val[0]);
                                        }
                                    })
                                    .show();

                        }

                        else {
                            preference.setSummary(""+val[0]);
                        }

                        editTextPreference.setText(""+val[0]);
                        bindPreferenceSummaryToValue(findPreference("prefNumberOfWords"));

                        return true;
                    } else {
                        // invalid you can show invalid message
                        Toast.makeText(getActivity(), "Choose something between 1 and 25", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
            });


            return true;
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }


}
