package com.seagazer.sample.viewtype

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.seagazer.multitype.ViewTypeCreator
import com.seagazer.sample.R
import com.seagazer.sample.datatype.Title

/**
 *
 * Author: Seagazer
 * Date: 2020/7/11
 */
class MainTitleCreator : ViewTypeCreator<Title, MainTitleCreator.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.main_title)
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): Holder {
        return Holder(inflater.inflate(R.layout.view_type_main_title, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, data: Title) {
        holder.title.text = data.mainTitle
    }

    override fun match(data: Title): Boolean {
        return !TextUtils.isEmpty(data.mainTitle) && TextUtils.isEmpty(data.subTitle)
    }

}