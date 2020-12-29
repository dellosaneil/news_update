package com.example.newstracker.recyclerView.result

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newstracker.databinding.ListLayoutResultsBinding
import com.example.newstracker.retrofit.dataclass.Article
import com.example.newstracker.room.entity.SavedArticlesEntity


class ResultAdapter(val listener : SaveArticleListener) : RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    private var newsArticles: List<Article>? = null
    private val TAG = "ResultAdapter"

    fun setNewsArticles(newsResponse: List<Article>) {
        newsArticles = newsResponse
        Log.i(TAG, "setNewsArticles: ${newsArticles!!.size}")
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ListLayoutResultsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val article = newsArticles?.get(position)
        article?.let{
            holder.bind(it)
        }
    }

    override fun getItemCount() = newsArticles?.size ?:0

    inner class ResultViewHolder(private val binding: ListLayoutResultsBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init{
            binding.rvTitle.setOnClickListener(this)
        }

        fun bind(article: Article) {
            binding.rvTitle.text = article.title
            binding.rvDescription.text = article.description
            binding.rvSource.text = article.source.name
        }

        override fun onClick(v: View?) {
            val article = newsArticles?.get(adapterPosition)
            val title = article?.title?: ""
            val description = article?.description ?: ""
            val articleLink = article?.url ?: ""
            val source = article?.source?.name ?: ""
            listener.onClickSaveListener(SavedArticlesEntity(title, description, articleLink, source))

        }
    }


//    val articleTitle : String,
//    val articleDescription : String,
//    val articleLink : String

    interface SaveArticleListener{
        fun onClickSaveListener(article : SavedArticlesEntity)
    }

}