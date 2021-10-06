package me.masi.services.parsers.api


interface RankingParser<out T> {
    /**
     * Parse files with copied epoch result.
     * @return list of parsed results.
     */
    fun parse(): List<T>
}
