package com.employee.management.util

import com.employee.management.enums.Days
import com.employee.management.enums.Shifts
import com.employee.management.model.ScheduleModel

/**
 * Assign shifts for employees.
 * No employee works more than one shift per day.
 * An employee can work a maximum of 5 days per week.
 * The company must have at least 2 employees per shift per day.
 * If fewer than 2 employees are available for a shift,
 * randomly assign additional employees who have not worked 5 days yet.
 * Resolve shift conflicts.
 */
fun getSchedule(scheduleList: MutableList<ScheduleModel>): LinkedHashMap<String,LinkedHashMap<String,String>> {
    val dayMap = LinkedHashMap<String,LinkedHashMap<String,String>>()
    // Iterate day by day
    Days.entries.map { it.toString() }.forEach { day ->
        val shiftMap = LinkedHashMap<String, String>()
        var names = ""
        // Iterate shift by shift
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
                            names = list.take(2).joinToString()
                            // If not two employee available for this shift
                            if (list.size < 2) {
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
                            else {
                                // Schedule Conflict
                                list.drop(2).forEach { name ->
                                    // drop employee schedule
                                    scheduleList.find { it.employeeName == name }?.let { item->
                                        val newItem = item.copy(
                                            scheduleMap = item.scheduleMap.also { map ->
                                                map.remove(day)
                                            }
                                        )
                                        scheduleList.replaceAll {
                                            if (it.employeeName == item.employeeName)
                                                newItem else it
                                        }
                                    }
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
            shiftMap.put(shift,names.takeIf { it.isNotEmpty() } ?: "No Employee Available")
        }
        // add shift against day
        dayMap.put(
            day,shiftMap
        )
    }
    return dayMap
}