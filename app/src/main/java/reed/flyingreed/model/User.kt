package reed.flyingreed.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by thinkreed on 2017/6/17.
 */

data class User(val name: String = "", val description: String = "", val id: Int = Int.MIN_VALUE) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readString(),
    source.readString(),
    source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(description)
        dest.writeInt(id)
    }
}