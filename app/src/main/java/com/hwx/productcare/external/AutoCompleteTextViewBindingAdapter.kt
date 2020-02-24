package com.hwx.productcare.external

/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.databinding.adapters.AdapterViewBindingAdapter.OnItemSelected
import androidx.databinding.adapters.AdapterViewBindingAdapter.OnItemSelectedComponentListener
import androidx.databinding.adapters.AdapterViewBindingAdapter.OnNothingSelected
import android.widget.AutoCompleteTextView
import android.widget.AutoCompleteTextView.Validator

@BindingMethods(
    BindingMethod(
        type = AutoCompleteTextView::class,
        attribute = "android:completionThreshold",
        method = "setThreshold"
    ),
    BindingMethod(
        type = AutoCompleteTextView::class,
        attribute = "android:popupBackground",
        method = "setDropDownBackgroundDrawable"
    ),
    BindingMethod(
        type = AutoCompleteTextView::class,
        attribute = "android:onDismiss",
        method = "setOnDismissListener"
    ),
    BindingMethod(
        type = AutoCompleteTextView::class,
        attribute = "android:onItemClick",
        method = "setOnItemClickListener"
    )
)
object AutoCompleteTextViewBindingAdapter {

    @BindingAdapter(value = [ "android:fixText", "android:isValid" ], requireAll = false)
    fun setValidator(
        view: AutoCompleteTextView, fixText: FixText?,
        isValid: IsValid?
    ) {
        if (fixText == null && isValid == null) {
            view.validator = null
        } else {
            view.validator = object : Validator {
                override fun isValid(text: CharSequence): Boolean {
                    return isValid?.isValid(text) ?: true
                }

                override fun fixText(invalidText: CharSequence): CharSequence {
                    return fixText?.fixText(invalidText) ?: invalidText
                }
            }
        }
    }

    @BindingAdapter(
        value = [ "android:onItemSelected", "android:onNothingSelected" ],
        requireAll = false
    )
    fun setOnItemSelectedListener(
        view: AutoCompleteTextView,
        selected: OnItemSelected?, nothingSelected: OnNothingSelected?
    ) {
        if (selected == null && nothingSelected == null) {
            view.onItemSelectedListener = null
        } else {
            view.onItemSelectedListener =
                OnItemSelectedComponentListener(selected, nothingSelected, null)
        }
    }

    interface IsValid {
        fun isValid(text: CharSequence): Boolean
    }

    interface FixText {
        fun fixText(invalidText: CharSequence): CharSequence
    }
}