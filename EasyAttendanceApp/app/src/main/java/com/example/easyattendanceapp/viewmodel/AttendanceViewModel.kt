package com.example.easyattendanceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyattendanceapp.data.entities.Attendance
import com.example.easyattendanceapp.data.entities.ClassEntity
import com.example.easyattendanceapp.data.entities.Student
import com.example.easyattendanceapp.data.entities.Teacher
import com.example.easyattendanceapp.repository.AttendanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AttendanceViewModel(private val repository: AttendanceRepository) : ViewModel() {

    private val _teacher = MutableStateFlow<Teacher?>(null)
    val teacher: StateFlow<Teacher?> = _teacher

    private val _classes = MutableStateFlow<List<ClassEntity>>(emptyList())
    val classes: StateFlow<List<ClassEntity>> = _classes

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students

    private val _attendance = MutableStateFlow<List<Attendance>>(emptyList())
    val attendance: StateFlow<List<Attendance>> = _attendance

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val loggedInTeacher = repository.loginTeacher(email, password)
            _teacher.value = loggedInTeacher
            onResult(loggedInTeacher != null)
        }
    }

    fun loadClasses() {
        viewModelScope.launch {
            _classes.value = repository.getAllClasses()
        }
    }

    fun addClass(classEntity: ClassEntity) {
        viewModelScope.launch {
            repository.addClass(classEntity)
            loadClasses()
        }
    }

    fun deleteClass(classEntity: ClassEntity) {
        viewModelScope.launch {
            repository.deleteClass(classEntity)
            loadClasses()
        }
    }

    fun loadStudents(classId: Int) {
        viewModelScope.launch {
            _students.value = repository.getStudentsByClass(classId)
        }
    }

    fun addStudent(student: Student) {
        viewModelScope.launch {
            repository.addStudent(student)
            loadStudents(student.classId)
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            repository.deleteStudent(student)
            loadStudents(student.classId)
        }
    }

    fun addAttendance(attendance: Attendance) {
        viewModelScope.launch {
            repository.addAttendance(attendance)
        }
    }

    fun loadAttendanceByClassAndDate(classId: Int, date: Long) {
        viewModelScope.launch {
            _attendance.value = repository.getAttendanceByClassAndDate(classId, date)
        }
    }

    fun loadAttendanceByStudent(studentId: Int) {
        viewModelScope.launch {
            _attendance.value = repository.getAttendanceByStudent(studentId)
        }
    }
}
