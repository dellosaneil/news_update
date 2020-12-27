package com.example.newstracker.bottomNavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newstracker.Constants.Companion.ARGUMENT_BUNDLE
import com.example.newstracker.R
import com.example.newstracker.recyclerView.preference.SearchPreferenceAdapter
import com.example.newstracker.recyclerView.preference.SearchPreferenceDecorator
import com.example.newstracker.room.entity.PreferenceEntity
import com.example.newstracker.viewModel.searchPreference.SearchPreferenceVM
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SearchFragment : Fragment(), SearchPreferenceAdapter.OnItemClickedListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addParameters: FloatingActionButton

    private lateinit var searchPreferenceVM: SearchPreferenceVM
    private lateinit var myAdapter: SearchPreferenceAdapter
    private lateinit var anotherView : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        anotherView = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView = anotherView.findViewById(R.id.searchFragment_recyclerView)

        initializeRecyclerView()

        addParameters = anotherView.findViewById(R.id.searchFragment_add)
        // Redirect Fragment into Fragment with adding capability
        addParameters.setOnClickListener {
            Navigation.findNavController(anotherView)
                .navigate(R.id.searchFragment_addNewCategory)
        }
        return anotherView
    }

    private fun initializeRecyclerView() {
        searchPreferenceVM =
            ViewModelProvider(requireActivity()).get(SearchPreferenceVM::class.java)
        myAdapter = SearchPreferenceAdapter(this)
        recyclerView.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val customDecorator = SearchPreferenceDecorator(5, 0)
            addItemDecoration(customDecorator)
            searchPreferenceVM.retrieveAllPreference()
                .observe(requireActivity(), { myAdapter.setSearchPreferences(it) })
        }
    }

    override fun onItemClicked(pref: PreferenceEntity) {
        val list = arrayOf(pref.label, pref.category,  pref.country, pref.keyword , pref.language)
        val bundle = bundleOf(ARGUMENT_BUNDLE to list)
        anotherView.findNavController().navigate(R.id.searchPreferences_newsArticles, bundle)
    }

}


