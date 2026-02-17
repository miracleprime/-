package parser

import model.Player
import java.io.InputStream

object CsvParser {
    fun readPlayersFromResource(fileName: String): List<Player> {
        val inputStream: InputStream = object {}.javaClass.getResourceAsStream("/$fileName")
            ?: throw IllegalArgumentException("Файл '$fileName' не найден в resources")
        val lines = inputStream.bufferedReader().readLines()
        return lines.drop(1).map { line ->
            val parts = line.split(";").map { it.trim() }
            require(parts.size == 12) { "Некорректная строка: $line" }
            Player(
                name = parts[0],
                team = parts[1],
                city = parts[2],
                position = parts[3],
                nationality = parts[4],
                agency = parts[5].takeIf { it.isNotBlank() },
                transferValue = parts[6].toDouble(),
                participations = parts[7].toInt(),
                goals = parts[8].toInt(),
                assists = parts[9].toInt(),
                yellowCards = parts[10].toInt(),
                redCards = parts[11].toInt()
            )
        }
    }
}