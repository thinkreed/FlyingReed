package reed.flyingreed.mvvm

/**
 * Created by thinkreed on 2017/7/5.
 */


class ObservableField<T>(value: T) {

    interface FieldListener<T> {
        fun onFiledChanged(field: T)
    }

    private var field: T = value
        set(value) {
            field = value
            notifyFieldChange()
        }

    private val fieldListeners by lazy {
        mutableListOf<FieldListener<T>>()
    }

    fun subscribe(fieldListener: FieldListener<T>) {
        fieldListeners.add(fieldListener)
    }

    fun unSubscribe(fieldListener: FieldListener<T>) {
        fieldListeners.remove(fieldListener)
    }

    private fun notifyFieldChange() {
        for (fieldListener in fieldListeners) {
            fieldListener.onFiledChanged(field)
        }
    }

}