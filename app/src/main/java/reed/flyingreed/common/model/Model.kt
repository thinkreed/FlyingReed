package reed.flyingreed.common.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by thinkreed on 2017/6/17
 */

data class Model(var music: Music = Music(),
                 var video: Video = Video(),
                 var user: User = User(),
                 var template: Template = Template.EMPTY,
                 var title: String = "",
                 var id: Int = Int.MIN_VALUE,
                 var cover: Uri = Uri.EMPTY,
                 var description: String = "",
                 var motivation: Motivation = Motivation()) : Parcelable{
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Model> = object : Parcelable.Creator<Model> {
            override fun createFromParcel(source: Parcel): Model = Model(source)
            override fun newArray(size: Int): Array<Model?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readParcelable<Music>(Music::class.java.classLoader),
    source.readParcelable<Video>(Video::class.java.classLoader),
    source.readParcelable<User>(User::class.java.classLoader),
    Template.values()[source.readInt()],
    source.readString(),
    source.readInt(),
    source.readParcelable<Uri>(Uri::class.java.classLoader),
    source.readString(),
    source.readParcelable<Motivation>(Motivation::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(music, 0)
        dest.writeParcelable(video, 0)
        dest.writeParcelable(user, 0)
        dest.writeInt(template.ordinal)
        dest.writeString(title)
        dest.writeInt(id)
        dest.writeParcelable(cover, 0)
        dest.writeString(description)
        dest.writeParcelable(motivation, 0)
    }
}
