package think.reed.refitshopmodule.mediacodec

import android.content.Context
import android.net.Uri
import android.view.Surface
import android.view.SurfaceHolder
import java.io.FileDescriptor

/**
 * Created by thinkreed on 2017/8/16.
 */
interface IMediaPlayer {

    fun setDisplay(var1: SurfaceHolder)

    fun setDataSource(context: Context, uri: Uri)

    fun setDataSource(context: Context, uri: Uri, params: Map<String, String>)

    fun setDataSource(file: FileDescriptor)

    fun setDataSource(path: String)

    fun prepareAsync()

    fun start()

    fun stop()

    fun pause()

    fun seekTo(offset: Long)

    fun getCurrentPosition(): Long

    fun getDuration(): Long

    fun release()

    fun reset()

    fun setOnPreparedListener(listener: IMediaPlayer.OnPreparedListener)

    fun setOnCompletionListener(listener: IMediaPlayer.OnCompletionListener)

    fun setOnBufferingUpdateListener(listener: IMediaPlayer.OnBufferingUpdateListener)

    fun setOnSeekCompleteListener(listener: IMediaPlayer.OnSeekCompleteListener)

    fun setOnErrorListener(listener: IMediaPlayer.OnErrorListener)

    fun setAudioStreamType(type: Int)

    fun setSurface(surface: Surface)


    interface OnErrorListener {
        fun onError(var1: IMediaPlayer, var2: Int, var3: Int): Boolean
    }

    interface OnSeekCompleteListener {
        fun onSeekComplete(mediaPlayer: IMediaPlayer)
    }

    interface OnBufferingUpdateListener {
        fun onBufferingUpdate(mediaPlayer: IMediaPlayer, var2: Int)
    }

    interface OnCompletionListener {
        fun onCompletion(mediaPlayer: IMediaPlayer)
    }

    interface OnPreparedListener {
        fun onPrepared(mediaPlayer: IMediaPlayer)
    }

    companion object {
        val MEDIA_INFO_UNKNOWN = 1
        val MEDIA_INFO_STARTED_AS_NEXT = 2
        val MEDIA_INFO_VIDEO_RENDERING_START = 3
        val MEDIA_INFO_VIDEO_TRACK_LAGGING = 700
        val MEDIA_INFO_BUFFERING_START = 701
        val MEDIA_INFO_BUFFERING_END = 702
        val MEDIA_INFO_NETWORK_BANDWIDTH = 703
        val MEDIA_INFO_BAD_INTERLEAVING = 800
        val MEDIA_INFO_NOT_SEEKABLE = 801
        val MEDIA_INFO_METADATA_UPDATE = 802
        val MEDIA_INFO_TIMED_TEXT_ERROR = 900
        val MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901
        val MEDIA_INFO_SUBTITLE_TIMED_OUT = 902
        val MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001
        val MEDIA_INFO_AUDIO_RENDERING_START = 10002
        val MEDIA_INFO_MEDIA_ACCURATE_SEEK_COMPLETE = 10100
        val MEDIA_ERROR_UNKNOWN = 1
        val MEDIA_ERROR_SERVER_DIED = 100
        val MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200
        val MEDIA_ERROR_IO = -1004
        val MEDIA_ERROR_MALFORMED = -1007
        val MEDIA_ERROR_UNSUPPORTED = -1010
        val MEDIA_ERROR_TIMED_OUT = -110
    }
}
