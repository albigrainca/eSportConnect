package fr.uha.grainca.esc.ui.participant

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Gesture
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Stairs
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.DoNotDisturb
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.Male
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
import fr.uha.grainca.esc.model.Game
import fr.uha.grainca.esc.model.Genre
import fr.uha.hassenforder.android.ui.AppTitle
import fr.uha.hassenforder.team.R
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class GamePickerViewModel @Inject constructor (private val dao: GameDAO): ViewModel() {

    val games: Flow<List<Game>> = dao.getAll()

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamePicker (
    vm: GamePickerViewModel = hiltViewModel(),
    @StringRes title : Int?,
    onSelect: (game : Game?) -> Unit,
    excludeGameIds: List<Long> = listOf(),
) {
    val list = vm.games.collectAsStateWithLifecycle(initialValue = emptyList())

    val filteredGames = if (excludeGameIds.isNotEmpty()) {
        list.value.filterNot { excludeGameIds.contains(it.gid) }
    } else {
        list.value
    }


    Dialog(onDismissRequest = { onSelect(null) }) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { AppTitle(pageTitleId = title?: R.string.game_select) },
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(
                    items = filteredGames,
                    key = { game -> game.gid }
                ) {
                        item -> GameItem(item, onSelect)
                }
            }
        }
    }
}

@Composable
private fun GameItem (game: Game, onSelect: (game : Game?) -> Unit) {
    ListItem (
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable(onClick = { onSelect(game) }),
        headlineContent = {
            Row() {
                Text(game.name, modifier = Modifier.padding(end = 8.dp))
            }
        },
    )
}