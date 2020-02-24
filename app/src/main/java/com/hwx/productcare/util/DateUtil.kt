package com.hwx.productcare.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun fromString(value: String, formatter: SimpleDateFormat): Date {
        return formatter.parse(value)!!
    }

    fun toString(date: Date?, formatter: SimpleDateFormat): String {
        return if (date != null) formatter.format(date) else ""
    }

    //20200126T161500
    val RECEIPT_DATE_FORMAT = SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault())

    val RECEIPT_ITEM_FORMAT =  SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    val BEAUTY_RECEIPT_DATE_FORMAT = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

}