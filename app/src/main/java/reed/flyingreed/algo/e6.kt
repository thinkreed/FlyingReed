package reed.flyingreed.algo


/**
 * Created by thinkreed on 2017/6/25.
 */

fun zigZag(str: String, numRows: Int): String {
/*                n=numRows,zigzag的具体表现如下,先向下numRows个数，再斜向上numRows - 1个数,
    1                           2n-1                         4n-3 第一行的下标差Δ=2 * (n - 1)
    2                     2n-2  2n                    4n-4   4n-2
    3               2n-3        2n+1              4n-5       .
    .           .               .               .            .    中间第i行的下标差则分为两种情况，斜线上的数和前
    .       n+2                 .           3n               .    一个直线上的数差2*(n - i),和后一个直线上的数差2 * (i - 1)
    n-1 n+1                     3n-3    3n-1                 5n-5
    n                           3n-2                         5n-4 最后一行的下标差Δ=2n-2
*/

    if (numRows == 1) return str

    var result = ""

    for (i in 0 until numRows) {

        var k = i
        var j = 0

        while (k < str.length) {
            result += str[k]
            //j为偶数，下标差Δ=2 * (numRows - i - 1)，为奇数下标差Δ=2 * i
            k += if (((i == 0) or (j % 2 == 0)) && i != numRows - 1) 2 * (numRows - i - 1) else 2 * i
            j++
        }
    }
    return result
}
