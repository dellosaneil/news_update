package com.example.newstracker.bottomNavigation

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.newstracker.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen_layout, rootKey)
    }
}