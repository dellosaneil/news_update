package com.example.newstracker.bottomNavigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newstracker.Constants
import com.example.newstracker.FragmentLifecycleLogging
import com.example.newstracker.R
import com.example.newstracker.callbackListener.SearchPreferenceSwipeListener
import com.example.newstracker.databinding.FragmentSavedBinding
import com.example.newstracker.recyclerView.RecyclerViewDecorator
import com.example.newstracker.recyclerView.SavedArticlesAdapter
import com.example.newstracker.viewModel.savedArticles.SavedArticlesVM
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SavedArticlesFragment : FragmentLifecycleLogging(), SavedArticlesAdapter.OnOpenLinkListener,
    SearchPreferenceSwipeListener.DeleteSwipe {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SavedArticlesVM
    private val myAdapter = SavedArticlesAdapter(this)
    private val TAG = "SavedArticlesFragment"
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity()).get(SavedArticlesVM::class.java)
        initializeRecyclerView()

        val itemSwipeListener = SearchPreferenceSwipeListener(this)
        val itemTouchHelper = ItemTouchHelper(itemSwipeListener)
        itemTouchHelper.attachToRecyclerView(binding.savedArticlesRecyclerView)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }


    private fun initializeRecyclerView() {

        Log.i(TAG, "initializeRecyclerView: ")
        binding.savedArticlesRecyclerView.run {
            setHasFixedSize(true)
            val decorator = RecyclerViewDecorator(6, 6)
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

    override fun openLinkListener(url: String) {
        Toast.makeText(requireContext(), "Redirecting...", Toast.LENGTH_LONG).show()
        val bundle = bundleOf(Constants.URL_LINK_EXTRA to url)
        navController.navigate(R.id.savedArticleFragment_webViewFragment, bundle)

    }

    override fun swipePreferenceIndex(index: Int) {
        val deletedArticle = viewModel.getSavedArticles()?.value?.get(index)
        val title = deletedArticle?.articleTitle

        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.delete_dialog_title))
            setMessage(getString(R.string.delete_dialog_message, title))
            setPositiveButton(getString(R.string.dialog_delete_confirm)) { _, _ ->
                title?.let { viewModel.deleteArticle(it) }
            }
            setNegativeButton(getString(R.string.dialog_delete_cancel)) {_,_ ->
                myAdapter.notifyItemChanged(index)
            }
            setCancelable(false)
        }.show()
    }
}