package com.example.newstracker.bottomNavigation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newstracker.Constants
import com.example.newstracker.FragmentLifecycleLogging
import com.example.newstracker.R
import com.example.newstracker.WebViewActivity
import com.example.newstracker.callbackListener.SearchPreferenceSwipeListener
import com.example.newstracker.databinding.FragmentSavedBinding
import com.example.newstracker.recyclerView.RecyclerViewDecorator
import com.example.newstracker.recyclerView.SavedArticlesAdapter
import com.example.newstracker.viewModel.savedArticles.SavedArticlesVM
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO

class SavedArticlesFragment : FragmentLifecycleLogging(), SavedArticlesAdapter.OnOpenLinkListener,
    SearchPreferenceSwipeListener.DeleteSwipe {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SavedArticlesVM
    private val myAdapter = SavedArticlesAdapter(this)
    private val TAG = "SavedArticlesFragment"

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
        val intent = Intent(requireActivity(), WebViewActivity::class.java)
        intent.putExtra(Constants.URL_LINK_EXTRA, url)
        startActivity(intent)
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