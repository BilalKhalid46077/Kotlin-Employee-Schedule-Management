package com.employee.management.model

data class ScheduleModel(
    val employeeName: String,
    val scheduleMap: HashMap<String,String> = hashMapOf()
)