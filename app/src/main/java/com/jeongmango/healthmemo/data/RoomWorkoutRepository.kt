package com.jeongmango.healthmemo.data

import com.jeongmango.healthmemo.data.local.GymSetEntity
import com.jeongmango.healthmemo.data.local.WorkoutDao
import com.jeongmango.healthmemo.data.local.toExercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomWorkoutRepository @Inject constructor(
    private val dao: WorkoutDao,
) : WorkoutRepository {

    override fun observeExercises(): Flow<List<Exercise>> =
        dao.observeExercisesWithSets().map { list -> list.map { it.toExercise() } }

    override suspend fun addSet(exerciseId: Long, weightKg: Double, reps: Int) {
        dao.insertSet(GymSetEntity(exerciseId = exerciseId, weightKg = weightKg, reps = reps))
    }

    override suspend fun toggleSet(exerciseId: Long, setId: Long) {
        dao.toggleSet(setId)
    }

    override suspend fun deleteSet(exerciseId: Long, setId: Long) {
        dao.deleteSet(setId)
    }
}
