package fr.uha.grainca.esc.ui.participant

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Error
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
fun ParticipantGameItem (game: Game) {
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