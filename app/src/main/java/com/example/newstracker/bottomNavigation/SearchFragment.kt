package com.example.newstracker.bottomNavigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newstracker.Constants.Companion.ARGUMENT_BUNDLE
import com.example.newstracker.Constants.Companion.SEARCH_DETAILS_DIALOG
import com.example.newstracker.FragmentLifecycleLogging
import com.example.newstracker.R
import com.example.newstracker.databinding.FragmentSearchBinding
import com.example.newstracker.dialog.SearchPreferenceDialog
import com.example.newstracker.recyclerView.preference.SearchPreferenceAdapter
import com.example.newstracker.recyclerView.preference.SearchPreferenceDecorator
import com.example.newstracker.room.entity.PreferenceEntity
import com.example.newstracker.viewModel.searchPreference.SearchPreferenceVM

class SearchFragment : FragmentLifecycleLogging(), SearchPreferenceAdapter.OnItemClickedListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchPreferenceVM: SearchPreferenceVM
    private lateinit var myAdapter: SearchPreferenceAdapter
    private lateinit var anotherView : View

    private val TAG = "SearchFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        anotherView = binding.root

        initializeRecyclerView()
        // Redirect Fragment into Fragment with adding capability
        binding.searchFragmentAdd.setOnClickListener {
            Navigation.findNavController(anotherView)
                .navigate(R.id.searchFragment_addNewCategory)
        }
        return anotherView
    }


    private fun initializeRecyclerView() {
        Log.i(TAG, "initializeRecyclerView: ")
        searchPreferenceVM =
            ViewModelProvider(requireActivity()).get(SearchPreferenceVM::class.java)
        myAdapter = SearchPreferenceAdapter(this)
        binding.searchFragmentRecyclerView.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val customDecorator = SearchPreferenceDecorator(5, 0)
            addItemDecoration(customDecorator)
            searchPreferenceVM.retrieveAllPreference()
                .observe(requireActivity(), { myAdapter.setSearchPreferences(it) })
        }
    }

    override fun searchBreakingNews(pref: PreferenceEntity) {
        Log.i(TAG, "onItemClicked: ")
        val list = arrayOf(pref.label, pref.category,  pref.country, pref.keyword , pref.language)
        val bundle = bundleOf(ARGUMENT_BUNDLE to list)
        anotherView.findNavController().navigate(R.id.searchPreferences_newsArticles, bundle)
    }

    override fun preferenceDetails(pref: PreferenceEntity) {
        val dialog = SearchPreferenceDialog(pref)
        dialog.show(parentFragmentManager, SEARCH_DETAILS_DIALOG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}


