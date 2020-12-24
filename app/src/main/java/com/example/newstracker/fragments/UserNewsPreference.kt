package com.example.newstracker.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.newstracker.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class UserNewsPreference : Fragment() {

    private var category: TextInputLayout? = null
    private var language: TextInputLayout? = null
    private var country: TextInputLayout? = null


    //country?.editText?.text RETRIEVE DATA

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_news_preference, container, false)
        initializeCategoryDropDown(view)
        initializeLanguageDropDown(view)
        initializeCountryDropDown(view)

        val trialButton = view.findViewById<MaterialButton>(R.id.news_saveButton)
        trialButton.setOnClickListener { retrieveValue() }

        return view
    }

    private fun indexNumber(key : String, array : Array<String>)  = array.indexOf(key)

    private fun retrieveValue(){
        val countryName = country?.editText?.text.toString()
        val countryCode = resources.getStringArray(R.array.country_list_values)[indexNumber(countryName, resources.getStringArray(R.array.country_list_keys))]
        val languageName = language?.editText?.text.toString()
        val languageCode = resources.getStringArray(R.array.language_list_values)[indexNumber(languageName, resources.getStringArray(R.array.language_list_keys))]
        val categoryCode = category?.editText?.text.toString()
        saveToRoomDatabase(countryCode, languageCode, categoryCode)
    }

    private fun saveToRoomDatabase(countryCode: String?, languageCode: String?, categoryCode: String) {


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