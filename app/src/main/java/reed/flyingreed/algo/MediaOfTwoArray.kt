package reed.flyingreed.algo

import org.xml.sax.ext.Locator2

/**
 * Created by thinkreed on 2017/6/23.
 */

fun findMedianOfTwoArray(a1: Array<Int>, a2: Array<Int>): Double {

    //保证a2比a1短
    if (a1.size < a2.size) return findMedianOfTwoArray(a2, a1)

    //a2为空，返回a1的中间点
    if (a2.isEmpty()) return (a1[(a1.size - 1) / 2].toDouble() + a1[a1.size / 2].toDouble()) / 2

    var low = 0
    var high = 2 * a2.size

    //二分查找均分点
    while (low <= high) {
        //以中点切分
        val mid2 = (low + high) / 2
        //通过mid2推断mid1,若mid1和mid2是两数组的两个切分点，则满足mid1+mid2 = a1.size + a2.size
        val mid1 = a1.size + a2.size - mid2

        //L1，R1分别表示数组a1的切分点C1的左相邻元素，右相邻元素
        //L2,R2分别表示数组a2的切分点C2的左相邻元素，右相邻元素
        val L1 = if (mid1 == 0) Int.MIN_VALUE else a1[(mid1 - 1) / 2]
        val L2 = if (mid2 == 0) Int.MIN_VALUE else a2[(mid2 - 1) / 2]
        val R1 = if (mid1 == 2 * a2.size) Int.MAX_VALUE else a1[mid1 / 2]
        val R2 = if (mid2 == 2 * a2.size) Int.MAX_VALUE else a2[mid2 / 2]

        //a1的左边部分太大了，将切分点C1左移，C2相应得右移
        if (L1 > R2) low = mid2 + 1
        //a2的左边部分太大了,将切分点C2左移，C1相应右移
        else if (L2 > R1) high = mid2 - 1
        //满足条件L1<=R2 && L2<=R1
        else return (maxOf(L1, L2).toDouble() + minOf(R1, R2).toDouble()) / 2
    }

    return -1.0
}