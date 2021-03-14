import afsk_decoder.AFSKDecoder

import java.io.File

fun main() {
  val file = File("/Users/abhishek/Documents/experiment/assignments/src/main/kotlin/file_3.wav")
  val messageBytes = AFSKDecoder().decode(file)
  messageBytes.forEach {
    print(it.toChar())
  }
}






