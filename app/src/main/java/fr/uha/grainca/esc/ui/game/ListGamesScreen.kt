package fr.uha.grainca.esc.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.DoNotDisturb
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import fr.uha.grainca.esc.model.Game
import fr.uha.grainca.esc.model.GameWithDetails
import fr.uha.grainca.esc.model.Genre
import fr.uha.grainca.esc.ui.event.UIConverter
import fr.uha.grainca.esc.ui.theme.ESportConnectTheme
import fr.uha.hassenforder.android.ui.AppMenu
import fr.uha.hassenforder.android.ui.AppMenuEntry
import fr.uha.hassenforder.android.ui.AppTitle
import fr.uha.hassenforder.android.ui.SwipeableItem
import fr.uha.hassenforder.team.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListGamesScreen(
    vm: ListGamesViewModel = hiltViewModel(),
    onCreate : () -> Unit,
    onEdit : (g : Game) -> Unit,
    ) {
    val games = vm.games.collectAsStateWithLifecycle(initialValue = emptyList())

    val menuEntries = listOf(
        AppMenuEntry.OverflowEntry(title = R.string.populate, listener = {vm.feed() } ),
        AppMenuEntry.OverflowEntry(title = R.string.clean, listener = {vm.clean() } )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AppTitle(pageTitleId = R.string.game_list)
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
                items = games.value,
                key = { game -> game.game.gid}
            ) { item ->
                SwipeableItem(
                    onEdit = { onEdit(item.game)},
                    onDelete = { vm.delete(item.game) },
                ) {
                    GameItem(item)
                }
            }
        }
    }

}

@Composable
fun GameItem(game: GameWithDetails) {
    val genre: ImageVector = when (game.game.genre) {
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
        headlineContent = {
            Row() {
                Text(game.game.name, modifier = Modifier.padding(end = 4.dp), fontWeight = FontWeight.ExtraBold)
            }
        },
        leadingContent = {
            AsyncImage(
                model = game.game.picture,
                modifier = Modifier.size(64.dp),
                contentDescription = "Selected image",
                error = rememberVectorPainter(Icons.Outlined.Error),
                placeholder = rememberVectorPainter(Icons.Outlined.Casino),
            )
        },

        trailingContent = {
            Icon(imageVector = genre, contentDescription = null, modifier = Modifier.size(48.dp))
        },
        supportingContent = {
            Column {
                Row () {
                    Text(game.game.creator, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Row () {
                    Text(UIConverter.secondConvert(game.game.releaseDate), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Row {
                    Text("Main Game Count: ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(game.mainGameCount.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold)

                }
                Row {
                    Text("Other Game Count: ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(game.otherGameCount.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}
