package com.example.newstracker.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.newstracker.FragmentLifecycleLogging
import com.example.newstracker.R
import com.example.newstracker.databinding.FragmentAddSearchPreferenceBinding
import com.example.newstracker.room.entity.PreferenceEntity
import com.example.newstracker.viewModel.addSearchPreference.AddSearchPreferenceVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AddSearchPreferenceFragment : FragmentLifecycleLogging() {


    private var _binding: FragmentAddSearchPreferenceBinding? = null
    private val binding get() = _binding!!
    private var toast: Toast? = null
    private val searchPreferenceViewModel: AddSearchPreferenceVM by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSearchPreferenceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeAutoCompleteTextViews()
        binding.newsSaveButton.setOnClickListener { checkValues() }
        setUpNavigateUp(view)
    }

    private fun setUpNavigateUp(view: View) {
        binding.topAppBarAddPreference.setNavigationOnClickListener {
            Navigation.findNavController(view).navigateUp()
        }
        binding.topAppBarAddPreference.title = getString(R.string.user_add_preference_title)
    }


    private fun indexNumber(key: String, array: Array<String>) = array.indexOf(key)

    /* 0 -> Preference Label
    *  1 -> Keyword
    *  2 -> Category
    *  3 -> Country Name
    *  4 -> Language Name */
    private fun checkValues() {
        val rawPreferenceStrings = arrayOf(
            binding.newsLabel.editText?.text.toString(),
            binding.newsKeyword.editText?.text.toString(),
            binding.newsCategory.editText?.text.toString(),
            binding.newsCountry.editText?.text.toString(),
            binding.newsLanguage.editText?.text.toString()
        )
        val preferenceKeys = arrayOf(
            resources.getStringArray(R.array.country_list_keys),
            resources.getStringArray(R.array.language_list_values)
        )
        val preferenceValues = arrayOf(
            resources.getStringArray(R.array.country_list_values),
            resources.getStringArray(R.array.language_list_values)
        )
        if (searchPreferenceViewModel.checkPreferenceLabel(rawPreferenceStrings[0])) {
            binding.newsLabel.isErrorEnabled = false
            val code = searchPreferenceViewModel.processPreferenceValues(
                rawPreferenceStrings,
                preferenceKeys,
                preferenceValues
            )
            handleCode(code)

        } else {
            binding.newsLabel.error = resources.getString(R.string.user_add_preference_error)
        }
    }

    private fun handleCode(code: Int) {
        when (code) {
            1 -> {
                binding.newsLabel.error = getString(R.string.user_add_preference_unique)
            }
            2 -> {
                showToastMessage()
            }
            else -> {
                binding.newsLabel.error = null
                Navigation.findNavController(requireView()).navigateUp()
            }
        }
    }

    @SuppressLint("ShowToast")
    private fun showToastMessage() {
        toast?.let {
            it.cancel()
            toast = Toast.makeText(
                requireContext(),
                resources.getString(R.string.user_add_preference_required),
                Toast.LENGTH_SHORT
            )
        } ?: run {
            toast = Toast.makeText(
                requireContext(),
                resources.getString(R.string.user_add_preference_required),
                Toast.LENGTH_SHORT
            )
        }
        toast?.show()
    }


    private fun initializeAutoCompleteTextViews() {
        val textViewValues = arrayOf(
            resources.getStringArray(R.array.country_list_keys),
            resources.getStringArray(R.array.language_list_keys),
            resources.getStringArray(R.array.category_list)
        )
        val editTextArray = arrayOf(
            binding.newsCountry.editText,
            binding.newsLanguage.editText,
            binding.newsCategory.editText
        )

        repeat(3) {
            val autoCompleteTextViewAdapter =
                ArrayAdapter(requireContext(), R.layout.list_item, textViewValues[it])
            (editTextArray[it] as? AutoCompleteTextView)?.setAdapter(autoCompleteTextViewAdapter)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}