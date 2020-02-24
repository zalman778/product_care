package com.hwx.productcare.db.entity

import androidx.room.Entity

@Entity(
      tableName = "product_for_notification"
    , primaryKeys = ["idNotification", "idProduct"]
)
data class ProductForNotification(
    var idNotification: Long,
    var idProduct: Long
)