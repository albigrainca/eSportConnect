package fr.uha.grainca.esc.ui.event

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import fr.uha.hassenforder.android.ui.OutlinedDateField
import fr.uha.hassenforder.android.ui.OutlinedSpinnerField
import fr.uha.hassenforder.team.R

@Composable
fun SuccessEventScreen(
    event: EventViewModel.EventUIState,
    uiCB: EventViewModel.EventUICallback
) {
    val showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        GamePicker(
            title = R.string.main_game_select,
            onSelect = { showDialog.value = false; if (it != null) uiCB.onEvent(EventViewModel.UIEvent.MainGameChanged(it.pid))}
        )
    }

    Column(
    ) {
        OutlinedTextField(
            value = event.name.current ?: "",
            onValueChange = { uiCB.onEvent(EventViewModel.UIEvent.NameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text (text = stringResource(R.string.eventname)) },
            supportingText = { if (event.name.errorId != null) Text(stringResource(id = event.name.errorId)) },
            isError = event.name.errorId != null,
        )
        OutlinedDateField(
            value = event.startDay.current,
            onValueChange = { uiCB.onEvent(EventViewModel.UIEvent.StartDayChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = R.string.start_day,
            errorId = event.startDay.errorId
        )
        OutlinedSpinnerField(
            value =event.duration.current.toString(),
            onValueChange = { try { val v : Int = it.toInt(); uiCB.onEvent(EventViewModel.UIEvent.DurationChanged(v)) } catch (e:Exception) {} },
            label = R.string.duration,
            option_views = stringArrayResource(id = R.array.durations),
            option_values = stringArrayResource(id = R.array.durations),
            errorId = event.duration.errorId,
        )
        MainGameField(
            value = event.mainGame.current,
            onValueChange = { if (it) showDialog.value = true else uiCB.onEvent(EventViewModel.UIEvent.MainGameChanged(null)) },
            label = R.string.maingame,
            errorId = event.mainGame.errorId,
        )
        ListOtherGamesField(
            value = event.otherGames.current,
            onAdd = { uiCB.onEvent(EventViewModel.UIEvent.OtherGamesAdded(it)) },
            onDelete = { uiCB.onEvent(EventViewModel.UIEvent.OtherGamesDeleted(it)) },
            errorId = event.otherGames.errorId
        )
    }

}