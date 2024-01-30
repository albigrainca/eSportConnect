package fr.uha.grainca.esc.ui.event

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import fr.uha.hassenforder.android.ui.AppMenu
import fr.uha.hassenforder.android.ui.AppMenuEntry
import fr.uha.hassenforder.android.ui.AppTitle
import fr.uha.hassenforder.android.ui.ErrorScreen
import fr.uha.hassenforder.android.ui.LoadingScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.uha.hassenforder.team.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventScreen(
    vm: EventViewModel = hiltViewModel(),
    eid : Long,
    back: () -> Unit,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val epoxy =  remember { mutableStateOf(false) }

    LaunchedEffect(vm.isLaunched) {
        if(!vm.isLaunched) {
            vm.edit(eid)
            vm.isLaunched = true
        }
    }

    val menuEntries = listOf (
        AppMenuEntry.ActionEntry(
            title = R.string.save,
            icon = Icons.Filled.Save,
            enabled = uiState.isSavable(),
            listener = { vm.save(); back() }
        ),
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { AppTitle (pageTitleId = R.string.title_event_edit, isModified = uiState.isModified()) },
                actions = { AppMenu(menuEntries) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (uiState.initialState) {
                EventViewModel.EventState.Loading -> {
                    LoadingScreen(text = stringResource(R.string.loading))
                }
                EventViewModel.EventState.Error -> {
                    ErrorScreen(text = stringResource(R.string.error))
                }
                is EventViewModel.EventState.Success -> {
                    SuccessEventScreen(uiState, vm.uiCallback)
                }
            }
        }
    }
}