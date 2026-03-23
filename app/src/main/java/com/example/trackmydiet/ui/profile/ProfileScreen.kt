package com.example.trackmydiet.ui.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.trackmydiet.data.local.entities.User
import com.example.trackmydiet.data.local.entities.WeightEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel
) {
    val user by viewModel.user.collectAsState()
    val weightEntries by viewModel.weightEntries.collectAsState()
    var showWeightDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Profile") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            user?.let {
                ProfileHeader(it)
                Spacer(modifier = Modifier.height(24.dp))
                WeightProgressSection(
                    weightEntries = weightEntries,
                    onAddWeightClick = { showWeightDialog = true }
                )
                Spacer(modifier = Modifier.height(24.dp))
                GoalsSection(it)
            }
        }
    }

    if (showWeightDialog) {
        AddWeightDialog(
            onDismiss = { showWeightDialog = false },
            onConfirm = { weight ->
                viewModel.addWeightEntry(weight)
                showWeightDialog = false
            }
        )
    }
}

@Composable
fun ProfileHeader(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(80.dp),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            Spacer(modifier = Modifier.width(24.dp))
            Column {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${user.age} years • ${user.gender}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${user.height} cm • ${user.weight} kg",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun WeightProgressSection(
    weightEntries: List<WeightEntry>,
    onAddWeightClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Weight Progress",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onAddWeightClick) {
                Icon(Icons.Rounded.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Log Weight")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (weightEntries.size < 2) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add more weight entries to see progress",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            WeightChart(weightEntries)
        }
    }
}

@Composable
fun WeightChart(entries: List<WeightEntry>) {
    val primaryColor = MaterialTheme.colorScheme.primary
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(24.dp)
        ) {
            val minWeight = entries.minOf { it.weight } - 2
            val maxWeight = entries.maxOf { it.weight } + 2
            val weightRange = maxWeight - minWeight
            
            val points = entries.mapIndexed { index, entry ->
                val x = index.toFloat() / (entries.size - 1) * size.width
                val y = (1 - (entry.weight - minWeight) / weightRange) * size.height
                Offset(x, y)
            }

            val path = Path().apply {
                moveTo(points.first().x, points.first().y)
                points.forEach { lineTo(it.x, it.y) }
            }

            drawPath(
                path = path,
                color = primaryColor,
                style = Stroke(width = 3.dp.toPx())
            )

            points.forEach { point ->
                drawCircle(
                    color = primaryColor,
                    radius = 4.dp.toPx(),
                    center = point
                )
            }
        }
    }
}

@Composable
fun GoalsSection(user: User) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Your Nutritional Goals",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        GoalItem("Daily Calories", "${user.calorieTarget} kcal")
        GoalItem("Protein", "${user.proteinTarget} g")
        GoalItem("Carbs", "${user.carbTarget} g")
        GoalItem("Fat", "${user.fatTarget} g")
    }
}

@Composable
fun GoalItem(label: String, value: String) {
    ListItem(
        headlineContent = { Text(label) },
        trailingContent = { Text(value, fontWeight = FontWeight.Bold) }
    )
}

@Composable
fun AddWeightDialog(
    onDismiss: () -> Unit,
    onConfirm: (Float) -> Unit
) {
    var weight by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Weight") },
        text = {
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = { weight.toFloatOrNull()?.let { onConfirm(it) } }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
