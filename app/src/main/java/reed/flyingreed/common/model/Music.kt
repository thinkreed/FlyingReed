package reed.flyingreed.common.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by thinkreed on 2017/6/17.
 */

data class Music(val title: String = "",
                 val path: String = "",
                 val cover: Uri = Uri.EMPTY,
                 val id: Int = Int.MIN_VALUE,
                 val artist: String = "") : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Music> = object : Parcelable.Creator<Music> {
            override fun createFromParcel(source: Parcel): Music = Music(source)
            override fun newArray(size: Int): Array<Music?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readString(),
    source.readString(),
    source.readParcelable<Uri>(Uri::class.java.classLoader),
    source.readInt(),
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(path)
        dest.writeParcelable(cover, 0)
        dest.writeInt(id)
        dest.writeString(artist)
    }
}