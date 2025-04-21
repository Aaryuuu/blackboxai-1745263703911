package com.example.easyattendanceapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.easyattendanceapp.data.entities.Attendance

@Dao
interface AttendanceDao {
    @Insert
    suspend fun insertAttendance(attendance: Attendance)

    @Query("SELECT * FROM attendance WHERE classId = :classId AND date = :date")
    suspend fun getAttendanceByClassAndDate(classId: Int, date: Long): List<Attendance>

    @Query("SELECT * FROM attendance WHERE studentId = :studentId")
    suspend fun getAttendanceByStudent(studentId: Int): List<Attendance>
}
