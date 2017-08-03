package reed.flyingreed.component

import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import reed.flyingreed.KotlinApplication
import reed.flyingreed.apis.GitHubService
import reed.flyingreed.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


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

    fun getModelsFromCursor(cursor: Cursor?, getModel: (Cursor) -> Model): MutableList<Model>? {
        var models: MutableList<Model>? = null
        if (cursor != null) {
            models = mutableListOf<Model>()
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    try {
                        models.add(getModel.invoke(cursor))
                    } catch (e: IllegalArgumentException) {
                        continue
                    }
                }
            }
            cursor.close()
        }
        return models
    }

    fun getVideos(uri: Uri): MutableList<Model>? {
        val cursor: Cursor? = KotlinApplication.instance.contentResolver.query(uri,
                null, null,
                null, MediaStore.Video.Media.TITLE + " ASC")
        return getModelsFromCursor(cursor,
                { videoCursor -> ModelFactory.createModelFromVideoCursor(videoCursor) })

    }

    fun getMusics(uri: Uri): MutableList<Model>? {
        val cursor: Cursor? = KotlinApplication.instance.contentResolver.query(uri,
                null, MediaStore.Audio.Media.IS_MUSIC + " !=0",
                null, MediaStore.Audio.Media.TITLE + " ASC")
        return getModelsFromCursor(cursor,
                { musicCursor -> ModelFactory.createModelFromMusicCursor(musicCursor) })
    }

    suspend fun getHttpData() {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        val service = retrofit.create(GitHubService::class.java)
        val result = service.listRepos("thinkreed")
        result.enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>?,
                                    response: retrofit2.Response<List<Repo>>?) {
                response?.body()?.map {
                    repo ->
                    Log.e("thinkreed", repo.name)
                }
            }

            override fun onFailure(call: Call<List<Repo>>?, t: Throwable?) {
                Log.e("thinkreed", "fail")
            }
        })
    }

    private fun notifyDataArrived(models: MutableList<Model>) =
            observers.map { observer -> observer.onDataArrived(models) }

    fun registerObserver(observer: Observer) = observers.add(observer)


    fun unregisterObserver(observer: Observer) = observers.remove(observer)


    fun clearObservers() = observers.clear()

}