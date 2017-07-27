package reed.flyingreed.experimental.camera

import android.content.Context
import android.util.AttributeSet
import android.view.TextureView

/**
 * Created by thinkreed on 2017/7/27.
 */
class PreviewTextureView(context: Context, attributeSet: AttributeSet?, defStyle: Int)
    : TextureView(context, attributeSet, defStyle) {

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context) : this(context, null)
}
