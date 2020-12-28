package com.example.newstracker.callbackListener

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SearchPreferenceSwipeListener(private val listener : DeleteSwipe) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.swipePreferenceIndex(viewHolder.adapterPosition)
    }

    interface DeleteSwipe{
        fun swipePreferenceIndex(index : Int)
    }

}
