package think.reed.refitshopmodule.mediacodec

import android.media.*
import android.view.Surface
import java.nio.ByteBuffer

/**
 * Created by thinkreed on 2017/8/11.
 */
class VideoCodec {

    private lateinit var inputBuffers: Array<ByteBuffer>
    private lateinit var outputBuffers: Array<ByteBuffer>
    private val mExtractor by lazy {
        CodecRegistry.getComponent(CodecRegistry.KEY_MEDIA_EXTRACTOR) as MediaExtractor
    }
    private lateinit var mDecoder: MediaCodec
    private val mInfo by lazy {
        CodecRegistry.getComponent(CodecRegistry.KEY_BUFFER_INFO) as MediaCodec.BufferInfo
    }
    private var outputDone = false
    private var inputDone = false

    fun doDecodeMP4(path: String, processMode: ProcessMode) {

        mExtractor.setDataSource(path)
        if (!initCodec(processMode)) return

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            inputBuffers = mDecoder.inputBuffers
            outputBuffers = mDecoder.outputBuffers
        }
        while (!outputDone) {
            if (!inputDone) {
                fillInputBuffer()
            }
            if (!outputDone) {
                processOutputBuffer(processMode)
            }
        }

        resetVideoCodec()

    }

    private fun resetVideoCodec() {
        mDecoder.stop()
        mDecoder.release()
        mExtractor.release()
        inputDone = false
        outputDone = false
    }

    private fun initCodec(processMode: ProcessMode): Boolean {
        for (i in 0 until mExtractor.trackCount) {
            val format = mExtractor.getTrackFormat(i)
            val mime = format.getString(MediaFormat.KEY_MIME)
            if (!isAvailableFormat(mime)) return false
            if (mime.startsWith("video/")) {
                mExtractor.selectTrack(i)
                mDecoder = MediaCodec.createDecoderByType(mime)
                when (processMode) {
                    ProcessMode.SYNC_WITH_SURFACE -> {
                        val surface = CodecRegistry.getComponent(CodecRegistry.KEY_SURFACE)
                                as Surface
                        mDecoder.configure(format, surface, null, 0)
                    }
                    ProcessMode.SYNC_WITH_BUFFER -> {
                        mDecoder.configure(format, null, null, 0)
                    }
                }
                mDecoder.start()
                return true
            }
        }
        return false
    }

    private fun fillInputBuffer() {
        val inputBufferId = mDecoder.dequeueInputBuffer(10000)
        if (inputBufferId > 0) {
            val destBuffer = if (android.os.Build.VERSION.SDK_INT
                    < android.os.Build.VERSION_CODES.LOLLIPOP) {
                inputBuffers[inputBufferId]
            } else {
                mDecoder.getInputBuffer(inputBufferId)
            }
            val chunkSize = mExtractor.readSampleData(destBuffer, 0)
            if (chunkSize < 0) {
                mDecoder.queueInputBuffer(inputBufferId, 0, 0,
                        0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                inputDone = true
            } else {
                val presentationTimeUs = mExtractor.sampleTime
                mDecoder.queueInputBuffer(inputBufferId, 0, chunkSize,
                        presentationTimeUs, 0)
                mExtractor.advance()
            }
        }
    }

    private fun processOutputBuffer(processMode: ProcessMode) {
        val outputBufferId = mDecoder.dequeueOutputBuffer(mInfo, 10000)
        if (outputBufferId > 0) {
            if (mInfo.flags.and(MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                outputDone = true
            }
            when (processMode) {
                ProcessMode.SYNC_WITH_BUFFER -> {
                    syncProcessingWithBuffer(outputBufferId)
                }
                ProcessMode.SYNC_WITH_SURFACE -> {
                    mDecoder.releaseOutputBuffer(outputBufferId, true)
                }
            }
        } else if (outputBufferId == MediaCodec.INFO_TRY_AGAIN_LATER) {

        } else if (outputBufferId == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
            outputBuffers = mDecoder.outputBuffers
        } else if (outputBufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
            val format = mDecoder.outputFormat
        }
    }

    private fun syncProcessingWithBuffer(outputBufferId: Int) {
        val buf = if (android.os.Build.VERSION.SDK_INT
                < android.os.Build.VERSION_CODES.LOLLIPOP) {
            outputBuffers[outputBufferId]
        } else {
            mDecoder.getOutputBuffer(outputBufferId)
        }
        val chunkLen = mInfo.size
        //process the output data
        buf.clear()
        mDecoder.releaseOutputBuffer(outputBufferId, false)
    }

    private fun isAvailableFormat(mimeType: String): Boolean {
        if (android.os.Build.VERSION.SDK_INT
                < android.os.Build.VERSION_CODES.LOLLIPOP) {
            for (i in 0 until MediaCodecList.getCodecCount()) {
                val codecInfo = MediaCodecList.getCodecInfoAt(i)
                if (checkInfo(codecInfo, mimeType)) return true
            }
        } else {
            MediaCodecList(MediaCodecList.ALL_CODECS).codecInfos.forEach {
                codecInfo ->
                if (checkInfo(codecInfo, mimeType)) return true
            }
        }

        return false
    }

    private fun checkInfo(codecInfo: MediaCodecInfo, mimeType: String): Boolean {
        codecInfo.supportedTypes.forEach {
            type ->
            if (type == mimeType) return true
        }
        return false
    }

    enum class ProcessMode {
        SYNC_WITH_BUFFER, SYNC_WITH_SURFACE
    }
}