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
                _pictureState.emit(FieldWrapper.buildPicture(uiState.value, g.picture))
                GameState.Success(game = g)
            } else {
                GameState.Error
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GameState.Loading)

    data class GameUIState (
        val initialState: GameState,
        val nameState: FieldWrapper<String>,
        val creatorState: FieldWrapper<String>,
        val releaseDateState: FieldWrapper<Date>,
        val genreState: FieldWrapper<Genre?>,
        val descriptionState: FieldWrapper<String>,
        val pictureState : FieldWrapper<Uri?>,
    ) {
        private fun _isModified (): Boolean? {
            if (initialState !is GameState.Success) return null
            if (nameState.current != initialState.game.name) return true
            if (creatorState.current != initialState.game.creator) return true
            if (releaseDateState.current != initialState.game.releaseDate) return true
            if (genreState.current != initialState.game.genre) return true
            if (descriptionState.current != initialState.game.description) return true
            if (pictureState.current != initialState.game.picture) return true
            if (pictureState.current != null) return true
            return false
        }

        private fun _hasError (): Boolean? {
            if (nameState.errorId != null) return true
            if (creatorState.errorId != null) return true
            if (releaseDateState.errorId != null) return true
            if (genreState.errorId != null) return true
            if (descriptionState.errorId != null) return true
            if (pictureState.errorId != null) return true
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
        _initialGameState, _nameState, _creatorState, _releaseDateState, _genreState, _descriptionState, _pictureState
    ) { i, n, c, r, g, d, p -> GameUIState(i, n, c, r, g, d, p) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GameUIState(
            GameState.Loading,
            FieldWrapper(),
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
        data class PictureChanged(val newValue: Uri?): UIEvent()
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
                    is UIEvent.PictureChanged -> _pictureState.emit(FieldWrapper.buildPicture(uiState.value, it.newValue))
                }
            }
        }
    )

    fun edit (gid: Long) = viewModelScope.launch {
        _id.emit(gid)
    }

    fun create(game: Game) = viewModelScope.launch {
        val gid : Long = repository.create(game)
        _id.emit(gid)
    }

    fun save() = viewModelScope.launch {
        if (_initialGameState.value !is GameState.Success) return@launch
        val oldGame = _initialGameState.value as GameState.Success
        val game = Game (
            _id.value,
            _nameState.value.current!!,
            _creatorState.value.current!!,
            _releaseDateState.value.current!!,
            _genreState.value.current!!,
            _descriptionState.value.current!!,
            _pictureState.value.current
        )
        repository.update(oldGame.game, game)
    }

}