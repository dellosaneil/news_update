package com.example.newstracker.bottomNavigation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import androidx.preference.get
import com.example.newstracker.Constants.Companion.CLEAR_ARTICLES
import com.example.newstracker.Constants.Companion.CLEAR_SAVED_SEARCH
import com.example.newstracker.Constants.Companion.SEEK_BAR_KEY
import com.example.newstracker.R
import com.example.newstracker.repository.PreferenceRepository
import com.example.newstracker.repository.SavedArticlesRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.prefs.PreferenceChangeListener
import java.util.prefs.Preferences
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener{

    @Inject
    lateinit var searchPreferences : PreferenceRepository

    @Inject
    lateinit var savedArticles : SavedArticlesRepository

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen_layout, rootKey)

        preferenceManager.findPreference<SeekBarPreference>(SEEK_BAR_KEY)?.onPreferenceChangeListener = this
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        return when(preference?.key){
            CLEAR_ARTICLES -> {
                lifecycleScope.launch(IO){
                    savedArticles.clearSavedArticles()
                }
                true
            }
            CLEAR_SAVED_SEARCH -> {
                lifecycleScope.launch(IO) {
                    searchPreferences.clearSearchPreferences()
                }
                true
            }
            else -> false
        }
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        return false
    }

}