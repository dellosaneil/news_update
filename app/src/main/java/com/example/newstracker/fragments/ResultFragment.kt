package com.example.newstracker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newstracker.Constants.Companion.ARGUMENT_BUNDLE
import com.example.newstracker.FragmentLifecycleLogging
import com.example.newstracker.R
import com.example.newstracker.recyclerView.result.ResultAdapter
import com.example.newstracker.repository.RetrofitRepository
import com.example.newstracker.retrofit.dataclass.Article
import com.example.newstracker.room.entity.PreferenceEntity
import com.example.newstracker.viewModel.result.ResultVM
import com.example.newstracker.viewModel.result.ResultVMFactory
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ResultFragment : FragmentLifecycleLogging() {

    private lateinit var recyclerView: RecyclerView
    private val viewModel: ResultVM by activityViewModels()
    private lateinit var myAdapter: ResultAdapter
    private lateinit var progressBar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news_articles, container, false)
        val prefs = arguments?.getStringArray(ARGUMENT_BUNDLE)
        progressBar = view.findViewById(R.id.newsArticles_progressBar)
        recyclerView = view.findViewById(R.id.recyclerView_newsArticles)
        initializeRecyclerView(prefs)
        return view
    }

    private fun initializeRecyclerView(prefs: Array<String>?) {
        Log.i(TAG, "initializeRecyclerView: ")
        myAdapter = ResultAdapter()
        if (prefs != null) {
            viewModel.placePreferences(
                PreferenceEntity(
                    prefs[0],
                    prefs[1],
                    prefs[2],
                    prefs[3],
                    prefs[4]
                )
            )
        }
        viewModel.retrieveArticles()

        recyclerView.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }

        viewModel.getArticles().observe(viewLifecycleOwner, {
            if (it?.isSuccessful == true) {
                it.body()?.articles?.let { articles ->
                    val temp = filterArticles(articles)
                    myAdapter.setNewsArticles(
                        temp
                    )
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.network_problem),
                    Toast.LENGTH_LONG
                ).show()
            }

        })

        viewModel.checkFinished().observe(requireActivity(), {
            if (it) {
                progressBar.visibility = View.INVISIBLE
                recyclerView.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.VISIBLE
                recyclerView.visibility = View.INVISIBLE
            }
        })


    }

    private val TAG = "ResultFragment"

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


}