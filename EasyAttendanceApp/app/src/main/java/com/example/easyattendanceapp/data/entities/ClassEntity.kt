package com.example.easyattendanceapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "classes")
data class ClassEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)
