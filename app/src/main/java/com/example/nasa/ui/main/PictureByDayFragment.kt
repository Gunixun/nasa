package com.example.nasa.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import coil.load
import com.example.nasa.R
import com.example.nasa.databinding.FragmentPictureByDayBinding
import com.example.nasa.model.PictureModel
import com.example.nasa.ui.BaseFragmentWithModel
import com.example.nasa.ui.settings.SettingsFragment
import com.example.nasa.utils.showSnackBar
import com.example.nasa.view_model.PictureByDayState
import com.example.nasa.view_model.PictureByDayViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*

class PictureByDayFragment :
    BaseFragmentWithModel<PictureByDayViewModel, FragmentPictureByDayBinding>
        (FragmentPictureByDayBinding::inflate) {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var currenDate: Date

    companion object {
        fun newInstance() = PictureByDayFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner) { state -> renderData(state) }
        currenDate = Date()
        initUi()
        viewModel.sendServerRequest(currenDate)
    }

    private fun renderData(pictureOfTheDayState: PictureByDayState) {
        binding.progress.isVisible = false
        when (pictureOfTheDayState) {
            is PictureByDayState.Loading -> {
                binding.progress.isVisible = true
            }
            is PictureByDayState.Success -> {
                setPictureModel(pictureOfTheDayState.serverResponseData)
            }
            is PictureByDayState.Error -> {
                binding.root.showSnackBar(
                    text = pictureOfTheDayState.error.toString(),
                    actionText = R.string.retry,
                    { viewModel.sendServerRequest(currenDate) }
                )
            }
        }
    }

    private fun setPictureModel(pictureModel: PictureModel) {
        binding.imageView.load(pictureModel.hdurl)
        binding.included.bottomSheetDescriptionHeader.text = pictureModel.title
        binding.included.bottomSheetDescription.text = pictureModel.explanation
    }

    private fun initUi() {
        setHasOptionsMenu(true)

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }

        connectChipGroup()
        initBottomSheetBehavior()
    }

    private fun connectChipGroup(){
        binding.chipToday.isChecked = true
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            when {
                binding.chipToday.id == checkedId -> {
                    currenDate = Date()
                }
                binding.chipYesterday.id == checkedId -> {
                    currenDate = Date(Date().getTime() - (1000 * 60 * 60 * 24))
                }
                binding.chipTwoDaysAgo.id == checkedId -> {
                    currenDate = Date(Date().getTime() - (1000 * 60 * 60 * 48))
                }
            }
            viewModel.sendServerRequest(currenDate)
        }
    }

    private fun initBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.included.bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> Toast.makeText(requireContext(), "app_bar_fav", Toast.LENGTH_SHORT)
                .show()
            R.id.app_bar_settings -> {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.container, SettingsFragment.newInstance())
                    ?.addToBackStack("")
                    ?.commit()
            }
            android.R.id.home -> {
                BottomNavigationDrawerFragment().show(requireActivity().supportFragmentManager,"")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}