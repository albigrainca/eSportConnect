package fr.uha.grainca.esc.database

import fr.uha.grainca.esc.model.Event
import fr.uha.grainca.esc.model.Game
import fr.uha.grainca.esc.model.GamerLevel
import fr.uha.grainca.esc.model.Genre
import fr.uha.grainca.esc.model.Participant
import java.util.Calendar
import java.util.Date
import java.util.Random

class GameDatabaseSeeder {

    private val random = Random();

    private suspend fun feedGames(): LongArray {
        val dao: GameDAO = ESportDatabase.get().gameDAO
        val ids = LongArray(4)
        ids[0] = dao.create(getRandomGame())
        ids[1] = dao.create(getRandomGame())
        ids[2] = dao.create(getRandomGame())
        ids[3] = dao.create(getRandomGame())
        return ids
    }

    private suspend fun feedParticipant(): LongArray {
        val dao: ParticipantDAO = ESportDatabase.get().participantDAO
        val ids = LongArray(4)
        ids[0] = dao.create(getRandomParticipant())
        ids[1] = dao.create(getRandomParticipant())
        ids[2] = dao.create(getRandomParticipant())
        ids[3] = dao.create(getRandomParticipant())
        return ids
    }

    private suspend fun feedEvents(gids: LongArray) {
        val dao: EventDAO = ESportDatabase.get().eventDAO
        val event = getRandomEvent(gids.get(0))
        val eid = dao.create(event)
    }

    suspend fun populate() {
        val gids = feedGames()
        feedEvents(gids)
    }

    suspend fun populateParticipant() {
        feedParticipant()
    }

    suspend fun clear() {
        ESportDatabase.get().clearAllTables()
    }

    private val gameNames = arrayOf(
        "Shadow Fight",
        "Castle Clash",
        "Last Empire",
        "Clash of Titans",
        "Dragon's Breath",
        "Mystic Quest",
        "Galaxy Raider",
        "Knight's Journey",
        "Pirate's Treasure",
        "Wizard's Challenge"
    )

    private val creators = arrayOf(
        "Nekki",
        "IGG",
        "Zonmob",
        "Gamevil",
        "DragoEnt",
        "MysticDev",
        "GalaxyWorks",
        "KnightGames",
        "PirateStudios",
        "WizardryInc"
    )

    private val descriptions = arrayOf(
        "A deep combat system",
        "Build and battle",
        "Survive the apocalypse",
        "Epic battles",
        "Breathe fire and conquer",
        "Embark on a mystical adventure",
        "Raid the stars",
        "A knight's tale",
        "Find the hidden treasures",
        "Master the arcane arts"
    )

    private val gameEvent = arrayOf(
        "Global Championship",
        "Ultimate League",
        "Master Series",
        "Warrior's Arena",
        "Battle Royale",
        "Champions Cup",
        "Elite Tournament",
        "Victory Challenge",
        "Arena Showdown",
        "Cyber Clash"
    )

    private val gamerNames = arrayOf(
        "ShadowNinja92",
        "BlazeGamerX",
        "CyberDragon99",
        "SonicSpeedster77",
        "PixelPirate88",
        "MysticMage55",
        "TitanSlayer123",
        "QuantumQuasar77",
        "FrostByte66",
        "PhoenixFirestorm"
    )

    private val realNames = arrayOf(
        "John Smith",
        "Emily Johnson",
        "Michael Brown",
        "Emma Williams",
        "Christopher Jones",
        "Olivia Davis",
        "William Miller",
        "Ava Wilson",
        "David Taylor",
        "Sophia Anderson"
    )

    private fun getRandomAge(): Int {
        return 18 + random.nextInt(18)
    }

    private fun getRandomGamerLevel(): GamerLevel {
        val levels = GamerLevel.values()
        return levels[random.nextInt(levels.size)]
    }

    private fun getRandomDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -random.nextInt(10))
        calendar.add(Calendar.DAY_OF_YEAR, -random.nextInt(365))
        return calendar.time
    }

    private fun getRandomFutureDateWithinOneYear(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, random.nextInt(365))
        return calendar.time
    }

    private fun getRandomGenre(): Genre {
        val genres = Genre.values()
        return genres[random.nextInt(genres.size)]
    }

    private fun getRandomName(names: Array<String>): String {
        return names.get(random.nextInt(names.size))
    }

    private fun getRandomEvent(mainGameId: Long): Event {
        return Event(
            0,
            getRandomName(gameEvent),
            getRandomFutureDateWithinOneYear(),
            1 + random.nextInt(10),
            mainGameId
        )
    }

    private fun getRandomGame(): Game {
        return Game(
            0,
            gameNames[random.nextInt(gameNames.size)],
            creators[random.nextInt(creators.size)],
            getRandomDate(),
            getRandomGenre(),
            descriptions[random.nextInt(descriptions.size)],
            null
        )
    }
    private fun getRandomParticipant(): Participant {
        return Participant(
            0,
            gamerNames[random.nextInt(gamerNames.size)],
            realNames[random.nextInt(realNames.size)],
            getRandomAge(),
            getRandomGamerLevel()
        )
    }
}