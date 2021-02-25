package com.example.newstracker.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.newstracker.R
import com.example.newstracker.databinding.DialogSearchPreferenceDetailsBinding
import com.example.newstracker.room.entity.PreferenceEntity

class SearchPreferenceDialog(private val details: PreferenceEntity) : DialogFragment() {

    private var _binding: DialogSearchPreferenceDetailsBinding? = null
    private val binding get() = _binding!!

    private val TAG = "SearchPreferenceDialog"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSearchPreferenceDetailsBinding.inflate(inflater, container, false)

        labelData(details.label)
        keywordData(details.keyword)
        categoryData(details.category)
        countryData(details.country)
        languageData(details.language)

        Log.i(TAG, "onCreateView: $details")

        binding.dialogPreferenceOkButton.setOnClickListener { dismiss() }

        return binding.root
    }

    private fun categoryData(get: String) {
        val category = if (get != "") {
            get
        } else {
            "Any"
        }
        val temp = getString(R.string.dialog_preference_category, category)
        binding.dialogPreferenceCategory.text = temp
    }

    private fun countryData(get: String) {
        val country = if (get != "") {
            get
        } else {
            "Any"
        }
        val temp = getString(R.string.dialog_preference_country, country)
        binding.dialogPreferenceCountry.text = temp

    }

    private fun languageData(get: String) {
        val language = if (get != "") {
            get
        } else {
            "Any"
        }
        val temp = getString(R.string.dialog_preference_language, language)
        binding.dialogPreferenceLanguage.text = temp


    }

    private fun labelData(get: String) {
        binding.dialogPreferenceLabel.text = get

    }

    private fun keywordData(get: String) {
        val keyWord = if (get != "") {
            get
        } else {
            "Any"
        }
        val temp = getString(R.string.dialog_preference_keyword, keyWord)
        binding.dialogPreferenceKeyword.text = temp
        

    }

}