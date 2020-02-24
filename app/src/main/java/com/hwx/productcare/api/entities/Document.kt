package com.hwx.productcare.api.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Document(
    @SerializedName("receipt")
    @Expose
    var receipt: Receipt
)