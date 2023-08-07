package com.yibo.yiboapp.views

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yibo.yiboapp.R

class SimpleSelectDialog(context: Context, val title: String, val selections: List<SimpleItem>,
                         private val listListener: SimpleSelectListener): Dialog(context){

    init {
        val view = View.inflate(context, R.layout.dialog_simple_select, null)
        setContentView(view)

        val textTitle = findViewById<TextView>(R.id.text_title)
        textTitle.text = title
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(getContext())
        val adapter = SelectionAdapter(selections)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL))
        setCancelable(true)
        setCanceledOnTouchOutside(true) //点击外部Dialog消失
    }

    private inner class SelectionAdapter(val items: List<SimpleItem>) :
        RecyclerView.Adapter<ItemHolder?>(), View.OnClickListener {
        override fun getItemCount(): Int { return items.size }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_account_type, parent, false)
            view.setOnClickListener(this)
            return ItemHolder(view)
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            holder.bindData(items[position])
            holder.itemView.tag = position
        }

        override fun onClick(v: View) {
            val position = v.tag as Int
            listListener.onItemSelected(items[position])
            dismiss()
        }
    }

    private class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textItem: TextView = view.findViewById(R.id.text_type)

        fun bindData(item: SimpleItem) {
            textItem.text = item.itemName
            textItem.setTextColor(ContextCompat.getColor(itemView.context, if(item.isChecked) R.color.colorPrimary else R.color.black))
        }
    }

    data class SimpleItem(val itemName: String, var isChecked: Boolean = false)

    interface SimpleSelectListener { fun onItemSelected(item: SimpleItem) }
}