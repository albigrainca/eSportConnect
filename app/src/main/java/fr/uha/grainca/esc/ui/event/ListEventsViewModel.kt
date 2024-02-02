package fr.uha.grainca.esc.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.grainca.esc.database.GameDatabaseSeeder
import fr.uha.grainca.esc.model.Event
import fr.uha.grainca.esc.model.Game
import fr.uha.grainca.esc.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListEventsViewModel @Inject constructor (
    private val repository: EventRepository
) : ViewModel() {
    fun feed() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            GameDatabaseSeeder().populate()
        }
    }

    fun clean() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            GameDatabaseSeeder().clear()
        }
    }

    fun delete(event: Event) = viewModelScope.launch {
        repository.delete(event)
    }

    val events = repository.getAll()

}