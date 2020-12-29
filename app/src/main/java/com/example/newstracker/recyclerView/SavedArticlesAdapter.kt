package com.example.newstracker.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newstracker.databinding.ListLayoutSavedArticlesBinding
import com.example.newstracker.room.entity.SavedArticlesEntity

class SavedArticlesAdapter : RecyclerView.Adapter<SavedArticlesAdapter.SavedArticlesViewHolder>() {

    private var savedArticles : List<SavedArticlesEntity> = ArrayList()

    fun setSavedArticles(newList : List<SavedArticlesEntity>){
        val oldList = savedArticles
        val diffResult = DiffUtil.calculateDiff(SavedArticlesDiffUtilCallback(oldList, newList))
        savedArticles = newList
        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedArticlesViewHolder {
        val binding = ListLayoutSavedArticlesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return SavedArticlesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedArticlesViewHolder, position: Int) {
        val article = savedArticles[position]
        holder.bind(article)
    }

    override fun getItemCount() = savedArticles.size


    class SavedArticlesDiffUtilCallback(private val oldList : List<SavedArticlesEntity> , private val newList : List<SavedArticlesEntity>) : DiffUtil.Callback(){
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].articleTitle == newList[newItemPosition].articleTitle
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }



    class SavedArticlesViewHolder(private val binding : ListLayoutSavedArticlesBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(savedArticle : SavedArticlesEntity){
            binding.savedArticlesTitle.text = savedArticle.articleTitle
            binding.savedArticlesDescription.text = savedArticle.articleDescription
            binding.savedArticlesSource.text = savedArticle.source
        }
    }

}