package com.hwx.productcare.api.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PropertiesUser(
    @SerializedName("propertyName")
    @Expose
    var propertyName: String ,

    @SerializedName("propertyValue")
    @Expose
    var propertyValue: String
)