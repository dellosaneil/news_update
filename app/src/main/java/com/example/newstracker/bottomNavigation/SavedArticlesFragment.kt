package com.example.newstracker.bottomNavigation

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import com.example.newstracker.room.entity.SavedArticlesEntity
import com.example.newstracker.viewModel.savedArticles.SavedArticlesVM
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class SavedArticlesFragment : FragmentLifecycleLogging(), SavedArticlesAdapter.OnOpenLinkListener,
    SearchPreferenceSwipeListener.DeleteSwipe, SearchView.OnQueryTextListener {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!
    private val savedArticleViewModel: SavedArticlesVM by viewModels()

    private val myAdapter = SavedArticlesAdapter(this)
    private lateinit var navController: NavController

    private var previousValues = listOf<SavedArticlesEntity>()

    private var latestSearch = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
        val itemSwipeListener = SearchPreferenceSwipeListener(this)
        val itemTouchHelper = ItemTouchHelper(itemSwipeListener)
        itemTouchHelper.attachToRecyclerView(binding.savedArticlesRecyclerView)
        navController = Navigation.findNavController(view)
        initializeSearchView()
    }

    private fun initializeSearchView() {
        val searchViewMenu = binding.savedArticlesToolbar.menu.findItem(R.id.searchView_menu)
        val searchView = searchViewMenu?.actionView as? SearchView
        searchView?.apply {
            isSubmitButtonEnabled = false
            setOnQueryTextListener(this@SavedArticlesFragment)
        }
    }


    private fun initializeRecyclerView() {
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
        savedArticleViewModel.isFinished().observe(viewLifecycleOwner, { finished ->
            if (finished) {
                visibilityControl()
                savedArticleViewModel.articleList().observe(viewLifecycleOwner) { articleList ->
                    articleList?.let{
                        myAdapter.setSavedArticles(it)
                        previousValues = it
                    }
                }
            }
        })
    }

    private fun visibilityControl() {
        binding.savedArticlesRecyclerView.visibility = View.VISIBLE
        binding.savedArticlesProgressBar.visibility = View.INVISIBLE
    }

    override fun openLinkListener(url: String) {
        Toast.makeText(requireContext(), getString(R.string.redirecting), Toast.LENGTH_LONG).show()
        val bundle = bundleOf(Constants.URL_LINK_EXTRA to url)
        navController.navigate(R.id.savedArticleFragment_webViewFragment, bundle)

    }

    override fun swipePreferenceIndex(index: Int) {
        val deletedArticle = previousValues[index]
        savedArticleViewModel.deleteArticle(deletedArticle)
        createSnackBar(deletedArticle, latestSearch)
        searchArticleList(latestSearch)
    }

    private fun createSnackBar(deletedArticle: SavedArticlesEntity, searched : String) {
        Snackbar.make(requireView(), getString(R.string.article_deleted), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.undo)) {
                lifecycleScope.launch(IO){
                    savedArticleViewModel.restoreDeletedArticle(deletedArticle)
                    withContext(Main) {
                        searchArticleList(searched)
                    }
                }
            }
            .show()
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        query?.let {
            searchArticleList(it)
        }
        return true
    }

    private fun searchArticleList(query: String) {
        latestSearch = query
        savedArticleViewModel.searchArticles(query)

    }
}













