package me.masi.dto.alliances

data class AlliancesRankingRow(
    val ranking: Int,
    val allianceTag: String,
    val membersCount: Int,
    val area: Int,
    val prestige: Long,
    val chairmanPlayerName: String,
    val epochNumber: Int,
)
