package com.example.nasa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.nasa.R
import com.example.nasa.databinding.ActivityMainBinding
import com.example.nasa.ui.animations.NavigationAnimationsFragment
import com.example.nasa.ui.home.PictureByDayFragment
import com.example.nasa.ui.mars.NavigationFragment
import com.example.nasa.ui.nebula.NebulaFragment
import com.example.nasa.ui.recycler.RecyclerFragment
import com.example.nasa.ui.settings.SettingsFragment
import com.example.nasa.utils.getCurrentDayNightMode
import com.example.nasa.utils.getCurrentTheme
import com.example.nasa.utils.getDayNightMode
import com.example.nasa.utils.getRealStyle

class MainActivity : AppCompatActivity(){
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getRealStyle(getCurrentTheme(this)))
        AppCompatDelegate.setDefaultNightMode(getDayNightMode(getCurrentDayNightMode(this)))

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            navigationTo(RecyclerFragment.newInstance())
        }
        initBottomNavigationView()
    }

    private fun initBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_view_home -> {
                    navigationTo(RecyclerFragment.newInstance())
                    true
                }
                R.id.bottom_view_mars -> {
                    navigationTo(NavigationFragment.newInstance())
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
        supportFragmentManager.beginTransaction().replace(R.id.container, f).commit()
    }
}