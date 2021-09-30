package services.inputreaders

import enums.EProcessorType
import services.inputreaders.api.InputReader
import services.processors.ParseOnlyProcessor
import services.processors.api.Processor
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException

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
            EProcessorType.PARSE_ONLY -> ParseOnlyProcessor()
            else -> selectProcessorFromInput()
        }
    }
}