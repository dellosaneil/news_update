package com.example.newstracker.bottomNavigation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newstracker.R
import com.example.newstracker.recyclerView.preference.SearchPreferenceAdapter
import com.example.newstracker.viewModel.searchPreference.SearchPreferenceVM
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addParameters: FloatingActionButton

    @InternalCoroutinesApi
    private lateinit var searchPreferenceVM: SearchPreferenceVM
    private lateinit var myAdapter: SearchPreferenceAdapter


    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView = view.findViewById(R.id.searchFragment_recyclerView)
        initializeRecyclerView()
        addParameters = view.findViewById(R.id.searchFragment_add)
        // Redirect Fragment into Fragment with adding capability
        addParameters.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.searchFragment_addNewCategory)
        }
        return view
    }


    private  val TAG = "SearchFragment"

    
    @InternalCoroutinesApi
    private fun initializeRecyclerView() {

        Log.i(TAG, "initializeRecyclerView: ${Thread.currentThread().name}")
        searchPreferenceVM =
            ViewModelProvider(requireActivity()).get(SearchPreferenceVM::class.java)
        myAdapter = SearchPreferenceAdapter()
        recyclerView.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        searchPreferenceVM.retrieveAllPreference()
            .observe(requireActivity(), Observer { myAdapter.setSearchPreferences(it) })
    }
}