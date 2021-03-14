package afsk_decoder

import java.io.File
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

class BitDecoder {

  fun decode(wavFile: File): List<Int> {
    val audioStream: AudioInputStream = AudioSystem.getAudioInputStream(wavFile)

    printAudioFileInfo(audioStream)

    val frameSize = audioStream.format.frameSize // 4
    val resultBitStream = mutableListOf<Int>()

    while (audioStream.available() > 0) {
      val byteArray = readFrameBytes(audioStream, frameSize) // 4 bytes size

      // todo need to verify this logic
      if (byteArray[1] != byteArray[2]) {
        resultBitStream.add(1)
        resultBitStream.add(1)
      } else {
        resultBitStream.add(0)
      }
    }

    return resultBitStream
  }

  private fun readFrameBytes(audioStream: AudioInputStream, frameSize: Int): ByteArray {
    val byteArray = ByteArray(frameSize)
    audioStream.read(byteArray)
    return byteArray
  }

  private fun printAudioFileInfo(audioInputStream: AudioInputStream) {
    println("frameLen: ${audioInputStream.frameLength}")
    println("audit format ${audioInputStream.format}")
    println("frame rate ${audioInputStream.format.frameRate}")
    println("frame Size ${audioInputStream.format.frameSize}")
    println("sample rate ${audioInputStream.format.sampleRate}")
  }
}
