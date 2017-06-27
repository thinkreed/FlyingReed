package reed.flyingreed.algo

/**
 * Created by thinkreed on 2017/6/24.
 */

fun processString(str: String): String {
    //处理输入的字符串，给其头尾加上特殊字符'$'和'^'分别表示开始和终结，中间插入#处理使得输入的字符串总是奇数长度拥有中心点
    //"abc"->"$#a#b#c#^"
    //"abbc"->"$#a#b#b#c#^"
    return str.toCharArray().joinToString(separator = "#", prefix = "$#", postfix = "#^")
}

//思路来自manacher算法 http://dl.acm.org/citation.cfm?doid=321892.321896
fun longest_palindromic_substring(str: String): String {

    val processedStr = processString(str)

    //每次的拓展中心
    var center = 0
    //每次已知最长回文子串的右边界
    var right = 0
    //辅助数组，记录每个位置上的最大回文长度
    val p = IntArray(processedStr.length)

    for (index in 1 until processedStr.length - 1) {
        //index关于center的对称点
        val index_mirror = 2 * center - index
        //manacher算法的重要结论，如果当前拓展点index在已知最长回文子串的右边界内，则根据对称性可以得出结论
        //当前拓展点的回文长度p[index]大于等于当前拓展点到已知最长回文子串的右边界的距离right - index和p的对称点上的
        //最大回文长度p[index_mirror]
        p[index] = if (right > index) minOf(p[index_mirror], right - index) else 0

        //从当前点i在p[i]的基础上继续拓展，如果两边元素不同则停止
        while (processedStr[index + p[index] + 1] == processedStr[index - 1 - p[index]]) {
            p[index]++
        }

        //如果本次拓展超出已知最长回文子串的右边界，则将最长回文子串的中心点移至index
        if (index + p[index] > right) {
            center = index
            right = index + p[index]
        }
    }

    var maxLen = 0
    var lCenter = 0

    //遍历辅助数组，获取最大回文子串的长度和中心点
    for ((index, value) in p.withIndex()) {
        if (value > maxLen) {
            maxLen = value
            lCenter = index
        }
    }

    //在原字符串中的起始点
    val originStart = (lCenter - 1 - maxLen) / 2


    return str.substring(originStart, originStart + maxLen)
}