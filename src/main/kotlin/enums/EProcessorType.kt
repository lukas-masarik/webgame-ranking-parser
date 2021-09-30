package enums

enum class EProcessorType(
    val processorNumber: Int,
) {
    EXIT(0),
    PARSE_ONLY(1),
    WINNERS_BY_PRESTIGE_OR_AREA(2),
    ;

    companion object {
        fun fromNumber(order: Int): EProcessorType {
            values().forEach {
                if (it.processorNumber == order) return it
            }
            throw IllegalArgumentException("Invalid processor number.")
        }
    }
}