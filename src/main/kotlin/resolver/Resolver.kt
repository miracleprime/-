package resolver

import model.Player
import model.Team

class Resolver(private val players: List<Player>) : IResolver {
    override fun getCountWithoutAgency(): Int {
        return players.count { it.agency == null }
    }

    override fun getBestScorerDefender(): Pair<String, Int> {
        val defenders = players.filter { it.position == "DEFENDER" }
        if (defenders.isEmpty()) return Pair("", 0)
        val best = defenders.maxByOrNull { it.goals }!!
        return Pair(best.name, best.goals)
    }

    override fun getTheExpensiveGermanPlayerPosition(): String {
        val germanPlayers = players.filter { it.nationality == "Germany" }
        if (germanPlayers.isEmpty()) return "Не найдено"
        val mostExpensive = germanPlayers.maxByOrNull { it.transferValue }!!
        return translatePosition(mostExpensive.position)
    }

    override fun getTheRudestTeam(): Team {
        val playersByTeam = players.groupBy { it.team }
        val teamAvgRed = playersByTeam.mapValues { (_, teamPlayers) ->
            teamPlayers.map { it.redCards }.average()
        }
        val maxEntry = teamAvgRed.maxByOrNull { it.value }
            ?: return Team("Нет данных", emptyList())
        val teamPlayers = playersByTeam[maxEntry.key] ?: emptyList()
        return Team(maxEntry.key, teamPlayers)
    }

    private fun translatePosition(engPos: String): String {
        return when (engPos) {
            "DEFENDER" -> "Защитник"
            "MIDFIELD" -> "Полузащитник"
            "FORWARD" -> "Нападающий"
            "GOALKEEPER" -> "Вратарь"
            else -> engPos
        }
    }
}