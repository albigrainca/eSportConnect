package fr.uha.grainca.esc.ui.participant

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.grainca.esc.model.Comparators
import fr.uha.grainca.esc.model.FullParticipant
import fr.uha.grainca.esc.model.Game
import fr.uha.grainca.esc.model.GamerLevel
import fr.uha.grainca.esc.model.Participant
import fr.uha.grainca.esc.repository.ParticipantRepository
import fr.uha.grainca.esc.ui.event.EventViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import fr.uha.hassenforder.android.kotlin.combine
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class ParticipantViewModel @Inject constructor (
    private val repository: ParticipantRepository
): ViewModel() {
    var isLaunched: Boolean = false

    @Immutable
    sealed interface ParticipantState {
        data class Success (val participant: FullParticipant) : ParticipantState
        object Loading : ParticipantState
        object Error : ParticipantState
    }

    data class FieldWrapper<T> (
        val current: T?=null,
        val errorId: Int?=null
    ) {
        companion object {
            fun buildGamerName(state: ParticipantUIState, newValue: String): FieldWrapper<String> {
                val errorId : Int? = ParticipantUIValidator.validateGamerNameChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildRealName(state: ParticipantUIState, newValue: String): FieldWrapper<String> {
                val errorId : Int? = ParticipantUIValidator.validateRealNameChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildAge(state: ParticipantUIState, newValue: Int): FieldWrapper<Int> {
                val errorId : Int? = ParticipantUIValidator.validateAgeChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildGamerLevel(state: ParticipantUIState, newValue: GamerLevel?): FieldWrapper<GamerLevel?> {
                val errorId : Int? = ParticipantUIValidator.validateGamerLevelChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildFavoriteGames(state : ParticipantUIState, newValue: List<Game>?): FieldWrapper<List<Game>> {
                val errorId : Int? = ParticipantUIValidator.validateFavoriteGamesChange(state, newValue)
                return FieldWrapper(newValue, errorId)
            }
        }
    }

    private val _gamerNameState = MutableStateFlow(FieldWrapper<String>())
    private val _realNameState = MutableStateFlow(FieldWrapper<String>())
    private val _ageState = MutableStateFlow(FieldWrapper<Int>())
    private val _gamerLevelState = MutableStateFlow(FieldWrapper<GamerLevel?>())
    private val _favoriteGamesState = MutableStateFlow(FieldWrapper<List<Game>>())


    private val _id: MutableStateFlow<Long> = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _initialParticipantState: StateFlow<ParticipantState> = _id
        .flatMapLatest { id -> repository.getFullParticipantById(id) }
        .map {
                p -> if (p != null) {
                    _gamerNameState.emit(FieldWrapper.buildGamerName(uiState.value, p.participant.gamerName))
                    _realNameState.emit(FieldWrapper.buildRealName(uiState.value, p.participant.realName))
                    _ageState.emit(FieldWrapper.buildAge(uiState.value, p.participant.age))
                    _gamerLevelState.emit(FieldWrapper.buildGamerLevel(uiState.value, p.participant.level))
                    _favoriteGamesState.emit(FieldWrapper.buildFavoriteGames(uiState.value, p.favoriteGames))
                    ParticipantState.Success(participant = p)
                } else {
                    ParticipantState.Error
                }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ParticipantState.Loading)

    private val _addFavoriteGameId: MutableSharedFlow<Long> = MutableSharedFlow(0)
    private val _delFavoriteGameId: MutableSharedFlow<Long> = MutableSharedFlow(0)

    init {
        _addFavoriteGameId
            .flatMapLatest { id -> repository.getGameById(id) }
            .map {
                g -> if (g != null) {
                    var fg : MutableList<Game>? = _favoriteGamesState.value.current?.toMutableList() ?: mutableListOf()
                    fg?.add(g)
                    _favoriteGamesState.emit(FieldWrapper.buildFavoriteGames(uiState.value, fg))
                }
            }
            .launchIn(viewModelScope)

        _delFavoriteGameId
            .map {
                var fg: MutableList<Game> = mutableListOf()
                _favoriteGamesState.value.current?.forEach { gg ->
                    if (gg.gid != it) fg.add(gg)
                }
                _favoriteGamesState.emit(FieldWrapper.buildFavoriteGames(uiState.value, fg))
            }
            .launchIn(viewModelScope)
    }

    data class ParticipantUIState (
        val initialState: ParticipantState,
        val gamerNameState: FieldWrapper<String>,
        val realNameState: FieldWrapper<String>,
        val ageState: FieldWrapper<Int>,
        val levelState: FieldWrapper<GamerLevel?>,
        val favoriteGames: FieldWrapper<List<Game>>,
        ) {
        private fun _isModified (): Boolean? {
            if (initialState !is ParticipantState.Success) return null
            if (gamerNameState.current != initialState.participant.participant.gamerName) return true
            if (realNameState.current != initialState.participant.participant.realName) return true
            if (ageState.current != initialState.participant.participant.age) return true
            if (levelState.current != initialState.participant.participant.level) return true
            if (! Comparators.shallowEqualsListGames(favoriteGames.current, initialState.participant.favoriteGames)) return true
            return false
        }

        private fun _hasError (): Boolean? {
            if (gamerNameState.errorId != null) return true
            if (realNameState.errorId != null) return true
            if (ageState.errorId != null) return true
            if (levelState.errorId != null) return true
            if (favoriteGames.errorId != null) return true
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

    val uiState : StateFlow<ParticipantUIState> = combine (
        _initialParticipantState,
        _gamerNameState,
        _realNameState,
        _ageState,
        _gamerLevelState,
        _favoriteGamesState
    ) { i, g, r, a, l, f -> ParticipantUIState(i, g, r, a, l, f) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ParticipantUIState(
            ParticipantState.Loading,
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
        )
    )

    sealed class UIEvent {
        data class GamerNameChanged(val newValue: String): UIEvent()
        data class RealNameChanged(val newValue: String): UIEvent()
        data class AgeChanged(val newValue: Int): UIEvent()
        data class LevelChanged(val newValue: GamerLevel?): UIEvent()
        data class FavoriteGamesAdded(val newValue: Long): UIEvent()
        data class FavoriteGamesDeleted(val newValue: Game): UIEvent()
    }

    data class ParticipantUICallback (
        val onEvent : (UIEvent) -> Unit,
    )

    val uiCallback = ParticipantUICallback (
        onEvent = {
            viewModelScope.launch {
                when (it) {
                    is UIEvent.GamerNameChanged -> _gamerNameState.emit(FieldWrapper.buildGamerName(uiState.value, it.newValue))
                    is UIEvent.RealNameChanged -> _realNameState.emit(FieldWrapper.buildRealName(uiState.value, it.newValue))
                    is UIEvent.AgeChanged -> _ageState.emit(FieldWrapper.buildAge(uiState.value, it.newValue))
                    is UIEvent.LevelChanged -> _gamerLevelState.emit(FieldWrapper.buildGamerLevel(uiState.value, it.newValue))
                    is UIEvent.FavoriteGamesAdded -> _addFavoriteGameId.emit(it.newValue)
                    is UIEvent.FavoriteGamesDeleted -> _delFavoriteGameId.emit(it.newValue.gid)
                }
            }
        }
    )

    fun edit (pid: Long) = viewModelScope.launch {
        _id.emit(pid)
    }

    fun create(participant: Participant) = viewModelScope.launch {
        val pid : Long = repository.createParticipant(participant)
        _id.emit(pid)
    }

    fun save() = viewModelScope.launch {
        if (_initialParticipantState.value !is ParticipantState.Success) return@launch
        val oldGame = _initialParticipantState.value as ParticipantState.Success
        val participant = FullParticipant (
            Participant (
                _id.value,
                _gamerNameState.value.current!!,
                _realNameState.value.current!!,
                _ageState.value.current!!,
                _gamerLevelState.value.current!!,
            ),
            favoriteGames = _favoriteGamesState.value.current!!,
        )
        repository.saveParticipant(oldGame.participant, participant)
    }

}