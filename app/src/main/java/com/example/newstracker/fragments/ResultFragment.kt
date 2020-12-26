package com.example.newstracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newstracker.Constants.Companion.ARGUMENT_BUNDLE
import com.example.newstracker.R
import com.example.newstracker.recyclerView.result.ResultAdapter
import com.example.newstracker.repository.RetrofitRepository
import com.example.newstracker.room.entity.PreferenceEntity
import com.example.newstracker.viewModel.result.ResultVM
import com.example.newstracker.viewModel.result.ResultVMFactory

class ResultFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var repository: RetrofitRepository
    private lateinit var viewModel: ResultVM
    private lateinit var myAdapter: ResultAdapter
    private lateinit var progressBar : ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news_articles, container, false)
        val prefs = arguments?.getStringArray(ARGUMENT_BUNDLE)
        progressBar = view.findViewById(R.id.newsArticles_progressBar)
        recyclerView = view.findViewById(R.id.recyclerView_newsArticles)
        initializeRecyclerView(prefs, view.context)
        return view
    }

    private fun initializeRecyclerView(prefs: Array<String>?, context: Context) {
        repository = RetrofitRepository()
        myAdapter = ResultAdapter()
        val viewModelFactory = ResultVMFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ResultVM::class.java)
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
            layoutManager = LinearLayoutManager(context)
        }
        viewModel.getArticles().observe(requireActivity(), {
            it.body()?.articles?.let { articles ->
                myAdapter.setNewsArticles(
                    articles
                )
            }
        })
        viewModel.checkFinished().observe(requireActivity(), {
            if(it){
                progressBar.visibility = View.INVISIBLE
                recyclerView.visibility = View.VISIBLE
            }
        })

    }
}