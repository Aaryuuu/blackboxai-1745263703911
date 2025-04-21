package com.example.easyattendanceapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.easyattendanceapp.data.AppDatabase
import com.example.easyattendanceapp.repository.AttendanceRepository
import com.example.easyattendanceapp.ui.AttendanceHistoryScreen
import com.example.easyattendanceapp.ui.AttendanceScreen
import com.example.easyattendanceapp.ui.ClassDetailScreen
import com.example.easyattendanceapp.ui.DashboardScreen
import com.example.easyattendanceapp.ui.LoginScreen
import com.example.easyattendanceapp.ui.theme.EasyAttendanceAppTheme
import com.example.easyattendanceapp.viewmodel.AttendanceViewModel
import androidx.activity.ComponentActivity
import android.os.Bundle

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: AttendanceViewModel
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)
        val repository = AttendanceRepository(db)
        viewModel = AttendanceViewModel(repository)

        setContent {
            EasyAttendanceAppTheme {
                navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(viewModel) {
                            navController.navigate("dashboard") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                    composable("dashboard") {
                        DashboardScreen(viewModel,
                            onClassSelected = { classEntity ->
                                navController.navigate("classDetail/${classEntity.id}")
                            },
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo("dashboard") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("classDetail/{classId}") { backStackEntry ->
                        val classId = backStackEntry.arguments?.getString("classId")?.toIntOrNull()
                        val classEntity = viewModel.classes.value.find { it.id == classId }
                        if (classEntity != null) {
                            ClassDetailScreen(
                                classEntity = classEntity,
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onTakeAttendance = {
                                    navController.navigate("attendance/${it.id}")
                                },
                                onViewHistory = {
                                    navController.navigate("attendanceHistory/${it.id}")
                                }
                            )
                        }
                    }
                    composable("attendance/{classId}") { backStackEntry ->
                        val classId = backStackEntry.arguments?.getString("classId")?.toIntOrNull()
                        val classEntity = viewModel.classes.value.find { it.id == classId }
                        if (classEntity != null) {
                            AttendanceScreen(
                                classEntity = classEntity,
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                    composable("attendanceHistory/{classId}") { backStackEntry ->
                        val classId = backStackEntry.arguments?.getString("classId")?.toIntOrNull()
                        val classEntity = viewModel.classes.value.find { it.id == classId }
                        if (classEntity != null) {
                            AttendanceHistoryScreen(
                                classEntity = classEntity,
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
