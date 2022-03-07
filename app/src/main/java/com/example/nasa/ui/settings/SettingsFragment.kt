package com.example.nasa.ui.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.nasa.R
import com.example.nasa.databinding.FragmentSettingsBinding
import com.example.nasa.ui.BaseFragment
import com.example.nasa.utils.*

val KEY_SETTINGS_LOCAL = "setting_local"
val KEY_CURRENT_THEME = "current_theme"

val KEY_CURRENT_DAY_NIGHT_MODE = "current_day_night_mode"

class SettingsFragment :
    BaseFragment<FragmentSettingsBinding>
        (FragmentSettingsBinding::inflate), View.OnClickListener {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.radioButtonDef.setOnClickListener(this)
        binding.radioButtonBlue.setOnClickListener(this)
        binding.radioButtonRed.setOnClickListener(this)
        binding.radioButtonGreen.setOnClickListener(this)
        binding.radioButtonDefMode.setOnClickListener(this)
        binding.radioButtonDay.setOnClickListener(this)
        binding.radioButtonNight.setOnClickListener(this)

        when (getCurrentTheme(requireActivity())) {
            Theme.Base -> binding.RadioGroup.check(R.id.radioButtonDef)
            Theme.Red -> binding.RadioGroup.check(R.id.radioButtonRed)
            Theme.Blue -> binding.RadioGroup.check(R.id.radioButtonBlue)
            Theme.Green -> binding.RadioGroup.check(R.id.radioButtonGreen)
        }
        when (getCurrentDayNightMode(requireActivity())) {
            DayNightMode.Base -> binding.RadioGroupDayNight.check(R.id.radioButtonDefMode)
            DayNightMode.Day -> binding.RadioGroupDayNight.check(R.id.radioButtonDay)
            DayNightMode.Night -> binding.RadioGroupDayNight.check(R.id.radioButtonNight)
        }
    }

    private fun setCurrentThemeLocal(currentTheme: Theme) {
        val sharedPreferences = requireActivity()
            .getSharedPreferences(KEY_SETTINGS_LOCAL, AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_CURRENT_THEME, currentTheme.value)
        editor.apply()
    }

    private fun setCurrentDayNightMode(mode: DayNightMode) {
        val sharedPreferences = requireActivity()
            .getSharedPreferences(KEY_SETTINGS_LOCAL, AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_CURRENT_DAY_NIGHT_MODE, mode.value)
        editor.apply()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.radioButtonDef -> {
                setCurrentThemeLocal(Theme.Base)

            }
            R.id.radioButtonRed -> {
                setCurrentThemeLocal(Theme.Red)
                activity?.recreate()
            }
            R.id.radioButtonBlue -> {
                setCurrentThemeLocal(Theme.Blue)
                activity?.recreate()
            }
            R.id.radioButtonGreen -> {
                setCurrentThemeLocal(Theme.Green)
                activity?.recreate()
            }
        }
        when (v.id) {

            R.id.radioButtonDefMode -> {
                setCurrentDayNightMode(DayNightMode.Base)
                AppCompatDelegate.setDefaultNightMode(getDayNightMode(DayNightMode.Base));
            }
            R.id.radioButtonDay -> {
                setCurrentDayNightMode(DayNightMode.Day)
                AppCompatDelegate.setDefaultNightMode(getDayNightMode(DayNightMode.Day));
            }
            R.id.radioButtonNight -> {
                setCurrentDayNightMode(DayNightMode.Night)
                AppCompatDelegate.setDefaultNightMode(getDayNightMode(DayNightMode.Night));
            }
        }

    }
}