package com.hwx.productcare.api.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Receipt(
    @SerializedName("ecashTotalSum")
    @Expose
    var ecashTotalSum: Long,
    @SerializedName("userInn")
    @Expose
    var userInn: String ,
    @SerializedName("items")
    @Expose
    var items: List<Item>,
    @SerializedName("requestNumber")
    @Expose
    var requestNumber: Long ,
    @SerializedName("provisionSum")
    @Expose
    var provisionSum: Long ,
    @SerializedName("fiscalSign")
    @Expose
    var fiscalSign: Long ,
    @SerializedName("fiscalDocumentNumber")
    @Expose
    var fiscalDocumentNumber: Long ,
    @SerializedName("operationType")
    @Expose
    var operationType: Long ,
    @SerializedName("taxationType")
    @Expose
    var taxationType: Long ,
    @SerializedName("messageFiscalSign")
    @Expose
    var messageFiscalSign: Long ,
    @SerializedName("fiscalDriveNumber")
    @Expose
    var fiscalDriveNumber: String ,
    @SerializedName("machineNumber")
    @Expose
    var machineNumber: String ,
    @SerializedName("cashTotalSum")
    @Expose
    var cashTotalSum: Long ,
    @SerializedName("LongernetSign")
    @Expose
    var LongernetSign: Long ,
    @SerializedName("shiftNumber")
    @Expose
    var shiftNumber: Long ,
    @SerializedName("operator")
    @Expose
    var operator: String ,
    @SerializedName("retailPlace")
    @Expose
    var retailPlace: String? ,
    @SerializedName("dateTime")
    @Expose
    var dateTime: String ,
    @SerializedName("ndsNo")
    @Expose
    var ndsNo: Long ,
    @SerializedName("totalSum")
    @Expose
    var totalSum: Long ,
    @SerializedName("fiscalDocumentFormatVer")
    @Expose
    var fiscalDocumentFormatVer: Long ,
    @SerializedName("rawData")
    @Expose
    var rawData: String ,
    @SerializedName("prepaidSum")
    @Expose
    var prepaidSum: Long ,
    @SerializedName("paymentAgentType")
    @Expose
    var paymentAgentType: Long ,
    @SerializedName("sellerAddress")
    @Expose
    var sellerAddress: String ,
    @SerializedName("propertiesUser")
    @Expose
    var propertiesUser: PropertiesUser ,
    @SerializedName("fnsSite")
    @Expose
    var fnsSite: String ,
    @SerializedName("user")
    @Expose
    var user: String ,
    @SerializedName("receiptCode")
    @Expose
    var receiptCode: Long ,
    @SerializedName("creditSum")
    @Expose
    var creditSum: Long ,
    @SerializedName("kktRegId")
    @Expose
    var kktRegId: String ,
    @SerializedName("buyerAddress")
    @Expose
    var buyerAddress: String
)