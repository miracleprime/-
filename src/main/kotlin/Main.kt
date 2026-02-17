import model.Player
import parser.CsvParser
import resolver.Resolver
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import org.jfree.data.category.DefaultCategoryDataset
import javax.swing.JFrame

fun main() {
    val players = CsvParser.readPlayersFromResource("fakePlayers.csv")
    println("Загружено игроков: ${players.size}")

    val resolver = Resolver(players)

    println("1. Игроков без агентства: ${resolver.getCountWithoutAgency()}")

    val (bestScorer, goals) = resolver.getBestScorerDefender()
    println("2. Лучший бомбардир-защитник: $bestScorer с $goals голами")

    val expensiveGermanPos = resolver.getTheExpensiveGermanPlayerPosition()
    println("3. Позиция самого дорогого немецкого игрока: $expensiveGermanPos")

    val rudestTeam = resolver.getTheRudestTeam()
    val avgRed = rudestTeam.players.map { it.redCards }.average()
    println("4. Самая грубая команда: ${rudestTeam.name} (среднее красных: $avgRed)")

    // Визуализация
    plotTopTeamsByTotalValue(players)
}

fun plotTopTeamsByTotalValue(players: List<Player>) {
    val totalByTeam = players.groupBy { it.team }
        .mapValues { (_, teamPlayers) -> teamPlayers.sumOf { it.transferValue } }
        .toList()
        .sortedByDescending { it.second }
        .take(10)

    val dataset = DefaultCategoryDataset()
    for ((team, value) in totalByTeam) {
        dataset.addValue(value, "Стоимость", team)
    }

    val chart: JFreeChart = ChartFactory.createBarChart(
        "Топ-10 команд по суммарной трансферной стоимости",
        "Команда",
        "Суммарная стоимость",
        dataset
    )

    val frame = JFrame("График")
    frame.contentPane.add(ChartPanel(chart))
    frame.setSize(800, 600)
    frame.isVisible = true
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
}