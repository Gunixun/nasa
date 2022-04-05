package com.example.nasa.ui.recycler

import androidx.recyclerview.widget.RecyclerView

fun interface OnClickItemListener {
    fun onItemClick(data: Data)
}

fun interface OnStartDragListener {
    fun onStartDrag(view: RecyclerView.ViewHolder)
}