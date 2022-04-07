package com.example.nasa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.nasa.R
import com.example.nasa.databinding.ActivityMainBinding
import com.example.nasa.ui.animations.NavigationAnimationsFragment
import com.example.nasa.ui.home.PictureByDayFragment
import com.example.nasa.ui.mars.NavigationFragment
import com.example.nasa.ui.nebula.NebulaFragment
import com.example.nasa.ui.settings.SettingsFragment
import com.example.nasa.ui.ux_examples.NavigationUxFragment
import com.example.nasa.utils.getCurrentDayNightMode
import com.example.nasa.utils.getCurrentTheme
import com.example.nasa.utils.getDayNightMode
import com.example.nasa.utils.getRealStyle
import com.example.nasa.view_model.PictureByDayViewModel
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener

class MainActivity : AppCompatActivity(){
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getRealStyle(getCurrentTheme(this)))
        AppCompatDelegate.setDefaultNightMode(getDayNightMode(getCurrentDayNightMode(this)))

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            binding.bottomNavigationView.isVisible = false
            navigationTo(SplashFragment.newInstance())

            Handler(Looper.myLooper()!!).postDelayed({
                navigationTo(PictureByDayFragment.newInstance())
                binding.bottomNavigationView.isVisible = true

            }, 5000L)
        }
        initBottomNavigationView()
    }

    private fun initBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_view_home -> {
                    navigationTo(PictureByDayFragment.newInstance())
                    true
                }
                R.id.bottom_view_mars -> {
                    navigationTo(NavigationUxFragment.newInstance())
                    true
                }
                R.id.bottom_view_moon -> {
                    navigationTo(NavigationAnimationsFragment.newInstance())
                    true
                }
                R.id.bottom_view_nebula -> {
                    navigationTo(NebulaFragment.newInstance())
                    true
                }
                R.id.bottom_view_settings -> {
                    navigationTo(SettingsFragment.newInstance())
                    true
                }
                else -> true
            }
        }
    }

    private fun navigationTo(f: Fragment) {
        supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in,
            R.anim.push_up_out,
            R.anim.push_up_in,
            R.anim.slide_out
        ).replace(R.id.container, f).commit()
    }
}