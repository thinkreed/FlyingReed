package think.reed.refitshopmodule.mediacodec

import android.content.Context
import android.net.Uri
import android.view.Surface
import android.view.SurfaceHolder
import java.io.FileDescriptor

/**
 * Created by thinkreed on 2017/8/16.
 */
abstract class AbsMediaPlayer:IMediaPlayer {
    override fun setDisplay(var1: SurfaceHolder) {
    }

    override fun setDataSource(context: Context, uri: Uri) {
    }

    override fun setDataSource(context: Context, uri: Uri, params: Map<String, String>) {
    }

    override fun setDataSource(file: FileDescriptor) {
    }

    override fun setDataSource(path: String) {
    }

    override fun prepareAsync() {
    }

    override fun start() {
    }

    override fun stop() {
    }

    override fun pause() {
    }

    override fun seekTo(offset: Long) {
    }

    override fun getCurrentPosition(): Long {
        return 0
    }

    override fun getDuration(): Long {
        return 0
    }

    override fun release() {
    }

    override fun reset() {
    }

    override fun setOnPreparedListener(listener: IMediaPlayer.OnPreparedListener) {
    }

    override fun setOnCompletionListener(listener: IMediaPlayer.OnCompletionListener) {
    }

    override fun setOnBufferingUpdateListener(listener: IMediaPlayer.OnBufferingUpdateListener) {
    }

    override fun setOnSeekCompleteListener(listener: IMediaPlayer.OnSeekCompleteListener) {
    }

    override fun setOnErrorListener(listener: IMediaPlayer.OnErrorListener) {
    }

    override fun setAudioStreamType(type: Int) {
    }

    override fun setSurface(surface: Surface) {
    }
}