package com.example.newstracker.recyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newstracker.R
import com.example.newstracker.databinding.ListLayoutResultsBinding
import com.example.newstracker.retrofit.dataclass.Article
import com.example.newstracker.room.entity.SavedArticlesEntity


class ResultAdapter(val linkListener : OpenLinkListener, val saveListener : SaveArticleListener) : RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

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
        RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

        init{
            binding.rvLogo.setOnClickListener(this)
            binding.listLayoutResultsCardView.setOnLongClickListener(this)
        }

        fun bind(article: Article) {
            binding.rvTitle.text = article.title
            binding.rvDescription.text = article.description
            binding.rvSource.text = article.source.name
        }

        override fun onClick(v: View?) {
            newsArticles?.get(adapterPosition)?.let { linkListener.onLongPressLinkListener(it.url) }
        }

        private fun createSavedEntity():SavedArticlesEntity{
            val article = newsArticles?.get(adapterPosition)
            val title = article?.title?: ""
            val description = article?.description ?: ""
            val articleLink = article?.url ?: ""
            val source = article?.source?.name ?: ""
            return SavedArticlesEntity(title, description, articleLink, source)
        }

        override fun onLongClick(v: View?): Boolean {
            return when(v?.id){
                R.id.listLayoutResults_cardView -> {
                    saveListener.saveArticleListener(createSavedEntity())
                    true
                }
                else -> false
            }
        }
    }

    interface OpenLinkListener{
        fun onLongPressLinkListener(urlLink : String)
    }
    interface SaveArticleListener{
        fun saveArticleListener(article : SavedArticlesEntity)
    }

}