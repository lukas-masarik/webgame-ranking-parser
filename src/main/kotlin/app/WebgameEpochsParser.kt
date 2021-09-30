import services.inputreaders.SimpleInputReader
import services.processors.api.Processor

fun main(args: Array<String>) {
    val processor = chooseProcessorFromInput()
    processor.apply()
}

fun chooseProcessorFromInput(): Processor {
    val inputReader = SimpleInputReader()
    return inputReader.selectProcessorFromInput()
}
