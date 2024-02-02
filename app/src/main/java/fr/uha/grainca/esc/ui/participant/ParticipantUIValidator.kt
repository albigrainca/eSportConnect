package fr.uha.grainca.esc.ui.participant

import fr.uha.grainca.esc.model.GamerLevel
import fr.uha.grainca.esc.model.Genre
import fr.uha.hassenforder.team.R

object ParticipantUIValidator {

    fun validateGamerNameChange(newValue: String) : Int? {
        return when {
            newValue.isEmpty() -> R.string.value_empty
            newValue.isBlank() -> R.string.value_blank
            newValue.length < 2 -> R.string.value_too_short
            newValue.length > 32 -> R.string.value_too_long
            else -> null
        }
    }

    fun validateRealNameChange(newValue: String) : Int? {
        return when {
            newValue.isEmpty() -> R.string.value_empty
            newValue.isBlank() -> R.string.value_blank
            newValue.length < 2 -> R.string.value_too_short
            newValue.length > 32 -> R.string.value_too_long
            else -> null
        }
    }

    fun validateAgeChange(newValue: Int?): Int? {
        return when {
            newValue == null -> R.string.value_empty
            newValue < 18 -> R.string.invalid_age_too_young
            newValue > 100 -> R.string.invalid_age_too_old
            else -> null
        }
    }

    fun validateGamerLevelChange(newValue: GamerLevel?) : Int? {
        return when {
            newValue == null -> R.string.gamer_level_must_set
            else -> null
        }
    }


}