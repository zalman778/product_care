package com.hwx.productcare.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hwx.productcare.db.dao.*
import com.hwx.productcare.db.entity.*


@Database(entities = [StoreReceipt::class, StoreReceiptItem::class, NotificationItem::class, ProductForNotification::class], version = 1)
@TypeConverters(value = [DateConverter::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun receiptDao(): StoreReceiptDao
    abstract fun receiptItemDao(): StoreReceiptItemDao
    abstract fun notificationItemDao(): NotificationItemDao
    abstract fun productForNotificationDao(): ProductForNotificationDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context): AppDatabase {

            val db = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "product_care.db"
            )
            .build()

            return db
        }
    }

}