package com.example.newstracker.bottomNavigation

import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import com.example.newstracker.Constants.Companion.CLEAR_ARTICLES
import com.example.newstracker.Constants.Companion.CLEAR_SAVED_SEARCH
import com.example.newstracker.Constants.Companion.DATA_STORE
import com.example.newstracker.Constants.Companion.SEEK_BAR_KEY
import com.example.newstracker.R
import com.example.newstracker.repository.PreferenceRepository
import com.example.newstracker.repository.SavedArticlesRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener{

    @Inject
    lateinit var searchPreferences : PreferenceRepository

    @Inject
    lateinit var savedArticles : SavedArticlesRepository

    private lateinit var dataStore: DataStore<Preferences>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStore = context?.createDataStore(DATA_STORE)!!
    }

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
        val seekBarValue = newValue as Int
        val dataStoreKey = intPreferencesKey(SEEK_BAR_KEY)
        lifecycleScope.launch(IO){
            dataStore.edit {
                it[dataStoreKey] = seekBarValue
            }
        }
        return true
    }

}