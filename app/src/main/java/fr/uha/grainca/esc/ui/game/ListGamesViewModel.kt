package fr.uha.grainca.esc.ui.game

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.grainca.esc.repository.GameRepository
import javax.inject.Inject

@HiltViewModel
class ListGamesViewModel @Inject constructor(
    private val repository: GameRepository
) : ViewModel()
{
    val games = repository.getAll()
}