package com.example.nasa.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.*
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import coil.load
import com.example.nasa.R
import com.example.nasa.databinding.FragmentPictureByDayBinding
import com.example.nasa.model.PictureByDayData
import com.example.nasa.ui.BaseFragmentWithModel
import com.example.nasa.utils.createMsgSnackBar
import com.example.nasa.utils.createErrSnackBar
import com.example.nasa.utils.hideSnackBar
import com.example.nasa.view_model.AppState
import com.example.nasa.view_model.PictureByDayViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import java.util.*
import java.util.regex.Pattern

class PictureByDayFragment :
    BaseFragmentWithModel<PictureByDayViewModel, FragmentPictureByDayBinding>
        (FragmentPictureByDayBinding::inflate) {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var currentDate: Date
    private var retryIter: Int = 0
    private var zoom: Boolean = false
    private var snackBar: Snackbar? =null

    companion object {
        fun newInstance() = PictureByDayFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner) { state -> renderData(state) }
        currentDate = Date()
        initUi()
        viewModel.sendServerRequest(currentDate)
    }

    private fun renderData(pictureOfTheDayState: AppState) {
        binding.progress.isVisible = false
        binding.imageView.isVisible = false
        binding.youtubePlayerView.isVisible = false
        when (pictureOfTheDayState) {
            is AppState.Loading -> {
                binding.progress.isVisible = true
                binding.root.hideSnackBar(snackBar)
            }
            is AppState.SuccessPBD -> {
                setPictureData(pictureOfTheDayState.serverResponseData)
                retryIter = 0
            }
            is AppState.Error -> {
                if (retryIter < 3) {
                    snackBar = binding.root.createErrSnackBar(
                        text = pictureOfTheDayState.error.toString(),
                        actionText = R.string.retry,
                        { viewModel.sendServerRequest(currentDate) }
                    )
                    snackBar?.show()
                } else{
                    binding.root.createMsgSnackBar(
                        text = this.resources.getString(R.string.fall_load_data)
                    ).show()
                }
                retryIter++
            }
        }
    }

    fun extractYTId(ytUrl: String): String {
        var vId: String? = null
        val pattern = Pattern.compile(
            "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
            Pattern.CASE_INSENSITIVE
        )
        val matcher = pattern.matcher(ytUrl)
        if (matcher.matches()) {
            vId = matcher.group(1)
        }
        return vId!!
    }

    private fun showNasaVideo(videoId:String){
        lifecycle.addObserver(binding.youtubePlayerView)
        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
    }

    private fun setPictureData(pictureByDayData: PictureByDayData) {
        if (pictureByDayData.mediaType == "video"){
            animationsVisibilityPictureByDay(binding.youtubePlayerView)
            showNasaVideo(extractYTId(pictureByDayData.url))
        } else {
            animationsVisibilityPictureByDay(binding.imageView)
            binding.imageView.load(pictureByDayData.hdurl)
            binding.included.bottomSheetDescriptionHeader.text = pictureByDayData.title
            binding.included.bottomSheetDescription.text = pictureByDayData.explanation
        }
    }

    private fun initUi() {
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }

        binding.imageView.setOnClickListener {
            val changeBounds = ChangeImageTransform()
            changeBounds.duration = 1500
            TransitionManager.beginDelayedTransition(binding.container, changeBounds)
            zoom = !zoom
            binding.imageView.scaleType = if(zoom) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.CENTER_INSIDE
        }

        connectChipGroup()
        initBottomSheetBehavior()
    }

    private fun connectChipGroup() {
        binding.chipToday.isChecked = true
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            when {
                binding.chipToday.id == checkedId -> {
                    currentDate = Date()
                }
                binding.chipYesterday.id == checkedId -> {
                    currentDate = Date(Date().getTime() - (1000 * 60 * 60 * 24))
                }
                binding.chipTwoDaysAgo.id == checkedId -> {
                    currentDate = Date(Date().getTime() - (1000 * 60 * 60 * 48))
                }
            }
            viewModel.sendServerRequest(currentDate)
        }
    }

    private fun initBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.included.bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.setHideable(false)
    }

    private fun animationsVisibilityPictureByDay(view: View) {
        val transition = TransitionSet()
        val fade = Fade()
        fade.duration = 4000
        val changeBounds = ChangeBounds()
        changeBounds.duration = 2000
        transition.ordering = TransitionSet.ORDERING_SEQUENTIAL
        transition.addTransition(fade)
        transition.addTransition(changeBounds)
        TransitionManager.beginDelayedTransition(binding.container,transition)
        view.visibility =  View.VISIBLE
    }
}