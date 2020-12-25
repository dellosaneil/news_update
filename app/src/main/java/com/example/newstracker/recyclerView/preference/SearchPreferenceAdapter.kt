package com.example.newstracker.recyclerView.preference

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newstracker.R
import com.example.newstracker.room.entity.PreferenceEntity

class SearchPreferenceAdapter(private val listener: OnItemClickedListener) :
    RecyclerView.Adapter<SearchPreferenceAdapter.SearchPreferenceViewHolder>() {

    private val TAG = "SearchPreferenceAdapter"
    private var searchPreferences: List<PreferenceEntity>? = null

    fun setSearchPreferences(preferences: List<PreferenceEntity>) {
        searchPreferences = preferences
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPreferenceViewHolder {
        return SearchPreferenceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.search_preference_list_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holderSearch: SearchPreferenceViewHolder, position: Int) {
        val prefs = searchPreferences?.get(position)
        if (prefs != null) {
            holderSearch.bind(prefs)
        }
    }

    override fun getItemCount() = searchPreferences?.size ?: 0


    inner class SearchPreferenceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val searchTextView = itemView.findViewById<TextView>(R.id.searchPreference_label)
        private val searchInfoButton =
            itemView.findViewById<ImageButton>(R.id.searchPreference_details)
        private val searchBreakingNews =
            itemView.findViewById<ImageButton>(R.id.searchPreference_searchBreakingNews)

        init {
            searchBreakingNews.setOnClickListener(this)
        }

        fun bind(prefs: PreferenceEntity) {
            searchTextView.text = prefs.label
        }

        override fun onClick(v: View?) {
            searchPreferences?.get(adapterPosition)?.let { listener.onItemClicked(it) }
        }
    }

    interface OnItemClickedListener {
        fun onItemClicked(pref: PreferenceEntity)
    }

}