package com.tinysoft.tmobiletest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tinysoft.tmobiletest.databinding.ActivityMainBinding
import com.tinysoft.tmobiletest.ui.homelist.HomeListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeListFragment.newInstance())
                .commitNow()
        }
    }
}