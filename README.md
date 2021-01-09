# multitype
Helper make easy to create multi type holder for recyclerview

#### 基本使用方法：
1. 添加依赖和相关配置：
```java
    // 1.根目录的build.gradle配置maven地址:
    allprojects {
	    repositories {
		    ...
		    maven { url 'https://www.jitpack.io' }
	    }
    }

    // 2.添加如下配置:
    android {
        // 添加java8支持
        compileOptions {
            sourceCompatibility JavaVersion.VERSION_1_8
            targetCompatibility JavaVersion.VERSION_1_8
        }
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    // 3.项目中添加远程依赖:
    dependencies {
            implementation 'com.github.seagazer:multitype:1.0.1'
	}
```

1. 使用方法：
* 继承`MultiTypeAdapter`实现自己的适配器
```kotlin
class SampleAdapter : MultiTypeAdapter() {

    val data = mutableListOf<Any>()

    override fun getData(position: Int) = data[position]

    override fun getItemCount() = data.size
}
```

* 继承`ViewTypeCreator`实现一种或多种样式构造器，泛型中第一个参数传入构造器绑定的数据类型，如果`String`类型数据对应`TextCreator`唯一一种样式，`match`方法返回`false`即可:
```kotlin
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
```

* 如果同一个数据结构体(`Title`)对应多种样式，需要在`match`中进行区分：
```kotlin
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
        // Title数据中只包含mainTitle时返回true，通过该构造器构造视图样式
        return !TextUtils.isEmpty(data.mainTitle) && TextUtils.isEmpty(data.subTitle)
    }
}

class SubTitleCreator : ViewTypeCreator<Title, SubTitleCreator.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.sub_title)
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): Holder {
        return Holder(inflater.inflate(R.layout.view_type_sub_title, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, data: Title) {
        holder.title.text = data.subTitle
    }

    override fun match(data: Title): Boolean {
        // Title数据中只包含subTitle时返回true，通过该构造器构造视图样式
        return !TextUtils.isEmpty(data.subTitle) && TextUtils.isEmpty(data.mainTitle)
    }
}
```

* 注册构造器和填充数据:
```kotlin
        val adapter = SampleAdapter().apply {
            // image type
            registerCreator(ImageCreator())
            // text type
            registerCreator(TextCreator())
            // the same bean but different view type
            registerCreator(MainTitleCreator())
            registerCreator(SubTitleCreator())
        }
        // fill data
        for (i in 0..10) {
            adapter.data.run {
                add(R.drawable.test)
                add("I am string")
                add(Title("I am MainTitle"))
                add(Title("", "I am SubTitle"))
            }
        }
        // recyclerView设置适配器
        recycler_view.adapter = adapter
```

* 通过以上步骤，就创建了多种样式的布局视图

##### 更多具体使用方式和场景可参考项目中的`app`工程

#### Demo展示：
<img src="https://upload-images.jianshu.io/upload_images/4420407-69631337dd9b613d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240" width="240" height="450"/>
