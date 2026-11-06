package com.jeongmango.healthmemo.ui.gym

import com.jeongmango.healthmemo.data.Exercise

data class GymUiState(
    val isLoading: Boolean = true,
    val exercises: List<Exercise> = emptyList(),
    val doneVolumeKg: Double = 0.0,
)