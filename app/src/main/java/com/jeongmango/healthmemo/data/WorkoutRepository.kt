package com.jeongmango.healthmemo.data

import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun observeExercises(): Flow<List<Exercise>>
    suspend fun addSet(exerciseId: Long, weightKg: Double, reps: Int)
    suspend fun toggleSet(exerciseId: Long, setId: Long)
    suspend fun deleteSet(exerciseId: Long, setId: Long)
}
