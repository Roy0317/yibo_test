package com.yibo.yiboapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yibo.yiboapp.databinding.TableHeaderBinding

class PeilvBigDataHeaderAdapterKt: RecyclerView.Adapter<HeaderViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewholder {
        return HeaderViewholder(TableHeaderBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: HeaderViewholder, position: Int) {
        val binding = holder.binding as TableHeaderBinding
        binding.category.visibility = View.GONE
    }

}

class HeaderViewholder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)