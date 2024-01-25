package fr.uha.grainca.esc.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import fr.uha.hassenforder.team.R
import androidx.compose.runtime.Composable
import fr.uha.grainca.esc.TeamFileProvider
import fr.uha.grainca.esc.model.Genre
import fr.uha.hassenforder.android.ui.OutlinedDateFieldGame
import fr.uha.hassenforder.android.ui.PictureField
import fr.uha.hassenforder.android.ui.OutlinedSpinnerFieldEnum
import java.util.Date

@Composable
fun SuccessGameScreen (
    game: GameViewModel.GameUIState,
    uiCB: GameViewModel.GameUICallback
) {
    val context = LocalContext.current

    Column {
        OutlinedTextField(
            value = game.nameState.current ?: "",
            onValueChange = { uiCB.onEvent(GameViewModel.UIEvent.NameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(id = R.string.name))},
            supportingText = { if (game.nameState.errorId != null) Text(stringResource(id = game.nameState.errorId))},
            isError = game.nameState.errorId !=null,
        )
        OutlinedTextField(
            value = game.creatorState.current ?: "",
            onValueChange = { uiCB.onEvent(GameViewModel.UIEvent.CreatorChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(id = R.string.creator))},
            supportingText = { if (game.creatorState.errorId != null) Text(stringResource(id = game.creatorState.errorId))},
            isError = game.creatorState.errorId != null,
        )
        OutlinedDateFieldGame(
            value = game.releaseDateState.current ?: Date(),
            daysBefore = null,
            daysAfter = 7,
            onValueChange = { uiCB.onEvent(GameViewModel.UIEvent.ReleaseDateChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = R.string.releaseDate,
            errorId = game.releaseDateState.errorId
        )
        OutlinedSpinnerFieldEnum(
            value = game.genreState.current,
            onValueChange = { uiCB.onEvent(GameViewModel.UIEvent.GenreChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            enumValues = Genre.values(),
            labelId = R.string.genre,
            errorId = game.genreState.errorId
        )
        OutlinedTextField(
            value = game.descriptionState.current ?: "",
            onValueChange = { uiCB.onEvent(GameViewModel.UIEvent.DescriptionChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(id = R.string.description))},
            supportingText = { if (game.descriptionState.errorId != null) Text(stringResource(id = game.descriptionState.errorId))},
            isError = game.descriptionState.errorId != null,
        )
        PictureField(
            value = game.pictureState.current,
            onValueChange = { uiCB.onEvent(GameViewModel.UIEvent.PictureChanged(it)) },
            newImageUriProvider = { TeamFileProvider.getImageUri(context) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.picture,
            errorId = game.pictureState.errorId
        )



    }
}