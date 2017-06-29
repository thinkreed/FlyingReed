package reed.flyingreed.model

import android.app.Activity
import android.os.Parcel
import android.os.Parcelable


/**
 * Created by thinkreed on 2017/6/29.
 */

data class Motivation(val target:Class< out Activity> = Activity::class.java) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Motivation> = object : Parcelable.Creator<Motivation> {
            override fun createFromParcel(source: Parcel): Motivation = Motivation(source)
            override fun newArray(size: Int): Array<Motivation?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readSerializable() as Class<out Activity>
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeSerializable(target)
    }
}