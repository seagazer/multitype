package com.seagazer.multitype

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * Author: Seagazer
 * Date: 2020/5/9
 */
abstract class ViewTypeCreator<T, VH : ViewHolder> {

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * @param inflater A layoutInflater to inflate a view resource.
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    abstract fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param data The data ready to bind to the holder.
     */
    abstract fun onBindViewHolder(holder: VH, data: T)

    /**
     * If the Data bind more than one viewTypeCreator, we use this function to mark the viewTypeCreator which you expect want.
     * Attention that you should not do too much work here, just make simple to mark diff for the viewTypeCreator and data.
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
     * @param data The data ready to bind ui of current viewTypeCreator.
     */
    abstract fun match(data: T): Boolean

    /**
     * Return the stable ID for the item at <code>position</code>. If {@link #hasStableIds()}
     * would return false this method should return {@link #NO_ID}. The default implementation
     * of this method returns {@link #NO_ID}.
     *
     * @param position Adapter position to query
     * @return the stable ID of the item at position
     */
    open fun getItemId(position: Int) = RecyclerView.NO_ID

}