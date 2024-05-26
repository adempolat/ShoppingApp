package com.adempolat.eterationshoppingapp.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CartItemEntity::class,FavoriteItemEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun favoriteDao(): FavoriteDao
}