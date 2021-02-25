package com.example.newstracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.newstracker.FragmentLifecycleLogging
import com.example.newstracker.R
import com.example.newstracker.databinding.FragmentAddSearchPreferenceBinding
import com.example.newstracker.repository.PreferenceRepository
import com.example.newstracker.room.entity.PreferenceEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject

@AndroidEntryPoint
class AddSearchPreferenceFragment : FragmentLifecycleLogging() {

    @Inject
    lateinit var repository: PreferenceRepository
    private var _binding: FragmentAddSearchPreferenceBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSearchPreferenceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeCategoryDropDown()
        initializeLanguageDropDown()
        initializeCountryDropDown()
        binding.newsSaveButton.setOnClickListener { retrieveValue(view) }
        setUpNavigateUp(view)
    }

    private fun setUpNavigateUp(view: View) {
        binding.topAppBarAddPreference.setNavigationOnClickListener {
            Navigation.findNavController(view).navigateUp()
        }
        binding.topAppBarAddPreference.title = getString(R.string.user_add_preference_title)
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
        lifecycleScope.launch(IO) {
            val newPreference = PreferenceEntity(
                preferenceLabel,
                categoryCode,
                countryCode,
                keywordText,
                languageCode
            )

            val isUpdate = repository.checkLabel(preferenceLabel)

            withContext(Main){
                if (isUpdate == 0) {
                    repository.addNewPreference(newPreference)
                    Navigation.findNavController(view)
                        .navigate(R.id.addNewSearchPreference_searchPreferences)
                } else {
                    binding.newsLabel.error =
                        resources.getString(R.string.user_add_preference_unique)
                }
            }
        }
    }


    private fun initializeCountryDropDown() {
        val items = resources.getStringArray(R.array.country_list_keys)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (binding.newsCountry.editText as? AutoCompleteTextView)
            ?.setAdapter(adapter)
    }

    private fun initializeLanguageDropDown() {
        val items = resources.getStringArray(R.array.language_list_keys)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (binding.newsLanguage.editText as? AutoCompleteTextView)
            ?.setAdapter(adapter)

    }

    private fun initializeCategoryDropDown() {
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