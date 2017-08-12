package think.reed.refitshopmodule.mediacodec

import android.media.*
import java.nio.ByteBuffer

/**
 * Created by thinkreed on 2017/8/11.
 */
class VideoCodec {

    private lateinit var inputBuffers: Array<ByteBuffer>
    private lateinit var outputBuffers: Array<ByteBuffer>

    fun doDecodeMP4(path: String) {

        val extractor = MediaExtractor()
        extractor.setDataSource(path)
        for (i in 0 until extractor.trackCount) {
            val format = extractor.getTrackFormat(i)
            val mime = format.getString(MediaFormat.KEY_MIME)
            if (!isAvailableFormat(mime)) return
            if (mime.startsWith("video/")) {
                extractor.selectTrack(i)
                val decoder = MediaCodec.createDecoderByType(mime)
                decoder.configure(format, null, null, 0)
                decoder.start()
                var outputDone = false
                var inputDone = false
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                    inputBuffers = decoder.inputBuffers
                    outputBuffers = decoder.outputBuffers
                }
                val info = MediaCodec.BufferInfo()
                while (!outputDone) {
                    if (!inputDone) {
                        val inputBufferId = decoder.dequeueInputBuffer(1000)
                        if (inputBufferId > 0) {
                            val destBuffer = if (android.os.Build.VERSION.SDK_INT
                                    < android.os.Build.VERSION_CODES.LOLLIPOP) {
                                inputBuffers[inputBufferId]
                            } else {
                                decoder.getInputBuffer(inputBufferId)
                            }
                            val chunkSize = extractor.readSampleData(destBuffer, 0)
                            if (chunkSize < 0) {
                                decoder.queueInputBuffer(inputBufferId, 0, 0,
                                        0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                                inputDone = true
                            } else {
                                val presentationTimeUs = extractor.sampleTime
                                decoder.queueInputBuffer(inputBufferId, 0, chunkSize,
                                        presentationTimeUs, 0)
                                extractor.advance()
                            }
                        }
                    }
                    if (!outputDone) {
                        val outputBufferId = decoder.dequeueOutputBuffer(info, 1000)
                        if (outputBufferId > 0) {
                            if (info.flags.and(MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                                outputDone = true
                            }
                            val buf = if (android.os.Build.VERSION.SDK_INT
                                    < android.os.Build.VERSION_CODES.LOLLIPOP) {
                                outputBuffers[outputBufferId]
                            } else {
                                decoder.getOutputBuffer(outputBufferId)
                            }
                            val chunkLen = info.size
                            buf.clear()
                            decoder.releaseOutputBuffer(outputBufferId, false)
                        } else if (outputBufferId == MediaCodec.INFO_TRY_AGAIN_LATER) {

                        } else if (outputBufferId == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                            outputBuffers = decoder.outputBuffers
                        } else if (outputBufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                            val format = decoder.outputFormat
                        }
                    }

                }
            }
        }
    }

    private fun isAvailableFormat(mimeType: String): Boolean {
        var result = false
        if (android.os.Build.VERSION.SDK_INT
                < android.os.Build.VERSION_CODES.LOLLIPOP) {
            for (i in 0 until MediaCodecList.getCodecCount()) {
                val codecInfo = MediaCodecList.getCodecInfoAt(i)
                result = checkInfo(codecInfo, mimeType)
            }
        } else {
            MediaCodecList(MediaCodecList.ALL_CODECS).codecInfos.forEach {
                codecInfo ->
                result = checkInfo(codecInfo, mimeType)
            }
        }

        return result
    }

    private fun checkInfo(codecInfo: MediaCodecInfo, mimeType: String): Boolean {
        var result = false
        codecInfo.supportedTypes.forEach {
            type ->
            if (type == mimeType) result = true
        }
        return result
    }
}