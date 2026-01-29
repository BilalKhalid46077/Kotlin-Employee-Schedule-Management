package com.employee.management.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.employee.management.model.ScheduleModel
import com.employee.management.ui.screens.AddScheduleScreen
import com.employee.management.ui.screens.ShowScheduleScreen
import com.employee.management.util.getSchedule

@Composable
fun NavigationGraph(innerPadding: PaddingValues) {
    val navController = rememberNavController()
    val scheduleList = remember { mutableListOf<ScheduleModel>() }
    val context = LocalContext.current
    NavHost(
        modifier = Modifier.padding(innerPadding),
        navController = navController,
        startDestination = NavRoutes.AddSchedule.route
    ) {
        composable(
            route = NavRoutes.AddSchedule.route
        ) {
            AddScheduleScreen(
                onAdd = { data->
                    if(scheduleList.find { it.employeeName == data.employeeName } != null){
                        Toast.makeText(context,"Employee name already exist!", Toast.LENGTH_SHORT).show()
                        return@AddScheduleScreen
                    }
                    scheduleList.add(data)
                    navController.navigate(NavRoutes.AddSchedule.route) {
                        popUpTo(navController.currentDestination?.route ?: return@navigate) {
                            inclusive = true
                        }
                    }
                },
                onShow = {
                    navController.navigate(NavRoutes.AddSchedule.route) {
                        popUpTo(navController.currentDestination?.route ?: return@navigate) {
                            inclusive = true
                        }
                    }
                    navController.navigate(NavRoutes.ShowSchedules.route)
                }
            )
        }
        composable(
            route = NavRoutes.ShowSchedules.route
        ) {
            ShowScheduleScreen(
                noOfEmployee = scheduleList.size,
                scheduleMap = getSchedule(scheduleList)
            )
        }
    }
}