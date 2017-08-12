package think.reed.refitshopmodule.mediacodec

import android.media.MediaCodec
import android.media.MediaExtractor

/**
 * Created by huweijie01 on 2017/8/12.
 */
object CodecRegistry {

    val KEY_MEDIA_EXTRACTOR = "media_extractor"
    val KEY_MEDIA_CODEC = "media_codec"
    val KEY_VIDEO_CODEC = "video_codec"
    val KEY_BUFFER_INFO = "buffer_info"
    val KEY_SURFACE = "surface"

    private val components by lazy {
        hashMapOf<String, Any>()
    }

    fun registerComponent(key: String, value: Any) {
        components.put(key, value)
    }

    fun unRegisterComponent(key: String) {
        components.remove(key)
    }

    fun getComponent(key: String): Any {
        val result = components[key]

        return if (result == null) {
            val value = when (key) {
                KEY_BUFFER_INFO -> {
                    MediaCodec.BufferInfo()
                }
                KEY_MEDIA_EXTRACTOR -> {
                    MediaExtractor()
                }
                KEY_VIDEO_CODEC -> {
                    VideoCodec()
                }
                else -> {
                    throw IllegalArgumentException()
                }
            }
            components.put(key, value)
            value

        } else {
            result
        }
    }
}