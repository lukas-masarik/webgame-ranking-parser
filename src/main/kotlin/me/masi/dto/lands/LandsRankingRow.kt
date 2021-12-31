package me.masi.dto.lands

data class LandsRankingRow(
    val ranking: Int,
    val landName: String,
    val landNumber: Int,
    val playerName: String,
    val area: Int,
    val prestige: Long,
    val alliance: String? = null,
    val stateSystem: String,
    val rounds: Int,
    val epochNumber: Int,
)
