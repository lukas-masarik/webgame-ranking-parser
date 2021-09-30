package services.inputreaders

import services.inputreaders.api.InputReader
import services.processors.WinnersByPrestigeOrAreaProcessor
import services.processors.ParseOnlyProcessor
import services.processors.PrestigeByStateSystemProcessor
import services.processors.api.Processor
import kotlin.system.exitProcess

class SimpleInputReader : InputReader {

    override fun selectProcessorFromInput(): Processor {
        print(
            """
                Available processes:
                    (1) Parse only
                    (2) Winners by prestige or area
                    (3) Prestige by state system
                    (0) Exit program
                
                Choose processor: 
            """.trimIndent()
        )
        val input = readLine()
        return extractProcessorFromInput(input)
    }

    private fun extractProcessorFromInput(input: String?): Processor {
        if (input?.toIntOrNull() == null) selectProcessorFromInput()

        return when (input!!.toInt()) {
            0 -> exitProcess(1)
            1 -> ParseOnlyProcessor()
            2 -> WinnersByPrestigeOrAreaProcessor()
            3 -> PrestigeByStateSystemProcessor()
            else -> selectProcessorFromInput()
        }
    }
}