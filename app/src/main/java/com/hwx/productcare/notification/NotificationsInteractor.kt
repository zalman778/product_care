package com.hwx.productcare.notification

/**
 * Consumer of application events, connected to Products:
 *
 * Possible events:
 * changed setting - default notification time
 * changed setting - notification frequency time
 * changed setting - night mode
 * added new receipt
 * changed expiry date of product in any receipt
 * disabled notifications for whole receipt
 * disabled notifications for product
 */
interface NotificationsInteractor {

    /**
     * Clears all changed settings keys
     */
    fun saveFirstPrefsState()

    /**
     * Handles settings changes, if they exists
     */
    suspend fun handleChangedSetting()

    /**
     * Handled new added receipt:
     */
    suspend fun handleInsertedReceiptItems(insertedIdsList: List<Long>)

    suspend fun handleDisabledNotificationsForReceipt(receiptId: Long)

    suspend fun handleDisabledNotificationsForReceiptItem(receiptItemId: Long)
}