package com.example.newstracker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.newstracker.R
import com.example.newstracker.repository.DatabaseRepository
import com.example.newstracker.room.NewsTrackerDatabase
import com.example.newstracker.room.entity.PreferenceEntity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

@InternalCoroutinesApi
class UserNewsPreference : Fragment() {

    private var category: TextInputLayout? = null
    private var language: TextInputLayout? = null
    private var country: TextInputLayout? = null
    private var label: TextInputLayout? = null
    private var keyword: TextInputLayout? = null

    private lateinit var repository: DatabaseRepository

    //country?.editText?.text RETRIEVE DATA

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_news_preference, container, false)
        val dao = NewsTrackerDatabase.getDatabase(view.context).preferenceDao()
        repository = DatabaseRepository(dao)
        initializeCategoryDropDown(view)
        initializeLanguageDropDown(view)
        initializeCountryDropDown(view)
        label = view?.findViewById(R.id.news_label)
        keyword = view?.findViewById(R.id.news_keyword)
        val trialButton = view.findViewById<MaterialButton>(R.id.news_saveButton)
        trialButton.setOnClickListener { retrieveValue() }
        return view
    }

    private fun indexNumber(key: String, array: Array<String>) = array.indexOf(key)

    private fun retrieveValue() {
        val preferenceLabel: String
        val preferenceName = label?.editText?.text.toString()
        if (preferenceName.isBlank()) {
            label?.error = resources.getString(R.string.user_add_preference_error)
        }else{
            label?.isErrorEnabled = false
            val countryName = country?.editText?.text.toString()
            val languageName = language?.editText?.text.toString()
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

            val keywordText = keyword?.editText?.text.toString()
            preferenceLabel = label?.editText?.text.toString()
            var countryCode = resources.getStringArray(R.array.country_list_values)[countryIndex]
            var languageCode = resources.getStringArray(R.array.language_list_values)[languageIndex]
            var categoryCode = category?.editText?.text.toString()
            countryCode = convertAll("All", countryCode)
            categoryCode = convertAll("Any", categoryCode)
            languageCode = convertAll("Any", languageCode)
            saveToRoomDatabase(
                preferenceLabel,
                keywordText,
                categoryCode,
                countryCode,
                languageCode
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
        categoryCode: String
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val newPreference  = PreferenceEntity(preferenceLabel, categoryCode, languageCode, countryCode, keywordText)
            val isUpdate = repository.checkLabel(preferenceLabel)
            if(isUpdate == 0){
                repository.addNewPreference(newPreference)
                activity?.supportFragmentManager?.popBackStack()
            }else{
                withContext(Main){
                    label?.error = resources.getString(R.string.user_add_preference_unique)
                }
            }
        }
    }

    private val TAG = "UserNewsPreference"

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
    }


    private fun initializeCountryDropDown(view: View?) {
        val items = resources.getStringArray(R.array.country_list_keys)
        country = view?.findViewById(R.id.news_country)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        if (country != null) {
            (country!!.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }
    }

    private fun initializeLanguageDropDown(view: View?) {
        val items = resources.getStringArray(R.array.language_list_keys)
        language = view?.findViewById(R.id.news_language)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        if (language != null) {
            (language!!.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }
    }

    private fun initializeCategoryDropDown(view: View?) {
        val items = resources.getStringArray(R.array.category_list)
        category = view?.findViewById(R.id.news_category)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        if (category != null) {
            (category!!.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }
    }

}