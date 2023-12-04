package fr.uha.grainca.esc.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import fr.uha.hassenforder.team.R
import fr.uha.grainca.esc.model.Game
import androidx.compose.runtime.Composable
import fr.uha.grainca.esc.model.Genre
import fr.uha.hassenforder.android.ui.OutlinedEnumRadioGroup
import fr.uha.hassenforder.android.ui.OutlinedSpinnerFieldEnum

@Composable
fun SuccessGameScreen (
    game: Game
) {
    Column {
        OutlinedTextField(
            value = game.name,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(id = R.string.name))}
        )
        OutlinedTextField(
            value = game.creator,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(id = R.string.creator))}
        )
        OutlinedTextField(
            value = game.releaseDate,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(id = R.string.releaseDate))}
        )
        OutlinedSpinnerFieldEnum(
            value = game.genre,
            onValueChange = { /* Update game genre */ },
            modifier = Modifier.fillMaxWidth(),
            enumValues = Genre.values(),
            labelId = R.string.genre
        )
        OutlinedTextField(
            value = game.description,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(id = R.string.description))}
        )


    }
}