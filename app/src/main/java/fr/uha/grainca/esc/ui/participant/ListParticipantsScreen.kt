package fr.uha.grainca.esc.ui.participant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Brightness5
import androidx.compose.material.icons.outlined.Brightness6
import androidx.compose.material.icons.outlined.Brightness7
import androidx.compose.material.icons.outlined.BrightnessAuto
import androidx.compose.material.icons.outlined.BrightnessHigh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.uha.grainca.esc.model.GamerLevel
import fr.uha.grainca.esc.model.Participant
import fr.uha.grainca.esc.model.ParticipantWithDetails
import fr.uha.hassenforder.android.ui.AppMenu
import fr.uha.hassenforder.android.ui.AppMenuEntry
import fr.uha.hassenforder.android.ui.AppTitle
import fr.uha.hassenforder.android.ui.SwipeableItem
import fr.uha.hassenforder.team.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListParticipantsScreen(
    vm: ListParticipantsViewModel = hiltViewModel(),
    onCreate : () -> Unit,
    onEdit : (p : Participant) -> Unit,
) {
    val participants = vm.participants.collectAsStateWithLifecycle(initialValue = emptyList())

    val menuEntries = listOf(
        AppMenuEntry.OverflowEntry(title = R.string.populate, listener = {vm.feed() } ),
        AppMenuEntry.OverflowEntry(title = R.string.clean, listener = {vm.clean() } )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AppTitle(pageTitleId = R.string.participants_list)
                },
                actions = { AppMenu(entries = menuEntries) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreate) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }
    ) {
            innerPadding -> LazyColumn(modifier = Modifier.padding(innerPadding)) {
        items(
            items = participants.value,
            key = { participant -> participant.participant.pid}
        ) { item ->
            SwipeableItem(
                onEdit = { onEdit(item.participant)},
                onDelete = {},
            ) {
                ParticipantItem(item)
            }
        }
    }
    }

}

@Composable
fun ParticipantItem(participant: ParticipantWithDetails) {
    val level : ImageVector = when(participant.participant.level) {
        GamerLevel.AMATEUR -> Icons.Outlined.BrightnessAuto
        GamerLevel.SEMIPRO -> Icons.Outlined.Brightness6
        GamerLevel.PRO -> Icons.Outlined.Brightness5
        GamerLevel.ELITE -> Icons.Outlined.Brightness7
        GamerLevel.LEGENDARY -> Icons.Outlined.BrightnessHigh
    }

    ListItem (
        headlineContent = {
            Row() {
                Text(participant.participant.gamerName, modifier = Modifier.padding(end = 4.dp))
            }
        },

        trailingContent = {
            Icon(imageVector = level, contentDescription = null, modifier = Modifier.size(48.dp))
        },

        supportingContent = {
            Column {
                Row () {
                    Text(participant.participant.age.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Row () {
                    Text(participant.participant.level.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Text(participant.guestCount.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    )
}