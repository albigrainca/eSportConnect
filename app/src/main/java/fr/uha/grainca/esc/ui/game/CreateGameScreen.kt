package fr.uha.grainca.esc.ui.game

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import fr.uha.grainca.esc.model.Game
import fr.uha.grainca.esc.model.Genre

@Composable
fun CreateGameScreen () {
    Scaffold (

    ){
        Column (
            modifier = Modifier.padding(it)
        ) {
            SuccessGameScreen(Game(0, "csgo", "albi", "22.03.2055", Genre.ACTION, "Le meilleur jeu"))
        }
    }

}