package com.hwx.productcare.feature.kkt.domain

import com.hwx.productcare.model.Receipt
import com.hwx.productcare.util.DateUtil
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.*

/**
 * Base interactor funcitonality for KKT QR processing
 */
interface BaseKktQrReaderInteractor {

    fun parseScannedCode(scannedCode: String): Receipt {
        val stream = ByteArrayInputStream(scannedCode.replace("&", "\n").toByteArray())
        val properties = Properties()
        try {
            properties.load(stream)
        } catch (e: IOException) {
            throw IllegalArgumentException(e)
        }
        val fn = properties.getProperty("fn").toLong()
        val fd = properties.getProperty("i").toLong()
        val fp = properties.getProperty("fp").toLong()
        val date = DateUtil.fromString(properties.getProperty("t"), DateUtil.RECEIPT_DATE_FORMAT)
        val summ = properties.getProperty("s").replace("\\.", "").toFloat()

        return Receipt(null, fn, fd, fp, date, summ, null)
    }

}