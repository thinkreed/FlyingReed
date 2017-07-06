package reed.flyingreed.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by thinkreed on 2017/7/5.
 */

data class Video(val id: Int = 0, val title: String = "", val uri: String = "") : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Video> = object : Parcelable.Creator<Video> {
            override fun createFromParcel(source: Parcel): Video = Video(source)
            override fun newArray(size: Int): Array<Video?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readInt(),
    source.readString(),
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(title)
        dest.writeString(uri)
    }
}