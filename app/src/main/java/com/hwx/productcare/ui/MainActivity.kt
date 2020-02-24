package com.hwx.productcare.ui

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import com.hwx.productcare.R
import dagger.android.support.DaggerAppCompatActivity


class MainActivity : DaggerAppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    fun updateStatusBarColor(color: Int) {// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }


}
