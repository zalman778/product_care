package com.hwx.productcare.external

import android.view.View
import android.view.animation.Animation
import android.view.ViewGroup
import android.view.animation.Transformation


object Animations {
    fun toggleView(view: View, isExpanded: Boolean): Boolean {

        return if (isExpanded) {
            view.animate().setDuration(200).rotation(180F)
            true
        } else {
            view.animate().setDuration(200).rotation(0F)
            false
        }
    }

    fun expand(view: View) {
        val animation =
            expandAction(view)
        view.startAnimation(animation)
    }

    private fun expandAction(view: View): Animation {

        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val actualheight = view.measuredHeight

        view.layoutParams.height = 0
        view.visibility = View.VISIBLE

        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {

                view.layoutParams.height = if (interpolatedTime == 1f)
                    ViewGroup.LayoutParams.WRAP_CONTENT
                else
                    (actualheight * interpolatedTime).toInt()
                view.requestLayout()
            }
        }


        animation.duration = (actualheight / view.context.resources.displayMetrics.density).toLong()

        view.startAnimation(animation)

        return animation


    }

    fun collapse(view: View) {

        val actualHeight = view.measuredHeight

        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {

                if (interpolatedTime == 1f) {
                    view.visibility = View.GONE
                } else {
                    view.layoutParams.height = actualHeight - (actualHeight * interpolatedTime).toInt()
                    view.requestLayout()

                }
            }
        }

        animation.duration = (actualHeight / view.context.resources.displayMetrics.density).toLong()
        view.startAnimation(animation)
    }
}