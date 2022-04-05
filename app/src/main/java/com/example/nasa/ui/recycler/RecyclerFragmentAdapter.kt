package com.example.nasa.ui.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nasa.databinding.FragmentRecyclerItemMarsBinding
import com.example.nasa.databinding.FragmentRecyclerItemMoonBinding
import com.example.nasa.ui.recycler.diffUtils.Change
import com.example.nasa.ui.recycler.diffUtils.DiffUtilsCallback
import com.example.nasa.ui.recycler.diffUtils.createCombinePayloads

class RecyclerFragmentAdapter(val onClickItemListener:OnClickItemListener): RecyclerView.Adapter<RecyclerFragmentAdapter.BaseViewHolder>() {

    private var listData: MutableList<Pair<Data, Boolean>> = arrayListOf()

    fun setData(data: MutableList<Pair<Data, Boolean>>){
        val diffResult = DiffUtil.calculateDiff(DiffUtilsCallback(listData, data))
        diffResult.dispatchUpdatesTo(this)
        listData.clear()
        listData.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when(TypeItem.fromInt(viewType)){
            TypeItem.MOON -> {
                val binding = FragmentRecyclerItemMoonBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                MoonViewHolder(binding.root)
            }
            TypeItem.MARS -> {
                val binding = FragmentRecyclerItemMarsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                MarsViewHolder(binding.root)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(listData[position].first)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()){
            val change = createCombinePayloads(payloads as List<Change<Pair<Data, Boolean>>>)
            val oldData = change.oldData
            val newData = change.newData
            if (oldData.first.name != newData.first.name)
                FragmentRecyclerItemMarsBinding.bind(holder.itemView).textViewName.text = newData.first.name
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount() = listData.size

    override fun getItemViewType(position: Int) = listData[position].first.type.value

    abstract class BaseViewHolder(view: View):RecyclerView.ViewHolder(view){
        abstract fun bind(data: Data)
    }

    inner class MoonViewHolder(view: View):BaseViewHolder(view){
        override fun bind(data: Data){
            FragmentRecyclerItemMoonBinding.bind(itemView).apply {
                textViewName.text = data.name
                textViewDescription.text = data.description
                imageViewMoon.setOnClickListener {
                    onClickItemListener.onItemClick(data)
                }
            }
        }
    }

    inner class MarsViewHolder(view: View):BaseViewHolder(view){
        override fun bind(data: Data){
            FragmentRecyclerItemMarsBinding.bind(itemView).apply {
                textViewName.text = data.name
                imageViewMars.setOnClickListener {
                    onClickItemListener.onItemClick(data)
                }
            }
        }
    }
}