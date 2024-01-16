package com.example.universityapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.universityapp.data.db.newsDao.NewsDao
import com.example.universityapp.data.db.userDao.UserDao
import com.example.universityapp.data.entity.NewsItem
import com.example.universityapp.data.entity.StudentDB

@Database(entities = [NewsItem::class, StudentDB::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )//.addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Создайте новую таблицу с новой схемой
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `user_new` (" +
                            "`Id` TEXT NOT NULL, " +
                            "`FIO` TEXT, " +
                            "`Photo` TEXT, " +
                            "`Email` TEXT, " +
                            "`StudentCod` TEXT, " +
                            "`TeacherCod` TEXT, " +
                            "`BirthDate` TEXT, " +
                            "PRIMARY KEY(`Id`))"
                )

                // Переносите данные из старой таблицы в новую
                database.execSQL(
                    "INSERT INTO `user_new` (`Id`, `FIO`, `Photo`, `Email`, `StudentCod`, `TeacherCod`, `BirthDate`) " +
                            "SELECT `Id`, `FIO`, `Photo`, null, null, null, null FROM `user`"
                )

                // Удалите старую таблицу
                database.execSQL("DROP TABLE `user`")

                // Переименуйте новую таблицу обратно в `user`
                database.execSQL("ALTER TABLE `user_new` RENAME TO `user`")
            }
        }
    }
}

