package afsk_decoder

class ByteDecoder {
  companion object {
    const val ENCODED_BYTE_BITS_SIZE = 11
    const val ENCODED_BYTE_START_BIT = 0
    const val ENCODED_BYTE_END_BIT = 1
    const val LEAD_TONE_DURATION = 2.5 * 1000 * 1000 // 2.5 SEC
    const val END_BLOCK_DURATION =  0.5 * 1000 * 1000 // 0.5 SEC
  }

  // bit stream element should be either 0 or 1
  fun decode(bitStream: List<Int>): List<Int> {
    println("bits size ${bitStream.size}")

    val removedLeadBits = removeLeadTone(bitStream)
    println("bits without leadTone size ${removedLeadBits.size}")

    val removedEndBlockBits = removeEndBlock(removedLeadBits)
    println("bits without leadTone size ${removedEndBlockBits.size}")

    val encodedBytes = removedEndBlockBits.chunked(ENCODED_BYTE_BITS_SIZE)
    println("total encoded bytes ${encodedBytes.size}")

    /* todo ask what to do if validation fails or it is gurrenteed that start bit will always 0 and last 2 bit will always 1
        currently not considering this check */
    // val validBytes = encodedBytes.filter { isValidEncodedByte(it) }

    return encodedBytes
      .asSequence()
      .filter { it.size == ENCODED_BYTE_BITS_SIZE }
      .map { removeStartAndStopBits(it) }
      .map { it.reversed() } //  least-significant bit first.
      .map { it.joinToString("") } // converting bits to binary string
      .map { Integer.parseInt(it, 2) }
      .toList()
  }

  private fun removeLeadTone(bits: List<Int>): List<Int> {
    var remainingToneDuration = LEAD_TONE_DURATION
    var index = 0
    while (remainingToneDuration > 0) {
      remainingToneDuration -= getBitDuration(bits[index])
      index++
    }

    println("removing lead tone till index: $index  remove count $index ")

    return bits.slice(index until bits.size)
  }

  private fun removeEndBlock(bits: List<Int>): List<Int> {
    val endBlockDuration = END_BLOCK_DURATION

    var index = bits.size - 1
    var remainingEndBlockDuration = endBlockDuration
    while (remainingEndBlockDuration > 0) {
      remainingEndBlockDuration -= getBitDuration(bits[index])
      index--
    }

    println("removeEndBlock from index: $index to Index ${bits.size - 1} remove count = ${bits.size - 1 - index}")
    return bits.slice(0..index)
  }

  private fun isValidEncodedByte(bits: List<Int>): Boolean {
    return (bits[0] == ENCODED_BYTE_START_BIT) && (bits[bits.size - 1] == ENCODED_BYTE_END_BIT) && (bits[bits.size - 2] == ENCODED_BYTE_END_BIT)
  }

  private fun getBitDuration(bit: Int): Int {
    val zeroBitDuration = 640 // microsec
    val oneBitDuration = 320 // microsec

    return if (bit == 0) {
      zeroBitDuration
    } else {
      oneBitDuration
    }
  }

  private fun removeStartAndStopBits(encodedByte: List<Int>): List<Int> = encodedByte.subList(1, 9)

}
