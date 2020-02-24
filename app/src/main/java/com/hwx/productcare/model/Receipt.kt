package com.hwx.productcare.model


import java.util.*

data class Receipt(
    var id: Long?,
    var fiscalNumber: Long,
    var fiscalDocument: Long,
    var fiscalMark: Long,
    var date: Date,
    var summ: Float,
    var shopTitle: String?
)