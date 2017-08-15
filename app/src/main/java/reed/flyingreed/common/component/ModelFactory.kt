package reed.flyingreed.common.component

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import reed.flyingreed.music.activity.MusicPlayerActivity
import reed.flyingreed.common.model.*

/**
 * Created by thinkreed on 2017/7/13.
 */

object ModelFactory {

    fun createModelFromMusicCursor(cursor: Cursor): Model {
        val albumUri = Uri.parse("content://media/external/audio/albumart")
        val song = Music(
                title = DataVerfiy.verifyReturnDefault({
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                }, "unKnown"),
                path = DataVerfiy.verifyReturnOrThrow {
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                },
                id = DataVerfiy.verifyReturnOrThrow {
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                },
                artist = DataVerfiy.verifyReturnDefault(
                        { cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)) },
                        "unKnown"),
                cover = ContentUris.withAppendedId(albumUri,
                        DataVerfiy.verifyReturnDefault(
                                {
                                    cursor.getLong(cursor
                                            .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                                },
                                0L)
                ))
        return Model(music = song,
                cover = song.cover,
                title = song.title,
                id = song.id,
                template = Template.ITEM_VIDEO,
                description = song.artist,
                motivation = Motivation(MusicPlayerActivity::class.java))
    }

    fun createModelFromVideoCursor(cursor: Cursor): Model {
        val thumbnail = Uri.parse("content://media/external/video/media")
        val video = Video(
                title = DataVerfiy.verifyReturnDefault(
                        { cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE)) },
                        "unKnown"),
                path = DataVerfiy.verifyReturnOrThrow {
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                },
                id = DataVerfiy.verifyReturnOrThrow {
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                },
                artist = DataVerfiy.verifyReturnDefault(
                        { cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ARTIST)) },
                        "unKnown"
                ),
                cover = ContentUris.withAppendedId(thumbnail,
                        DataVerfiy.verifyReturnDefault(
                                { cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID)) },
                                0L
                        )))
        return Model(video = video,
                cover = video.cover,
                title = video.title,
                id = video.id,
                template = Template.ITEM_VIDEO,
                description = video.artist)
    }
}