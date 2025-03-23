package com.example.universityapp.data.db.scheduleDao

import com.example.universityapp.data.entity.ScheduleItemDB

class ScheduleRepository(private val scheduleDao: ScheduleDao) {

    suspend fun insertSchedule(schedule: ScheduleItemDB) {
        scheduleDao.insertSchedule(schedule)
    }

    suspend fun updateSchedule(schedule: ScheduleItemDB) {
        scheduleDao.updateSchedule(schedule)
    }

    suspend fun getSchedule(date: String, lessonNumber: Int): ScheduleItemDB? {
        return scheduleDao.getSchedule(date, lessonNumber)
    }

    suspend fun getScheduleByDate(date: String): List<ScheduleItemDB>? {
        return scheduleDao.getScheduleByDate(date)
    }

    suspend fun deleteAllSchedules() {
        scheduleDao.deleteAllSchedules()
    }

    suspend fun deleteSchedule(schedule: ScheduleItemDB) = scheduleDao.deleteSchedule(schedule)
}