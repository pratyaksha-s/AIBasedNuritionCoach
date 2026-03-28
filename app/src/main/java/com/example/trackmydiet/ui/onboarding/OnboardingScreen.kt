package com.example.trackmydiet.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.trackmydiet.domain.ActivityLevel

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onComplete: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var step by remember { mutableIntStateOf(1) }

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var activityLevel by remember { mutableStateOf(ActivityLevel.MODERATE) }

    LaunchedEffect(uiState) {
        if (uiState is OnboardingUiState.Success) {
            onComplete()
        }
    }

    Scaffold(
        modifier = Modifier.imePadding()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (step) {
                    1 -> WelcomeStep(name = name, onNameChange = { name = it })
                    2 -> MetricsStep(
                        age = age, onAgeChange = { age = it },
                        gender = gender, onGenderChange = { gender = it },
                        height = height, onHeightChange = { height = it },
                        weight = weight, onWeightChange = { weight = it }
                    )
                    3 -> ActivityStep(
                        activityLevel = activityLevel,
                        onActivityLevelChange = { activityLevel = it }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (uiState is OnboardingUiState.Loading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            if (step < 3) {
                                step++
                            } else {
                                viewModel.completeOnboarding(
                                    name = name,
                                    age = age.toIntOrNull() ?: 25,
                                    gender = gender,
                                    height = height.toFloatOrNull() ?: 170f,
                                    weight = weight.toFloatOrNull() ?: 70f,
                                    activityLevel = activityLevel
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isStepValid(step, name, age, height, weight)
                    ) {
                        Text(if (step < 3) "Next" else "Finish")
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = if (step < 3) Icons.AutoMirrored.Rounded.ArrowForward else Icons.Rounded.Check,
                            contentDescription = null
                        )
                    }
                }

                if (uiState is OnboardingUiState.Error) {
                    Text(
                        text = (uiState as OnboardingUiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

private fun isStepValid(step: Int, name: String, age: String, height: String, weight: String): Boolean {
    return when (step) {
        1 -> name.isNotBlank()
        2 -> age.toIntOrNull() != null && height.toFloatOrNull() != null && weight.toFloatOrNull() != null
        else -> true
    }
}

@Composable
fun WelcomeStep(name: String, onNameChange: (String) -> Unit) {
    Text(
        text = "Welcome to AI nutrition coach!",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Let's get started with your personalized nutrition plan. What's your name?",
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(32.dp))
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = name.isBlank() && name.isNotEmpty()
    )
}

@Composable
fun MetricsStep(
    age: String, onAgeChange: (String) -> Unit,
    gender: String, onGenderChange: (String) -> Unit,
    height: String, onHeightChange: (String) -> Unit,
    weight: String, onWeightChange: (String) -> Unit
) {
    Text(
        text = "Tell us about yourself",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(24.dp))

    OutlinedTextField(
        value = age,
        onValueChange = { if (it.all { char -> char.isDigit() }) onAgeChange(it) },
        label = { Text("Age") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        isError = age.isNotEmpty() && age.toIntOrNull() == null,
        supportingText = {
            if (age.isNotEmpty() && age.toIntOrNull() == null) {
                Text("Please enter a valid age")
            }
        }
    )
    Spacer(modifier = Modifier.height(16.dp))

    Text("Gender", style = MaterialTheme.typography.labelLarge)
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = gender == "Male", onClick = { onGenderChange("Male") })
        Text("Male")
        Spacer(modifier = Modifier.width(16.dp))
        RadioButton(selected = gender == "Female", onClick = { onGenderChange("Female") })
        Text("Female")
    }
    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = height,
        onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) onHeightChange(it) },
        label = { Text("Height (cm)") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        isError = height.isNotEmpty() && height.toFloatOrNull() == null,
        supportingText = {
            if (height.isNotEmpty() && height.toFloatOrNull() == null) {
                Text("Please enter a valid height")
            }
        }
    )
    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = weight,
        onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) onWeightChange(it) },
        label = { Text("Weight (kg)") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        isError = weight.isNotEmpty() && weight.toFloatOrNull() == null,
        supportingText = {
            if (weight.isNotEmpty() && weight.toFloatOrNull() == null) {
                Text("Please enter a valid weight")
            }
        }
    )
}

@Composable
fun ActivityStep(activityLevel: ActivityLevel, onActivityLevelChange: (ActivityLevel) -> Unit) {
    Text(
        text = "Your Activity Level",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))
    val levels = ActivityLevel.entries.toTypedArray()

    levels.forEach { level ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = activityLevel == level,
                onClick = { onActivityLevelChange(level) }
            )
            Text(text = level.name)
        }
    }
}
