package com.example.easyattendanceapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.easyattendanceapp.data.entities.Teacher

@Dao
interface TeacherDao {
    @Query("SELECT * FROM teachers WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): Teacher?

    @Insert
    suspend fun insert(teacher: Teacher)
}
