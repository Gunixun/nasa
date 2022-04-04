package com.example.nasa.ui.recycler

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.nasa.R
import com.example.nasa.databinding.FragmentRecyclerBinding
import com.example.nasa.ui.BaseFragment

class RecyclerFragment : BaseFragment<FragmentRecyclerBinding>(FragmentRecyclerBinding::inflate) {

    companion object {
        fun newInstance() = RecyclerFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RecyclerFragmentAdapter { data ->
            Toast.makeText(
                requireActivity(),
                data.name,
                Toast.LENGTH_SHORT
            ).show()
        }
        adapter.setData(createData())
        binding.recyclerView.adapter = adapter
    }

    private fun createData(): MutableList<Pair<Data, Boolean>> {
        val data = arrayListOf(
            Pair(Data(getString(R.string.moon), getString(R.string.descriptions_moon)), false),
            Pair(Data(getString(R.string.moon), getString(R.string.descriptions_moon)), false),
            Pair(
                Data(
                    getString(R.string.mars),
                    getString(R.string.descriptions_mars),
                    TypeItem.MARS
                ), false
            ),
            Pair(Data(getString(R.string.moon), getString(R.string.descriptions_moon)), false),
            Pair(
                Data(
                    getString(R.string.mars),
                    getString(R.string.descriptions_mars),
                    TypeItem.MARS
                ), false
            ),
            Pair(
                Data(
                    getString(R.string.mars),
                    getString(R.string.descriptions_mars),
                    TypeItem.MARS
                ), false
            ),
            Pair(Data(getString(R.string.moon), getString(R.string.descriptions_moon)), false),
            Pair(
                Data(
                    getString(R.string.mars),
                    getString(R.string.descriptions_mars),
                    TypeItem.MARS
                ), false
            ),
            Pair(Data(getString(R.string.moon), getString(R.string.descriptions_moon)), false),
            Pair(
                Data(
                    getString(R.string.mars),
                    getString(R.string.descriptions_mars),
                    TypeItem.MARS
                ), false
            ),
        )
        data.shuffle()
        return data
    }
}