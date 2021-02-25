package com.example.newstracker.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newstracker.Constants.Companion.URL_LINK_EXTRA
import com.example.newstracker.FragmentLifecycleLogging
import com.example.newstracker.R
import com.example.newstracker.databinding.FragmentResultsBinding
import com.example.newstracker.recyclerView.RecyclerViewDecorator
import com.example.newstracker.recyclerView.ResultAdapter
import com.example.newstracker.repository.SavedArticlesRepository
import com.example.newstracker.retrofit.dataclass.Article
import com.example.newstracker.retrofit.dataclass.NewsResponse
import com.example.newstracker.room.entity.PreferenceEntity
import com.example.newstracker.room.entity.SavedArticlesEntity
import com.example.newstracker.viewModel.result.ResultVM
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject


@AndroidEntryPoint
class ResultFragment : FragmentLifecycleLogging(), ResultAdapter.OpenLinkListener,
    ResultAdapter.SaveArticleListener {

    private val scope = CoroutineScope(IO)

    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!

    private val args : ResultFragmentArgs? by navArgs()

    @Inject
    lateinit var savedArticlesDao: SavedArticlesRepository

    private val resultViewModel: ResultVM by activityViewModels()
    private lateinit var myAdapter: ResultAdapter
    private lateinit var navController: NavController

    private var toast: Toast? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        args?.preferences?.let { searchArticlesWithPreference(it) }
        initializeRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.topAppBar.setNavigationOnClickListener {
            navController.navigateUp()
        }
    }

    private fun initializeToolbar(label: String?, number : Int) {
        binding.topAppBar.title = getString(R.string.results_toolbar, label, number.toString())
    }


    //    Observer Variables
    private val visibilityObserver = Observer<Boolean> { changeVisibility(it) }
    private var articleObserver = Observer<Response<NewsResponse>?> { updateRecyclerView(it) }

    //    Observer functions
    private fun changeVisibility(isFinished: Boolean?) {
        if (isFinished == true) {
            binding.newsArticlesProgressBar.visibility = View.INVISIBLE
            binding.newsArticleRecyclerView.visibility = View.VISIBLE
        } else {
            binding.newsArticlesProgressBar.visibility = View.VISIBLE
            binding.newsArticleRecyclerView.visibility = View.INVISIBLE
        }
    }

    private fun updateRecyclerView(articles: Response<NewsResponse>?) {
        if (articles?.isSuccessful == true) {
            articles.body()?.articles?.let { result ->
                val uniqueArticles = filterArticles(result)
                myAdapter.setNewsArticles(
                    uniqueArticles
                )
                initializeToolbar(args?.preferences?.label, uniqueArticles.size)
            }
        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.network_problem),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun initializeRecyclerView() {
        myAdapter = ResultAdapter(this, this)
        binding.newsArticleRecyclerView.run {
            setHasFixedSize(true)
            val decorator = RecyclerViewDecorator(6, 6)
            addItemDecoration(decorator)
            adapter = myAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
        observeData()
    }

    //   Put data into view model
    private fun searchArticlesWithPreference(prefs: PreferenceEntity) {
        resultViewModel.placePreferences(prefs)
        resultViewModel.retrieveArticles()
    }

    //    Assign observer to LiveData
    private fun observeData() {
        resultViewModel.getArticles()?.observe(viewLifecycleOwner, articleObserver)
        resultViewModel.checkFinished()?.observe(viewLifecycleOwner, visibilityObserver)
    }

    //    Filter data to lessen probability of having the same article
    private fun filterArticles(articles: List<Article>): List<Article> {
        val tempSet = mutableSetOf<String>()
        val filteredArticles = mutableListOf<Article>()
        for (article in articles) {
            if (!tempSet.contains(article.title)) {
                tempSet.add(article.title)
                filteredArticles.add(article)
            }
        }
        return filteredArticles
    }

    override fun onDestroy() {
        super.onDestroy()
        resultViewModel.clearAllData()
        scope.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPressLinkListener(urlLink: String) {
        val bundle = bundleOf(URL_LINK_EXTRA to urlLink)
        navController.navigate(R.id.newsArticlesFragment_webViewFragment, bundle)
    }

    override fun saveArticleListener(article: SavedArticlesEntity) {
        scope.launch {
            val isInDatabase = savedArticlesDao.checkArticle(article.articleTitle)
            if (isInDatabase == 0) {
                withContext(Main) {
                    saveAlertDialog(article)
                }
            } else {
                withContext(Main) {
                    toastHandler(resources.getString(R.string.save_article_error))
                }
            }

        }
    }

    private fun saveAlertDialog(article: SavedArticlesEntity) {
        view?.context?.let {
            MaterialAlertDialogBuilder(it).run {
                setTitle(resources.getString(R.string.dialog_save_article_title))
                setMessage(
                    resources.getString(
                        R.string.dialog_save_article_message,
                        article.articleTitle
                    )
                )
                setPositiveButton(resources.getString(R.string.dialog_save_article_positive)) { _, _ ->
                    scope.launch {
                        savedArticlesDao.saveArticle(article)
                    }
                    toastHandler(resources.getString(R.string.save_article_success))
                }
                setNegativeButton(resources.getString(R.string.dialog_save_article_negative)) { _, _ ->
                    toastHandler(resources.getString(R.string.save_article_cancelled))
                }
                setCancelable(false)
                show()
            }
        }
    }

    @SuppressLint("ShowToast")
    private fun toastHandler(message: String) {
        toast = if (toast != null) {
            toast?.cancel()
            Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG)
        } else {
            Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG)
        }
        toast?.show()
    }
}