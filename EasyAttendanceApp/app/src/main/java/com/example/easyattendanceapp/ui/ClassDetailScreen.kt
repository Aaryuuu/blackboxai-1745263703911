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
import com.example.easyattendanceapp.viewmodel.AttendanceViewModel

@Composable
fun ClassDetailScreen(
    classEntity: ClassEntity,
    viewModel: AttendanceViewModel,
    onBack: () -> Unit,
    onTakeAttendance: (ClassEntity) -> Unit,
    onViewHistory: (ClassEntity) -> Unit
) {
    val students by viewModel.students.collectAsState()
    var newStudentName by remember { mutableStateOf("") }
    var newStudentRoll by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(classEntity.id) {
        viewModel.loadStudents(classEntity.id)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Class: ${classEntity.name}", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newStudentName,
            onValueChange = { newStudentName = it },
            label = { Text("Student Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newStudentRoll,
            onValueChange = { newStudentRoll = it },
            label = { Text("Roll Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (newStudentName.isNotBlank() && newStudentRoll.isNotBlank()) {
                    isLoading = true
                    viewModel.addStudent(
                        Student(
                            classId = classEntity.id,
                            name = newStudentName,
                            rollNumber = newStudentRoll
                        )
                    )
                    newStudentName = ""
                    newStudentRoll = ""
                    isLoading = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Add Student")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(students) { student ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(student.name, style = MaterialTheme.typography.bodyLarge)
                            Text("Roll No: ${student.rollNumber}", style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { viewModel.deleteStudent(student) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Student")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { onTakeAttendance(classEntity) }) {
                Text("Take Attendance")
            }
            Button(onClick = { onViewHistory(classEntity) }) {
                Text("View History")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }
}
