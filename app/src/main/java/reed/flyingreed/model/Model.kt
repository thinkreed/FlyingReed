package reed.flyingreed.model

import android.net.Uri

/**
 * Created by thinkreed on 2017/6/17.a
 */

data class Model(var song: Song = Song(),
                 var user: User = User(),
                 var template: Template = Template.EMPTY,
                 var title: String = "",
                 var id: Int = Int.MIN_VALUE,
                 var cover: Uri = Uri.EMPTY,
                 var description:String = "")
