package com.hwx.productcare.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hwx.productcare.model.Notification
import com.hwx.productcare.model.Receipt
import java.util.*

@Entity(tableName = "notification_item")
data class NotificationItem (

    @PrimaryKey
    var id: Long?, //requestCode in peding intent

    var text: String,

    var date: Date
) {

    constructor(notification: Notification): this(notification.id, notification.text, notification.date)

    fun toModel(): Notification {
        return Notification(id, text, date)
    }
}