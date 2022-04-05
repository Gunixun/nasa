package com.example.nasa.ui.recycler.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.nasa.ui.recycler.RecyclerFragmentAdapter

class ItemTouchHelperCallback(private val recycleAdapter: RecyclerFragmentAdapter): ItemTouchHelper.Callback(){

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if (viewHolder is RecyclerFragmentAdapter.MarsViewHolder){
            return makeMovementFlags(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.START
            )
        }
        return makeMovementFlags(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.ACTION_STATE_IDLE
        )

    }

    override fun onMove(
        recyclerView: RecyclerView,
        source: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        recycleAdapter.onItemMove(source.bindingAdapterPosition, target.bindingAdapterPosition)
        return true
    }

    override fun onSwiped(source: RecyclerView.ViewHolder, direction: Int) {
        recycleAdapter.onItemDismiss(source.bindingAdapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if(viewHolder is RecyclerFragmentAdapter.MarsViewHolder)
            if(actionState!=ItemTouchHelper.ACTION_STATE_IDLE)
                viewHolder.onItemSelected()
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        if(viewHolder is RecyclerFragmentAdapter.MarsViewHolder)
            viewHolder.onItemClear()
    }
}