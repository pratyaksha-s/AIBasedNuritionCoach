package com.example.trackmydiet.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trackmydiet.data.local.entities.User
import com.example.trackmydiet.data.local.entities.WeightEntry
import com.example.trackmydiet.data.repository.UserRepository
import com.example.trackmydiet.data.repository.WeightRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val weightRepository: WeightRepository
) : ViewModel() {

    val user: StateFlow<User?> = userRepository.user.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val weightEntries: StateFlow<List<WeightEntry>> = weightRepository.allWeightEntries.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addWeightEntry(weight: Float) {
        viewModelScope.launch {
            weightRepository.addWeightEntry(weight)
        }
    }
}

class ProfileViewModelFactory(
    private val userRepository: UserRepository,
    private val weightRepository: WeightRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userRepository, weightRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
