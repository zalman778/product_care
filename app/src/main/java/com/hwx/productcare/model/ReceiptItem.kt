package com.hwx.productcare.model

import java.util.*

data class ReceiptItem(
    var id: Long?,
    var name: String,
    var nds: Int,
    var sum: Double,
    var quantity: Double,
    var price: Int,
    var expiryDate: Date?
)