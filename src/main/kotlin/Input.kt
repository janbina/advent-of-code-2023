import java.io.File
import java.lang.IllegalStateException
import java.net.URL
import kotlin.Result

fun getDayInputFile(
    day: Int,
    forceDownload: Boolean = false,
): Result<File> {
    File("input").mkdirs()
    val file = File("input/day${day.toString().padStart(2, '0')}.txt")

    if (file.exists() && !forceDownload) {
        return Result.success(file)
    }

    val session = runCatching { File(".session").readText().trim() }.getOrElse { "" }
    if (session.isBlank()) {
        return Result.failure(IllegalStateException("Missing session cookie"))
    }

    println("Downloading input file for day $day")

    val url = URL("https://adventofcode.com/$YEAR/day/$day/input")
    val connection = url.openConnection().apply {
        setRequestProperty("User-Agent", "github.com/janbina")
        setRequestProperty("Cookie", "session=$session")
    }

    return runCatching {
        val input = connection.inputStream
        file.outputStream().use { fos -> input.copyTo(fos) }
        file
    }.onSuccess {
        println("Input file for day $day downloaded")
    }.onFailure {
        println("Error downloading file for day $day")
    }
}
