package edu.jc.corsage.masterani.Fragments

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import edu.jc.corsage.masterani.R

/**
 * Created by j3chowdh on 10/1/2017.
 */

class SettingFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }
}