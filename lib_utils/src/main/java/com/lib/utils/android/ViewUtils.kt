package com.lib.utils.android

import android.graphics.Paint
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView

/**
 *
 *
 * @author: Admin.
 * @date  : 2019-08-04.
 */
object ViewUtils {
    /**
     * 根据ListView的所有子项的高度设置其高度
     *
     * @param listView
     */
    fun setListViewHeightByAllChildrenViewHeight(listView: ListView) {
        val listAdapter = listView.adapter
        if (listAdapter != null) {
            var totalHeight = 0
            for (i in 0 until listAdapter.count) {
                val listItem = listAdapter.getView(i, null, listView)
                listItem.measure(0, 0)
                totalHeight += listItem.measuredHeight
            }
            val params = listView.layoutParams
            params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
            (params as ViewGroup.MarginLayoutParams).setMargins(10, 10, 10, 10)
            listView.layoutParams = params
        }
    }

    /**
     * 下划线
     */
    fun setFlagsBottom(tv: TextView) {
        tv.paint.flags = Paint.UNDERLINE_TEXT_FLAG //下划线
        tv.paint.isAntiAlias = true //抗锯齿
    }

    /**
     * //中划线
     */
    fun setFlagsCenter(tv: TextView) {
        tv.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG//中划线
        tv.paint.isAntiAlias = true //抗锯齿
    }

    /**
     * 取消设置的的划线
     */
    fun cancelFlags(tv: TextView) {
        tv.paint.flags = 0 // 取消设置的的划线
    }
}