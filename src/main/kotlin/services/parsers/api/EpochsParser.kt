package services.parsers.api


interface EpochsParser<out T> {
    /**
     * Parse files with copied epoch result.
     * @return list of parsed results.
     */
    fun parse(): List<T>
}
