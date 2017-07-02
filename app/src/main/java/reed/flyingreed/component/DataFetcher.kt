package reed.flyingreed.component

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import reed.flyingreed.KotlinApplication
import reed.flyingreed.controller.activity.PlayerActivity
import reed.flyingreed.model.Model
import reed.flyingreed.model.Motivation
import reed.flyingreed.model.Music
import reed.flyingreed.model.Template


/**
 * Created by thinkreed on 2017/6/17.
 */

object DataFetcher {

    private val observers by lazy {
        mutableListOf<Observer>()
    }

    suspend fun getData(uri: Uri) {
        when (uri.scheme) {
            "http", "https" -> getHttpData(uri)
            "file" -> getFileData(uri)
            "content" -> getContentData(uri)
            else -> throw IllegalArgumentException("not supported scheme")
        }

    }

    private suspend fun getHttpData(uri: Uri) {
        val client = OkHttpClient()
        val request = Request.Builder().url(uri.toString()).build()
        val response: Response? = client.newCall(request).execute()
        Log.d("thinkreed", response?.body()?.string())
        response?.close()
    }

    private suspend fun getFileData(uri: Uri): Unit {
    }

    private suspend fun getContentData(uri: Uri): Unit {
        val models: MutableList<Model> by lazy {
            mutableListOf<Model>()
        }
        val cursor: Cursor? = KotlinApplication.instance.contentResolver.query(uri, null, MediaStore.Audio.Media.IS_MUSIC + " !=0", null, MediaStore.Audio.Media.TITLE + " ASC")
        cursor?.let {
            if (cursor.count > 0) {
                val albumUri = Uri.parse("content://media/external/audio/albumart")
                while (cursor.moveToNext()) {
                    val song = Music(title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                            path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)),
                            id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)),
                            artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                            cover = ContentUris.withAppendedId(albumUri, cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))))
                    models.add(Model(music = song,
                            cover = song.cover,
                            title = song.title,
                            id = song.id,
                            template = Template.ITEM_MUSIC_INFO,
                            description = song.artist,
                            motivation = Motivation(PlayerActivity::class.java)))
                }
            }
            cursor.close()
            notifyDataArrived(models)
        }
    }

    private suspend fun notifyDataArrived(models: MutableList<Model>) =
            observers.map { observer -> observer.onDataArrived(models) }

    fun registerObserver(observer: Observer) = observers.add(observer)


    fun unregisterObserver(observer: Observer) = observers.remove(observer)


    fun clearObservers() = observers.clear()

}