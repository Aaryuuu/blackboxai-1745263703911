package com.example.easyattendanceapp.repository

import com.example.easyattendanceapp.data.AppDatabase
import com.example.easyattendanceapp.data.entities.Attendance
import com.example.easyattendanceapp.data.entities.ClassEntity
import com.example.easyattendanceapp.data.entities.Student
import com.example.easyattendanceapp.data.entities.Teacher

class AttendanceRepository(private val db: AppDatabase) {

    suspend fun loginTeacher(email: String, password: String) =
        db.teacherDao().login(email, password)

    suspend fun getAllClasses(): List<ClassEntity> =
        db.classDao().getAllClasses()

    suspend fun addClass(classEntity: ClassEntity) =
        db.classDao().insertClass(classEntity)

    suspend fun deleteClass(classEntity: ClassEntity) =
        db.classDao().deleteClass(classEntity)

    suspend fun getStudentsByClass(classId: Int): List<Student> =
        db.studentDao().getStudentsByClass(classId)

    suspend fun addStudent(student: Student) =
        db.studentDao().insertStudent(student)

    suspend fun deleteStudent(student: Student) =
        db.studentDao().deleteStudent(student)

    suspend fun addAttendance(attendance: Attendance) =
        db.attendanceDao().insertAttendance(attendance)

    suspend fun getAttendanceByClassAndDate(classId: Int, date: Long): List<Attendance> =
        db.attendanceDao().getAttendanceByClassAndDate(classId, date)

    suspend fun getAttendanceByStudent(studentId: Int): List<Attendance> =
        db.attendanceDao().getAttendanceByStudent(studentId)
}
