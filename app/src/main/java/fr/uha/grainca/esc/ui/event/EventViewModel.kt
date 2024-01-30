package fr.uha.grainca.esc.ui.event

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.grainca.esc.model.Comparators
import fr.uha.grainca.esc.model.Event
import fr.uha.grainca.esc.model.FullEvent
import fr.uha.grainca.esc.model.Game
import fr.uha.grainca.esc.repository.EventRepository
import fr.uha.hassenforder.android.kotlin.combine
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor (
    private val repository : EventRepository
): ViewModel() {

    var isLaunched: Boolean = false

    @Immutable
    sealed interface EventState {
        data class Success(val event: FullEvent) : EventState
        object Loading : EventState
        object Error : EventState
    }

    data class FieldWrapper<T>(
        val current: T? = null,
        val errorId: Int? = null
    ) {
        companion object {
            fun buildName(state : EventUIState, newValue: String): FieldWrapper<String> {
                val errorId : Int? = EventUIValidator.validateNameChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildStartDay(state : EventUIState, newValue: Date): FieldWrapper<Date> {
                val errorId : Int? = EventUIValidator.validateStartDayChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildDuration(state : EventUIState, newValue: Int): FieldWrapper<Int> {
                val errorId : Int? = EventUIValidator.validateDurationChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildMainGame(state : EventUIState, newValue: Game?): FieldWrapper<Game> {
                val errorId : Int? = EventUIValidator.validateMainGameChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildOtherGames(state : EventUIState, newValue: List<Game>?): FieldWrapper<List<Game>> {
                val errorId : Int? = EventUIValidator.validateOtherGamesChange(state, newValue)
                return FieldWrapper(newValue, errorId)
            }
        }
    }

    private val _nameState = MutableStateFlow(FieldWrapper<String>())
    private val _startDayState = MutableStateFlow(FieldWrapper<Date>())
    private val _durationState = MutableStateFlow(FieldWrapper<Int>())
    private val _mainGameState = MutableStateFlow(FieldWrapper<Game>())
    private val _otherGamesState = MutableStateFlow(FieldWrapper<List<Game>>())

    private val _eventId: MutableStateFlow<Long> = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _initialEventState: StateFlow<EventState> = _eventId
        .flatMapLatest { id -> repository.getEventById(id) }
        .map { e ->
            if (e != null) {
                _nameState.emit(FieldWrapper.buildName(uiState.value, e.event.name))
                _startDayState.emit(FieldWrapper.buildStartDay(uiState.value, e.event.startDay))
                _durationState.emit(FieldWrapper.buildDuration(uiState.value, e.event.duration))
                _mainGameState.emit(FieldWrapper.buildMainGame(uiState.value, e.mainGame))
                _otherGamesState.emit(FieldWrapper.buildOtherGames(uiState.value, e.otherGames))
                EventState.Success(event = e)
            } else {
                EventState.Error
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EventState.Loading)

    private val _updateMainGameId: MutableSharedFlow<Long> = MutableSharedFlow(0)
    private val _addOtherGameId: MutableSharedFlow<Long> = MutableSharedFlow(0)
    private val _delOtherGameId: MutableSharedFlow<Long> = MutableSharedFlow(0)

    init {
        _updateMainGameId
            .flatMapLatest { id -> repository.getGameById(id) }
            .map { g ->
                if (g != null) {
                    _mainGameState.emit(FieldWrapper.buildMainGame(uiState.value, g))
                } else {
                    _mainGameState.emit(FieldWrapper.buildMainGame(uiState.value, null))
                }
            }
            .launchIn(viewModelScope)

        _addOtherGameId
            .flatMapLatest { id -> repository.getGameById(id) }
            .map {
                    g -> if (g != null) {
                var og : MutableList<Game>? = _otherGamesState.value.current?.toMutableList() ?: mutableListOf()
                og?.add(g)
                _otherGamesState.emit(FieldWrapper.buildOtherGames(uiState.value, og))
            }
            }
            .launchIn(viewModelScope)

        _delOtherGameId
            .map {
                var og: MutableList<Game> = mutableListOf()
                _otherGamesState.value.current?.forEach { gg ->
                    if (gg.pid != it) og.add(gg)
                }
                _otherGamesState.emit(FieldWrapper.buildOtherGames(uiState.value, og))
            }
            .launchIn(viewModelScope)
    }

    data class EventUIState(
        val initialState: EventState,
        val name: FieldWrapper<String>,
        val startDay: FieldWrapper<Date>,
        val duration: FieldWrapper<Int>,
        val mainGame: FieldWrapper<Game>,
        val otherGames: FieldWrapper<List<Game>>,
    ) {
        private fun _isModified(): Boolean? {
            if (initialState !is EventState.Success) return null
            if (name.current != initialState.event.event.name) return true
            if (name.current != initialState.event.event.name) return true
            if (startDay.current != initialState.event.event.startDay) return true
            if (duration.current != initialState.event.event.duration) return true
            if (! Comparators.shallowEqualsGame(mainGame.current, initialState.event.mainGame)) return true
            if (! Comparators.shallowEqualsListGames(otherGames.current, initialState.event.otherGames)) return true
            return false
        }

        private fun _hasError(): Boolean? {
            if (name.errorId != null) return true
            if (startDay.errorId != null) return true
            if (duration.errorId != null) return true
            if (mainGame.errorId != null) return true
            if (otherGames.errorId != null) return true
            return false
        }

        fun isModified(): Boolean {
            val isModified = _isModified()
            if (isModified == null) return false
            return isModified
        }

        fun isSavable(): Boolean {
            val hasError = _hasError()
            if (hasError == null) return false
            val isModified = _isModified()
            if (isModified == null) return false
            return ! hasError && isModified
        }
    }

    val uiState : StateFlow<EventUIState> = combine (
        _initialEventState,
        _nameState, _startDayState, _durationState,
        _mainGameState,
        _otherGamesState
    ) { initial, n, s, d, mg, og -> EventUIState(initial, n, s, d, mg, og) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EventUIState(
            EventState.Loading,
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
        )
    )

    sealed class UIEvent {
        data class NameChanged(val newValue: String): UIEvent()
        data class StartDayChanged(val newValue: Date): UIEvent()
        data class DurationChanged(val newValue: Int): UIEvent()
        data class MainGameChanged(val newValue: Long?): UIEvent()
        data class OtherGamesAdded(val newValue: Long): UIEvent()
        data class OtherGamesDeleted(val newValue: Game): UIEvent()
    }

    data class EventUICallback(
        val onEvent : (UIEvent) -> Unit,
    )

    val uiCallback = EventUICallback(
        onEvent = {
            viewModelScope.launch {
                when (it) {
                    is UIEvent.NameChanged -> _nameState.emit(FieldWrapper.buildName(uiState.value, it.newValue))
                    is UIEvent.StartDayChanged -> _startDayState.emit(FieldWrapper.buildStartDay(uiState.value, it.newValue))
                    is UIEvent.DurationChanged -> _durationState.emit(FieldWrapper.buildDuration(uiState.value, it.newValue))
                    is UIEvent.MainGameChanged -> {
                        if (it.newValue != null) _updateMainGameId.emit(it.newValue)
                        else _mainGameState.emit(FieldWrapper.buildMainGame(uiState.value, null))
                    }
                    is UIEvent.OtherGamesAdded -> _addOtherGameId.emit(it.newValue)
                    is UIEvent.OtherGamesDeleted -> _delOtherGameId.emit(it.newValue.pid)
                }
            }
        }
    )

    fun edit(pid: Long) = viewModelScope.launch {
        _eventId.emit(pid)
    }

    fun create(event: Event) = viewModelScope.launch {
        val pid : Long = repository.createEvent(event)
        _eventId.emit(pid)
    }

    fun save() = viewModelScope.launch {
        if (_initialEventState.value !is EventState.Success) return@launch
        val oldEvent = _initialEventState.value as EventState.Success
        val event = FullEvent (
            Event (
                eid = _eventId.value,
                name = _nameState.value.current!!,
                startDay = _startDayState.value.current!!,
                duration = _durationState.value.current!!,
                mainGameId = _mainGameState.value.current?.pid ?: 0
            ),
            mainGame = _mainGameState.value.current,
            otherGames = _otherGamesState.value.current!!
        )
        repository.saveEvent(oldEvent.event, event)
    }

}
