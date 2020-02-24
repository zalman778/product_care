package com.hwx.productcare.util

import com.hwx.productcare.model.ReceiptItem
import org.joda.time.DateTime

object ReceiptItemUtil {
    fun createReceiptItem() = ReceiptItem(1, "Test name", 10, 100.0, 1.0, 100, EXPIRY_DATE_TIME.toDate())



    val EXPIRY_DATE_TIME = DateTime().withDate(2020, 2, 5).withTime(0, 0, 0, 0)
}