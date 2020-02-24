package com.hwx.productcare.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hwx.productcare.db.entity.StoreReceipt
import com.hwx.productcare.model.Receipt


@Dao
interface StoreReceiptDao {

    @Query("SELECT * FROM store_receipt WHERE id=:id")
    fun get(id: Int): LiveData<StoreReceipt>

    @Query("SELECT * FROM store_receipt")
    fun getAll(): LiveData<List<StoreReceipt>>

    @Query("SELECT * FROM store_receipt WHERE fiscal_number = :fn and fiscal_document = :fd and fiscal_mark = :fp LIMIT 1")
    fun lookUp(fn: Long, fd: Long, fp: Long): StoreReceipt?

    @Query("DELETE FROM store_receipt")
    fun clearTable()


    @Insert
    fun insert(receipt: StoreReceipt): Long

    @Query("DELETE FROM store_receipt WHERE id = :id")
    fun deleteById(id: Long)

    @Query("DELETE FROM store_receipt WHERE fiscal_document = :fd and fiscal_mark = :fm and fiscal_number = :fn")
    fun deleteByParams(fd: Long, fm: Long, fn: Long)

    @Update
    fun update(vararg todos: StoreReceipt)
}