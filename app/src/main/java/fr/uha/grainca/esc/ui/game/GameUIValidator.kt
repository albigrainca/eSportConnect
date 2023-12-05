package fr.uha.grainca.esc.ui.game

import android.net.Uri
import fr.uha.grainca.esc.model.Genre
import fr.uha.hassenforder.team.R
import java.util.*
import java.util.concurrent.TimeUnit


object GameUIValidator {

    fun validateNameChange(newValue: String) : Int? {
        return when {
            newValue.isEmpty() -> R.string.value_empty
            newValue.isBlank() -> R.string.value_blank
            newValue.length < 2 -> R.string.value_too_short
            newValue.length > 12 -> R.string.value_too_long
            else -> null
        }
    }

    fun validateCreatorChange(newValue: String) : Int? {
        return when {
            newValue.isEmpty() -> R.string.value_empty
            newValue.isBlank() -> R.string.value_blank
            newValue.length < 2 -> R.string.value_too_short
            newValue.length > 32 -> R.string.value_too_long
            else -> null
        }
    }

    fun validateDateChange(newValue: Date) : Int? {
        val currentDate = Calendar.getInstance()
        val maxPastDate = Calendar.getInstance().apply {
            add(Calendar.YEAR, -10) // 10 years in the past
        }
        val minFutureDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1) // One day in the future
        }

        return when {
            newValue.after(minFutureDate.time) -> R.string.date_too_future
            newValue.before(maxPastDate.time) -> R.string.date_too_past
            else -> null
        }
    }

    fun validateGenreChange(newValue: Genre?) : Int? {
        return when {
            newValue == null -> R.string.genre_must_set
            else -> null
        }
    }

    fun validateDescriptionChange(newValue: String) : Int? {
        return when {
            newValue.isEmpty() -> R.string.value_empty
            newValue.isBlank() -> R.string.value_blank
            newValue.length < 3 -> R.string.value_too_short
            else -> null
        }
    }

    fun validatePictureChange(newValue: Uri?) : Int? {
        return null
    }

}