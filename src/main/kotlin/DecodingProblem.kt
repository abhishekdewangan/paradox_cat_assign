import afsk_decoder.AFSKDecoder

import java.io.File

// todo currently not reading in buffered manner which will cause memory issue will revisit that later
fun main() {
  val file = File("/Users/abhishek/Documents/experiment/assignments/src/main/kotlin/file_3.wav")
  val messageBytes = AFSKDecoder().decode(file)
  messageBytes.forEach {
    print(it.toChar())
  }
}






