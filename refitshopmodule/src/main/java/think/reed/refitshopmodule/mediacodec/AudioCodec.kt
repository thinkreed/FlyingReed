package think.reed.refitshopmodule.mediacodec

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.ByteBuffer
import java.util.ArrayList

/**
 * Created by huweijie01 on 2017/8/17.
 */
class AudioCodec {
    private val fontExtractor by lazy {
        MediaExtractor()
    }

    private val backExtractor by lazy {
        MediaExtractor()
    }
    private var mChunk = ByteArray(1024*20)
    private val fisBuf = ByteArray(8192)
    private lateinit var mFis: FileInputStream
    private var mFileList: MutableList<String> = ArrayList()
    private lateinit var codecInputBuffers: Array<ByteBuffer>
    private lateinit var codecOutputBuffers: Array<ByteBuffer>
    private lateinit var info: MediaCodec.BufferInfo

    @Throws(IOException::class)
    private fun readFully(inputStream: FileInputStream, buf: ByteArray, count: Int): Int {
        var byteCounts = 0
        while (byteCounts < count) {
            var readCounts = 0
            readCounts = inputStream.read(buf, 0, count)
            if (readCounts > 0) {
                byteCounts += readCounts
            } else {
                return -1
            }
        }
        return byteCounts
    }

    class Pair {
        var data: ByteArray? = null
        var size: Int = 0

        internal constructor() {

        }

        internal constructor(data: ByteArray, size: Int) {
            this.data = data
            this.size = size
        }

        companion object {

            @JvmStatic fun main(args: Array<String>) {
                // TODO Auto-generated method stub

            }
        }

    }

    fun getM3U8DecodedAudioData(p: Pair, pos: Int, mediaCodec: MediaCodec): Int {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            codecInputBuffers = mediaCodec.getInputBuffers()
            codecOutputBuffers = mediaCodec.getOutputBuffers()
        }

        var sawInputEOS = false

        if (mFileList.size < 2) {
            return -1
        }

        var fisReadBytes = -1
        try {
            while (fisReadBytes < 0 && mFileList.size > 1) {
                if (mFis == null) {
                    try {
                        mFis = FileInputStream(mFileList[0])
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }

                }
                fisReadBytes = readFully(mFis, fisBuf, 7)
                if (fisReadBytes < 0) {
                    mFis.close()
                    val file = File(mFileList[0])
                    mFileList!!.removeAt(0)
                    if (file.exists()) {
                        file.delete()
                    }
                    continue
                }
                val frameLength = (fisBuf[3].toInt() and 0x3 shl 11) +
                        (fisBuf[4].toInt() and 0xFF shl 3) + (fisBuf[5].toInt() and 0xE0 shr 5)
                if (frameLength < 7) {
                    mFis.close()
                    val file = File(mFileList[0])
                    mFileList.removeAt(0)
                    if (file.exists()) {
                        file.delete()
                    }
                    fisReadBytes = -1
                    continue
                }
                fisReadBytes = readFully(mFis, fisBuf, frameLength - 7)
                if (fisReadBytes < 0) {
                    mFis.close()
                    val file = File(mFileList[0])
                    mFileList.removeAt(0)
                    if (file.exists()) {
                        file.delete()
                    }
                    continue
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return 0
        }

        if (fisReadBytes < 0) {
            return -1
        }

        var audioBufferSize = 0
        // data input process
        val inputBufIndex: Int

        p.data = mChunk
        p.size = 0

        try {
            inputBufIndex = mediaCodec.dequeueInputBuffer(1000)

            if (inputBufIndex >= 0 && !sawInputEOS) {
                val dstBuf: ByteBuffer
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    dstBuf = mediaCodec.getInputBuffer(inputBufIndex)
                } else {
                    dstBuf = codecInputBuffers[inputBufIndex]
                }

                dstBuf.put(fisBuf, 0, fisReadBytes)

                mediaCodec.queueInputBuffer(inputBufIndex, 0, // offset
                        fisReadBytes, 0, if (sawInputEOS) MediaCodec.BUFFER_FLAG_END_OF_STREAM else 0)
            }

            // decode process
            val res = mediaCodec.dequeueOutputBuffer(info, 1000)
            if (res >= 0) {
                val outputBufIndex = res
                val buf: ByteBuffer
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    buf = mediaCodec.getOutputBuffer(outputBufIndex)
                } else {
                    buf = codecOutputBuffers[outputBufIndex]
                }

                // final byte[] chunk = new byte[info.size];
                val chunkLen = info.size
                if (mChunk.size < chunkLen + pos) {
                    mChunk = ByteArray(chunkLen + pos)
                    p.data = mChunk
                }
                buf.get(mChunk, pos, chunkLen)
                buf.clear()

                if (chunkLen > 0) {
                    p.size = chunkLen
                    audioBufferSize = chunkLen
                }

                mediaCodec.releaseOutputBuffer(outputBufIndex, false)

                if (info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                } else {
                }
            } else if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                if (res == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    codecOutputBuffers = mediaCodec.getOutputBuffers()
                }
            } else if (res == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                val oformat = mediaCodec.getOutputFormat()
                val newSamplerate = oformat.getInteger(MediaFormat.KEY_SAMPLE_RATE)
            }
        } catch (e2: IllegalStateException) {
            e2.printStackTrace()
            return -1
        } catch (e4: IllegalArgumentException) {
            e4.printStackTrace()

            return -1
        } catch (e6: MediaCodec.CryptoException) {
            e6.printStackTrace()
            return -1
        }

        return audioBufferSize
    }
}