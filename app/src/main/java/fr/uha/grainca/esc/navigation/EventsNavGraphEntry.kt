package fr.uha.grainca.esc.navigation

//import androidx.navigation.NavType
//import androidx.navigation.compose.composable
//import androidx.navigation.navArgument
//import androidx.navigation.navigation
//import fr.uha.grainca.esc.model.Event
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.NavHostController
//import fr.uha.hassenforder.team.R
//
//
//private sealed class EventNavGraphEntry(
//    val route: String,
//    val title: Int,
//) {
//
//    // to list all teams
//    object Events: EventNavGraphEntry(
//        route = "events",
//        title = R.string.action_events,
//    )
//
//    // to create a team
//    object Create: EventNavGraphEntry(
//        route = "team",
//        title = R.string.action_event_create,
//    )
//
//    // to edit a team
//    object Edit: EventNavGraphEntry(
//        route = "event/{eid}",
//        title = R.string.action_event_edit,
//    ) {
//        fun to (tid : Long) : String {
//            return route.replace("{eid}", tid.toString())
//        }
//    }
//
//}
//
//fun NavGraphBuilder.eventsNavGraph (
//    navController: NavHostController
//) {
//    navigation(EventNavGraphEntry.Events.route, BottomBarNavGraphEntry.Events.route) {
//        composable(route = EventNavGraphEntry.Events.route) {
//            ListEventsScreen(
//                onCreate = { navController.navigate(EventNavGraphEntry.Create.route) },
//                onEdit = { e : Event -> navController.navigate(EventNavGraphEntry.Edit.to(e.eid)) }
//            )
//        }
//        composable(route = EventNavGraphEntry.Create.route) {
//            CreateEventScreen (back = { navController.popBackStack() } )
//        }
//        composable(
//            route = EventNavGraphEntry.Edit.route,
//            arguments = listOf(navArgument("eid") { type = NavType.LongType })
//        ) {
//                backStackEntry ->
//            EditEventScreen(eid = backStackEntry.arguments?.getLong("eid")!!, back = { navController.popBackStack() } )
//        }
//    }
//}