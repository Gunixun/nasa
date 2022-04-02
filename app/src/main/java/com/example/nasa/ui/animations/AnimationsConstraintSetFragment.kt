package com.example.nasa.ui.animations

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import com.example.nasa.R
import com.example.nasa.databinding.FragmentAminationsConstraintSetBinding
import com.example.nasa.ui.BaseFragment

class AnimationsConstraintSetFragment :
    BaseFragment<FragmentAminationsConstraintSetBinding>(FragmentAminationsConstraintSetBinding::inflate) {

    private var showDescription = false

    companion object {
        fun newInstance() = AnimationsConstraintSetFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backgroundImage.setOnClickListener {
            animationsConstraintSet()
        }
    }

    private fun animationsConstraintSet() {
        showDescription = !showDescription
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.constraintContainer)

        val changeBounds = ChangeBounds()
        changeBounds.interpolator = AnticipateOvershootInterpolator(2.0f)
        changeBounds.duration = 1000L

        TransitionManager.beginDelayedTransition(binding.constraintContainer, changeBounds)
        if (showDescription) {
            constraintSet.connect(
                R.id.title,
                ConstraintSet.END,
                R.id.constraint_container,
                ConstraintSet.END
            )
            constraintSet.connect(
                R.id.title,
                ConstraintSet.START,
                R.id.constraint_container,
                ConstraintSet.START
            )

            constraintSet.connect(
                R.id.description,
                ConstraintSet.BOTTOM,
                R.id.constraint_container,
                ConstraintSet.BOTTOM
            )
            constraintSet.clear(R.id.description, ConstraintSet.TOP)
            constraintSet.applyTo(binding.constraintContainer)
        } else {
            constraintSet.connect(
                R.id.title,
                ConstraintSet.END,
                R.id.backgroundImage,
                ConstraintSet.START
            )

            constraintSet.connect(
                R.id.description,
                ConstraintSet.TOP,
                R.id.constraint_container,
                ConstraintSet.BOTTOM
            )

            constraintSet.clear(R.id.title, ConstraintSet.START)
            constraintSet.clear(R.id.description, ConstraintSet.BOTTOM)
            constraintSet.applyTo(binding.constraintContainer)
        }
    }
}