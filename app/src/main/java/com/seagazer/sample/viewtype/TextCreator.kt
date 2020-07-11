package com.seagazer.sample.viewtype

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.seagazer.multitype.ViewTypeCreator
import com.seagazer.sample.R

/**
 *
 * Author: Seagazer
 * Date: 2020/7/11
 */
class TextCreator : ViewTypeCreator<String, TextCreator.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.text)
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): Holder {
        return Holder(inflater.inflate(R.layout.view_type_text, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, data: String) {
        holder.text.text = data
    }

    override fun match(data: String): Boolean {
        return false
    }

}