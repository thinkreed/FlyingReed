package reed.flyingreed.component

/**
 * Created by thinkreed on 2017/7/13.
 */
object DataVerfiy {

    fun <T> verifyReturnDefault(executor: () -> T, defaultValue: T): T {
        return try {
            executor.invoke()
        } catch (e: Exception) {
            defaultValue
        }
    }

    fun <T> verifyReturnOrThrow(executor: () -> T): T {
        return try {
            executor.invoke()
        } catch (e: Exception) {
            throw IllegalArgumentException("error")
        }
    }
}