package com.example.newstracker.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newstracker.Constants.Companion.ARGUMENT_BUNDLE
import com.example.newstracker.Constants.Companion.URL_LINK_EXTRA
import com.example.newstracker.FragmentLifecycleLogging
import com.example.newstracker.R
import com.example.newstracker.WebViewActivity
import com.example.newstracker.databinding.FragmentResultsBinding
import com.example.newstracker.recyclerView.RecyclerViewDecorator
import com.example.newstracker.recyclerView.ResultAdapter
import com.example.newstracker.retrofit.dataclass.Article
import com.example.newstracker.retrofit.dataclass.NewsResponse
import com.example.newstracker.room.NewsTrackerDatabase
import com.example.newstracker.room.dao.SavedArticlesDao
import com.example.newstracker.room.entity.PreferenceEntity
import com.example.newstracker.room.entity.SavedArticlesEntity
import com.example.newstracker.viewModel.result.ResultVM
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.Response

class ResultFragment : FragmentLifecycleLogging(), ResultAdapter.OpenLinkListener,
    ResultAdapter.SaveArticleListener {

    private val scope = CoroutineScope(IO)

    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!
    private lateinit var savedArticlesDao: SavedArticlesDao

    private val viewModel: ResultVM by activityViewModels()
    private lateinit var myAdapter: ResultAdapter
    private lateinit var navController: NavController

    private var toast: Toast? = null

    private val TAG = "ResultFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        val prefs = arguments?.getParcelable<PreferenceEntity>(ARGUMENT_BUNDLE)

        initializeToolbar(prefs?.label)
        prefs?.let { searchArticlesWithPreference(it) }
        initializeRecyclerView()
        return binding.root
    }

    private fun initializeToolbar(label: String?) {
        binding.topAppBar.title = label
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedArticlesDao = NewsTrackerDatabase.getDatabase(view.context).savedArticlesDao()
        navController = Navigation.findNavController(view)

        binding.topAppBar.setNavigationOnClickListener {
            navController.navigateUp()
        }
    }


    //    Observer Variables
    private val visibilityObserver = Observer<Boolean> { changeVisibility(it) }
    private var articleObserver = Observer<Response<NewsResponse>?> { updateRecyclerView(it) }

    //    Observer functions
    private fun changeVisibility(isFinished: Boolean?) {
        Log.i(TAG, "changeVisibility: ")
        if (isFinished == true) {
            binding.newsArticlesProgressBar.visibility = View.INVISIBLE
            binding.newsArticleRecyclerView.visibility = View.VISIBLE
        } else {
            binding.newsArticlesProgressBar.visibility = View.VISIBLE
            binding.newsArticleRecyclerView.visibility = View.INVISIBLE
        }
    }

    private fun updateRecyclerView(articles: Response<NewsResponse>?) {
        Log.i(TAG, "updateRecyclerView: ")
        if (articles?.isSuccessful == true) {
            articles.body()?.articles?.let { result ->
                val temp = filterArticles(result)
                myAdapter.setNewsArticles(
                    temp
                )
            }
        } else {
            Log.i(TAG, "Error Body: ${articles?.message()}")
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.network_problem),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun initializeRecyclerView() {
        Log.i(TAG, "initializeRecyclerView: ")
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
        Log.i(TAG, "searchArticlesWithPreference: ")
        viewModel.placePreferences(prefs)
        viewModel.retrieveArticles()
    }

    //    Assign observer to LiveData
    private fun observeData() {
        Log.i(TAG, "observeData: ")
        viewModel.getArticles()?.observe(viewLifecycleOwner, articleObserver)
        viewModel.checkFinished()?.observe(viewLifecycleOwner, visibilityObserver)
    }

    //    Filter data to lessen probability of having the same article
    private fun filterArticles(articles: List<Article>): List<Article> {
        Log.i(TAG, "filterArticles: ")
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
        viewModel.clearAllData()
        scope.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPressLinkListener(urlLink: String) {
        val intent = Intent(requireActivity(), WebViewActivity::class.java)
        intent.putExtra(URL_LINK_EXTRA, urlLink)
        startActivity(intent)
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
                    showSavingResult(resources.getString(R.string.save_article_error))
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
                    showSavingResult(resources.getString(R.string.save_article_success))
                }
                setNegativeButton(resources.getString(R.string.dialog_save_article_negative)) { _, _ ->
                    showSavingResult(resources.getString(R.string.save_article_cancelled))
                }
                setCancelable(false)
                show()
            }
        }
    }

    @SuppressLint("ShowToast")
    private fun showSavingResult(message: String) {
        toast = if (toast != null) {
            toast?.cancel()
            Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG)
        } else {
            Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG)
        }
        toast?.show()
    }
}