package fr.uha.grainca.esc.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.grainca.esc.model.Game
import fr.uha.grainca.esc.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


class GameViewModel (private val repository: GameRepository): ViewModel() {
    private val _id : MutableStateFlow<Long> = MutableStateFlow(0)

    fun create (game : Game) = viewModelScope.launch {
        val pid : Long = repository.create(game)
        _id.emit(pid)
    }

    sealed interface GameState {
        data class Success (val game: Game) : GameState
        object Loading : GameState
        object Error : GameState
    }

    val gameState : StateFlow<GameState> = _id
        .flatMapLatest { id -> repository.getGameById(id) }
        .map {
            p -> if (p != null) {
                GameState.Success(p)
            } else {
                GameState.Error
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GameState.Loading)


}