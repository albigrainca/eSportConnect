package fr.uha.grainca.esc.ui.event

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brightness5
import androidx.compose.material.icons.outlined.Brightness6
import androidx.compose.material.icons.outlined.Brightness7
import androidx.compose.material.icons.outlined.BrightnessAuto
import androidx.compose.material.icons.outlined.BrightnessHigh
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.uha.grainca.esc.model.GamerLevel
import fr.uha.grainca.esc.model.Participant

@Composable
fun EventParticipantItem (participant: Participant) {
    val level : ImageVector =
        when (participant.level) {
            GamerLevel.AMATEUR -> Icons.Outlined.BrightnessAuto
            GamerLevel.SEMIPRO -> Icons.Outlined.Brightness6
            GamerLevel.PRO -> Icons.Outlined.Brightness5
            GamerLevel.ELITE -> Icons.Outlined.Brightness7
            GamerLevel.LEGENDARY -> Icons.Outlined.BrightnessHigh
        }
    ListItem (
        headlineContent = {
            Row() {
                Text(participant.gamerName, modifier = Modifier.padding(end = 8.dp))
            }
        },
        supportingContent = {
            Row() {
                Text(participant.level.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Row() {
                Text(participant.age.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },

        trailingContent = {
            Icon(imageVector = level, contentDescription = "level", modifier = Modifier.size(48.dp) )
        },
    )
}