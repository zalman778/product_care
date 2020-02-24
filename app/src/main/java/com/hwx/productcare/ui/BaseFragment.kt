package com.hwx.productcare.ui

import android.text.Html
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.hwx.productcare.R
import dagger.android.support.DaggerFragment

open class BaseFragment: DaggerFragment() {

    private var snackbar: Snackbar? = null

    fun showErrorWithAction(view: View, message: String, action: () -> Unit) {
        val htmlText = Html.fromHtml("<font color=\"#ffffff\">$message</font>")
        snackbar = Snackbar
            .make(view, htmlText, Snackbar.LENGTH_INDEFINITE)
            .apply {
                setAction(view.context.getString(R.string.retry_value)) { action() }
                show()
            }
    }

    fun hideErrorIfShowed() {
        snackbar?.dismiss()
    }
}