package com.example.universityapp.ui.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universityapp.adapters.ScheduleAdapter
import com.example.universityapp.data.entity.Auditorium
import com.example.universityapp.data.entity.Discipline
import com.example.universityapp.data.entity.Lesson
import com.example.universityapp.data.entity.Photo
import com.example.universityapp.data.entity.Schedule
import com.example.universityapp.data.entity.ScheduleItemDB
import com.example.universityapp.data.entity.Teacher
import com.example.universityapp.databinding.FragmentScheduleBinding
import com.example.universityapp.mvvm.ScheduleFragMVVM
import com.example.universityapp.ui.activities.UserPageActivity
import com.example.universityapp.utils.ScheduleCustomFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ScheduleFragment : Fragment() {
    private lateinit var binding: FragmentScheduleBinding
    private lateinit var myAdapter: ScheduleAdapter
    private lateinit var scheduleMvvm: ScheduleFragMVVM
    private lateinit var sPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = ScheduleAdapter()
        sPref = requireContext().getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        scheduleMvvm = ViewModelProvider(
            this,
            ScheduleCustomFactory(sPref.getString("saved_token", "").toString(), context)
        )[ScheduleFragMVVM::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        scheduleMvvm.getSchedule(currentDate.format(formatter))

        onClickCalender()
        prepareRecyclerView()
        observeSchedule()
        onScheduleClick()
    }

    private fun onClickCalender() {
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            val hasDb = scheduleMvvm.isScheduleSavedInDatabase(selectedDate)
            if(!hasDb){
                scheduleMvvm.getSchedule(selectedDate)
                Log.d("CalendarView", "Clicked date: $selectedDate")
            }else{
                scheduleMvvm.getScheduleByDate(selectedDate).observe(viewLifecycleOwner, Observer { schedule ->
                    notVisibleEmptyDiscipline()
                    val l = convertToLesson(schedule)
                    if(l != null){
                        myAdapter.setScheduleList(l)
                    }
                })
            }
        }
    }

    private fun onScheduleClick() {
        myAdapter.onItemClicked(object : ScheduleAdapter.OnItemScheduleClicked {
            override fun onClickListener(id: String) {
                val intent = Intent(context, UserPageActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            }
        })
    }

    private fun observeSchedule() {
        scheduleMvvm.observeSchedule().observe(viewLifecycleOwner, object : Observer<Schedule?> {
            override fun onChanged(t: Schedule?) {
                if (!t!!.isEmpty()) {
                    notVisibleEmptyDiscipline()
                    myAdapter.setScheduleList(t[0].TimeTable.Lessons)
                    saveSchedule(t)
                } else {
                    Toast.makeText(
                        requireContext().applicationContext,
                        "No lesson this day",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun visibleEmptyDiscipline() {
        binding.apply {
            recyclerViewSchedule.visibility = View.INVISIBLE
            emptySchedule.visibility = View.VISIBLE
        }
    }

    private fun notVisibleEmptyDiscipline() {
        binding.apply {
            recyclerViewSchedule.visibility = View.VISIBLE
            emptySchedule.visibility = View.INVISIBLE
        }
    }

    private fun prepareRecyclerView() {
        binding.recyclerViewSchedule.apply {
            adapter = myAdapter
            layoutManager =
                LinearLayoutManager(requireContext()) //GridLayoutManager(context,3, GridLayoutManager.VERTICAL,false)
        }
    }

    private fun saveSchedule(schedules: Schedule?) {
        val scheduleItemDB = convertScheduleToScheduleItemDB(schedules)
        if (scheduleItemDB != null) {
            for(s in scheduleItemDB){
                scheduleMvvm.insertSchedule(s)
            }
        }
    }

    private fun convertScheduleToScheduleItemDB(schedules: Schedule?): List<ScheduleItemDB>? {
        if (schedules == null || schedules.isEmpty()) return null

        val scheduleItem = schedules[0]

        val scheduleItemDB = mutableListOf<ScheduleItemDB>()
        for(lesson in scheduleItem.TimeTable.Lessons){
            if(lesson.Disciplines != null){
                for(discipline in lesson.Disciplines){
                    val auditorium = discipline.Auditorium
                    val teacher = discipline.Teacher
                    scheduleItemDB.add(ScheduleItemDB(
                        FacultyName = scheduleItem.FacultyName,
                        Group = scheduleItem.Group,
                        PlanNumber = scheduleItem.PlanNumber,
                        Date = scheduleItem.TimeTable.Date,
                        LessonNumber = lesson.Number,
                        SubgroupCount = lesson.SubgroupCount,
                        CampusId = auditorium?.CampusId,
                        CampusTitle = auditorium?.CampusTitle,
                        AuditoriumId = auditorium?.Id,
                        AuditoriumNumber = auditorium?.Number,
                        Title = discipline.Title ?: "default_title", // Значение по умолчанию для Title
                        DisciplineGroup = discipline.Group
                            ?: "default_group", // Значение по умолчанию для Group
                        DisciplineId = discipline.Id ?: -1, // Значение по умолчанию для Id
                        Language = discipline.Language?.toString()
                            ?: "RU", // Значение по умолчанию для Language
                        LessonType = discipline.LessonType ?: -1, // Значение по умолчанию для LessonType
                        Remote = if (discipline.Remote) 1 else 0, // Значение по умолчанию для Remote
                        SubgroupNumber = discipline.SubgroupNumber
                            ?: -1, // Значение по умолчанию для SubgroupNumber
                        FIO = teacher?.FIO,
                        TeacherId = teacher?.Id,
                        Photo = teacher?.Photo?.toString(),
                        UserName = teacher?.UserName,
                        TimeTableBlockd = scheduleItem.TimeTableBlockd
                    ))
                }
            }
        }
        return scheduleItemDB
    }

    private fun convertToLesson(scheduleItemDBs: List<ScheduleItemDB>?): List<Lesson>? {
        if (scheduleItemDBs == null || scheduleItemDBs.isEmpty()) return null

        val lessons = mutableListOf<Lesson>()
        for (scheduleItemDB in scheduleItemDBs) {
            val lesson = Lesson(
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
                        Remote = scheduleItemDB.Remote == 1,
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
                Number = scheduleItemDB.LessonNumber!!,
                SubgroupCount = scheduleItemDB.SubgroupCount!!
            )
            lessons.add(lesson)
        }
        return lessons
    }
}