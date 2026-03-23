package com.example.trackmydiet.domain.calculator

import com.example.trackmydiet.domain.ActivityLevel

data class UserProfile(
    val name : String,
    val weight : Float,
    val height : Float,
    val age : Int,
    val gender : String,
    val activity : ActivityLevel
)