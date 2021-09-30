package enums

enum class EProcessorType(
    val processorNumber: Int,
) {
    PARSE_ONLY(1),
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