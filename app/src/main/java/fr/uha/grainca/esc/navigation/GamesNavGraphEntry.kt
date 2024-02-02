package fr.uha.grainca.esc.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import fr.uha.grainca.esc.model.Game
import fr.uha.grainca.esc.ui.game.CreateGameScreen
import fr.uha.grainca.esc.ui.game.EditGameScreen
import fr.uha.grainca.esc.ui.game.ListGamesScreen
import fr.uha.hassenforder.team.R

private sealed class GameNavGraphEntry(
    val route: String,
    val title: Int,
) {

    // to list all games
    object Games: GameNavGraphEntry(
        route = "games",
        title = R.string.action_games,
    )

    // to create a games
    object Create: GameNavGraphEntry(
        route = "game",
        title = R.string.action_game_create,
    )

    // to edit a person
    object Edit: GameNavGraphEntry(
        route = "game/{gid}",
        title = R.string.action_game_edit,
    ) {
        fun to (gid : Long) : String {
            return route.replace("{gid}", gid.toString())
        }
    }

}

fun NavGraphBuilder.gamesNavGraph (
    navController: NavHostController
) {
    navigation(GameNavGraphEntry.Games.route, BottomBarNavGraphEntry.Games.route) {
        composable(route = GameNavGraphEntry.Games.route) {
            ListGamesScreen(
                onCreate = { navController.navigate(GameNavGraphEntry.Create.route) },
                onEdit = { g : Game -> navController.navigate(GameNavGraphEntry.Edit.to(g.gid)) }
            )
        }
        composable(route = GameNavGraphEntry.Create.route) {
            CreateGameScreen (back = { navController.popBackStack() } )
        }
        composable(
            route = GameNavGraphEntry.Edit.route,
            arguments = listOf(navArgument("gid") { type = NavType.LongType })
        ) {
                backStackEntry ->
            EditGameScreen(gid = backStackEntry.arguments?.getLong("gid")!!, back = { navController.popBackStack() } )
        }
    }
}