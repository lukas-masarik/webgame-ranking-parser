package me.masi.services.parsers

import me.masi.dto.alliances.AlliancesRanking
import me.masi.dto.alliances.AlliancesRankingRow
import java.io.File

class AlliancesRankingParser : AbstractRankingParser<AlliancesRanking>() {

    override fun parse(): List<AlliancesRanking> {
        val epochFiles = getEpochFilesForAlliancesFromResources()
        return parseAlliancesRankingFiles(epochFiles)
    }

    private fun parseAlliancesRankingFiles(epochFiles: List<File>): List<AlliancesRanking> {
        val alliancesRankings = mutableListOf<AlliancesRanking>()
        epochFiles.forEach {
            val epochContent = it.readLines()
            val epochNumber = parseEpochNumber(epochContent.first())

            val alliancesRankingRows = mutableListOf<AlliancesRankingRow>()
            // skip first 2 lines without land data
            epochContent.subList(fromIndex = 2, toIndex = epochContent.size).forEach {
                alliancesRankingRows.add(parseAlliancesRankingRow(it, epochNumber))
            }

            alliancesRankings.add(AlliancesRanking(epochNumber = epochNumber, alliancesRankingRows = alliancesRankingRows))
        }
        return alliancesRankings
    }

    fun parseAlliancesRankingRow(alliancesRankingRow: String, epochNumber: Int): AlliancesRankingRow {
        val alliancesRankingRowMatchResult =
            REGEX_ALLIANCES_RANKING_ROW.toRegex().find(alliancesRankingRow)?.groupValues ?: throw IllegalArgumentException("Invalid alliances ranking row.")
        return AlliancesRankingRow(
            ranking = alliancesRankingRowMatchResult[1].toInt(),
            allianceTag = alliancesRankingRowMatchResult[2],
            membersCount = alliancesRankingRowMatchResult[3].toInt(),
            area = alliancesRankingRowMatchResult[4].toInt(),
            prestige = alliancesRankingRowMatchResult[5].toLong(),
            chairmanPlayerName = alliancesRankingRowMatchResult[6],
            epochNumber = epochNumber
        )
    }
}

private const val REGEX_ALLIANCES_RANKING_ROW = "(\\d+)\\.\\t\\[(.*)\\].*\\t(\\d+)\\t(\\d+)km2\\t(\\d+)\\t.*#\\d+\\)\\s-\\s(.*)"
