package fr.uha.grainca.esc.ui.event

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.uha.grainca.esc.model.Game

@Composable
fun MainGameField (
    value : Game?,
    onValueChange : (Boolean) -> Unit,
    modifier : Modifier = Modifier,
    @StringRes label: Int? = null,
    errorId : Int?,
) {
    Column(
        modifier = modifier
            .padding(top = 4.dp, bottom = 4.dp)
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color.Black), MaterialTheme.shapes.extraSmall)
            .padding(start = 16.dp)
            .clickable(
                onClick = { onValueChange(true) }
            ),
    ) {
        val color = if (errorId == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        if (label != null) {
            Row () {
                Text(text = stringResource(id = label), color = color, modifier = Modifier.weight(1.0F))
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "remove",
                    modifier = Modifier.clickable(
                        onClick = { onValueChange(false) }
                    )
                )
            }
        }
        if (value != null) {
            EventGameItem (value)
        }
        if (errorId != null){
            Text(
                text = stringResource(id = errorId),
                color = color,
            )
        }
    }
}