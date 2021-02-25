package com.example.newstracker.recyclerView

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newstracker.R
import com.example.newstracker.databinding.ListLayoutArticlesBinding
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
        val binding = ListLayoutArticlesBinding.inflate(
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

    inner class SavedArticlesViewHolder(private val binding: ListLayoutArticlesBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        init{
            binding.rvLogo.setOnClickListener(this)
        }

        fun bind(savedArticle: SavedArticlesEntity) {
            savedArticle.articleLink?.let { colorUrlLink(it) }
            binding.rvTitle.text = savedArticle.articleTitle
            binding.rvDescription.text = savedArticle.articleDescription
            binding.rvSource.text = savedArticle.source

            Glide.with(binding.root)
                .load(savedArticle.urlImage)
                .placeholder(R.drawable.ic_image_gallery)
                .into(binding.rvImage)

        }

        private fun colorUrlLink(link : String){
            val isSecure = link.subSequence(0,5)
            if(isSecure != "https"){
                binding.rvTitle.setTextColor(Color.RED)
                binding.rvLogo.setImageResource(R.drawable.ic_unsecure)
            }else{
                binding.rvTitle.setTextColor(Color.BLACK)
                binding.rvLogo.setImageResource(R.drawable.ic_globe)
            }
        }

        override fun onClick(v: View?) {
            savedArticles[adapterPosition].articleLink?.let { openLinkListener.openLinkListener(it) }
        }
    }
    interface OnOpenLinkListener{
        fun openLinkListener(url : String)
    }



}