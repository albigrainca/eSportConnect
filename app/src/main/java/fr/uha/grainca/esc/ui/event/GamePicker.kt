package fr.uha.grainca.esc.ui.event

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
) {
    val list = vm.games.collectAsStateWithLifecycle(initialValue = emptyList())
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
                    items = list.value,
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
    val genre : ImageVector =
        when (game.genre) {
            Genre.ACTION -> Icons.Filled.FlashOn
            Genre.ADVENTURE -> Icons.Filled.Explore
            Genre.PUZZLE -> Icons.Filled.Extension
            Genre.SPORTS -> Icons.Filled.SportsSoccer
            Genre.STRATEGY -> Icons.Filled.Lightbulb
            Genre.RPG -> Icons.Filled.Password
            Genre.SIMULATION -> Icons.Filled.Build
            Genre.RACING -> Icons.Filled.DirectionsCar
            Genre.FIGHTING -> Icons.Filled.FitnessCenter
            Genre.HORROR -> Icons.Filled.VisibilityOff
            Genre.PLATFORMER -> Icons.Filled.Stairs
            Genre.SHOOTER -> Icons.Filled.Gesture
            Genre.MMO -> Icons.Filled.Group
            Genre.MUSIC -> Icons.Filled.MusicNote
            Genre.ARCADE -> Icons.Filled.Games
        }
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
        /*

                        supportingContent = {
                            Row() {
                                Text(game.description, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                },
                 */
        /*
                leadingContent = {
                    if (game.picture != null) {
                        AsyncImage(
                            model = game.picture,
                            modifier = Modifier.size(64.dp),
                            contentDescription = null,
                            error = rememberVectorPainter(Icons.Outlined.Error),
                            placeholder = rememberVectorPainter(Icons.Outlined.Casino),
                        )
                    }
        },
         */
        /*
                trailingContent = {
                    Icon(imageVector = genre, contentDescription = "genre", modifier = Modifier.size(48.dp) )
                },
         */
    )
}