package com.jeongmango.healthmemo.data

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class InMemoryWorkoutRepository @Inject constructor(): WorkoutRepository {

    private val exercises = MutableStateFlow(
        listOf(
            Exercise(id = 1, name = "벤치프레스"),
            Exercise(id = 2, name = "스쿼트"),
            Exercise(id = 3, name = "랫풀다운"),
        )
    )
    private var nextSetId = 1L


    override fun observeExercises(): Flow<List<Exercise>> = exercises.asStateFlow()

    override suspend fun addSet(exerciseId: Long, weightKg: Double, reps: Int) {
        val newSet = GymSet(id = nextSetId++, weightKg = weightKg, reps = reps)
        updateExercise(exerciseId) { it.copy(sets = it.sets + newSet) }
    }


    override suspend fun toggleSet(exerciseId: Long, setId: Long) {
        updateExercise(exerciseId) { exercise ->
            exercise.copy(
                sets = exercise.sets.map { if (it.id == setId) it.copy(isDone = !it.isDone) else it }
            )
        }
    }

    override suspend fun deleteSet(exerciseId: Long, setId: Long) {
        updateExercise(exerciseId) { exercise ->
            exercise.copy(sets = exercise.sets.filterNot { it.id == setId })
        }
    }

    private fun updateExercise(exerciseId: Long, transform: (Exercise) -> Exercise) {
        exercises.update { list ->
            list.map { if (it.id == exerciseId) transform(it) else it }
        }
    }
}