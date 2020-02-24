package com.hwx.productcare.db.entity

import androidx.room.*
import com.hwx.productcare.model.Receipt
import com.hwx.productcare.model.ReceiptItem
import java.util.*

@Entity(
        tableName = "store_receipt_item"
        , foreignKeys = [ForeignKey(
            entity = StoreReceipt::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("id_receipt"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )],
        indices= [Index(value = arrayOf("id", "id_receipt"))]
)
class StoreReceiptItem (

        @PrimaryKey(autoGenerate = true)
        var id: Long?,

        @ColumnInfo(name = "id_receipt")
        var idReceipt: Int?,

        var name: String,

        var nds: Int,

        var sum: Double,

        var quantity: Double,

        @ColumnInfo(name = "payment_type")
        var paymentType: Int,

        var price: Int,

        @ColumnInfo(name = "expiry_date")
        var expiryDate: Date?
) {

    constructor(receiptItem: ReceiptItem): this(receiptItem.id, null, receiptItem.name, receiptItem.nds, receiptItem.sum, receiptItem.quantity, 0, receiptItem.price, receiptItem.expiryDate)

    fun toModel(): ReceiptItem {
        return ReceiptItem(id, name, nds, sum, quantity, price, expiryDate)
    }
}