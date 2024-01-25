package fr.uha.grainca.esc.ui.game

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.DoNotDisturb
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.Male
import androidx.compose.material.icons.outlined.Phone
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
import fr.uha.grainca.esc.model.Genre
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
    val game = vm.games.collectAsStateWithLifecycle(initialValue = emptyList())

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
                items = game.value,
                key = { game -> game }
            ) { item ->
                SwipeableItem(
                    onEdit = { onEdit(item)},
                    onDelete = {},
                ) {
                    GameItem(item)
                }
            }
        }
    }

}

@Composable
fun GameItem(game: Game) {
    val genre : ImageVector = when(game.genre) {
        Genre.ACTION -> Icons.Outlined.DoNotDisturb
        Genre.ADVENTURE -> Icons.Outlined.DoNotDisturb
        Genre.PUZZLE -> Icons.Outlined.DoNotDisturb
        Genre.SPORTS -> Icons.Outlined.DoNotDisturb
        Genre.STRATEGY -> Icons.Outlined.DoNotDisturb
        Genre.RPG -> Icons.Outlined.DoNotDisturb
        Genre.SIMULATION -> Icons.Outlined.DoNotDisturb
        Genre.RACING -> Icons.Outlined.DoNotDisturb
        Genre.FIGHTING -> Icons.Outlined.DoNotDisturb
        Genre.HORROR -> Icons.Outlined.DoNotDisturb
        Genre.PLATFORMER -> Icons.Outlined.DoNotDisturb
        Genre.SHOOTER -> Icons.Outlined.DoNotDisturb
        Genre.MMO -> Icons.Outlined.DoNotDisturb
        Genre.MUSIC -> Icons.Outlined.DoNotDisturb
        Genre.ARCADE -> Icons.Outlined.DoNotDisturb
    }

    ListItem (
        headlineContent = {
            Row() {
                Text(game.name, modifier = Modifier.padding(end = 4.dp))
            }
        },
        leadingContent = {
            AsyncImage(
                model = game.picture,
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
            Row () {
                Text(game.creator, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Row () {
                Text(game.releaseDate.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ListPreview() {
    ESportConnectTheme {
        val calendar = Calendar.getInstance()
        calendar.set(2023, Calendar.SEPTEMBER, 27)
        val specificDate = calendar.time

        GameItem(Game(
            0,
            "Counter-Strike 2",
            "Valve",
            specificDate,
            Genre.ACTION,
            "Counter-Strike 2 est un jeu vidéo de tir à la première personne multijoueur en ligne",
            null))
    }
}
