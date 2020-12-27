package com.example.newstracker.recyclerView.result

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newstracker.databinding.ResultListLayoutBinding
import com.example.newstracker.retrofit.dataclass.Article


class ResultAdapter : RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    private var newsArticles: List<Article>? = null
    private val TAG = "ResultAdapter"

    fun setNewsArticles(newsResponse: List<Article>) {
        newsArticles = newsResponse
        Log.i(TAG, "setNewsArticles: ${newsArticles!!.size}")
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ResultListLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val article = newsArticles?.get(position)
        if (article != null) {
            holder.bind(article)
        }
    }

    override fun getItemCount(): Int {
        if (newsArticles == null) {
            return 0
        }
        return newsArticles!!.size
    }


    class ResultViewHolder(private val binding: ResultListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.rvTitle.text = article.title
            binding.rvDescription.text = article.description
            binding.rvSource.text = article.source.name
        }
    }
}