package com.jeongmango.healthmemo.data

data class Exercise(
    val id: Long,
    val name: String,
    val sets: List<GymSet> = emptyList(),
)

data class GymSet(
    val id: Long,
    val weightKg: Double,
    val reps: Int,
    val isDone: Boolean = false,
) {
    val volumeKg: Double get() = weightKg * reps
}