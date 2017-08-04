package reed.flyingreed.controller.legos

import reed.flyingreed.lego.Lego
import reed.flyingreed.lego.LegoFactory

/**
 * Created by thinkreed on 2017/8/4.
 */
class TextLegoFactory : LegoFactory {
    override fun createLego(): Lego {
        return TextLego()
    }
}