package com.hwx.productcare.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hwx.productcare.model.Receipt
import java.util.*

@Entity(tableName = "store_receipt")
data class StoreReceipt(

    @PrimaryKey(autoGenerate = true)
    var id: Long?,

    @ColumnInfo(name = "fiscal_number")
    var fiscalNumber: Long,

    @ColumnInfo(name = "fiscal_document")
    var fiscalDocument: Long,

    @ColumnInfo(name = "fiscal_mark")
    var fiscalMark: Long,

    @ColumnInfo(name = "date")
    var date: Date,

    @ColumnInfo(name = "summ")
    var summ: Float,

    @ColumnInfo(name = "shop_title")
    var shopTitle: String

) {
    constructor(receipt: Receipt): this(null, receipt.fiscalNumber, receipt.fiscalDocument, receipt.fiscalMark, receipt.date, receipt.summ, receipt.shopTitle.orEmpty())

    fun toModel(): Receipt {
        return Receipt(id, fiscalNumber, fiscalDocument, fiscalMark, date, summ, shopTitle)
    }
}