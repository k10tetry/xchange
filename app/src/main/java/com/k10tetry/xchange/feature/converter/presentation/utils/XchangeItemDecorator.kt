package com.k10tetry.xchange.feature.converter.presentation.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject

class XchangeItemDecorator @Inject constructor() : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val margin = 16F.toPx(parent.resources).toInt()
        val position = parent.getChildAdapterPosition(view) + 1
        val bottom = if (position == parent.adapter?.itemCount) 2 * margin else 0
        val (top, left, right) = if (parent.layoutManager is GridLayoutManager) {
            when {
                position % (parent.layoutManager as GridLayoutManager).spanCount == 1 -> Triple(
                    margin,
                    margin,
                    margin / 2
                )

                position % (parent.layoutManager as GridLayoutManager).spanCount == 0 -> Triple(
                    margin,
                    margin / 2,
                    margin
                )

                else -> Triple(
                    margin,
                    margin / 2,
                    margin / 2
                )
            }
        } else {
            Triple(margin, margin, margin)
        }
        outRect.set(left, top, right, bottom)
    }

}