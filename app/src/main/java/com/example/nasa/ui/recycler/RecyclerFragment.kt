package com.example.nasa.ui.recycler

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.nasa.R
import com.example.nasa.databinding.FragmentRecyclerBinding
import com.example.nasa.ui.BaseFragment
import com.example.nasa.ui.recycler.utils.ItemTouchHelperCallback
import com.example.nasa.ui.recycler.utils.generateData

class RecyclerFragment : BaseFragment<FragmentRecyclerBinding>(FragmentRecyclerBinding::inflate) {

    companion object {
        fun newInstance() = RecyclerFragment()
    }

    lateinit var itemTouchHelper:ItemTouchHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RecyclerFragmentAdapter({ data ->
            Toast.makeText(
                requireActivity(),
                data.name,
                Toast.LENGTH_SHORT
            ).show()
        },
            object : OnStartDragListener {
                override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                    itemTouchHelper.startDrag(viewHolder)
                }
            })
        adapter.setData(createData())

        binding.recyclerView.adapter = adapter
        binding.floatButtonAddItem.setOnClickListener {
            adapter.appendItem(
                generateData(
                    getString(R.string.mars),
                    getString(R.string.descriptions_mars),
                    TypeItem.MARS
                )
            )
        }
        ItemTouchHelper(ItemTouchHelperCallback(adapter)).attachToRecyclerView(binding.recyclerView)
        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun createData(): MutableList<Pair<Data, Boolean>> {
        val listData = arrayListOf(
            generateData(
                getString(R.string.moon),
                getString(R.string.descriptions_moon),
                TypeItem.MOON
            ),
            generateData(
                getString(R.string.moon),
                getString(R.string.descriptions_moon),
                TypeItem.MOON
            ),
            generateData(
                getString(R.string.mars),
                getString(R.string.descriptions_mars),
                TypeItem.MARS
            ),
            generateData(
                getString(R.string.moon),
                getString(R.string.descriptions_moon),
                TypeItem.MOON
            ),
            generateData(
                getString(R.string.moon),
                getString(R.string.descriptions_moon),
                TypeItem.MOON
            ),
            generateData(
                getString(R.string.moon),
                getString(R.string.descriptions_moon),
                TypeItem.MOON
            ),
            generateData(
                getString(R.string.moon),
                getString(R.string.descriptions_moon),
                TypeItem.MOON
            ),
            generateData(
                getString(R.string.moon),
                getString(R.string.descriptions_moon),
                TypeItem.MOON
            ),
            generateData(
                getString(R.string.mars),
                getString(R.string.descriptions_mars),
                TypeItem.MARS
            ),
            generateData(
                getString(R.string.mars),
                getString(R.string.descriptions_mars),
                TypeItem.MARS
            ),
            generateData(
                getString(R.string.mars),
                getString(R.string.descriptions_mars),
                TypeItem.MARS
            ),
            generateData(
                getString(R.string.mars),
                getString(R.string.descriptions_mars),
                TypeItem.MARS
            ),
            generateData(
                getString(R.string.mars),
                getString(R.string.descriptions_mars),
                TypeItem.MARS
            ),
            generateData(
                getString(R.string.mars),
                getString(R.string.descriptions_mars),
                TypeItem.MARS
            ),
        )
        listData.shuffle()
        listData.add(0, generateData(getString(R.string.header), "", type = TypeItem.HEADER))
        return listData
    }
}