package fr.uha.grainca.esc.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.grainca.esc.database.GameDatabaseSeeder
import fr.uha.grainca.esc.model.Game
import fr.uha.grainca.esc.repository.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListGamesViewModel @Inject constructor(
    private val repository: GameRepository
) : ViewModel()
{
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

    fun delete(game: Game) = viewModelScope.launch {
        repository.delete(game)
    }

    val games = repository.getAll()
}