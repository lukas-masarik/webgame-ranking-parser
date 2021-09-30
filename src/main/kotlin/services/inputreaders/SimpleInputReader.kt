package services.inputreaders

import enums.EProcessorType
import services.inputreaders.api.InputReader
import services.processors.WinnersByPrestigeOrAreaProcessor
import services.processors.ParseOnlyProcessor
import services.processors.api.Processor
import java.lang.IllegalArgumentException
import kotlin.system.exitProcess

class SimpleInputReader : InputReader {

    override fun selectProcessorFromInput(): Processor {
        val input = askForInput()
        return extractProcessor(input)
    }

    private fun askForInput(): String? {
        print(
            """
                Available processes:
                    (1) Parse only
                    (2) Winners by prestige or area
                    (0) Exit program
                
                Choose processor: 
            """.trimIndent()
        )
        return readLine()
    }

    private fun extractProcessor(input: String?): Processor {
        if (input?.toIntOrNull() == null) selectProcessorFromInput()

        val processorType = try {
            EProcessorType.fromNumber(input!!.toInt())
        } catch (e: IllegalArgumentException) {
            null
        }
        if (processorType == null) selectProcessorFromInput()

        return when (processorType) {
            EProcessorType.EXIT -> exitProcess(1)
            EProcessorType.PARSE_ONLY -> ParseOnlyProcessor()
            EProcessorType.WINNERS_BY_PRESTIGE_OR_AREA -> WinnersByPrestigeOrAreaProcessor()
            else -> selectProcessorFromInput()
        }
    }
}