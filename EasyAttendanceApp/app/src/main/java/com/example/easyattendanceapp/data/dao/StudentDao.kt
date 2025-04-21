package com.example.easyattendanceapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import com.example.easyattendanceapp.data.entities.Student

@Dao
interface StudentDao {
    @Query("SELECT * FROM students WHERE classId = :classId")
    suspend fun getStudentsByClass(classId: Int): List<Student>

    @Insert
    suspend fun insertStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)
}
