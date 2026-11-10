package com.jeongmango.healthmemo.ui.gym

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeongmango.healthmemo.data.Exercise
import com.jeongmango.healthmemo.data.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class GymViewModel @Inject constructor(
    private val repository: WorkoutRepository,
) : ViewModel() {

    val uiState: StateFlow<GymUiState> = repository.observeExercises()
        .map { exercises ->
            GymUiState(
                isLoading = false,
                exercises = exercises,
                doneVolumeKg = calculateDoneVolume(exercises),
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GymUiState(),
        )

    fun onAddSet(exerciseId: Long, weightKg: Double, reps: Int) {
        if (weightKg <= 0 || reps <= 0) return
        viewModelScope.launch {
            repository.addSet(exerciseId, weightKg, reps)
        }
    }

    fun onToggleSet(exerciseId: Long, setId: Long) {
        viewModelScope.launch {
            repository.toggleSet(exerciseId, setId)
        }
    }

    fun onDeleteSet(exerciseId: Long, setId: Long) {
        viewModelScope.launch {
            repository.deleteSet(exerciseId, setId)
        }
    }


    private fun calculateDoneVolume(exercises: List<Exercise>): Double =
        exercises.sumOf { exercise ->
            exercise.sets.filter { it.isDone }.sumOf { it.volumeKg }
        }
}