package com.hwx.productcare.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hwx.productcare.db.entity.StoreReceiptItem


@Dao
interface StoreReceiptItemDao {

    @Query("SELECT * FROM store_receipt_item WHERE id=:id")
    fun loadSingle(id: Int): LiveData<StoreReceiptItem>

    @Query("select * from store_receipt_item where id in (:ids)")
    fun findByIdsList(ids: List<Long>): List<StoreReceiptItem>

    @Query("SELECT * FROM store_receipt_item WHERE id_receipt=:receiptId")
    fun findLiveByReceiptId(receiptId: Long): LiveData<List<StoreReceiptItem>>

    @Query("SELECT * FROM store_receipt_item WHERE id_receipt=:receiptId")
    fun findByReceiptId(receiptId: Long): List<StoreReceiptItem>

    @Query("DELETE FROM store_receipt_item")
    fun clearTable()

    @Insert
    fun insertAll(vararg todo: StoreReceiptItem): List<Long>

    @Delete
    fun delete(todo: StoreReceiptItem)

    @Update
    fun update(vararg todos: StoreReceiptItem)

    @Query("select distinct t.* " +
            "from " +
            "store_receipt_item t," +
            "product_for_notification pn " +
            "where pn.idProduct = t.id" +
            "  and pn.idNotification in (:ids)")
    fun findByNotificationsIdsList(ids: List<Long>): List<StoreReceiptItem>
}