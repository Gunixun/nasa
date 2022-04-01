package com.example.nasa.ui.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.example.nasa.databinding.FragmentAnimationsRotateBinding
import com.example.nasa.ui.BaseFragment

class AnimationRotateFragment :
    BaseFragment<FragmentAnimationsRotateBinding>(FragmentAnimationsRotateBinding::inflate) {

    private var openMenuState: Boolean = false
    private val duration = 1000L

    companion object {
        fun newInstance() = AnimationRotateFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener {
            animationsVisibilityMenu()
        }
    }

    private fun animationsVisibilityMenu() {
        openMenuState = !openMenuState
        var alfaMenu = 0f
        var alfaBackground = 0f
        var clickableState = false
        var rotationDegreesStart = 360f
        var rotationDegreesEnd = 0f

        var movePosStart = -130f
        var movePosEnd = -20f

        if (openMenuState) {
            rotationDegreesStart =
                rotationDegreesEnd.also { rotationDegreesEnd = rotationDegreesStart }
            movePosStart = movePosEnd.also { movePosEnd = movePosStart }

            alfaMenu = 1f
            alfaBackground = 0.5f
            clickableState = true
        }

        ObjectAnimator.ofFloat(
            binding.optionOneContainer,
            View.TRANSLATION_Y,
            2 * movePosStart,
            2 * movePosEnd
        ).setDuration(duration).start()
        ObjectAnimator.ofFloat(
            binding.optionTwoContainer,
            View.TRANSLATION_Y,
            movePosStart,
            movePosEnd
        ).setDuration(duration).start()

        ObjectAnimator.ofFloat(
            binding.fab,
            View.ROTATION_Y,
            rotationDegreesStart,
            rotationDegreesEnd
        ).setDuration(duration).start()

        fun animateMenuContainer(container: LinearLayout) {
            container.animate()
                .alpha(alfaMenu)
                .setDuration(2 * duration / 3)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        container.isClickable = clickableState
                    }
                })
        }

        animateMenuContainer(binding.optionOneContainer)
        animateMenuContainer(binding.optionTwoContainer)

        binding.transparentBackground.animate()
            .alpha(alfaBackground)
            .setDuration(duration)
    }


}