package services.processors

import services.parsers.SimpleEpochsParser
import services.processors.api.Processor

class ParseOnlyProcessor : Processor {
    private val parser = SimpleEpochsParser()

    override fun apply() {
        val epochs = parser.parse()
        println("Successfully parsed ${epochs.size} epochs.")
    }
}