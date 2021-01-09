package com.seagazer.sample

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seagazer.sample.datatype.Title
import com.seagazer.sample.viewtype.ImageCreator
import com.seagazer.sample.viewtype.MainTitleCreator
import com.seagazer.sample.viewtype.SubTitleCreator
import com.seagazer.sample.viewtype.TextCreator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = SampleAdapter().apply {
            // image type
            registerCreator(ImageCreator())
            // text type
            registerCreator(TextCreator())
            // the same bean but different view type
            registerCreator(MainTitleCreator())
            registerCreator(SubTitleCreator())
        }
        fillData(adapter)
        recycler_view.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.set(0, 8, 0, 8)
            }
        })
        recycler_view.adapter = adapter
    }

    private fun fillData(adapter: SampleAdapter) {
        for (i in 0..10) {
            adapter.data.run {
                add(R.drawable.test)
                add("I am string")
                add(Title("I am MainTitle"))
                add(Title("", "I am SubTitle"))
            }
        }
    }
}
