package com.employee.management.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppDropdownMenu(
    selectedItem: String = "",
    list: List<String> = emptyList(),
    onItemSelect: (String) -> Unit = {},
    modifier: Modifier
) {
    var mExpanded by remember { mutableStateOf(false) }
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                // Apply a rounded border of 2.dp thickness and Red color
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(16.dp)
                )
                // Add padding so the content doesn't overlap the border
                .padding(10.dp)
                .clickable{
                    mExpanded = !mExpanded
                }
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterStart),
                text = selectedItem
            )
            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = {
                  mExpanded = !mExpanded
            }) {
                Icon(
                    imageVector = icon,
                    contentDescription = ""
                )
            }

        }
        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
        ) {
            list.forEach { label ->
                DropdownMenuItem(
                    text = { Text(text = label) },
                    onClick = {
                        onItemSelect(label)
                        mExpanded = false
                    }
                )
            }
        }
    }
}