package com.yibo.yiboapp.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yibo.yiboapp.R
import com.yibo.yiboapp.entify.Card
import com.yibo.yiboapp.manager.BankingManager
import com.yibo.yiboapp.mvvm.ifNullOrEmpty
import com.yibo.yiboapp.utils.Utils
import java.text.SimpleDateFormat

class AccountManagerNewAdapter(val cards: List<Card>):
    RecyclerView.Adapter<AccountManagerNewAdapter.AccountHolder>(), OnClickListener {

    private var callback: PickAccountNewListener? = null

    fun setCallback(cb: PickAccountNewListener){ callback = cb }

    override fun getItemCount(): Int = cards.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_account_manager_new, parent, false)
        view.setOnClickListener(this)
        return AccountHolder(view)
    }

    override fun onBindViewHolder(holder: AccountHolder, position: Int) {
        holder.bindData(position, cards[position])
    }

    class AccountHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageType: ImageView
        private val textType: TextView
        private val textRealName: TextView
        private val textAccount: TextView
        private val textCreateTime: TextView

        private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        init {
            imageType = view.findViewById(R.id.imageType)
            textType = view.findViewById(R.id.textType)
            textRealName = view.findViewById(R.id.textRealName)
            textAccount = view.findViewById(R.id.textAccount)
            textCreateTime = view.findViewById(R.id.textCreateTime)
        }

        fun bindData(position: Int, card: Card) {
            itemView.tag = position
            itemView.backgroundTintList = ColorStateList.valueOf(Color.parseColor(card.bgColorCode))
            loadImage(imageType, card)
            textType.text = if(card.type == 1) card.bankName!! else card.typeName
            textRealName.text = card.realName
            textAccount.text = card.cardNo
            textCreateTime.text = "绑定时间：${sdf.format(card.createTime)}"
        }

        private fun loadImage(imageView: ImageView, card: Card){
            val defaultImage = when(card.type){
                1 -> R.drawable.icon_pay_bank
                2 -> R.drawable.icon_pay_usdt
                3 -> R.drawable.icon_pay_gopay
                4 -> R.drawable.icon_pay_okpay
                5 -> R.drawable.icon_pay_topay
                6 -> R.drawable.icon_pay_vpay
                7 -> R.drawable.icon_pay_alipay
                else -> R.drawable.icon_pay_bank
            }

            Glide.with(imageView.context)
                .load(card.imageUrl)
                .apply(RequestOptions().error(defaultImage))
                .into(imageView)
        }
    }

    override fun onClick(v: View) {
        val position = v.tag as Int
        callback?.onPickAccount(cards[position])
    }

    interface PickAccountNewListener{ fun onPickAccount(card: Card) }
}