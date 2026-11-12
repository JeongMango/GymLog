package com.jeongmango.healthmemo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Transaction
    @Query("SELECT * FROM exercises ORDER BY id")
    fun observeExercisesWithSets(): Flow<List<ExerciseWithSets>>

    @Insert
    suspend fun insertSet(set: GymSetEntity)

    @Query("UPDATE gym_sets SET isDone = NOT isDone WHERE id = :setId")
    suspend fun toggleSet(setId: Long)

    @Query("DELETE FROM gym_sets WHERE id = :setId")
    suspend fun deleteSet(setId: Long)
}
