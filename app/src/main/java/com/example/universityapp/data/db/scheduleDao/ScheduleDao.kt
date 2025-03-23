package com.example.universityapp.data.db.scheduleDao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.universityapp.data.entity.ScheduleItemDB

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: ScheduleItemDB)

    @Update
    suspend fun updateSchedule(schedule: ScheduleItemDB)

    @Query("SELECT * FROM schedule WHERE Date = :date AND LessonNumber = :lessonNumber")
    suspend fun getSchedule(date: String, lessonNumber: Int): ScheduleItemDB?

    @Query("SELECT * FROM schedule WHERE Date LIKE '%' || :date || '%'")
    suspend fun getScheduleByDate(date: String): List<ScheduleItemDB>?

    @Query("DELETE FROM schedule WHERE 1 = 1")
    suspend fun deleteAllSchedules()

    @Delete
    suspend fun deleteSchedule(schedule: ScheduleItemDB)
}