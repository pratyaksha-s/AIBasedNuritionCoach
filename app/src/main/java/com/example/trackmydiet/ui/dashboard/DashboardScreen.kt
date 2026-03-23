package com.example.trackmydiet.ui.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackmydiet.data.local.entities.Meal
import com.example.trackmydiet.data.local.entities.User
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onAddFoodClick: () -> Unit
) {
    val user by viewModel.user.collectAsState()
    val meals by viewModel.meals.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddFoodClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Food")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                user?.let {
                    CalorieSummaryCard(it, meals)
                }
            }
            
            item {
                Text(
                    text = "Today's Meals",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            if (meals.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No meals logged yet today", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                    }
                }
            } else {
                items(meals) { meal ->
                    MealItem(meal)
                }
            }
        }
    }
}

@Composable
fun CalorieSummaryCard(user: User, meals: List<Meal>) {
    val totalCalories = meals.sumOf { it.calories }
    val progress = if (user.calorieTarget > 0) totalCalories.toFloat() / user.calorieTarget else 0f
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Daily Progress",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Box(contentAlignment = Alignment.Center) {
                CircularProgress(progress = progress)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$totalCalories",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "of ${user.calorieTarget} kcal",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MacroItem("Protein", meals.sumOf { it.protein }, user.proteinTarget, "g")
                MacroItem("Carbs", meals.sumOf { it.carbs }, user.carbTarget, "g")
                MacroItem("Fat", meals.sumOf { it.fat }, user.fatTarget, "g")
            }
        }
    }
}

@Composable
fun CircularProgress(progress: Float) {
    val animatedProgress by animateFloatAsState(targetValue = progress.coerceAtMost(1f))
    val color = MaterialTheme.colorScheme.primary
    val trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f)
    
    Canvas(modifier = Modifier.size(160.dp)) {
        drawCircle(
            color = trackColor,
            style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
        )
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = 360f * animatedProgress,
            useCenter = false,
            style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
fun MacroItem(label: String, value: Int, target: Int, unit: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall)
        Text(
            text = "$value/$target$unit",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        LinearProgressIndicator(
            progress = { if (target > 0) value.toFloat() / target else 0f },
            modifier = Modifier.width(60.dp).padding(top = 4.dp).height(4.dp),
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
fun MealItem(meal: Meal) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Fastfood,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = meal.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = timeFormat.format(Date(meal.timestamp)), style = MaterialTheme.typography.labelSmall)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "${meal.calories} kcal", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(
                    text = "P:${meal.protein}g C:${meal.carbs}g F:${meal.fat}g",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
