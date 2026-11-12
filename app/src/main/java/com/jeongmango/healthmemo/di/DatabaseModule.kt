package com.jeongmango.healthmemo.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jeongmango.healthmemo.data.local.WorkoutDao
import com.jeongmango.healthmemo.data.local.WorkoutDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWorkoutDatabase(@ApplicationContext context: Context): WorkoutDatabase =
        Room.databaseBuilder(context, WorkoutDatabase::class.java, "workout.db")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    db.execSQL(
                        "INSERT INTO exercises (id, name) VALUES (1, '벤치프레스'), (2, '스쿼트'), (3, '랫풀다운')"
                    )
                }
            })
            .build()

    @Provides
    fun provideWorkoutDao(database: WorkoutDatabase): WorkoutDao = database.workoutDao()
}
