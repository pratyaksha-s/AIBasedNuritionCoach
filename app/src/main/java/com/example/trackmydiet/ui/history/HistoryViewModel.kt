package com.example.trackmydiet.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trackmydiet.data.local.entities.Meal
import com.example.trackmydiet.data.local.entities.User
import com.example.trackmydiet.data.repository.MealRepository
import com.example.trackmydiet.data.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModel(
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

    fun setSelectedDate(date: Long) {
        _selectedDate.value = date
    }
}

class HistoryViewModelFactory(
    private val userRepository: UserRepository,
    private val mealRepository: MealRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(userRepository, mealRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
