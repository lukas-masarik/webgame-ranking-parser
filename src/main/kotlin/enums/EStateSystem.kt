package enums

enum class EStateSystem(
    val rawValue: String,
) {
    ANARCHY("Anar"),
    COMMUNISM("Kom"),
    DEMOCRACY("Demo"),
    DICTATORSHIP("Dikt"),
    FEUDALISM("Feud"),
    FUNDAMENTALISM("Fund"),
    REPUBLIC("Rep"),
    ROBOCRACY("Robo"),
    TECHNOCRACY("Tech"),
    UTOPIA("Utop"),
    ;

    companion object {
        fun fromRawValue(rawValue: String): EStateSystem {
            values().forEach {
                if (it.rawValue == rawValue) return it
            }
            throw IllegalArgumentException("Unknown state system: $rawValue")
        }
    }
}