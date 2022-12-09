package com.muralex.navstructure.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.muralex.shared.app.utils.Constants.Action
import com.muralex.navstructure.databinding.ListItemSectionBinding
import com.muralex.shared.domain.data.navstructure.Section
import com.muralex.navstructure.presentation.utils.setHomeListItemImage


class SectionsListAdapter : ListAdapter<Section, SectionsListAdapter.ViewHolder>(ListDiffCallBack()) {
    private var onItemClickListener : ( (Action, Section) -> Unit )?= null

    fun setOnItemClickListener (listener:  (Action, Section) -> Unit ) {
        onItemClickListener = listener
    }

    class ViewHolder (private val binding: ListItemSectionBinding) : RecyclerView.ViewHolder (binding.root) {
        fun bind(item: Section, onItemClickListener: ((Action, Section) -> Unit)?) {

            binding.apply {
                tvTitle.text = item.title
                tvDesc.text = item.desc
                ivListImage.setHomeListItemImage(item.image)

                onItemClickListener?.let { clicker ->
                    cardWrap.setOnClickListener {
                        clicker(Action.Click, item)
                    }
                }
            }
        }

        companion object {
            fun from (parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSectionBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClickListener )
    }
}

class ListDiffCallBack : DiffUtil.ItemCallback<Section>() {
    override fun areItemsTheSame(oldItem: Section, newItem: Section): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Section, newItem: Section): Boolean {
        return oldItem == newItem
    }
}

