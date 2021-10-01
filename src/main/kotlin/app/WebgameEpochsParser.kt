import services.inputreaders.SimpleInputReader

fun main(args: Array<String>) {
    val inputReader = SimpleInputReader()
    val processor = inputReader.selectProcessorFromInput()
    processor.process()
}
