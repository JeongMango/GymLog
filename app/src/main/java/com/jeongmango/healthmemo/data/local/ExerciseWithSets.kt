package com.jeongmango.healthmemo.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.jeongmango.healthmemo.data.Exercise
import com.jeongmango.healthmemo.data.GymSet

data class ExerciseWithSets(
    @Embedded val exercise: ExerciseEntity,
    @Relation(parentColumn = "id", entityColumn = "exerciseId")
    val sets: List<GymSetEntity>,
)

fun ExerciseWithSets.toExercise(): Exercise = Exercise(
    id = exercise.id,
    name = exercise.name,
    sets = sets.sortedBy { it.id }.map { it.toGymSet() },
)

private fun GymSetEntity.toGymSet(): GymSet = GymSet(
    id = id,
    weightKg = weightKg,
    reps = reps,
    isDone = isDone,
)