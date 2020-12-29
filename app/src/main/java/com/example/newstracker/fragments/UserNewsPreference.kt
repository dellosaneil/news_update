package com.example.newstracker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.newstracker.FragmentLifecycleLogging
import com.example.newstracker.R
import com.example.newstracker.databinding.FragmentUserNewsPreferenceBinding
import com.example.newstracker.repository.DatabaseRepository
import com.example.newstracker.room.NewsTrackerDatabase
import com.example.newstracker.room.entity.PreferenceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserNewsPreference : FragmentLifecycleLogging() {

    private lateinit var repository: DatabaseRepository
    private val TAG = "UserNewsPreference"
    private var _binding: FragmentUserNewsPreferenceBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserNewsPreferenceBinding.inflate(inflater, container, false)
        val view = binding.root

        val dao = NewsTrackerDatabase.getDatabase(view.context).preferenceDao()
        repository = DatabaseRepository(dao)

        initializeCategoryDropDown()
        initializeLanguageDropDown()
        initializeCountryDropDown()

        binding.newsSaveButton.setOnClickListener { retrieveValue(view) }
        return view
    }

    private fun indexNumber(key: String, array: Array<String>) = array.indexOf(key)

    private fun retrieveValue(view: View) {
        val preferenceName = binding.newsLabel.editText?.text.toString()
        if (preferenceName.isBlank()) {
            binding.newsLabel.error = resources.getString(R.string.user_add_preference_error)
        } else {
            binding.newsLabel.isErrorEnabled = false
            val countryName = binding.newsCountry.editText?.text.toString()
            val languageName = binding.newsLanguage.editText?.text.toString()
            var countryIndex =
                indexNumber(countryName, resources.getStringArray(R.array.country_list_keys))
            var languageIndex =
                indexNumber(languageName, resources.getStringArray(R.array.language_list_keys))
            if (countryIndex == -1) {
                countryIndex = 0
            }
            if (languageIndex == -1) {
                languageIndex = 0
            }

            val keywordText = binding.newsKeyword.editText?.text.toString()
            var countryCode = resources.getStringArray(R.array.country_list_values)[countryIndex]
            var languageCode = resources.getStringArray(R.array.language_list_values)[languageIndex]
            var categoryCode = binding.newsCategory.editText?.text.toString()
            countryCode = convertAll("All", countryCode)
            categoryCode = convertAll("Any", categoryCode)
            languageCode = convertAll("Any", languageCode)

            saveToRoomDatabase(
                preferenceName,
                keywordText,
                countryCode,
                languageCode,
                categoryCode,
                view
            )
        }
    }


    //converts all default values to blanks.
    private fun convertAll(defaultText: String, inputValue: String): String {
        return if (defaultText == inputValue) {
            " "
        } else {
            inputValue
        }
    }

    private fun saveToRoomDatabase(
        preferenceLabel: String,
        keywordText: String,
        countryCode: String,
        languageCode: String,
        categoryCode: String,
        view: View
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val newPreference = PreferenceEntity(
                preferenceLabel,
                categoryCode,
                countryCode,
                keywordText,
                languageCode
            )

            Log.i(TAG, "saveToRoomDatabase: ")
            val isUpdate = repository.checkLabel(preferenceLabel)
            Log.i(TAG, "saveToRoomDatabase: $isUpdate")

            if (isUpdate == 0) {
                Log.i(TAG, "saveToRoomDatabase:Update ")
                repository.addNewPreference(newPreference)
                Navigation.findNavController(view)
                    .navigate(R.id.addNewSearchPreference_searchPreferences)
            } else {
                withContext(Main) {
                    binding.newsLabel.error =
                        resources.getString(R.string.user_add_preference_unique)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
    }


    private fun initializeCountryDropDown() {
        Log.i(TAG, "initializeCountryDropDown: ")
        val items = resources.getStringArray(R.array.country_list_keys)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (binding.newsCountry.editText as? AutoCompleteTextView)
            ?.setAdapter(adapter)
    }

    private fun initializeLanguageDropDown() {
        Log.i(TAG, "initializeLanguageDropDown: ")
        val items = resources.getStringArray(R.array.language_list_keys)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (binding.newsLanguage.editText as? AutoCompleteTextView)
            ?.setAdapter(adapter)

    }

    private fun initializeCategoryDropDown() {
        Log.i(TAG, "initializeCategoryDropDown: ")
        val items = resources.getStringArray(R.array.category_list)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (binding.newsCategory.editText as? AutoCompleteTextView)
            ?.setAdapter(adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}