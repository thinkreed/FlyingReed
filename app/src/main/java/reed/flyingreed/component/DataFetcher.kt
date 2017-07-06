package reed.flyingreed.component

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import reed.flyingreed.KotlinApplication
import reed.flyingreed.controller.activity.MusicPlayerActivity
import reed.flyingreed.controller.activity.VideoPlayerActivity
import reed.flyingreed.model.*
import reed.flyingreed.model.dtos.VideoInfo


/**
 * Created by thinkreed on 2017/6/17.
 */

object DataFetcher {

    private val observers by lazy {
        mutableListOf<Observer>()
    }

    private val mMainHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    suspend fun getData(uri: Uri, strategy: (uri: Uri) -> MutableList<Model>?) {
        val models = strategy(uri)
        models?.let {
            mMainHandler.post {
                notifyDataArrived(models)
            }
        }
    }

    fun getVideos(uri: Uri): MutableList<Model>? {
        var models: MutableList<Model>? = null
        val cursor: Cursor? = KotlinApplication.instance.contentResolver.query(uri,
                null, null,
                null, MediaStore.Video.Media.TITLE + " ASC")
        cursor?.let {
            models = mutableListOf<Model>()
            if (cursor.count > 0) {
                val thumbnail = Uri.parse("content://media/external/video/media")
                while (cursor.moveToNext()) {
                    val video = Video(title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE)),
                            path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)),
                            id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID)),
                            artist = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ARTIST)),
                            cover = ContentUris.withAppendedId(thumbnail,
                                    cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID))))
                    models?.add(Model(video = video,
                            cover = video.cover,
                            title = video.title,
                            id = video.id,
                            template = Template.ITEM_VIDEO,
                            description = video.artist,
                            motivation = Motivation(VideoPlayerActivity::class.java)))
                }
            }
            cursor.close()
        }
        return models
    }

    fun getMusics(uri: Uri): MutableList<Model>? {
        var models: MutableList<Model>? = null
        val cursor: Cursor? = KotlinApplication.instance.contentResolver.query(uri,
                null, MediaStore.Audio.Media.IS_MUSIC + " !=0",
                null, MediaStore.Audio.Media.TITLE + " ASC")
        cursor?.let {
            models = mutableListOf<Model>()
            if (cursor.count > 0) {
                val albumUri = Uri.parse("content://media/external/audio/albumart")
                while (cursor.moveToNext()) {
                    val song = Music(title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                            path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)),
                            id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)),
                            artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                            cover = ContentUris.withAppendedId(albumUri,
                                    cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))))
                    models?.add(Model(music = song,
                            cover = song.cover,
                            title = song.title,
                            id = song.id,
                            template = Template.ITEM_VIDEO,
                            description = song.artist,
                            motivation = Motivation(MusicPlayerActivity::class.java)))
                }
            }
            cursor.close()
        }
        return models
    }

    private suspend fun getHttpData(uri: Uri) {
        val client = OkHttpClient()
        val request = Request.Builder().url(uri.toString()).build()
        val response: Response? = client.newCall(request).execute()
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(VideoInfo::class.java)
        val videoInfo = jsonAdapter.fromJson(response?.body()?.string())
        val videos = videoInfo?.videos
        response?.close()
    }

    private fun notifyDataArrived(models: MutableList<Model>) =
            observers.map { observer -> observer.onDataArrived(models) }

    fun registerObserver(observer: Observer) = observers.add(observer)


    fun unregisterObserver(observer: Observer) = observers.remove(observer)


    fun clearObservers() = observers.clear()

}