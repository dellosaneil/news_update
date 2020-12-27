package com.example.newstracker.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.newstracker.R
import com.example.newstracker.databinding.DialogSearchPreferenceDetailsBinding
import com.example.newstracker.room.entity.PreferenceEntity

class SearchPreferenceDialog(private val details : PreferenceEntity) : DialogFragment() {

    private var _binding: DialogSearchPreferenceDetailsBinding? = null
    private val binding get() = _binding!!

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

        binding.dialogPreferenceOkButton.setOnClickListener { dismiss() }

        return binding.root
    }

    private fun categoryData(get: String?){
        val temp = getString(R.string.dialog_preference_category, get)
        binding.dialogPreferenceCategory.text = temp
    }

    private fun countryData(get: String?) {
        val temp = getString(R.string.dialog_preference_country, get)
        binding.dialogPreferenceCountry.text = temp
    }


    private fun languageData(get: String?) {
        val temp = getString(R.string.dialog_preference_language, get)
        binding.dialogPreferenceLanguage.text = temp
    }


    private fun labelData(get: String?) {
        binding.dialogPreferenceLabel.text = get
    }

    private fun keywordData(get: String?) {
        val temp = getString(R.string.dialog_preference_keyword, get)
        binding.dialogPreferenceKeyword.text = temp
    }

}