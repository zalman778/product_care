package com.hwx.productcare.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hwx.productcare.db.entity.NotificationItem
import java.util.*


@Dao
interface NotificationItemDao {
    @Query("SELECT * FROM notification_item")
    fun getAll(): List<NotificationItem>

    @Query("SELECT * FROM notification_item where date >= :dateBegin and date <= :dateEnd")
    fun findByDatePeriod(dateBegin: Date, dateEnd: Date): List<NotificationItem>

    @Update
    fun update(toUpdateList: List<NotificationItem>): Int

    @Insert
    fun insert(vararg obj: NotificationItem): List<Long>

    @Query("select t.* from " +
            "notification_item t," +
            "product_for_notification pn " +
            "where pn.idProduct in (:ids)" +
            "  and pn.idNotification = t.id")
    fun findByReceiptItemsListIds(ids: List<Long>): List<NotificationItem>

    @Query("DELETE FROM notification_item WHERE id in (:ids)")
    fun deleteByIdsList(ids: List<Long>)
}