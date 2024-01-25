package fr.uha.grainca.esc.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import fr.uha.grainca.esc.navigation.BottomBar
import fr.uha.grainca.esc.navigation.BottomNavGraph
import fr.uha.grainca.esc.ui.theme.ESportConnectTheme

@Composable
fun EventAppScreen() {
    val navController = rememberNavController()

    ESportConnectTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Scaffold(
                bottomBar = { BottomBar(navController = navController) }
            ) {
                    innerPadding -> BottomNavGraph(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
            }
        }
    }
}