package fr.uha.grainca.esc.ui.game

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.uha.grainca.esc.database.ESportDatabase
import fr.uha.grainca.esc.model.Game
import fr.uha.grainca.esc.model.Genre
import fr.uha.grainca.esc.repository.GameRepository
import fr.uha.hassenforder.android.ui.ErrorScreen
import fr.uha.hassenforder.android.ui.LoadingScreen
import java.util.Calendar
import java.util.Date

import fr.uha.hassenforder.team.R

@Composable
fun CreateGameScreen (
    vm : GameViewModel = GameViewModel(GameRepository(ESportDatabase.get().gameDAO))
) {

    val state by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = vm.isLaunched) {
        if (!vm.isLaunched) {
            val game = Game(0, "eSportConnect", "Albi Grainca", Date(), Genre.ACTION, "Plan you event with this app")
            vm.create(game)
            vm.isLaunched = true
        }
    }
    Scaffold (

    ) {
        Column (
            modifier = Modifier.padding(it)
        ) {
            when (state.initialState) {
                GameViewModel.GameState.Loading ->
                    LoadingScreen(text = stringResource(id = R.string.loading))
                GameViewModel.GameState.Error ->
                    ErrorScreen(text = stringResource(id = R.string.error))
                is GameViewModel.GameState.Success ->
                    SuccessGameScreen(state, vm.uiCallback)
            }
        }
    }

}