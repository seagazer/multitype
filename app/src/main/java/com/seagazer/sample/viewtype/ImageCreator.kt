package com.seagazer.sample.viewtype

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.seagazer.multitype.ViewTypeCreator
import com.seagazer.sample.R

/**
 *
 * Author: Seagazer
 * Date: 2020/7/11
 */
class ImageCreator : ViewTypeCreator<Int, ImageCreator.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): Holder {
        return Holder(inflater.inflate(R.layout.view_type_image, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, data: Int) {
        holder.image.setImageResource(data)
    }

    override fun match(data: Int): Boolean {
        return false
    }

}