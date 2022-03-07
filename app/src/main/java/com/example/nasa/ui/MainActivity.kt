package com.example.nasa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.example.nasa.R
import com.example.nasa.ui.main.PictureByDayFragment
import com.example.nasa.utils.getCurrentDayNightMode
import com.example.nasa.utils.getCurrentTheme
import com.example.nasa.utils.getDayNightMode
import com.example.nasa.utils.getRealStyle

class MainActivity : AppCompatActivity(), NavToolBar{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getRealStyle(getCurrentTheme(this)))
        AppCompatDelegate.setDefaultNightMode(getDayNightMode(getCurrentDayNightMode(this)));
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.container,
                PictureByDayFragment.newInstance()
            ).commit()
        }
    }

    override fun supplyToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }
}