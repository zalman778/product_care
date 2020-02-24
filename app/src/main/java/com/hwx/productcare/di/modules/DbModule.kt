package com.hwx.productcare.di.modules

import android.content.Context
import androidx.room.Room
import com.hwx.productcare.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule  {

    @Provides
    @Singleton
    fun provideDb(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "product_care.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideStoreReceiptDao(appDatabase: AppDatabase) =
        appDatabase.receiptDao()

    @Provides
    @Singleton
    fun provideStoreReceiptItemDao(appDatabase: AppDatabase) =
        appDatabase.receiptItemDao()

    @Provides
    @Singleton
    fun provideNotificationItemDao(appDatabase: AppDatabase) =
        appDatabase.notificationItemDao()

    @Provides
    @Singleton
    fun provideProductForNotificationDao(appDatabase: AppDatabase) =
        appDatabase.productForNotificationDao()



//    @Provides
//    @Singleton
//    fun provideDb(context: Context): AppDatabase {
//        return Room.databaseBuilder(
//            context,
//            AppDatabase::class.java, "product_care.db"
//        ).build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideSanpinCategoryDao(appDatabase: AppDatabase) =
//        appDatabase.sanpinCategoryDao()
//
//    @Provides
//    @Singleton
//    fun provideSanpinGroupDao(appDatabase: AppDatabase) =
//        appDatabase.sanpinGroupDao()
//
//    @Provides
//    @Singleton
//    fun provideSanpinProductDao(appDatabase: AppDatabase) =
//        appDatabase.sanpinProductDao()
//
//    @Provides
//    @Singleton
//    fun provideSanpinGroupProductDao(appDatabase: AppDatabase) =
//        appDatabase.sanpinGroupProductDao()

}