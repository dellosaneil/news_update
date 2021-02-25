package com.example.newstracker.recyclerView

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newstracker.R
import com.example.newstracker.databinding.ListLayoutSavedArticlesBinding
import com.example.newstracker.room.entity.SavedArticlesEntity

class SavedArticlesAdapter(val openLinkListener: OnOpenLinkListener) : RecyclerView.Adapter<SavedArticlesAdapter.SavedArticlesViewHolder>() {

    private var savedArticles = listOf<SavedArticlesEntity>()

    fun setSavedArticles(newList: List<SavedArticlesEntity>) {
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


    class SavedArticlesDiffUtilCallback(
        private val oldList: List<SavedArticlesEntity>,
        private val newList: List<SavedArticlesEntity>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].articleTitle == newList[newItemPosition].articleTitle
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]
    }

    inner class SavedArticlesViewHolder(private val binding: ListLayoutSavedArticlesBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        init{
            binding.savedArticlesLogo.setOnClickListener(this)
        }

        fun bind(savedArticle: SavedArticlesEntity) {
            colorUrlLink(savedArticle.articleLink)
            binding.savedArticlesTitle.text = savedArticle.articleTitle
            binding.savedArticlesDescription.text = savedArticle.articleDescription
            binding.savedArticlesSource.text = savedArticle.source
        }

        private fun colorUrlLink(link : String){
            val isSecure = link.subSequence(0,5)
            if(isSecure != "https"){
                binding.savedArticlesTitle.setTextColor(Color.RED)
                binding.savedArticlesLogo.setImageResource(R.drawable.ic_unsecure)
            }else{
                binding.savedArticlesTitle.setTextColor(Color.BLACK)
                binding.savedArticlesLogo.setImageResource(R.drawable.ic_globe)
            }
        }

        override fun onClick(v: View?) {
            openLinkListener.openLinkListener(savedArticles[adapterPosition].articleLink)
        }
    }
    interface OnOpenLinkListener{
        fun openLinkListener(url : String)
    }



}