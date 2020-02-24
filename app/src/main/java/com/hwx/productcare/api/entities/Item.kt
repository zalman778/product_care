package com.hwx.productcare.api.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.hwx.productcare.model.ReceiptItem

data class Item(
    @SerializedName("name")
    @Expose
    var name: String ,
    @SerializedName("nds")
    @Expose
    var nds: Int ,
    @SerializedName("sum")
    @Expose
    var sum: Double ,
    @SerializedName("quantity")
    @Expose
    var quantity: Double ,
    @SerializedName("paymentType")
    @Expose
    var paymentType: Int ,
    @SerializedName("price")
    @Expose
    var price: Int
) {
    fun toModel(): ReceiptItem {
        return ReceiptItem(
            null, name, nds, sum, quantity, price, null
        )
    }
}