package com.example.trackmydiet.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.trackmydiet.ui.dashboard.MealItem
import com.example.trackmydiet.ui.dashboard.CalorieSummaryCard
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onBack: () -> Unit
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val meals by viewModel.meals.collectAsState()
    val user by viewModel.user.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Food History") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CalendarHeader(
                selectedDate = selectedDate,
                onDateSelected = { viewModel.setSelectedDate(it) }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    user?.let {
                        CalorieSummaryCard(user = it, meals = meals)
                    }
                }

                item {
                    Text(
                        text = "Logged Meals",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (meals.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No records for this day", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                        }
                    }
                } else {
                    items(meals) { meal ->
                        MealItem(meal = meal)
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarHeader(
    selectedDate: Long,
    onDateSelected: (Long) -> Unit
) {
    val calendar = remember { Calendar.getInstance() }
    val sdf = remember { SimpleDateFormat("MMMM yyyy", Locale.getDefault()) }
    
    var currentMonth by remember { mutableStateOf(Calendar.getInstance().apply { timeInMillis = selectedDate }) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                currentMonth = (currentMonth.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
            }) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Previous Month")
            }
            Text(
                text = sdf.format(currentMonth.time),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = {
                currentMonth = (currentMonth.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
            }) {
                Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = "Next Month")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val days = listOf("S", "M", "T", "W", "T", "F", "S")
            days.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Simple week view for brevity, or full month grid
        val startCalendar = (currentMonth.clone() as Calendar).apply {
            set(Calendar.DAY_OF_MONTH, 1)
            val dayOfWeek = get(Calendar.DAY_OF_WEEK)
            add(Calendar.DAY_OF_MONTH, -(dayOfWeek - 1))
        }

        for (i in 0 until 5) { // 5 weeks
            Row(modifier = Modifier.fillMaxWidth()) {
                for (j in 0 until 7) {
                    val dateMillis = startCalendar.timeInMillis
                    val isSelected = isSameDay(dateMillis, selectedDate)
                    val isCurrentMonth = startCalendar.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH)
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                            .clickable { onDateSelected(dateMillis) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = startCalendar.get(Calendar.DAY_OF_MONTH).toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = when {
                                isSelected -> MaterialTheme.colorScheme.onPrimary
                                isCurrentMonth -> MaterialTheme.colorScheme.onSurface
                                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            }
                        )
                    }
                    startCalendar.add(Calendar.DAY_OF_MONTH, 1)
                }
            }
        }
    }
}

private fun isSameDay(millis1: Long, millis2: Long): Boolean {
    val cal1 = Calendar.getInstance().apply { timeInMillis = millis1 }
    val cal2 = Calendar.getInstance().apply { timeInMillis = millis2 }
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}
