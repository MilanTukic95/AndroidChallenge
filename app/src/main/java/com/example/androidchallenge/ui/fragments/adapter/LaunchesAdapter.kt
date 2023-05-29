package com.example.androidchallenge.ui.fragments.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.androidchallenge.R
import com.example.androidchallenge.data.model.LaunchDetail
import com.example.androidchallenge.databinding.ItemLaunchBinding
import com.example.androidchallenge.utils.DateTimeUtils
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class LaunchesAdapter(
    var list: List<LaunchDetail> = arrayListOf(),
) : RecyclerView.Adapter<LaunchesAdapter.ViewHolder>() {

    private val TYPE_SUCCESS = 1
    private val TYPE_UPCOMING = 2
    private val TYPE_FAILURE = 3

    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(item: LaunchDetail)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        listener = onItemClickListener
    }


    inner class ViewHolder(val binding: ItemLaunchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLaunchBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {

                when (holder.itemViewType) {
                    TYPE_SUCCESS -> binding.mcvContainer.strokeColor =
                        ContextCompat.getColor(holder.itemView.context, R.color.green)
                    TYPE_UPCOMING -> binding.mcvContainer.strokeColor =
                        ContextCompat.getColor(holder.itemView.context, R.color.yellow)
                    TYPE_FAILURE -> binding.mcvContainer.strokeColor =
                        ContextCompat.getColor(holder.itemView.context, R.color.red)
                }


                val formattedDate = this.date?.let { DateTimeUtils.formatDate(it) }

                binding.tvName.text = this.name
                binding.tvNameLaunchpad.text = this.nameLaunchpad
                binding.tvRegion.text = this.region
                binding.tvDate.text = formattedDate

                Glide.with(holder.itemView.context)
                    .load(this.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivImage)

                holder.itemView.setOnClickListener {
                    listener?.onItemClick(this)
                }
            }
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = list[position]

        if (item.success) {
            return TYPE_SUCCESS
        }

        if (item.upcoming) {
            return TYPE_UPCOMING
        }

        return TYPE_FAILURE
    }
}