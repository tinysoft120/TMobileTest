package com.tinysoft.tmobiletest.ui

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.navOptions
import com.tinysoft.tmobiletest.MainActivity
import com.tinysoft.tmobiletest.R

/**
 * Base Fragment in main activity
 * */
abstract class AbsMainFragment(@LayoutRes layout: Int) : Fragment(layout) {

    val mainActivity get() = activity as MainActivity
}