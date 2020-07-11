package com.seagazer.multitype

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.forEach
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * An abstract class to create multi type of {@link RecyclerView.Adapter}.
 *
 * Author: Seagazer
 * Date: 2020/5/9
 */
abstract class MultiTypeAdapter : RecyclerView.Adapter<ViewHolder>() {
    private val dataCache: ArrayList<Class<*>> = ArrayList()// [DataType]
    private val creatorCache: SparseArray<SparseArray<ViewTypeCreator<Any, *>>> = SparseArray()// DataTypeIndex - [ViewTypeCreators]
    private val viewTypeCache: SparseArray<ViewTypeCreator<Any, *>> = SparseArray()// ViewType - ViewTypeCreator

    abstract fun getData(position: Int): Any

    /**
     * Register a viewTypeCreator to create a view for this data type.
     *
     * @param creator A viewTypeCreator instance.
     */
    inline fun <reified T : Any> registerCreator(creator: ViewTypeCreator<T, *>) {
        registerCreatorInner(T::class.java, creator)
    }

    /**
     * A data may bind more than one viewTypeCreator, so we create an identityHashCode to
     * mark every viewTypeCreator for the same data type.
     *
     * @param clazz The type of data.
     * @param creator The viewTypeCreator to bind this data type.
     */
    fun registerCreatorInner(clazz: Class<*>, creator: ViewTypeCreator<*, *>) {
        var index = dataCache.indexOf(clazz)
        if (index == -1) {
            dataCache.add(clazz)
            index = dataCache.size - 1
        }
        var cache = creatorCache[index]
        if (cache == null) {
            cache = SparseArray()
        }
        val id = System.identityHashCode(creator)
        @Suppress("UNCHECKED_CAST")
        cache.put(id, creator as ViewTypeCreator<Any, *>)
        creatorCache.put(index, cache)
    }

    override fun getItemViewType(position: Int): Int {
        val data = getData(position)
        val viewType = getCreatorViewType(data)
        return if (viewType != -1) {
            viewType
        } else
            super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        val itemViewType = getItemViewType(position)
        val viewCreator = getViewCreatorByViewType(itemViewType)
        return viewCreator.getItemId(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewCreator: ViewTypeCreator<*, *> = getViewCreatorByViewType(viewType)
        return viewCreator.onCreateViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getData(position)
        @Suppress("UNCHECKED_CAST")
        val viewCreator: ViewTypeCreator<Any, ViewHolder> =
            getViewCreatorByViewType(getItemViewType(position)) as ViewTypeCreator<Any, ViewHolder>
        viewCreator.onBindViewHolder(holder, data)
    }

    /**
     * Find the viewType by a data instance.
     * If the Data bind more than one viewTypeCreator, we use function <match> to mark the viewTypeCreator which you expect want.
     * For example:
     * <p>
     * Data bean Person bind ManViewTypeCreator and FemaleViewTypeCreator
     * <p>
     * <code>
     * data class Person(val sex: Int)
     * <p>
     * class ManViewTypeCreator : ViewTypeCreator<Person, ManHolder>(){
     *     override fun match(data: Person) = data.sex == Sex.MAN
     * }
     * <p>
     * class FemaleViewTypeCreator : ViewTypeCreator<Person, FemaleHolder>(){
     *     override fun match(data: Person) = data.sex == Sex.FEMALE
     * }
     * </code>
     *
     * @param data The data instance.
     * @return The viewType.
     */
    private fun getCreatorViewType(data: Any): Int {
        val clazz: Class<*> = data::class.java
        var viewType: Int
        val index = dataCache.indexOf(clazz)
        if (dataCache.size > 0 && index != -1) {
            val creators: SparseArray<ViewTypeCreator<Any, *>> = creatorCache[index]
            // The Data bind more than one viewTypeCreator.
            if (creators.size() > 1) {
                creators.forEach { id, viewCreator ->
                    if (viewCreator.match(data)) {
                        viewType = id
                        if (viewTypeCache.indexOfKey(viewType) < 0) {
                            viewTypeCache.put(viewType, viewCreator)
                        }
                        return viewType
                    }
                }
            }
            // The Data only bind one viewTypeCreator.
            else if (creators.size() == 1) {
                viewType = creators.keyAt(0)
                if (viewTypeCache.indexOfKey(viewType) < 0) {
                    viewTypeCache.put(viewType, creators.valueAt(0))
                }
                return viewType
            }
        }
        throw RuntimeException("Current dataType [$clazz] is not found in DataTypeCache:\n$dataCache \nPlease check the Type of data for your custom creator.")
    }

    /**
     * Find the viewTypeCreator by current viewType.
     *
     * @param viewType Current viewType.
     * @return The ViewTypeCreator.
     */
    private fun getViewCreatorByViewType(viewType: Int): ViewTypeCreator<Any, *> {
        return viewTypeCache[viewType]
    }

}