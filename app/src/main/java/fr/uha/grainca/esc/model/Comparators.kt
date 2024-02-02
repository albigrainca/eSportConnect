package fr.uha.grainca.esc.model

class Comparators {
    companion object {

        fun shallowEqualsGame(oldGame: Game?, newGame: Game?): Boolean {
            if (oldGame == null && newGame == null) return true
            if (oldGame != null && newGame == null) return false
            if (oldGame == null && newGame != null) return false
            oldGame as Game
            newGame as Game
            if (oldGame.gid != newGame.gid) return false
            return true
        }

        fun shallowEqualsParticipant(oldParticipant: Participant?, newParticipant: Participant?): Boolean {
            if (oldParticipant == null && newParticipant == null) return true
            if (oldParticipant != null && newParticipant == null) return false
            if (oldParticipant == null && newParticipant != null) return false
            oldParticipant as Participant
            newParticipant as Participant
            if (oldParticipant.pid != newParticipant.pid) return false
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
            oldGames.forEach { g -> oldMap.add(g.gid) }
            newGames.forEach { g -> if (! oldMap.contains(g.gid)) return false }
            val newMap = mutableSetOf<Long>()
            newGames.forEach { g -> newMap.add(g.gid) }
            oldGames.forEach { g -> if (! newMap.contains(g.gid)) return false }
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

        fun shallowEqualsListParticipants(oldParticipants: List<Participant>?, newParticipants: List<Participant>?): Boolean {
            if (oldParticipants == null && newParticipants == null) return true
            if (oldParticipants != null && newParticipants == null) return false
            if (oldParticipants == null && newParticipants != null) return false
            oldParticipants as List<Participant>
            newParticipants as List<Participant>
            if (oldParticipants.size != newParticipants.size) return false
            val oldMap = mutableSetOf<Long>()
            oldParticipants.forEach { p -> oldMap.add(p.pid) }
            newParticipants.forEach { p -> if (! oldMap.contains(p.pid)) return false }
            val newMap = mutableSetOf<Long>()
            newParticipants.forEach { p -> newMap.add(p.pid) }
            oldParticipants.forEach { p -> if (! newMap.contains(p.pid)) return false }
            return true
        }

    }
}