package com.example.newstracker.recyclerView.preference

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SearchPreferenceDecorator(private val paddingTop: Int, private val paddingSide: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = paddingTop
        outRect.left = paddingSide
        outRect.right = paddingSide
    }
}
