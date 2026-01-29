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
import com.employee.management.enums.Days
import com.employee.management.enums.Shifts
import com.employee.management.model.ScheduleModel
import com.employee.management.ui.screens.AddScheduleScreen
import com.employee.management.ui.screens.ShowScheduleScreen
import kotlin.collections.contains

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
                scheduleMap = getSchedule(scheduleList)
            )
        }
    }
}


/**
 * Assign shifts for employees.
 * No employee works more than one shift per day.
 * An employee can work a maximum of 5 days per week.
 * The company must have at least 2 employees per shift per day.
 * If fewer than 2 employees are available for a shift,
 * randomly assign additional employees who have not worked 5 days yet.
 */
private fun getSchedule(scheduleList: MutableList<ScheduleModel>): LinkedHashMap<String,LinkedHashMap<String,String>> {
    val dayMap = LinkedHashMap<String,LinkedHashMap<String,String>>()
    // Iterate data day by day
    Days.entries.map { it.toString() }.forEach { day ->
        val shiftMap = LinkedHashMap<String, String>()
        var names = "No Employee Available"
        // Iterate data shift by shift
        Shifts.entries.map {
            it.toString()
        }.filter { it != Shifts.Off.name }.forEach { shift ->
            // Must have two employee per shift per day
            scheduleList.filter {
                it.scheduleMap.contains(day)
            }.also { list ->
                    list.filter { it.scheduleMap[day] == shift }.map { it.employeeName }
                        .also { list ->
                            if(list.isNotEmpty()){
                                names = list.joinToString()
                                // If not two employee available for this shift
                                if (list.isNotEmpty() && list.size < 2) {
                                    // Pick any employee who is not working 5 days a week.
                                    scheduleList.filter {
                                        it.scheduleMap.size < 5
                                                && !it.scheduleMap.contains(day)
                                                && !list.contains(it.employeeName)
                                    }.map { it }.firstOrNull()?.let { // update day and shift in employee schedule
                                            item ->
                                        val newItem = item.copy(
                                            scheduleMap = item.scheduleMap.also { map ->
                                                map.put(day, shift)
                                            }
                                        )
                                        scheduleList.replaceAll {
                                            if (it.employeeName == item.employeeName)
                                                newItem
                                            else it
                                        }
                                        names = names +", ${item.employeeName}"
                                    }
                                }
                            } else {
                                // If no employee available on this day
                                // Find any two employee who has not added 5 days yet
                                scheduleList.filter {
                                    it.scheduleMap.size < 5
                                            && !it.scheduleMap.contains(day)
                                }.take(2).also { list ->
                                    names = list.joinToString { it.employeeName }
                                    list.forEach { item ->
                                        // update employee schedule
                                        val newItem = item.copy(
                                            scheduleMap = item.scheduleMap.also { map ->
                                                map.put(day, shift)
                                            }
                                        )
                                        scheduleList.replaceAll {
                                            if (it.employeeName == item.employeeName)
                                                newItem else it
                                        }
                                    }
                                }
                            }
                        }
            }
            // Add names against shift
            shiftMap.put(shift,names)
        }
        // add shift against day
        dayMap.put(
            day,shiftMap
        )
    }
    return dayMap
}