package fr.uha.grainca.esc.ui.event

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.DoNotDisturb
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.Male
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import fr.uha.grainca.esc.model.Game
import fr.uha.grainca.esc.model.Genre

@Composable
fun EventGameItem (game: Game) {
    val genre : ImageVector =
        when (game.genre) {
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
                Text(game.name, modifier = Modifier.padding(end = 8.dp))
            }
        },
        supportingContent = {
            Row() {
                Text(game.description, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
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
        trailingContent = {
            Icon(imageVector = genre, contentDescription = "genre", modifier = Modifier.size(48.dp) )
        },
    )
}