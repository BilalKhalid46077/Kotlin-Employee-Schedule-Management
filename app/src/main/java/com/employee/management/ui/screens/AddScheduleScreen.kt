package com.employee.management.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.employee.management.model.ScheduleModel
import com.employee.management.enums.Days
import com.employee.management.enums.Shifts
import com.employee.management.ui.theme.KotlinEmployeeManagementTheme
import com.employee.management.ui.theme.Typography

@Composable
fun AddScheduleScreen(
    onAdd: (ScheduleModel) -> Unit,
    onShow: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    val scheduleMap = remember { mutableStateOf<HashMap<String, String>>(hashMapOf()) }
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add Schedule",
            style = Typography.titleLarge
        )
        Spacer(modifier = Modifier.padding(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Employee Name",
                style = Typography.labelMedium
            )
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = name,
                onValueChange = {
                    name = it
                }
            )
        }
        HorizontalDivider(
            Modifier.padding(vertical = 20.dp)
        )
        Days.entries.map { it.toString() }.forEach { day ->
            AddScheduleItem(
                title = day,
                scheduleMap
            )
        }
        HorizontalDivider(
            Modifier.padding(vertical = 20.dp)
        )
        Button(onClick = {
            if (name.isEmpty() || scheduleMap.value.isEmpty()) {
                Toast.makeText(context, "Please add all fields and add schedule for max 5 days!", Toast.LENGTH_SHORT).show()
                return@Button
            } else if (
                scheduleMap.value.size > 5
            ) {
                Toast.makeText(
                    context,
                    "Please add schedule for only 5 days per week!",
                    Toast.LENGTH_SHORT
                ).show()
                return@Button
            }

            onAdd(
                ScheduleModel(
                    scheduleMap = scheduleMap.value,
                    employeeName = name,
                )
            )

        }) {
            Text("Add Schedule")
        }

        Button(onClick = onShow) {
            // Show all schedules
            Text("Show Schedules")
        }
    }
}

@Composable
private fun AddScheduleItem(
    title: String,
    scheduleMap: MutableState<HashMap<String, String>>,
) {
    val list = Shifts.entries.map { it.toString() }
    var selectedItem by remember { mutableStateOf(list[0]) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = Typography.labelMedium
        )
        AppDropdownMenu(
            selectedItem = selectedItem,
            list = list,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onItemSelect = {
                selectedItem = it
                if (it == list[0]) {
                    scheduleMap.value.remove(title).takeIf { scheduleMap.value.contains(title) }
                } else {
                    scheduleMap.value.put(
                        title, it
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun AddSchedulePreview() {
    KotlinEmployeeManagementTheme {
        AddScheduleScreen(
            onAdd = {}
        )
    }
}