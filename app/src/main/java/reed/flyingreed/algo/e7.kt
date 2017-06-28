package reed.flyingreed.algo

/**
 * Created by thinkreed on 2017/6/27.
 */

fun reverse(x: Int): Int {

    var result = 0
    var origin = x

    while (origin != 0) {
        val tail = origin % 10
        val newResult = result * 10 + tail
        //如果溢出，新结果反操作后将不会等于原结果
        if ((newResult - tail) / 10 != result) {
            return 0
        }
        result = newResult
        origin /= 10
    }

    return result
}