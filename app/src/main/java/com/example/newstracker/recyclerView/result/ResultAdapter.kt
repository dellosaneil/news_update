package com.example.newstracker.recyclerView.result

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newstracker.R
import com.example.newstracker.retrofit.dataclass.Article


class ResultAdapter : RecyclerView.Adapter<ResultAdapter.MainActivityViewHolder>() {

    private var newsArticles: List<Article>? = null

    private val TAG = "ResultAdapter"
    fun setNewsArticles(newsResponse: List<Article>) {
        newsArticles = newsResponse
        Log.i(TAG, "setNewsArticles: ${newsArticles!!.size}")
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainActivityViewHolder {
        return MainActivityViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.result_list_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainActivityViewHolder, position: Int) {
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


    class MainActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rvLogo = itemView.findViewById<ImageView>(R.id.rv_logo)
        private val rvTitle = itemView.findViewById<TextView>(R.id.rv_title)
        private val rvDescription = itemView.findViewById<TextView>(R.id.rv_description)
        private val rvSource = itemView.findViewById<TextView>(R.id.rv_source)

        fun bind(article: Article) {
            rvTitle.text = article.title
            rvDescription.text = article.description
            rvSource.text = article.source.name
        }
    }

}