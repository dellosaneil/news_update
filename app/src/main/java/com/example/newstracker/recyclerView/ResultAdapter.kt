package com.example.newstracker.recyclerView

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newstracker.R
import com.example.newstracker.databinding.ListLayoutResultsBinding
import com.example.newstracker.retrofit.dataclass.Article
import com.example.newstracker.room.entity.SavedArticlesEntity


class ResultAdapter(val linkListener: OpenLinkListener, val saveListener: SaveArticleListener) :
    RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    private var newsArticles = listOf<Article>()

    fun setNewsArticles(newsResponse: List<Article>) {
        val oldList = newsArticles
        val diffResult = DiffUtil.calculateDiff(ResultDiffCallback(oldList, newsResponse))
        newsArticles = newsResponse
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ListLayoutResultsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val article = newsArticles[position]
        article.let {
            holder.bind(it)
        }
    }

    override fun getItemCount() = newsArticles.size

    private class ResultDiffCallback(
        private val oldList: List<Article>,
        private val newList: List<Article>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].source == newList[newItemPosition].source

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]

    }


    inner class ResultViewHolder(private val binding: ListLayoutResultsBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

        init {
            binding.rvLogo.setOnClickListener(this)
            binding.listLayoutResultsCardView.setOnLongClickListener(this)
        }

        fun bind(article: Article) {
            colorUrlLink(article.url)
            binding.rvTitle.text = article.title
            binding.rvDescription.text = article.description
            binding.rvSource.text = article.source.name

            Glide
                .with(binding.root)
                .load(article.urlToImage)
                .fitCenter()
                .placeholder(R.drawable.ic_image_gallery)
                .into(binding.rvImage)
        }

        private fun colorUrlLink(link: String) {
            val isSecure = link.subSequence(0, 5)
            if (isSecure != "https") {
                binding.rvTitle.setTextColor(Color.RED)
                binding.rvLogo.setImageResource(R.drawable.ic_unsecure)
            } else {
                binding.rvTitle.setTextColor(Color.BLACK)
                binding.rvLogo.setImageResource(R.drawable.ic_news_recycler_view)
            }
        }


        override fun onClick(v: View?) {
            newsArticles[adapterPosition].let { linkListener.onPressLinkListener(it.url) }
        }

        private fun createSavedEntity(): SavedArticlesEntity {
            val article = newsArticles[adapterPosition]
            val title = article.title
            val description = article.description
            val articleLink = article.url
            val source = article.source.name
            val urlImage = article.urlToImage
            return SavedArticlesEntity(title, description, articleLink, source, urlImage)
        }

        override fun onLongClick(v: View?): Boolean {
            return when (v?.id) {
                R.id.listLayoutResults_cardView -> {
                    saveListener.saveArticleListener(createSavedEntity())
                    true
                }
                else -> false
            }
        }
    }

    interface OpenLinkListener {
        fun onPressLinkListener(urlLink: String)
    }

    interface SaveArticleListener {
        fun saveArticleListener(article: SavedArticlesEntity)
    }

}