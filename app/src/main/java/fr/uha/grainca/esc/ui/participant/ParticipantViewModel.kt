package fr.uha.grainca.esc.ui.participant

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.grainca.esc.model.GamerLevel
import fr.uha.grainca.esc.model.Participant
import fr.uha.grainca.esc.repository.ParticipantRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import fr.uha.hassenforder.android.kotlin.combine
import javax.inject.Inject

@HiltViewModel
class ParticipantViewModel @Inject constructor (
    private val repository: ParticipantRepository
): ViewModel() {
    var isLaunched: Boolean = false

    @Immutable
    sealed interface ParticipantState {
        data class Success (val participant: Participant) : ParticipantState
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
        }
    }

    private val _gamerNameState = MutableStateFlow(FieldWrapper<String>())
    private val _realNameState = MutableStateFlow(FieldWrapper<String>())
    private val _ageState = MutableStateFlow(FieldWrapper<Int>())
    private val _gamerLevelState = MutableStateFlow(FieldWrapper<GamerLevel?>())

    private val _id: MutableStateFlow<Long> = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _initialParticipantState: StateFlow<ParticipantState> = _id
        .flatMapLatest { id -> repository.getParticipantById(id) }
        .map {
                p -> if (p != null) {
                    _gamerNameState.emit(FieldWrapper.buildGamerName(uiState.value, p.gamerName))
                    _realNameState.emit(FieldWrapper.buildRealName(uiState.value, p.realName))
                    _ageState.emit(FieldWrapper.buildAge(uiState.value, p.age))
                    _gamerLevelState.emit(FieldWrapper.buildGamerLevel(uiState.value, p.level))
                    ParticipantState.Success(participant = p)
                } else {
                    ParticipantState.Error
                }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ParticipantState.Loading)

    data class ParticipantUIState (
        val initialState: ParticipantState,
        val gamerNameState: FieldWrapper<String>,
        val realNameState: FieldWrapper<String>,
        val ageState: FieldWrapper<Int>,
        val levelState: FieldWrapper<GamerLevel?>,
    ) {
        private fun _isModified (): Boolean? {
            if (initialState !is ParticipantState.Success) return null
            if (gamerNameState.current != initialState.participant.gamerName) return true
            if (realNameState.current != initialState.participant.realName) return true
            if (ageState.current != initialState.participant.age) return true
            if (levelState.current != initialState.participant.level) return true
            return false
        }

        private fun _hasError (): Boolean? {
            if (gamerNameState.errorId != null) return true
            if (realNameState.errorId != null) return true
            if (ageState.errorId != null) return true
            if (levelState.errorId != null) return true
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
        _initialParticipantState, _gamerNameState, _realNameState, _ageState, _gamerLevelState
    ) { i, g, r, a, l -> ParticipantUIState(i, g, r, a, l) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ParticipantUIState(
            ParticipantState.Loading,
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
                }
            }
        }
    )

    fun edit (pid: Long) = viewModelScope.launch {
        _id.emit(pid)
    }

    fun create(participant: Participant) = viewModelScope.launch {
        val pid : Long = repository.create(participant)
        _id.emit(pid)
    }

    fun save() = viewModelScope.launch {
        if (_initialParticipantState.value !is ParticipantState.Success) return@launch
        val oldGame = _initialParticipantState.value as ParticipantState.Success
        val participant = Participant (
            _id.value,
            _gamerNameState.value.current!!,
            _realNameState.value.current!!,
            _ageState.value.current!!,
            _gamerLevelState.value.current!!,
        )
        repository.update(oldGame.participant, participant)
    }

}