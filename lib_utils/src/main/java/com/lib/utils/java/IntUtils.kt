package com.lib.utils.java

import java.util.*

/**
 * 整形数据工具类，提供一些有关整形数据的便捷方法
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 */
object IntUtils {
    /**
     * 获取指定数量的随机数，可以指定是否允许重复
     *
     * @param size     指定数量,取值范围大于0
     * @param maxValue 最大值，如果小于0，无限制
     * @param repeat   是否允许重复，true：允许
     * @return 长度为size的随机数数组
     */
    fun getRandoms(size: Int, maxValue: Int, repeat: Boolean): IntArray {
        val random = Random()
        val randoms = IntArray(size)
        if (repeat) {                                                                        //如果允许重复
            if (maxValue < 0) {                                                            //如果无限制
                for (w in randoms.indices) {
                    randoms[w] = random.nextInt()
                }
            } else {                                                                        //如果有限制
                for (w in randoms.indices) {
                    randoms[w] = random.nextInt(maxValue)
                }
            }
        } else {                                                                            //如果不允许重复
            if (maxValue < 0) {                                                            //如果无限制
                var length = 0
                while (length <= size) {                                                    //条件为长度小于等于size
                    var skip = false
                    val ran = random.nextInt()
                    for (e in 0 until length) {                                    //遍历寻找是否有重复的
                        if (randoms[e] == ran) {                                            //有的话
                            skip = true                                                //跳
                            break                                                        //结束本层循环
                        }
                    }
                    if (skip) {                                                            //如果跳
                        continue
                    }
                    randoms[length++] = ran                                            //如果不跳，放进去，长度加1
                }
            } else {                                                                        //如果有限制
                var length = 0
                while (length <= size) {                                                    //条件为长度小于等于size
                    var skip = false
                    val ran = random.nextInt(maxValue)
                    for (e in 0 until length) {                                    //遍历寻找是否有重复的
                        if (randoms[e] == ran) {                                            //有的话
                            skip = true                                                //跳
                            break                                                        //结束本层循环
                        }
                    }
                    if (skip) {                                                            //如果跳
                        continue
                    }
                    randoms[length++] = ran                                            //如果不跳，放进去，长度加1
                }
            }
        }
        return randoms
    }

}