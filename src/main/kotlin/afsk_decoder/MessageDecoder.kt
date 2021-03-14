package afsk_decoder

import java.lang.RuntimeException

class MessageDecoder {
  companion object {
    private const val DATA_BLOCKS_SIZE = 31 // 31 BYTES  WITH LAST BYTE AS CHECKSUM
  }

  fun decode(byteStream: List<Int>): List<Int> {
    checkValidStartAndEndBytes(byteStream)

    val withoutStartAndEndBytes = removeStartAndEndBytes(byteStream)

    val dataBlocks = withoutStartAndEndBytes.chunked(DATA_BLOCKS_SIZE)

    return dataBlocks
      .filter { isValidDataBlock(it) } // validating checksum
      .map { removeChecksumBlock(it) } // removing checksum byte from each message tone
      .flatten()
  }

  private fun removeStartAndEndBytes(byteStream: List<Int>): List<Int> =
    byteStream.subList(2, byteStream.size - 2)

  private fun checkValidStartAndEndBytes(byteStream: List<Int>) {
    val firstByte = 0x42
    val secondByte = 0x03
    val lastByte = 0x00

    if (byteStream[0] != firstByte ||
      byteStream[1] != secondByte ||
      byteStream[byteStream.size - 1] != lastByte
    ) throw RuntimeException("no valid start and end bytes")

  }

  // checksum verification
  private fun isValidDataBlock(dataBytes: List<Int>): Boolean {
    val checksum = dataBytes.last()
    // logic simple sum of the 30 bytes which should be equal to the 31st one.
    val dataSum =
      dataBytes
        .subList(0, dataBytes.size - 1)
        .reduce { acc, i -> acc + i }

    return dataSum == checksum
  }

  private fun removeChecksumBlock(dataBytes: List<Int>): List<Int> {
    val checksumByteIdx = dataBytes.size - 1
    return dataBytes.subList(0, checksumByteIdx - 1)
  }


}
