package com.example.universityapp.data.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.universityapp.data.db.newsDao.NewsDao
import com.example.universityapp.data.db.scheduleDao.ScheduleDao
import com.example.universityapp.data.db.userDao.UserDao
import com.example.universityapp.data.entity.NewsItem
import com.example.universityapp.data.entity.ScheduleItemDB
import com.example.universityapp.data.entity.StudentDB
import java.util.concurrent.Executors

@Database(entities = [NewsItem::class, StudentDB::class, ScheduleItemDB::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun userDao(): UserDao
    abstract fun scheduleDao(): ScheduleDao


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
                ).setQueryCallback({ sqlQuery, bindArgs ->
                    Log.d("RoomQuery", "SQL Query: $sqlQuery SQL Args: $bindArgs")
                }, Executors.newSingleThreadExecutor())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

/*private val MIGRATION_1_2 = object : Migration(1, 2) {
           override fun migrate(database: SupportSQLiteDatabase) {
               // Создайте новую таблицу с новой схемой
               database.execSQL(
                   "CREATE TABLE IF NOT EXISTS `user` (" +
                           "`Id` TEXT NOT NULL, " +
                           "`FIO` TEXT, " +
                           "`Photo` TEXT, " +
                           "`Email` TEXT, " +
                           "`StudentCod` TEXT, " +
                           "`TeacherCod` TEXT, " +
                           "`BirthDate` TEXT, " +
                           "PRIMARY KEY(`Id`))"
               )

               database.execSQL(
                   "CREATE TABLE IF NOT EXISTS `schedule_new` (" +
                           "`FacultyName` TEXT, " +
                           "`Group` TEXT, " +
                           "`PlanNumber` TEXT, " +
                           "`Date` TEXT, " +
                           "`LessonNumber` INTEGER, " +
                           "`SubgroupCount` INTEGER, " +
                           "`CampusId` INTEGER, " +
                           "`CampusTitle` TEXT, " +
                           "`AuditoriumId` INTEGER, " +
                           "`AuditoriumNumber` TEXT, " +
                           "`Title` TEXT, " +
                           "`DisciplineGroup` TEXT, " +
                           "`DisciplineId` INTEGER, " +
                           "`Language` TEXT, " +
                           "`LessonType` INTEGER, " +
                           "`Remote` INTEGER, " +
                           "`SubgroupNumber` INTEGER, " +
                           "`FIO` TEXT, " +
                           "`TeacherId` TEXT, " +
                           "`Photo` TEXT, " +
                           "`UserName` TEXT, " +
                           "`TimeTableBlockd` INTEGER, " +
                           "PRIMARY KEY(`Date`, `LessonNumber`, `SubgroupNumber`, `TimeTableBlockd`))"
               )

               Переносите данные из старой таблицы в новую
               database.execSQL(
                   "INSERT INTO `user_new` (`Id`, `FIO`, `Photo`, `Email`, `StudentCod`, `TeacherCod`, `BirthDate`) " +
                           "SELECT `Id`, `FIO`, `Photo`, null, null, null, null FROM `user`"
               )

               // Удалите старую таблицу
               database.execSQL("DROP TABLE `user`")

               // Переименуйте новую таблицу обратно в `user`
               database.execSQL("ALTER TABLE `user_new` RENAME TO `user`")
           }
       }*/

/*private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Создайте новую таблицу с новой схемой
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS `schedule` (" +
                    "`Id` TEXT NOT NULL, " +
                    "`FacultyName` TEXT, " +
                    "`Group` TEXT, " +
                    "`PlanNumber` TEXT, " +
                    "`Date` TEXT, " +
                    "`LessonNumber` INTEGER, " +
                    "`SubgroupCount` INTEGER, " +
                    "`CampusId` INTEGER, " +
                    "`CampusTitle` TEXT, " +
                    "`AuditoriumId` INTEGER, " +
                    "`AuditoriumNumber` TEXT, " +
                    "`Title` TEXT, " +
                    "`DisciplineGroup` TEXT, " +
                    "`DisciplineId` INTEGER, " +
                    "`Language` TEXT, " +
                    "`LessonType` INTEGER, " +
                    "`Remote` INTEGER, " +
                    "`SubgroupNumber` INTEGER, " +
                    "`FIO` TEXT, " +
                    "`TeacherId` TEXT, " +
                    "`Photo` TEXT, " +
                    "`UserName` TEXT, " +
                    "`TimeTableBlockd` INTEGER, " +
                    "PRIMARY KEY(`Id`))"
        )
    }
}

private val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
    }
}

private val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL(
            "CREATE TABLE IF NOT EXISTS `schedule_new` (" +
                    "`Id` TEXT NOT NULL, " +
                    "`ScheduleId` TEXT, " +
                    "`FacultyName` TEXT, " +
                    "`Group` TEXT, " +
                    "`PlanNumber` TEXT, " +
                    "`Date` TEXT, " +
                    "`LessonNumber` INTEGER, " +
                    "`SubgroupCount` INTEGER, " +
                    "`CampusId` INTEGER, " +
                    "`CampusTitle` TEXT, " +
                    "`AuditoriumId` INTEGER, " +
                    "`AuditoriumNumber` TEXT, " +
                    "`Title` TEXT, " +
                    "`DisciplineGroup` TEXT, " +
                    "`DisciplineId` INTEGER, " +
                    "`Language` TEXT, " +
                    "`LessonType` INTEGER, " +
                    "`Remote` INTEGER, " +
                    "`SubgroupNumber` INTEGER, " +
                    "`FIO` TEXT, " +
                    "`TeacherId` TEXT, " +
                    "`Photo` TEXT, " +
                    "`UserName` TEXT, " +
                    "`TimeTableBlockd` INTEGER, " +
                    "PRIMARY KEY(`Id`))")

        // Удалите старую таблицу
        database.execSQL("DROP TABLE `schedule`")

        // Переименуйте новую таблицу обратно в `user`
        database.execSQL("ALTER TABLE `schedule_new` RENAME TO `schedule`")
    }
}*/