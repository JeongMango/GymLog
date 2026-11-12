package com.jeongmango.healthmemo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ExerciseEntity::class, GymSetEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
}
