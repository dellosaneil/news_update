package com.example.newstracker.recyclerView.preference

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newstracker.R
import com.example.newstracker.room.entity.PreferenceEntity

class SearchPreferenceAdapter : RecyclerView.Adapter<SearchPreferenceAdapter.SearchPreferenceViewHolder>() {

    private var searchPreferences : List<PreferenceEntity>? = null

    fun setSearchPreferences(preferences : List<PreferenceEntity>){
        searchPreferences = preferences
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPreferenceViewHolder {
        return SearchPreferenceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_preference_list_layout, parent, false))
    }

    override fun onBindViewHolder(holderSearch: SearchPreferenceViewHolder, position: Int) {
        val prefs = searchPreferences?.get(position)
        if (prefs != null) {
            holderSearch.bind(prefs)
        }


    }

    override fun getItemCount() = searchPreferences?.size ?: 0


    class SearchPreferenceViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val searchTextView = itemView.findViewById<TextView>(R.id.searchPreference_label)
        private val searchInfoButton = itemView.findViewById<ImageButton>(R.id.searchPreference_details)
        private val searchDeleteButton = itemView.findViewById<ImageButton>(R.id.searchPreference_delete)

        fun bind(prefs : PreferenceEntity){
            searchTextView.text = prefs.label
        }


    }


}