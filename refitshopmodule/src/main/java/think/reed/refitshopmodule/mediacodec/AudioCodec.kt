package think.reed.refitshopmodule.mediacodec

import android.media.MediaExtractor

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
}