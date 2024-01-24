package fr.uha.grainca.esc.model

class Comparators {
    companion object {

        fun shallowEqualsGame(oldGame: Game?, newGame: Game?): Boolean {
            if (oldGame == null && newGame == null) return true
            if (oldGame != null && newGame == null) return false
            if (oldGame == null && newGame != null) return false
            oldGame as Game
            newGame as Game
            if (oldGame.pid != newGame.pid) return false
            return true
        }

        fun shallowEqualsListGames(oldGames: List<Game>?, newGames: List<Game>?): Boolean {
            if (oldGames == null && newGames == null) return true
            if (oldGames != null && newGames == null) return false
            if (oldGames == null && newGames != null) return false
            oldGames as List<Game>
            newGames as List<Game>
            if (oldGames.size != newGames.size) return false
            val oldMap = mutableSetOf<Long>()
            oldGames.forEach { p -> oldMap.add(p.pid) }
            newGames.forEach { p -> if (! oldMap.contains(p.pid)) return false }
            val newMap = mutableSetOf<Long>()
            newGames.forEach { p -> newMap.add(p.pid) }
            oldGames.forEach { p -> if (! newMap.contains(p.pid)) return false }
            return true
        }

        fun shallowEqualsEvent(oldEvent: Event?, newEvent: Event?): Boolean {
            if (newEvent == null && oldEvent == null) return true
            if (newEvent != null && oldEvent == null) return false
            if (newEvent == null && oldEvent != null) return false
            val safeNew : Event = newEvent as Event
            val safeOld : Event = oldEvent as Event
            if (safeNew.eid != safeOld.eid) return false
            if (safeNew.name != safeOld.name) return false
            if (safeNew.startDay != safeOld.startDay) return false
            if (safeNew.duration != safeOld.duration) return false
            if (safeNew.mainGameId != safeOld.mainGameId) return false
            return true
        }

    }
}