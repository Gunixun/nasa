package com.example.nasa.ui.recycler

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nasa.R
import com.example.nasa.databinding.FragmentRecyclerItemHeaderBinding
import com.example.nasa.databinding.FragmentRecyclerItemMarsBinding
import com.example.nasa.databinding.FragmentRecyclerItemMoonBinding
import com.example.nasa.ui.recycler.diffUtils.Change
import com.example.nasa.ui.recycler.diffUtils.DiffUtilsCallback
import com.example.nasa.ui.recycler.diffUtils.createCombinePayloads
import com.example.nasa.ui.recycler.utils.ItemTouchHelperAdapter
import com.example.nasa.ui.recycler.utils.ItemTouchHelperViewHolder
import com.example.nasa.ui.recycler.utils.generateData
import java.util.*

class RecyclerFragmentAdapter(val onClickItemListener: OnClickItemListener, val onStartDragListener: OnStartDragListener) :
    RecyclerView.Adapter<RecyclerFragmentAdapter.BaseViewHolder>(), ItemTouchHelperAdapter {

    private var listData: MutableList<Pair<Data, Boolean>> = arrayListOf()

    fun setData(data: MutableList<Pair<Data, Boolean>>) {
        val diffResult = DiffUtil.calculateDiff(DiffUtilsCallback(listData, data))
        diffResult.dispatchUpdatesTo(this)
        listData.clear()
        listData.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (TypeItem.fromInt(viewType)) {
            TypeItem.MOON -> {
                val binding = FragmentRecyclerItemMoonBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MoonViewHolder(binding.root)
            }
            TypeItem.MARS -> {
                val binding = FragmentRecyclerItemMarsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MarsViewHolder(binding.root)
            }
            TypeItem.HEADER -> {
                val binding = FragmentRecyclerItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HeaderViewHolder(binding.root)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(listData[position].first)
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val change = createCombinePayloads(payloads as List<Change<Pair<Data, Boolean>>>)
            val oldData = change.oldData
            val newData = change.newData
            if (oldData.first.name != newData.first.name)
                FragmentRecyclerItemMarsBinding.bind(holder.itemView).textViewName.text =
                    newData.first.name
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount() = listData.size

    override fun getItemViewType(position: Int) = listData[position].first.type.value

    fun appendItem(data: Pair<Data, Boolean>) {
        listData.add(data)
        notifyItemInserted(listData.size - 1)
    }

    override fun onItemMove(sourcePosition: Int, targetPosition: Int) {
        if (targetPosition > 0){
            Collections.swap(listData, sourcePosition, targetPosition)
            notifyItemMoved(sourcePosition, targetPosition)
        }
    }

    override fun onItemDismiss(position: Int) {
        listData.removeAt(position)
        notifyItemRemoved(position)
    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(data: Data)
    }

    inner class MoonViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(data: Data) {
            FragmentRecyclerItemMoonBinding.bind(itemView).apply {
                textViewName.text = data.name
                textViewDescription.text = data.description
                imageViewMoon.setOnClickListener {
                    onClickItemListener.onItemClick(data)
                }
            }
        }
    }

    inner class MarsViewHolder(view: View) : BaseViewHolder(view), ItemTouchHelperViewHolder {
        @SuppressLint("ClickableViewAccessibility")
        override fun bind(data: Data) {
            FragmentRecyclerItemMarsBinding.bind(itemView).apply {
                textViewName.text = data.name
                textViewMarsDescription.text = data.description
                itemView.setOnClickListener {
                    onClickItemListener.onItemClick(data)
                }
                imageViewAddItem.setOnClickListener {
                    listData.add(
                        layoutPosition,
                        generateData(
                            itemView.context.getString(R.string.mars),
                            itemView.context.getString(R.string.descriptions_mars),
                            TypeItem.MARS
                        )
                    )
                    notifyItemInserted(layoutPosition)
                }
                imageViewRemoveItem.setOnClickListener {
                    listData.removeAt(layoutPosition)
                    notifyItemRemoved(layoutPosition)
                }
                imageViewMoveItemUp.setOnClickListener {
                    if (layoutPosition > 1) {
                        Collections.swap(listData, layoutPosition, layoutPosition - 1)
                        notifyItemMoved(layoutPosition, layoutPosition - 1)
                    }
                }
                imageViewMoveItemDown.setOnClickListener {
                    if (layoutPosition != listData.size - 1) {
                        Collections.swap(listData, layoutPosition, layoutPosition + 1)
                        notifyItemMoved(layoutPosition, layoutPosition + 1)
                    }
                }
                textViewMarsDescription.visibility = if(listData[layoutPosition].second) View.VISIBLE else View.GONE
                imageViewMars.setOnClickListener {
                    listData[layoutPosition] = listData[layoutPosition].let {
                        it.first to !it.second
                    }
                    notifyItemChanged(layoutPosition)
                }
                dragHandleImageView.setOnTouchListener { v, event ->
                    if(event.actionMasked == MotionEvent.ACTION_DOWN
                        || event.actionMasked == MotionEvent.ACTION_UP){
                        onStartDragListener.onStartDrag(this@MarsViewHolder)
                    }
                    false
                }
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.CYAN)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }

    inner class HeaderViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(data: Data) {
            FragmentRecyclerItemHeaderBinding.bind(itemView).apply {
                textViewName.text = data.name
            }
        }
    }
}