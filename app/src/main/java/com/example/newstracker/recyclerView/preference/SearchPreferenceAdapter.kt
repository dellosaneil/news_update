package com.example.newstracker.recyclerView.preference

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newstracker.R
import com.example.newstracker.databinding.SearchPreferenceListLayoutBinding
import com.example.newstracker.room.entity.PreferenceEntity

class SearchPreferenceAdapter(private val listener: OnItemClickedListener) :
    RecyclerView.Adapter<SearchPreferenceAdapter.SearchPreferenceViewHolder>() {

    private var searchPreferences: List<PreferenceEntity>? = null

    fun setSearchPreferences(preferences: List<PreferenceEntity>) {
        searchPreferences = preferences
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPreferenceViewHolder {
        val binding = SearchPreferenceListLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return SearchPreferenceViewHolder(binding)
    }

    override fun onBindViewHolder(holderSearch: SearchPreferenceViewHolder, position: Int) {
        val prefs = searchPreferences?.get(position)
        if (prefs != null) {
            holderSearch.bind(prefs)
        }
    }

    override fun getItemCount() = searchPreferences?.size ?: 0


    inner class SearchPreferenceViewHolder(private val binding: SearchPreferenceListLayoutBinding) :
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
                R.id.searchPreference_searchBreakingNews -> searchPreferences?.get(adapterPosition)
                    ?.let { listener.searchBreakingNews(it) }

                R.id.searchPreference_details -> searchPreferences?.get(adapterPosition)
                    ?.let { listener.preferenceDetails(it) }
            }


        }
    }

    interface OnItemClickedListener {
        fun searchBreakingNews(pref: PreferenceEntity)
        fun preferenceDetails(pref: PreferenceEntity)
    }

}
