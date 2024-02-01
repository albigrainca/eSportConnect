package fr.uha.grainca.esc.ui.participant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import fr.uha.grainca.esc.model.GamerLevel
import fr.uha.hassenforder.android.ui.OutlinedSpinnerField
import fr.uha.hassenforder.android.ui.OutlinedSpinnerFieldEnum
import fr.uha.hassenforder.team.R

@Composable
fun SuccessParticipantScreen (
    participant: ParticipantViewModel.ParticipantUIState,
    uiCB: ParticipantViewModel.ParticipantUICallback
) {
    val context = LocalContext.current

    Column {
        OutlinedTextField(
            value = participant.gamerNameState.current ?: "",
            onValueChange = { uiCB.onEvent(ParticipantViewModel.UIEvent.GamerNameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(id = R.string.gamer_name)) },
            supportingText = { if (participant.gamerNameState.errorId != null) Text(stringResource(id = participant.gamerNameState.errorId)) },
            isError = participant.gamerNameState.errorId !=null,
        )
        OutlinedTextField(
            value = participant.realNameState.current ?: "",
            onValueChange = { uiCB.onEvent(ParticipantViewModel.UIEvent.RealNameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(id = R.string.real_name)) },
            supportingText = { if (participant.realNameState.errorId != null) Text(stringResource(id = participant.realNameState.errorId)) },
            isError = participant.realNameState.errorId != null,
        )
        OutlinedSpinnerField(
            value = participant.ageState.current.toString(),
            onValueChange = { try { val v : Int = it.toInt(); uiCB.onEvent(ParticipantViewModel.UIEvent.AgeChanged(v)) } catch (e:Exception) {} },
            label = R.string.age,
            option_views = stringArrayResource(id = R.array.durations),
            option_values = stringArrayResource(id = R.array.durations),
            errorId = participant.ageState.errorId,
        )
        OutlinedSpinnerFieldEnum(
            value = participant.levelState.current,
            onValueChange = { uiCB.onEvent(ParticipantViewModel.UIEvent.LevelChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            enumValues = GamerLevel.values(),
            labelId = R.string.gamer_level,
            errorId = participant.levelState.errorId
        )
    }
}