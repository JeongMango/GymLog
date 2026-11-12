package com.jeongmango.healthmemo.di

import com.jeongmango.healthmemo.data.RoomWorkoutRepository
import com.jeongmango.healthmemo.data.WorkoutRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindWorkoutRepository(impl: RoomWorkoutRepository): WorkoutRepository
}
