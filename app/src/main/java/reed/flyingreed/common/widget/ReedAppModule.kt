package reed.flyingreed.common.widget

import android.content.Context
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.load.resource.SimpleResource
import android.graphics.BitmapFactory
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder
import java.io.InputStream


/**
 * Created by thinkreed on 2017/7/16.
 */

@GlideModule
class ReedAppModule : AppGlideModule() {

    override fun applyOptions(context: Context?, builder: GlideBuilder?) {
        builder?.let {
        }
    }

    override fun registerComponents(context: Context?, registry: Registry?) {
        registry?.let {
            registry
                    .append(InputStream::class.java, BitmapFactory.Options::class.java,
                    BitmapSizeDecoder())
                    .register(BitmapFactory.Options::class.java, Size::class.java,
                            OptionsSizeResourceTranscoder())

        }
    }

    override fun isManifestParsingEnabled(): Boolean {
        return true
    }

    data class Size(val width: Int, val height: Int)

    class BitmapSizeDecoder : ResourceDecoder<InputStream, BitmapFactory.Options> {
        override fun handles(source: InputStream?, options: Options?): Boolean {
            return true
        }

        override fun decode(source: InputStream?, width: Int, height: Int, options: Options?)
                : Resource<BitmapFactory.Options>? {
            val bmOptions = BitmapFactory.Options()
            bmOptions.inJustDecodeBounds = true
            BitmapFactory.decodeStream(source, null, bmOptions)
            return SimpleResource(bmOptions)
        }

        val id: String
            get() = javaClass.name
    }

    class OptionsSizeResourceTranscoder : ResourceTranscoder<BitmapFactory.Options, Size> {

        override fun transcode(toTranscode: Resource<BitmapFactory.Options>?): Resource<Size> {
            val options = toTranscode?.get()
            if (options != null) {
                val size = Size(options.outWidth, options.outHeight)
                return SimpleResource(size)
            }
            return SimpleResource(Size(0, 0))

        }

    }
}

