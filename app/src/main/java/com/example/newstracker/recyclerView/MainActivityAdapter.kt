package com.example.newstracker.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newstracker.R
import com.example.newstracker.retrofit.dataclass.Article


class MainActivityAdapter : RecyclerView.Adapter<MainActivityAdapter.MainActivityViewHolder>() {

    private var newsArticles : List<Article>? = null

    fun setNewsArticles(newsResponse: List<Article>){
        newsArticles = newsResponse
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainActivityViewHolder {
        return MainActivityViewHolder(LayoutInflater.from(parent.context). inflate(R.layout.main_activity_list_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MainActivityViewHolder, position: Int) {
        val article = newsArticles?.get(position)
        if (article != null) {
            holder.bind(article)
        }
    }

    override fun getItemCount(): Int{
        if(newsArticles == null){
            return 0
        }
        return newsArticles!!.size
    }


    class MainActivityViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val rvLogo = itemView.findViewById<ImageView>(R.id.rv_logo)
        private val rvTitle = itemView.findViewById<TextView>(R.id.rv_title)
        private val rvDescription = itemView.findViewById<TextView>(R.id.rv_description)

        fun bind(article : Article){
            var shortTitle = article.title.take(75)
            if(article.title.length > 75){
                shortTitle += "..."
            }

            rvTitle.text = shortTitle
            rvDescription.text = article.description

        }
    }

}