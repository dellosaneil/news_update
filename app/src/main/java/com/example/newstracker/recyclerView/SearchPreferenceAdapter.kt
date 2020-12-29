package com.example.newstracker.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newstracker.R
import com.example.newstracker.databinding.ListLayoutSearchPreferencesBinding
import com.example.newstracker.room.entity.PreferenceEntity


class SearchPreferenceAdapter(private val listener: OnItemClickedListener) :
    RecyclerView.Adapter<SearchPreferenceAdapter.SearchPreferenceViewHolder>() {

    private var searchPreferences: List<PreferenceEntity> = ArrayList()

    fun setSearchPreferences(preferences: List<PreferenceEntity>) {
        val oldList = searchPreferences
        val diffResult = DiffUtil.calculateDiff(
            DiffCallbackSearchPreference(oldList, preferences)
        )
        searchPreferences = preferences
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPreferenceViewHolder {
        val binding = ListLayoutSearchPreferencesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return SearchPreferenceViewHolder(binding)
    }

    override fun onBindViewHolder(holderSearch: SearchPreferenceViewHolder, position: Int) {
        val prefs = searchPreferences[position]
        holderSearch.bind(prefs)
    }

    override fun getItemCount() = searchPreferences.size

    class DiffCallbackSearchPreference(
        private val oldList: List<PreferenceEntity>,
        private val newList: List<PreferenceEntity>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].label == newList[newItemPosition].label
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }


    inner class SearchPreferenceViewHolder(private val binding: ListLayoutSearchPreferencesBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            binding.searchPreferenceSearchBreakingNews.setOnClickListener(this)
            binding.searchPreferenceDetails.setOnClickListener(this)
        }


        fun bind(prefs: PreferenceEntity) {
            binding.searchPreferenceLabel.text = prefs.label
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.searchPreference_searchBreakingNews -> searchPreferences[adapterPosition]
                    .let { listener.searchBreakingNews(it) }

                R.id.searchPreference_details -> searchPreferences[adapterPosition]
                    .let { listener.preferenceDetails(it) }
            }
        }
    }


    interface OnItemClickedListener {
        fun searchBreakingNews(pref: PreferenceEntity)
        fun preferenceDetails(pref: PreferenceEntity)
    }

}
