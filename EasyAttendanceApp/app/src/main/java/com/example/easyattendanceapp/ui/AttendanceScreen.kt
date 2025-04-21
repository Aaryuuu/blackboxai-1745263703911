package com.example.easyattendanceapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.easyattendanceapp.data.entities.ClassEntity
import com.example.easyattendanceapp.data.entities.Student
import com.example.easyattendanceapp.data.entities.Attendance
import com.example.easyattendanceapp.viewmodel.AttendanceViewModel
import java.util.*

@Composable
fun AttendanceScreen(
    classEntity: ClassEntity,
    viewModel: AttendanceViewModel,
    onBack: () -> Unit
) {
    val students by viewModel.students.collectAsState()
    val attendanceList by viewModel.attendance.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    var attendanceMap by remember {
        mutableStateOf(
            attendanceList.associateBy { it.studentId }.toMutableMap()
        )
    }

    LaunchedEffect(classEntity.id) {
        viewModel.loadStudents(classEntity.id)
        viewModel.loadAttendanceByClassAndDate(classEntity.id, today)
    }

    LaunchedEffect(attendanceList) {
        attendanceMap = attendanceList.associateBy { it.studentId }.toMutableMap()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Attendance for ${classEntity.name}", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(students) { student ->
                val attendance = attendanceMap[student.id]
                val isPresent = attendance?.isPresent ?: false
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(student.name, style = MaterialTheme.typography.bodyLarge)
                    Row {
                        RadioButton(
                            selected = isPresent,
                            onClick = {
                                attendanceMap[student.id] = Attendance(
                                    id = attendance?.id ?: 0,
                                    classId = classEntity.id,
                                    studentId = student.id,
                                    date = today,
                                    isPresent = true
                                )
                            }
                        )
                        Text("Present", modifier = Modifier.padding(end = 16.dp))
                        RadioButton(
                            selected = !isPresent,
                            onClick = {
                                attendanceMap[student.id] = Attendance(
                                    id = attendance?.id ?: 0,
                                    classId = classEntity.id,
                                    studentId = student.id,
                                    date = today,
                                    isPresent = false
                                )
                            }
                        )
                        Text("Absent")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                attendanceMap.values.forEach { attendance ->
                    viewModel.addAttendance(attendance)
                }
                isLoading = false
                onBack()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Save Attendance")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }
}
