package com.example.nasa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.nasa.R
import com.example.nasa.ui.main.PictureByDayFragment

class MainActivity : AppCompatActivity(), NavToolBar{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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