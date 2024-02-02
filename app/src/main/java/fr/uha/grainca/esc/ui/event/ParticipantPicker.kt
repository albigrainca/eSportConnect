package fr.uha.grainca.esc.ui.event

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brightness5
import androidx.compose.material.icons.outlined.Brightness6
import androidx.compose.material.icons.outlined.Brightness7
import androidx.compose.material.icons.outlined.BrightnessAuto
import androidx.compose.material.icons.outlined.BrightnessHigh
import androidx.compose.material.icons.outlined.DoNotDisturb
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.grainca.esc.database.GameDAO
import fr.uha.grainca.esc.database.ParticipantDAO
import fr.uha.grainca.esc.model.Game
import fr.uha.grainca.esc.model.GamerLevel
import fr.uha.grainca.esc.model.Genre
import fr.uha.grainca.esc.model.Participant
import fr.uha.grainca.esc.ui.participant.ParticipantItem
import fr.uha.hassenforder.android.ui.AppTitle
import fr.uha.hassenforder.team.R
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ParticipantPickerViewModel @Inject constructor (private val dao: ParticipantDAO): ViewModel() {

    val partcipants: Flow<List<Participant>> = dao.getAll()

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParticipantPicker (
    vm: ParticipantPickerViewModel = hiltViewModel(),
    @StringRes title : Int?,
    onSelect: (participant: Participant?) -> Unit,
    excludeParticipantIds: List<Long> = listOf(),

    ) {
    val list = vm.partcipants.collectAsStateWithLifecycle(initialValue = emptyList())

    val filteredPaticipants = if (excludeParticipantIds.isNotEmpty()) {
        list.value.filterNot { excludeParticipantIds.contains(it.pid) }
    } else {
        list.value
    }

    Dialog(onDismissRequest = { onSelect(null) }) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { AppTitle(pageTitleId = title?: R.string.participant_select) },
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(
                    items = filteredPaticipants,
                    key = { participant -> participant.pid }
                ) {
                        item -> ParticipantItem(item, onSelect)
                }
            }
        }
    }
}

@Composable
private fun ParticipantItem (participant: Participant, onSelect: (participant: Participant?) -> Unit) {
    ListItem (
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable(onClick = { onSelect(participant) }),
        headlineContent = {
            Row() {
                Text(participant.gamerName, modifier = Modifier.padding(end = 8.dp))
            }
        },
    )
}