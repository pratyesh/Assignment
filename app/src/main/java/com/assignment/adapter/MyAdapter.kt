package com.assignment.adapter

import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.assignment.R
import com.assignment.model.Row
import com.assignment.util.ImageUtil

class MyAdapter constructor(dataModelList: ArrayList<Row>) : RecyclerView.Adapter<MyAdapter.BaseViewHolder>() {

    val mDataModelList: ArrayList<Row> = dataModelList

    fun updateList(dataModelList: List<Row>) {
        val startPosition = itemCount
        val count = dataModelList.size
        mDataModelList.addAll(dataModelList);
        refreshOnItemUI(startPosition, count)
    }

    fun refreshOnItemUI(startPosition: Int, count: Int) {
        notifyItemRangeInserted(startPosition, count);
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return BaseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataModelList.size
    }

    fun getItem(position: Int): Row {
        return mDataModelList[position]
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val dataModel = getItem(position)
        setImage(dataModel, holder)

        holder.titleText.text = dataModel.title
        holder.description.text = dataModel.description
    }

    internal fun setImage(dataModel: Row, holder: BaseViewHolder) {
        dataModel.imageHref?.let {
            ImageUtil.getInstance().loadImage(it, holder.image)
        }
    }

    inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: AppCompatImageView
        var titleText: AppCompatTextView
        var description: AppCompatTextView

        init {
            image = itemView.findViewById(R.id.image)
            titleText = itemView.findViewById(R.id.titleText)
            description = itemView.findViewById(R.id.description)
        }
    }
}