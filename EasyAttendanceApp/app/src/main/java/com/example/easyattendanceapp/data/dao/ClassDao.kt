package com.example.easyattendanceapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import com.example.easyattendanceapp.data.entities.ClassEntity

@Dao
interface ClassDao {
    @Query("SELECT * FROM classes")
    suspend fun getAllClasses(): List<ClassEntity>

    @Insert
    suspend fun insertClass(classEntity: ClassEntity)

    @Delete
    suspend fun deleteClass(classEntity: ClassEntity)
}
