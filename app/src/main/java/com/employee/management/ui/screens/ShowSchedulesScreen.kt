package com.employee.management.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.employee.management.ui.theme.KotlinEmployeeManagementTheme
import com.employee.management.ui.theme.Typography

@Composable
fun ShowScheduleScreen(
    noOfEmployee: Int,
    scheduleMap: LinkedHashMap<String, LinkedHashMap<String, String>>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Employee Schedule",
            style = Typography.titleLarge
        )
        Text(
            text = "Total Number Of Employees: $noOfEmployee",
            style = Typography.titleMedium
        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            scheduleMap.forEach { map->
                item{
                    ScheduleItemView(map.key, map.value)
                }
            }
        }
    }
}

@Composable
private fun ScheduleItemView(
    day: String,
    shiftMap: LinkedHashMap<String, String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = day,
            style = Typography.titleLarge
        )
        shiftMap.forEach { shift ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = shift.key,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = ":",
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = shift.value
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(
                vertical = 10.dp
            )
        )
    }
}

@Preview
@Composable
private fun ShowSchedulePreview() {
    KotlinEmployeeManagementTheme {
        ShowScheduleScreen(
            3,
            linkedMapOf()
        )
    }
}
