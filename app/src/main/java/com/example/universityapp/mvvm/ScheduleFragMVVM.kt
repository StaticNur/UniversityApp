package com.example.universityapp.mvvm

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universityapp.data.db.AppDatabase
import com.example.universityapp.data.db.scheduleDao.ScheduleRepository
import com.example.universityapp.data.entity.Auditorium
import com.example.universityapp.data.entity.Discipline
import com.example.universityapp.data.entity.Lesson
import com.example.universityapp.data.entity.Photo
import com.example.universityapp.data.entity.Schedule
import com.example.universityapp.data.entity.ScheduleItem
import com.example.universityapp.data.entity.ScheduleItemDB
import com.example.universityapp.data.entity.Teacher
import com.example.universityapp.data.entity.TimeTable
import com.example.universityapp.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ScheduleFragMVVM(val token: String, val context: Context?) : ViewModel() {
    private val TAG: String = "ScheduleMVVM"
    private val mutableStudent: MutableLiveData<Schedule> = MutableLiveData<Schedule>()

    private var scheduleRepository: ScheduleRepository

    init {
        val scheduleDao = AppDatabase.getInstance(context!!.applicationContext).scheduleDao()
        scheduleRepository = ScheduleRepository(scheduleDao)
    }

    fun getSchedule(date: String?) {
        RetrofitInstance.mrsuApi.getSchedule("Bearer $token", date)
            .enqueue(object : Callback<Schedule> {
                override fun onResponse(call: Call<Schedule>, response: Response<Schedule>) {
                    println(response.body())
                    mutableStudent.value = response.body()
                }

                override fun onFailure(call: Call<Schedule>, t: Throwable) {
                    /*val scheduleItemDBs: List<ScheduleItemDB>? = getScheduleByDate(date.toString()).value;
                    println(scheduleItemDBs.toString())
                    if (scheduleItemDBs !== null && scheduleItemDBs.size > 0) {
                        val schedule: Schedule = Schedule();
                        for (scheduleItemDB in scheduleItemDBs){
                            val scheduleItem: ScheduleItem = builderSchedule(scheduleItemDB)
                            schedule.add(scheduleItem)
                        }
                        Log.e(TAG, schedule.toString())
                        mutableStudent.value = schedule;
                    }*/
                    Log.e(TAG, t.message.toString())
                }

            })
    }

    fun builderSchedule(scheduleItemDB: ScheduleItemDB): ScheduleItem {
        return ScheduleItem(
            FacultyName = scheduleItemDB.FacultyName!!,
            Group = scheduleItemDB.Group!!,
            PlanNumber = scheduleItemDB.PlanNumber!!,
            TimeTable = TimeTable(
                Date = scheduleItemDB.Date,
                Lessons = listOf(
                    Lesson(
                        Disciplines = listOf(
                            Discipline(
                                Auditorium = Auditorium(
                                    CampusId = scheduleItemDB.CampusId!!,
                                    CampusTitle = scheduleItemDB.CampusTitle!!,
                                    Id = scheduleItemDB.AuditoriumId!!,
                                    Number = scheduleItemDB.AuditoriumNumber!!,
                                    Title = scheduleItemDB.Title!!
                                ),
                                Group = scheduleItemDB.DisciplineGroup!!,
                                Id = scheduleItemDB.DisciplineId!!,
                                Language = scheduleItemDB.Language!!,
                                LessonType = scheduleItemDB.LessonType!!,
                                Remote = scheduleItemDB.Remote!! == 1,
                                SubgroupNumber = scheduleItemDB.SubgroupNumber!!,
                                Teacher = Teacher(
                                    FIO = scheduleItemDB.FIO!!,
                                    Id = scheduleItemDB.TeacherId!!,
                                    Photo = Photo(
                                        UrlMedium = scheduleItemDB.Photo,
                                        UrlSmall = scheduleItemDB.Photo,
                                        UrlSource = scheduleItemDB.Photo
                                    ),
                                    UserName = scheduleItemDB.UserName!!
                                ),
                                Title = scheduleItemDB.Title
                            )
                        ),
                        Number = scheduleItemDB.LessonNumber,
                        SubgroupCount = scheduleItemDB.SubgroupCount!!
                    )
                )
            ),
            TimeTableBlockd = scheduleItemDB.TimeTableBlockd!!
        )
    }


    fun observeSchedule(): LiveData<Schedule> {
        return mutableStudent
    }

    fun getScheduleByDate(date: String): LiveData<List<ScheduleItemDB>> {
        val result = MutableLiveData<List<ScheduleItemDB>>()
        viewModelScope.launch(Dispatchers.IO) {
            val schedule = scheduleRepository.getScheduleByDate(date)
            withContext(Dispatchers.Main) {
                result.value = schedule ?: emptyList()
            }
        }
        return result
    }

    fun insertSchedule(schedule: ScheduleItemDB) {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleRepository.insertSchedule(schedule)
            withContext(Dispatchers.Main) {
            }
        }
    }

    fun updateSchedule(schedule: ScheduleItemDB) {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleRepository.updateSchedule(schedule)
            withContext(Dispatchers.Main) {
            }
        }
    }

    fun deleteSchedule(news: ScheduleItemDB) = viewModelScope.launch(Dispatchers.IO) {
        scheduleRepository.deleteSchedule(news)
    }

    fun isScheduleSavedInDatabase(date: String): Boolean {
        val schedule: List<ScheduleItemDB>? = runBlocking(Dispatchers.IO) {
            scheduleRepository.getScheduleByDate(date)
        }
        return !schedule.isNullOrEmpty()
    }

    fun deleteAllSchedule() {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleRepository.deleteAllSchedules()
        }
    }
}