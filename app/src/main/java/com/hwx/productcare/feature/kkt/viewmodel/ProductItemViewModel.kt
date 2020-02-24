package com.hwx.productcare.feature.kkt.viewmodel

import android.app.DatePickerDialog
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hwx.productcare.R
import com.hwx.productcare.util.DateUtil
import java.util.*
import javax.inject.Inject

class ProductItemViewModel @Inject constructor(
      private val onClick: (Boolean) -> Unit
    , private val datePickerCallback: ((Date) -> Unit)
) : ViewModel() {

    private var mlvItemExpanded = MutableLiveData<Boolean>(false)
    private var mlvExpiryDate = MutableLiveData<String>("")
    val lvExpiryDate: MutableLiveData<String> = mlvExpiryDate

    fun onExpandBtnClick(view: View) {
        mlvItemExpanded.value = !(mlvItemExpanded.value!!)
        onClick(mlvItemExpanded.value!!)
    }

    fun onDatePickBtnClick(view: View) {
        // Get Current Date
        val calender = Calendar.getInstance()
        var mYear = calender.get(Calendar.YEAR)
        var mMonth = calender.get(Calendar.MONTH)
        var mDay = calender.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(view.context, R.style.DatePickerDialog,
            DatePickerDialog.OnDateSetListener {
                    _, year, monthOfYear, dayOfMonth -> run {
                val calendar = Calendar.getInstance()
                calendar.set(year, monthOfYear, dayOfMonth, 0, 0)
                datePickerCallback(calendar.time)

            }
            }, mYear, mMonth, mDay)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()

    }

    fun updateExpiryDate(pExpiryDate: Date) {
        mlvExpiryDate.value = DateUtil.toString(pExpiryDate, DateUtil.RECEIPT_ITEM_FORMAT)
    }

}