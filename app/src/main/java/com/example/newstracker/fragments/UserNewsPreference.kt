package com.example.newstracker.fragments

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.newstracker.R
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout

class UserNewsPreference : Fragment() {

    private var category : TextInputLayout? = null
    private var language : TextInputLayout? = null
    private var country : TextInputLayout? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_news_preference, container, false)
        initializeCategoryDropDown(view)
        initializeLanguageDropDown(view)
        initializeCountryDropDown(view)
        return view
    }

    private fun initializeCountryDropDown(view: View?) {
        val items = resources.getStringArray(R.array.country_list)
        country = view?.findViewById(R.id.news_country)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        if (country != null) {
            (country!!.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }
    }

    private fun initializeLanguageDropDown(view: View?) {
        val items = resources.getStringArray(R.array.language_list)
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