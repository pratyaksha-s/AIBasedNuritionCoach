package com.example.trackmydiet.ui.dashboard

import android.graphics.Bitmap
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trackmydiet.data.local.entities.Meal
import com.example.trackmydiet.data.local.entities.User
import com.example.trackmydiet.data.repository.MealRepository
import com.example.trackmydiet.data.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModel(
    private val userRepository: UserRepository,
    private val mealRepository: MealRepository
) : ViewModel() {

    val user: StateFlow<User?> = userRepository.user.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    private val _selectedDate = MutableStateFlow(System.currentTimeMillis())
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()

    val meals: StateFlow<List<Meal>> = _selectedDate
        .flatMapLatest { date -> mealRepository.getMealsForDate(date) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Idle)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun analyzeFood(description: String?, bitmap: Bitmap?) {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            try {
                val base64Image = bitmap?.let { imageToBase64(it) }
                mealRepository.analyzeAndSaveMeal(description, base64Image)
                _uiState.value = DashboardUiState.Success
            } catch (e: Exception) {
                _uiState.value = DashboardUiState.Error(e.message ?: "Analysis failed")
            }
        }
    }

    private fun imageToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    fun resetUiState() {
        _uiState.value = DashboardUiState.Idle
    }
}

class DashboardViewModelFactory(
    private val userRepository: UserRepository,
    private val mealRepository: MealRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(userRepository, mealRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

sealed class DashboardUiState {
    data object Idle : DashboardUiState()
    data object Loading : DashboardUiState()
    data object Success : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}
