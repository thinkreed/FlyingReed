package reed.flyingreed.lego

import android.view.View

/**
 * Created by thinkreed on 2017/8/3.
 */
abstract class ViewGroupLego(parentView: View) : BaseLego(parentView) {

    private val mChildren by lazy {
        mutableListOf<Lego>()
    }

    init {

        this.onInit()

        this.onRender()
    }

    override fun onInit() {

        installLego()

        for (lego in mChildren) {
            lego.onInit()
        }
    }

    override fun onRender() {
        for (lego in mChildren) {
            lego.onRender()
        }
    }

    fun addLego(lego: Lego) {
        mChildren.add(lego)
    }

    fun removeLego(lego: Lego) {
        mChildren.remove(lego)
    }

    abstract fun installLego()
}