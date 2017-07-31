package reed.flyingreed.algo

import java.util.ArrayList

/**
 * Created by thinkreed on 2017/7/31.
 */

fun isIntegerPowerOf2(n: Int): Boolean {
    //2整数次幂的二进制特点是只有一个1且1在首位，如果对它减一，则会得到一个除首位是0，其他全是1
    //的数，两数相与为0则该数是2的整数次幂
    return n.and(n - 1) == 0
}