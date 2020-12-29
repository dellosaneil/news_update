package com.example.newstracker.bottomNavigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newstracker.FragmentLifecycleLogging
import com.example.newstracker.databinding.FragmentSavedBinding
import com.example.newstracker.databinding.FragmentSearchBinding
import com.example.newstracker.recyclerView.RecyclerViewDecorator
import com.example.newstracker.recyclerView.SavedArticlesAdapter
import com.example.newstracker.viewModel.savedArticles.SavedArticlesVM
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class SavedArticlesFragment : FragmentLifecycleLogging() {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SavedArticlesVM
    private val myAdapter = SavedArticlesAdapter()
    private val TAG = "SavedArticlesFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity()).get(SavedArticlesVM::class.java)
        initializeRecyclerView()

        return binding.root
    }

    private fun initializeRecyclerView() {

        Log.i(TAG, "initializeRecyclerView: ")
        binding.savedArticlesRecyclerView.apply {
            setHasFixedSize(true)
            val decorator = RecyclerViewDecorator(8, 2)
            addItemDecoration(decorator)
            adapter = myAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
        observeViewModel()

    }

    private fun observeViewModel() {
        Log.i(TAG, "observeViewModel: ")

        viewModel.isFinished().observe(viewLifecycleOwner, {
            if (it) {
                visibilityControl()
                startObserveForChanges()
            }
        })


    }

    private fun startObserveForChanges() {
        viewModel.getSavedArticles()?.observe(viewLifecycleOwner, {
            Log.i(TAG, "observeViewModel: OBSERVE")
            myAdapter.setSavedArticles(it)
        })
    }

    private fun visibilityControl() {
        binding.savedArticlesRecyclerView.visibility = View.VISIBLE
        binding.savedArticlesProgressBar.visibility = View.INVISIBLE
    }

}