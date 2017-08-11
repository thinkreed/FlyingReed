package think.reed.refitshopmodule.mediacodec

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat

/**
 * Created by thinkreed on 2017/8/11.
 */
class VideoCodec {
    fun doDecodeMP4(path: String) {
        val extractor = MediaExtractor()
        extractor.setDataSource(path)
        for (i in 0 until extractor.trackCount) {
            val format = extractor.getTrackFormat(i)
            val mime = format.getString(MediaFormat.KEY_MIME)
            if (mime.startsWith("video/")) {
                val decoder = MediaCodec.createDecoderByType(mime)
                decoder.configure(format, null, null, 0)
                decoder.start()
                val outputDone = false
                val inputBuffer = decoder.inputBuffers
                val outputBuffer = decoder.outputBuffers
                while (!outputDone) {
                    val inputBufferId = decoder.dequeueInputBuffer(2000)
                }
            }
        }
    }
}