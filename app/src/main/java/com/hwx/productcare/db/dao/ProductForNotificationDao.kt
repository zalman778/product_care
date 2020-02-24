package com.hwx.productcare.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hwx.productcare.db.entity.ProductForNotification


@Dao
interface ProductForNotificationDao {

    @Query("select * from product_for_notification where idNotification in (:ids)")
    fun findByNotificationIdsList(ids: List<Long>): List<ProductForNotification>

    @Query("DELETE FROM product_for_notification WHERE idNotification in (:ids)")
    fun deleteByNotificationId(ids: List<Long>)

    @Insert
    fun insert(vararg obj: ProductForNotification)

}