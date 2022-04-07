package com.example.nasa.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.*
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.*
import android.transition.*
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.provider.FontRequest
import androidx.core.provider.FontsContractCompat
import androidx.core.view.isVisible
import coil.load
import com.example.nasa.R
import com.example.nasa.databinding.FragmentPictureByDayBinding
import com.example.nasa.model.PictureByDayData
import com.example.nasa.ui.BaseFragmentWithModel
import com.example.nasa.utils.createErrSnackBar
import com.example.nasa.utils.createMsgSnackBar
import com.example.nasa.utils.hideSnackBar
import com.example.nasa.view_model.AppState
import com.example.nasa.view_model.PictureByDayViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity
import java.util.*
import java.util.regex.Pattern

class PictureByDayFragment :
    BaseFragmentWithModel<PictureByDayViewModel, FragmentPictureByDayBinding>
        (FragmentPictureByDayBinding::inflate) {

    private val APP_PREFERENCES = "settings"
    private val APP_PREFERENCES_TUTORIAL_KEY = "ShowTutorialState"

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var currentDate: Date
    private var retryIter: Int = 0
    private var zoom: Boolean = false
    private var snackBar: Snackbar? = null

    companion object {
        fun newInstance() = PictureByDayFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner) { state -> renderData(state) }
        currentDate = Date()
        initUi()
        viewModel.sendServerRequest(currentDate)

        val settings = requireContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        if(!settings.getBoolean(APP_PREFERENCES_TUTORIAL_KEY, false)) {
            createTutorial()
        }
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
                } else {
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

    private fun showNasaVideo(videoId: String) {
        lifecycle.addObserver(binding.youtubePlayerView)
        binding.youtubePlayerView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
    }

    private fun setPictureData(pictureByDayData: PictureByDayData) {
        if (pictureByDayData.mediaType == "video") {
            animationsVisibilityPictureByDay(binding.youtubePlayerView)
            showNasaVideo(extractYTId(pictureByDayData.url))
        } else {
            animationsVisibilityPictureByDay(binding.imageView)
            binding.imageView.load(pictureByDayData.hdurl)
        }
        binding.included.bottomSheetDescriptionHeader.text = pictureByDayData.title
        updateStyleDescriptionText(pictureByDayData.explanation)
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
            binding.imageView.scaleType =
                if (zoom) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.CENTER_INSIDE
        }

        setFont()
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
        TransitionManager.beginDelayedTransition(binding.container, transition)
        view.visibility = View.VISIBLE
    }

    private fun setFont() {
        val request = FontRequest(
            "com.google.android.gms.fonts",
            "com.google.android.gms",
            "name=Roboto&amp;weight=500",
            R.array.com_google_android_gms_fonts_certs
        )
        val callback = object : FontsContractCompat.FontRequestCallback() {
            override fun onTypefaceRetrieved(typeface: Typeface?) {
                binding.included.bottomSheetDescriptionHeader.typeface = typeface
                super.onTypefaceRetrieved(typeface)
            }

            override fun onTypefaceRequestFailed(reason: Int) {
                Toast.makeText(context, "Ошибка подгрузки шрифта${reason}", Toast.LENGTH_SHORT)
                    .show()
                super.onTypefaceRequestFailed(reason)
            }
        }
        FontsContractCompat.requestFont(
            requireContext(),
            request,
            callback,
            Handler(Looper.myLooper()!!)
        )

    }

    private fun updateStyleDescriptionText(text: String) {
        var textPosIndex = 0
        var currentSpan: CharacterStyle? = null
        val rnd = Random()
        val flag = SpannableString.SPAN_INCLUSIVE_INCLUSIVE
        val sizeText = text.length

        var spannableStringBuilder = SpannableStringBuilder(text)
        binding.included.bottomSheetDescription.setText(
            spannableStringBuilder,
            TextView.BufferType.EDITABLE
        )
        spannableStringBuilder =
            binding.included.bottomSheetDescription.text as SpannableStringBuilder

        val blurRadius = 5f
        val blurMaskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.SOLID)
        spannableStringBuilder.setSpan(MaskFilterSpan(blurMaskFilter), 0, sizeText / 4, flag)

        val fontSizeInPx = 30
        spannableStringBuilder.setSpan(
            AbsoluteSizeSpan(fontSizeInPx),
            sizeText / 4,
            2 * sizeText / 4,
            flag
        )

        val proportion = 2.0f
        spannableStringBuilder.setSpan(
            ScaleXSpan(proportion),
            2 * sizeText / 4,
            3 * sizeText / 4,
            flag
        )

        spannableStringBuilder.setSpan(UnderlineSpan(), 3 * sizeText / 4, sizeText, flag)


        object : CountDownTimer(9999999999999, 300) {
            override fun onTick(millisUntilFinished: Long) {
                currentSpan?.let { spannableStringBuilder.removeSpan(it) }
                textPosIndex++

                if (textPosIndex < spannableStringBuilder.length) {

                    val color =
                        Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                    currentSpan = ForegroundColorSpan(color)

                    spannableStringBuilder.setSpan(
                        currentSpan,
                        textPosIndex,
                        textPosIndex + 1,
                        flag
                    )
                } else {
                    textPosIndex = 0
                }
            }

            override fun onFinish() {
            }
        }.start()

    }

    private fun createTutorial() {

        fun createBuilder(view: View, title: String, description: String): GuideView.Builder {
            return GuideView.Builder(requireContext())
                .setTitle(title)
                .setContentText(description)
                .setGravity(Gravity.center)
                .setDismissType(DismissType.anywhere)
                .setTargetView(view)
                .setDismissType(DismissType.anywhere)
        }

        val builder = createBuilder(
            binding.chipToday,
            "Картинка сегодняшнего дня",
            "Необходимо выбрать, чтобы посмотреть картинку текущего дня"

        ).setGuideListener {
            val builder2 = createBuilder(
                binding.chipYesterday,
                "Картинка вчерашнего дня",
                "Необходимо выбрать, чтобы посмотреть картинку вчерашнего дня"

            ).setGuideListener {
                val builder3 = createBuilder(
                    binding.chipTwoDaysAgo,
                    "Картинка позавчерашнего дня",
                    "Необходимо выбрать, чтобы посмотреть картинку позавчерашнего дня"

                ).setGuideListener {
                    val settings = requireContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
                    settings.edit().putBoolean(APP_PREFERENCES_TUTORIAL_KEY, true).apply()
                }
                builder3.build().show()
            }
            builder2.build().show()
        }
        builder.build().show()
    }
}