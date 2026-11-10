package com.jeongmango.healthmemo.ui.gym

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jeongmango.healthmemo.data.Exercise
import com.jeongmango.healthmemo.data.GymSet
import com.jeongmango.healthmemo.data.InMemoryWorkoutRepository
import com.jeongmango.healthmemo.ui.theme.HealthMemoTheme
import java.util.Locale

@Composable
fun GymScreen(
    modifier: Modifier = Modifier,
    viewModel: GymViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GymContent(
    uiState = uiState,
    onAddSet = viewModel::onAddSet,
    onToggleSet = viewModel::onToggleSet,
    onDeleteSet = viewModel::onDeleteSet,
    modifier = modifier,
    )

}

@Composable
fun GymContent(
    uiState: GymUiState,
    onAddSet: (exerciseId: Long, weightKg: Double, reps: Int) -> Unit,
    onToggleSet: (exerciseId: Long, setId: Long) -> Unit,
    onDeleteSet: (exerciseId: Long, setId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                VolumeSummary(doneVolumeKg = uiState.doneVolumeKg)
            }
            items(uiState.exercises, key = { it.id }) { exercise ->
                ExerciseCard(
                    exercise = exercise,
                    onAddSet = { weightKg, reps -> onAddSet(exercise.id, weightKg, reps) },
                    onToggleSet = { setId -> onToggleSet(exercise.id, setId) },
                    onDeleteSet = { setId -> onDeleteSet(exercise.id, setId) },
                )
            }
        }
    }
}

@Composable
fun VolumeSummary(doneVolumeKg: Double, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "오늘의 헬스", style = MaterialTheme.typography.headlineSmall)
        Text(
            text = "완료 볼륨 ${doneVolumeKg.toKgText()}kg",
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    onAddSet: (weightKg: Double, reps: Int) -> Unit,
    onToggleSet: (setId: Long) -> Unit,
    onDeleteSet: (setId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = exercise.name, style = MaterialTheme.typography.titleMedium)
            exercise.sets.forEachIndexed { index, set ->
                key(set.id) {
                    SetRow(
                        setNumber = index + 1,
                        set = set,
                        onToggle = { onToggleSet(set.id) },
                        onDelete = { onDeleteSet(set.id) },
                    )
                }
            }
            AddSetInput(onAdd = onAddSet, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun SetRow(
    setNumber: Int,
    set: GymSet,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "${setNumber}세트", modifier = Modifier.width(48.dp))
        Text(
            text = "${set.weightKg.toKgText()}kg × ${set.reps}",
            modifier = Modifier.weight(1f),
        )
        Checkbox(checked = set.isDone, onCheckedChange = { onToggle() })
        IconButton(onClick = onDelete) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "세트 삭제")
        }
    }
}


@Composable
fun AddSetInput(
    onAdd: (weightKg: Double, reps: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var weightText by remember { mutableStateOf("") }
    var repsText by remember { mutableStateOf("") }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = weightText,
            onValueChange = { weightText = it },
            label = { Text("kg") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.weight(1f),
        )
        OutlinedTextField(
            value = repsText,
            onValueChange = { repsText = it },
            label = { Text("회") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
        )
        Button(
            onClick = {
                onAdd(weightText.toDoubleOrNull() ?: 0.0, repsText.toIntOrNull() ?: 0)
                repsText = ""
            },
        ) {
            Text("추가")
        }
    }
}

private fun Double.toKgText(): String =
    if (this % 1.0 == 0.0) {
        String.format(Locale.KOREA, "%,d", toLong())
    } else {
        String.format(Locale.KOREA, "%,.1f", this)
    }

@Preview(showBackground = true)
@Composable
private fun GymContentPreview() {
    HealthMemoTheme {
        GymContent(
            uiState = GymUiState(
                isLoading = false,
                exercises = listOf(
                    Exercise(
                        id = 1,
                        name = "벤치프레스",
                        sets = listOf(
                            GymSet(id = 1, weightKg = 60.0, reps = 10, isDone = true),
                            GymSet(id = 2, weightKg = 62.5, reps = 8),
                        ),
                    ),
                    Exercise(id = 2, name = "스쿼트"),
                ),
                doneVolumeKg = 600.0,
            ),
            onAddSet = { _, _, _ -> },
            onToggleSet = { _, _ -> },
            onDeleteSet = { _, _ -> },
        )
    }
}
