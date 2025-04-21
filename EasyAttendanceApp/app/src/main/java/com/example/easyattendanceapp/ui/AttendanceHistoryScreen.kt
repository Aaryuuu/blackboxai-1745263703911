package com.example.easyattendanceapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.easyattendanceapp.data.entities.Attendance
import com.example.easyattendanceapp.data.entities.ClassEntity
import com.example.easyattendanceapp.data.entities.Student
import com.example.easyattendanceapp.viewmodel.AttendanceViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AttendanceHistoryScreen(
    classEntity: ClassEntity,
    viewModel: AttendanceViewModel,
    onBack: () -> Unit
) {
    val students by viewModel.students.collectAsState()
    val attendanceList by viewModel.attendance.collectAsState()

    LaunchedEffect(classEntity.id) {
        viewModel.loadStudents(classEntity.id)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Attendance History for ${classEntity.name}", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(students) { student ->
                val studentAttendance = attendanceList.filter { it.studentId == student.id }
                if (studentAttendance.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(student.name, style = MaterialTheme.typography.bodyLarge)
                            studentAttendance.forEach { attendance ->
                                val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(attendance.date))
                                Text("$dateStr: ${if (attendance.isPresent) "Present" else "Absent"}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }
}
