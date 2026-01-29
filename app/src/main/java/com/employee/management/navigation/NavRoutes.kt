package com.employee.management.navigation

// Routes using in app
sealed class NavRoutes(val route: String) {
    object AddSchedule : NavRoutes("add_schedule")
    object ShowSchedules : NavRoutes("show_schedule")
}