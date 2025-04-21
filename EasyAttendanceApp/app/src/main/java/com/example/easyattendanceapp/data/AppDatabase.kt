package com.example.easyattendanceapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.easyattendanceapp.data.dao.AttendanceDao
import com.example.easyattendanceapp.data.dao.ClassDao
import com.example.easyattendanceapp.data.dao.StudentDao
import com.example.easyattendanceapp.data.dao.TeacherDao
import com.example.easyattendanceapp.data.entities.Attendance
import com.example.easyattendanceapp.data.entities.ClassEntity
import com.example.easyattendanceapp.data.entities.Student
import com.example.easyattendanceapp.data.entities.Teacher

@Database(
    entities = [Teacher::class, ClassEntity::class, Student::class, Attendance::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun teacherDao(): TeacherDao
    abstract fun classDao(): ClassDao
    abstract fun studentDao(): StudentDao
    abstract fun attendanceDao(): AttendanceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "easy_attendance_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
