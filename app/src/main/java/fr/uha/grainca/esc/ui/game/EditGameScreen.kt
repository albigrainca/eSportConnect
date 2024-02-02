package fr.uha.grainca.esc.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.uha.hassenforder.android.ui.AppMenu
import fr.uha.hassenforder.android.ui.AppMenuEntry
import fr.uha.hassenforder.android.ui.AppTitle
import fr.uha.hassenforder.android.ui.ErrorScreen
import fr.uha.hassenforder.android.ui.LoadingScreen
import fr.uha.hassenforder.team.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGameScreen (
    vm : GameViewModel = hiltViewModel(),
    gid : Long,
    back : () -> Unit
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.edit(gid)
    }

    val menuEntries = listOf (
        AppMenuEntry.ActionEntry(
            title = R.string.save,
            icon = Icons.Filled.Save,
            enabled = state.isSavable(),
            listener = { vm.save(); back() }
        )
    )

    Scaffold (
        topBar = {
            TopAppBar(
                title = { AppTitle(appNameId = R.string.app_name, pageTitleId = R.string.game_edit, isModified = state.isModified()) },
                actions = { AppMenu(entries = menuEntries) }
            )
        }
    )
    {
        Column (
            modifier = Modifier.padding(it)
        ) {
            when (state.initialState) {
                GameViewModel.GameState.Loading ->
                    LoadingScreen(text = stringResource(id = R.string.loading))
                GameViewModel.GameState.Error ->
                    ErrorScreen(text = stringResource(id = R.string.error))
                is GameViewModel.GameState.Success ->
                    SuccessGameScreen(state, vm.uiCallback )
            }
        }
    }
}