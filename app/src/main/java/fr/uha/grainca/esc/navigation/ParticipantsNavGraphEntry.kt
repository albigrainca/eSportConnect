package fr.uha.grainca.esc.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import fr.uha.grainca.esc.model.Participant
import fr.uha.grainca.esc.ui.participant.CreateParticipantScreen
import fr.uha.grainca.esc.ui.participant.EditParticipantScreen
import fr.uha.grainca.esc.ui.participant.ListParticipantsScreen
import fr.uha.hassenforder.team.R

private sealed class ParticipantNavGraphEntry(
    val route: String,
    val title: Int,
) {

    // to list all participant
    object Participants: ParticipantNavGraphEntry(
        route = "participants",
        title = R.string.action_participant,
    )

    // to create a participant
    object Create: ParticipantNavGraphEntry(
        route = "participant",
        title = R.string.action_participant_create,
    )

    // to edit a participant
    object Edit: ParticipantNavGraphEntry(
        route = "participant/{pid}",
        title = R.string.action_participant_edit,
    ) {
        fun to (pid : Long) : String {
            return route.replace("{pid}", pid.toString())
        }
    }

}

fun NavGraphBuilder.participantsNavGraph (
    navController: NavHostController
) {
    navigation(ParticipantNavGraphEntry.Participants.route, BottomBarNavGraphEntry.Participants.route) {
        composable(route = ParticipantNavGraphEntry.Participants.route) {
            ListParticipantsScreen(
                onCreate = { navController.navigate(ParticipantNavGraphEntry.Create.route) },
                onEdit = { p : Participant -> navController.navigate(ParticipantNavGraphEntry.Edit.to(p.pid)) }
            )
        }
        composable(route = ParticipantNavGraphEntry.Create.route) {
            CreateParticipantScreen (back = { navController.popBackStack() } )
        }
        composable(
            route = ParticipantNavGraphEntry.Edit.route,
            arguments = listOf(navArgument("pid") { type = NavType.LongType })
        ) {
                backStackEntry ->
            EditParticipantScreen(pid = backStackEntry.arguments?.getLong("pid")!!, back = { navController.popBackStack() } )
        }
    }
}