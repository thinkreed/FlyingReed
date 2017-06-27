package reed.flyingreed.model

import android.net.Uri
import java.util.*

/**
 * Created by thinkreed on 2017/6/17.
 */

data class Song(val title: String = "",
                val path: String = "",
                val cover: Uri = Uri.EMPTY,
                val id: Int = Int.MIN_VALUE,
                val artist: String = "")