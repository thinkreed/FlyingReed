package reed.flyingreed.lego

/**
 * Created by thinkreed on 2017/8/3.
 *
 * Act just like a tiny fragment.
 */
interface Lego{
    /**
     * Called when this lego is to be initialized.
     */
    fun onInit()

    /**
     * Called when this lego is about to be rendered.
     */
    fun onRender()
}