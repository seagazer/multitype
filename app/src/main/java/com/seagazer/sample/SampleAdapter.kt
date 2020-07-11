package com.seagazer.sample

import com.seagazer.multitype.MultiTypeAdapter

/**
 *
 * Author: Seagazer
 * Date: 2020/7/11
 */
class SampleAdapter : MultiTypeAdapter() {

    val data = mutableListOf<Any>()

    override fun getData(position: Int) = data[position]

    override fun getItemCount() = data.size
}