package fr.uha.grainca.esc.ui.game

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.grainca.esc.model.Game
import fr.uha.grainca.esc.model.Genre
import fr.uha.grainca.esc.repository.GameRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Date
import fr.uha.hassenforder.android.kotlin.combine
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor (
    private val repository: GameRepository
): ViewModel() {
    var isLaunched: Boolean = false

    @Immutable
    sealed interface GameState {
        data class Success (val game: Game) : GameState
        object Loading : GameState
        object Error : GameState
    }

    data class FieldWrapper<T> (
        val current: T?=null,
        val errorId: Int?=null
    ) {
        companion object {
            fun buildName(state: GameUIState, newValue: String): FieldWrapper<String> {
                val errorId : Int? = GameUIValidator.validateNameChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildCreator(state: GameUIState, newValue: String): FieldWrapper<String> {
                val errorId : Int? = GameUIValidator.validateCreatorChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildReleaseDate(state: GameUIState, newValue: Date): FieldWrapper<Date> {
                val errorId : Int? = GameUIValidator.validateDateChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildGenre(state: GameUIState, newValue: Genre?): FieldWrapper<Genre?> {
                val errorId : Int? = GameUIValidator.validateGenreChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildDescription(state: GameUIState, newValue: String): FieldWrapper<String> {
                val errorId : Int? = GameUIValidator.validateDescriptionChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildPicture(state: GameUIState, newValue: Uri?): FieldWrapper<Uri?> {
                val errorId : Int? = GameUIValidator.validatePictureChange(newValue)
                return FieldWrapper(newValue, errorId)
            }
        }
    }

    private val _nameState = MutableStateFlow(FieldWrapper<String>())
    private val _creatorState = MutableStateFlow(FieldWrapper<String>())
    private val _releaseDateState = MutableStateFlow(FieldWrapper<Date>())
    private val _genreState = MutableStateFlow(FieldWrapper<Genre?>())
    private val _descriptionState = MutableStateFlow(FieldWrapper<String>())
    private val _pictureState = MutableStateFlow(FieldWrapper<Uri?>())

    private val _id: MutableStateFlow<Long> = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _initialGameState: StateFlow<GameState> = _id
        .flatMapLatest { id -> repository.getGameById(id) }
        .map {
            g -> if (g != null) {
                _nameState.emit(FieldWrapper.buildName(uiState.value, g.name))
                _creatorState.emit(FieldWrapper.buildCreator(uiState.value, g.creator))
                _releaseDateState.emit(FieldWrapper.buildReleaseDate(uiState.value, g.releaseDate))
                _genreState.emit(FieldWrapper.buildGenre(uiState.value, g.genre))
                _descriptionState.emit(FieldWrapper.buildDescription(uiState.value, g.description))
                GameState.Success(game = g)
            } else {
                GameState.Error
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GameState.Loading)

    data class GameUIState (
        val initialState: GameState,
        val name: FieldWrapper<String>,
        val creator: FieldWrapper<String>,
        val releaseDate: FieldWrapper<Date>,
        val genre: FieldWrapper<Genre?>,
        val description: FieldWrapper<String>,
    ) {
        private fun _isModified (): Boolean? {
            if (initialState !is GameState.Success) return null
            if (name.current != initialState.game.name) return true
            if (creator.current != initialState.game.creator) return true
            if (releaseDate.current != initialState.game.releaseDate) return true
            if (genre.current != initialState.game.genre) return true
            if (description.current != initialState.game.description) return true
            return false
        }

        private fun _hasError (): Boolean? {
            if (name.errorId != null) return true
            if (creator.errorId != null) return true
            if (releaseDate.errorId != null) return true
            if (genre.errorId != null) return true
            if (description.errorId != null) return true
            return false
        }

        fun isModified (): Boolean {
            val isModified = _isModified()
            if (isModified == null) return false
            return isModified
        }

        fun isSavable (): Boolean {
            val hasError = _hasError()
            if (hasError == null) return false
            val isModified = _isModified()
            if (isModified == null) return false
            return ! hasError && isModified
        }
    }

    val uiState : StateFlow<GameUIState> = combine (
        _initialGameState, _nameState, _creatorState, _releaseDateState, _genreState, _descriptionState
    ) { i, n, c, r, g, d -> GameUIState(i, n, c, r, g, d) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GameUIState(
            GameState.Loading,
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
        )
    )

    sealed class UIEvent {
        data class NameChanged(val newValue: String): UIEvent()
        data class CreatorChanged(val newValue: String): UIEvent()
        data class ReleaseDateChanged(val newValue: Date): UIEvent()
        data class GenreChanged(val newValue: Genre?): UIEvent()
        data class DescriptionChanged(val newValue: String): UIEvent()
    }

    data class GameUICallback (
        val onEvent : (UIEvent) -> Unit,
    )

    val uiCallback = GameUICallback (
        onEvent = {
            viewModelScope.launch {
                when (it) {
                    is UIEvent.NameChanged -> _nameState.emit(FieldWrapper.buildName(uiState.value, it.newValue))
                    is UIEvent.CreatorChanged -> _creatorState.emit(FieldWrapper.buildCreator(uiState.value, it.newValue))
                    is UIEvent.ReleaseDateChanged -> _releaseDateState.emit(FieldWrapper.buildReleaseDate(uiState.value, it.newValue))
                    is UIEvent.GenreChanged -> _genreState.emit(FieldWrapper.buildGenre(uiState.value, it.newValue))
                    is UIEvent.DescriptionChanged -> _descriptionState.emit(FieldWrapper.buildDescription(uiState.value, it.newValue))
                }
            }
        }
    )

    fun edit (pid: Long) = viewModelScope.launch {
        _id.emit(pid)
    }

    fun create(game: Game) = viewModelScope.launch {
        val pid : Long = repository.create(game)
        _id.emit(pid)
    }

}