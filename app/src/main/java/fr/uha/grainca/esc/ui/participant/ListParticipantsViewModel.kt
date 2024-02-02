package fr.uha.grainca.esc.ui.participant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.grainca.esc.database.GameDatabaseSeeder
import fr.uha.grainca.esc.model.Game
import fr.uha.grainca.esc.model.Participant
import fr.uha.grainca.esc.repository.ParticipantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListParticipantsViewModel @Inject constructor(
    private val repository: ParticipantRepository
) : ViewModel()
{
    fun feed() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            GameDatabaseSeeder().populateParticipant()
        }
    }

    fun clean() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            GameDatabaseSeeder().clear()
        }
    }

    fun delete(participant: Participant) = viewModelScope.launch {
        repository.delete(participant)
    }

    val participants = repository.getAll()
}