package reed.flyingreed.mvvm

import android.os.Bundle

/**
 * Created by thinkreed on 2017/6/17.
 */
interface Command {
    fun execute(bundle: Bundle)
}