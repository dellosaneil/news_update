package com.example.newstracker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newstracker.Constants.Companion.ARGUMENT_BUNDLE
import com.example.newstracker.FragmentLifecycleLogging
import com.example.newstracker.R
import com.example.newstracker.databinding.FragmentNewsArticlesBinding
import com.example.newstracker.recyclerView.result.ResultAdapter
import com.example.newstracker.recyclerView.result.ResultDecorator
import com.example.newstracker.retrofit.dataclass.Article
import com.example.newstracker.retrofit.dataclass.NewsResponse
import com.example.newstracker.room.entity.PreferenceEntity
import com.example.newstracker.viewModel.result.ResultVM
import retrofit2.Response

class ResultFragment : FragmentLifecycleLogging() {


    private var _binding: FragmentNewsArticlesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ResultVM by activityViewModels()
    private lateinit var myAdapter: ResultAdapter

    private val TAG = "ResultFragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsArticlesBinding.inflate(inflater, container, false)
        val prefs = arguments?.getStringArray(ARGUMENT_BUNDLE)
        searchArticlesWithPreference(prefs)
        initializeRecyclerView()
        return binding.root
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
        myAdapter = ResultAdapter()
        binding.newsArticleRecyclerView.apply {
            val decorator = ResultDecorator(10, 5)
            addItemDecoration(decorator)
            adapter = myAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
        observeData()
    }
    
    //   Put data into view model
    private fun searchArticlesWithPreference(prefs: Array<String>?){
        Log.i(TAG, "searchArticlesWithPreference: ")
        if (prefs != null) {
            viewModel.placePreferences(PreferenceEntity(prefs[0], prefs[1], prefs[2], prefs[3], prefs[4]))
        }
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}