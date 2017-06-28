package reed.flyingreed.algo

import com.google.gson.annotations.Until

/**
 * Created by thinkreed on 2017/6/28.
 */

fun myatoi(str: String): Int {
    if (str.isEmpty()) return 0

    //去除头尾的空格
    val tmp = str.trim()
    var result = 0
    var sign = 1
    //判断正负号
    if ((tmp[0] == '-') or (tmp[0] == '+')) {
        sign = if (tmp[0] == '+') 1 else -1
    }
    for (i in 1 until tmp.length) {
        val cur = tmp[i] - '0'
        //字符串不是数字
        if ((cur < 0) or (cur > 9)) {
            break
        }
        //判断溢出
        if ((result > Int.MAX_VALUE / 10) or ((result == Int.MAX_VALUE / 10) && (cur > Int.MAX_VALUE % 10))) {
            return if (sign == 1) Int.MAX_VALUE else Int.MIN_VALUE
        }
        result = result * 10 + cur
    }
    return sign * result
}