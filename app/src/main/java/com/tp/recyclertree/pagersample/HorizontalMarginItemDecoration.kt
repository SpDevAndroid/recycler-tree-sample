package com.tp.recyclertree.pagersample

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Adds margin to the left and right sides of the RecyclerView item.
 * Adapted from https://stackoverflow.com/a/27664023/4034572
 * @param horizontalMarginInDp the margin resource, in dp.
 */

class HorizontalMarginItemDecoration(
    context: Context,
    @DimenRes leftMargin: Int?,
    @DimenRes rightMargin: Int?
) :
    RecyclerView.ItemDecoration() {
    constructor(context: Context, @DimenRes horizontalMarginInDp: Int) : this(
        context,
        horizontalMarginInDp,
        horizontalMarginInDp
    )

    private val leftMarginInPx: Int? =
        leftMargin?.let { context.resources.getDimension(it).toInt() }
    private val rightMarginInPx: Int? =
        rightMargin?.let { context.resources.getDimension(it).toInt() }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        leftMarginInPx?.let {
            outRect.left = it
        }
        rightMarginInPx?.let {
            outRect.right = it
        }
    }
}