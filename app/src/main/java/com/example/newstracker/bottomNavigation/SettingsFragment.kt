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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            CLEAR_ARTICLES, CLEAR_SAVED_SEARCH -> {
                materialAlertDialog(preference.key)
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

    private fun materialAlertDialog(key : String?){
        val text = if(key == CLEAR_ARTICLES) "Saved Articles" else "Saved Search Preferences"
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.settings_dialog_delete_title, text))
            .setMessage(getString(R.string.settings_dialog_delete_content, text))
            .setPositiveButton(getString(R.string.settings_dialog_clear)){_,_ ->
                clearTable(key)
            }
            .setNegativeButton(getString(R.string.dialog_search_cancel)){dialog , _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun clearTable(key: String?) {
        lifecycleScope.launch(IO){
            if(key == CLEAR_ARTICLES) {
                savedArticles.clearSavedArticles()
            }else{
                searchPreferences.clearSearchPreferences()
            }
        }
    }
}













