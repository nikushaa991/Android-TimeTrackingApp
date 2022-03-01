package com.example.timetrackingapp.presentation.timetracking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timetrackingapp.databinding.TimeTrackingDataItemBinding
import com.example.timetrackingapp.databinding.TimeTrackingHeaderItemBinding
import com.example.timetrackingapp.databinding.TimeTrackingLoaderItemBinding
import com.example.timetrackingapp.domain.model.Task
import com.example.timetrackingapp.shared.secondsToTimeText

class TimeTrackingAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = mutableListOf<TimeTrackingItem>()

    fun submit(items: List<TimeTrackingItem>) {
        this@TimeTrackingAdapter.items = items.toMutableList()
        notifyDataSetChanged()
    }

    fun setLoading(isLoading: Boolean) {
        if (isLoading)
            items.add(TimeTrackingItem.Loader)
        else
            items.removeIf { it is TimeTrackingItem.Loader }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is TimeTrackingItem.Data -> VIEW_TYPE_DATA
            is TimeTrackingItem.Loader -> VIEW_TYPE_LOADING
            is TimeTrackingItem.Header -> VIEW_TYPE_HEADER
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_DATA -> AdapterItemViewHolder(
                TimeTrackingDataItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            VIEW_TYPE_LOADING -> AdapterLoaderViewHolder(
                TimeTrackingLoaderItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            VIEW_TYPE_HEADER -> AdapterHeaderViewHolder(
                TimeTrackingHeaderItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> {
                throw IllegalStateException("No such view type")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AdapterItemViewHolder -> holder.bind(items[position] as TimeTrackingItem.Data)
            else -> Unit
        }
    }

    companion object {
        private const val VIEW_TYPE_DATA = 0
        private const val VIEW_TYPE_HEADER = 1
        private const val VIEW_TYPE_LOADING = 2
    }

    class AdapterHeaderViewHolder(binding: TimeTrackingHeaderItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    class AdapterLoaderViewHolder(binding: TimeTrackingLoaderItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    class AdapterItemViewHolder(private val binding: TimeTrackingDataItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TimeTrackingItem.Data) {
            with(binding) {
                task.text = item.data.name
                desc.text = item.data.desc
                time.text = item.data.time.secondsToTimeText()
            }
        }
    }
}

sealed class TimeTrackingItem {
    data class Data(val data: Task) : TimeTrackingItem()
    object Header : TimeTrackingItem()
    object Loader : TimeTrackingItem()
}