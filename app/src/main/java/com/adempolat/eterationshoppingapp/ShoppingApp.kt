package com.adempolat.eterationshoppingapp

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.adempolat.eterationshoppingapp.data.dao.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShoppingApp : Application() {

    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "shopping_app_database").build()
    }
}