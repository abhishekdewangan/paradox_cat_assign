package afsk_decoder

import java.io.File

class AFSKDecoder {

  fun decode(file: File): List<Int> =
    file
      .let { file -> BitDecoder().decode(file) }
      .let { bitStream -> ByteDecoder().decode(bitStream) }
      .let { byteStream -> MessageDecoder().decode(byteStream) }
}
