package com.yibo.yiboapp.ui

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ToastUtils
import com.yibo.yiboapp.R
import com.yibo.yiboapp.data.PeilvPlayData
import com.yibo.yiboapp.data.UsualMethod
import com.yibo.yiboapp.data.YiboPreference
import com.yibo.yiboapp.databinding.TableLayoutSimpleBinding
import com.yibo.yiboapp.interfaces.NormalTouzhuListener
import com.yibo.yiboapp.utils.Utils


class PeilvSimpleBigDataAdapterKt(val context: Context, val position: Int) :
    RecyclerView.Adapter<PeilvSimpleViewHolder>() {

    private lateinit var normalTouzhuListener: NormalTouzhuListener
    private lateinit var tableCellListener: TableCellListener
    private var dataList = ArrayList<PeilvPlayData>()
    companion object {
        const val NORMAL_TZ_TYPE = 0
        const val CHAT_TYPE = 1
    }

    private var containerType = 0
    private var cpCode = ""
    private var isFengpan = false //判断是否为封盘


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeilvSimpleViewHolder {
        return PeilvSimpleViewHolder(TableLayoutSimpleBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: PeilvSimpleViewHolder, position: Int) {

        val binding = holder.binding as TableLayoutSimpleBinding
        val data = dataList[position]

        binding.apply {
            switchCellBg(data.focusDrawable, cell, numberTv, peilv, data.color)
            if (!Utils.isEmptyString(data.helpNumber)) {
                if (Utils.isNumeric(data.number)) {
                    categoryNumberBallon.visibility = View.VISIBLE
                    category.visibility = View.GONE
                    categoryNumberBallon.text = data.number
                } else {
                    categoryNumberBallon.visibility = View.GONE
                    category.visibility = View.VISIBLE
                    category.text = data.number
                }
            }
            if (!Utils.isEmptyString(data.number)) {
                val number = data.number.trim { it <= ' ' }
                var helpNumber = ""
                if (!Utils.isEmptyString(data.helpNumber)) {
                    helpNumber = data.helpNumber.trim { it <= ' ' }
                }

                //如果号码不是数字，或者辅助号码不为空，将显示区域占满cell
                val showTxt = if (!Utils.isEmptyString(helpNumber)) helpNumber else number
                if (!Utils.isEmptyString(showTxt)) {
                    if (Utils.isNumeric(showTxt)) {
                        numberTv.visibility = View.GONE
                        binding.number.visibility = View.VISIBLE
                        val version = YiboPreference.instance(context).gameVersion

                        if (showTxt.length >= 3) {
                            binding.number.textSize = 14f
                        } else {
                            binding.number.textSize = 18f
                        }
                        UsualMethod.figureImgeByCpCode(
                            cpCode,
                            showTxt,
                            version,
                            binding.number,
                            0,
                            context
                        )

                    } else {
                        numberTv.visibility = View.VISIBLE
                        binding.number.visibility = View.GONE
                        numberTv.text = showTxt

                        //解决尾数连，号码显示不全，如果是多选情况下，并且字符串长度大于等于13
                        if (data.isCheckbox && showTxt.length >= 11) {
                            category.textSize = 14f
                            numberTv.textSize = 14f
                        } else {
                            category.textSize = 15f
                            numberTv.textSize = 15f
                        }
                    }
                }
            } else {
                number.visibility = View.GONE
                numberTv.visibility = View.GONE
            }

            if (!Utils.isEmptyString(data.peilv)) {
                if (!Utils.isEmptyString(data.peilv)) {
                    peilv.visibility = View.VISIBLE
                    peilv.text = data.peilv
                    //                peilvTV.setText(Utils.doubleToString(Double.valueOf(data.getPeilv())));
                } else {
                    peilv.visibility = View.GONE
                }
            } else {
                peilv.visibility = View.GONE
            }

            if (data.isCheckbox) {
                checkbox.visibility = View.VISIBLE
                llCheck.visibility = View.VISIBLE
                checkbox.isChecked = data.isSelected
                peilv.visibility = View.GONE
            } else {
                checkbox.visibility = View.GONE
                llCheck.visibility = View.GONE
                peilv.visibility = View.VISIBLE
            }

            cell.setOnClickListener {
                if (this@PeilvSimpleBigDataAdapterKt::tableCellListener.isInitialized) {
                    if (isFengpan) {
                        ToastUtils.showShort(R.string.result_not_open_now)
                        return@setOnClickListener
                    }

                    val playData: PeilvPlayData = dataList[position]

                    if (playData.isSelected) {
                        playData.money = 0f
                        playData.isSelected = false
                        if (getContainerType() == CHAT_TYPE) {
                            numberTv.setTextColor(Color.WHITE)
                            peilv.setTextColor(Color.WHITE)
                            playData.color = Color.WHITE
                            playData.focusDrawable = R.drawable.chat_table_textview_bg
                        } else {
                            playData.focusDrawable = R.drawable.table_textview_bg
                        }
                    } else {
                        playData.isSelected = true
                        if (getContainerType() == CHAT_TYPE) {
                            numberTv.setTextColor(Color.BLACK)
                            peilv.setTextColor(Color.BLACK)
                            playData.color = Color.BLACK
                            playData.focusDrawable = R.drawable.chat_table_textview_bg_press_simple
                        } else {
                            playData.focusDrawable = R.drawable.table_textview_bg_press_simple
                        }
                    }
                    switchCellBg(playData.focusDrawable, cell, numberTv, peilv, playData.color)
//                    dataList[position] = playData
                    //通知前台，更新底部面板中的注数及总金额
                    if (this@PeilvSimpleBigDataAdapterKt::normalTouzhuListener.isInitialized) {
                        normalTouzhuListener.onNormalUpdate(this@PeilvSimpleBigDataAdapterKt.position, position, true)
                    }
                }
            }
        }


    }


    fun setList(list: List<PeilvPlayData>) {
        dataList.clear()
        dataList.addAll(list)
    }


    private fun switchCellBg(
        focusDrawable: Int,
        cell: LinearLayout?,
        numberStringTV: TextView,
        peilvTV: TextView,
        color: Int
    ) {
        if (cell == null) {
            return
        }
        for (i in 0 until cell.childCount) {
            val v = cell.getChildAt(i)
            if (focusDrawable > 0) {
                if (getContainerType() == CHAT_TYPE) {
                    numberStringTV.setTextColor(color)
                    peilvTV.setTextColor(color)
                }
                v.background = ContextCompat.getDrawable(context, focusDrawable)
            } else {
                if (getContainerType() == CHAT_TYPE) {
                    numberStringTV.setTextColor(Color.WHITE)
                    peilvTV.setTextColor(Color.WHITE)
                    v.background =
                        ContextCompat.getDrawable(context, R.drawable.chat_table_textview_bg)
                } else {
                    v.background = ContextCompat.getDrawable(context, R.drawable.table_textview_bg)
                }
            }
        }
    }

    fun setContainerType(containerType: Int) {
        this.containerType = containerType
    }

    private fun getContainerType(): Int {
        return containerType
    }

    fun setCpCode(cpCode: String) {
        this.cpCode = cpCode
    }

    fun setFengpan(isFengpan: Boolean) {
        this.isFengpan = isFengpan
    }


    interface TableCellListener {
        fun onCellSelect(data: PeilvPlayData?, cellIndex: Int)
    }

    fun setTableCellListener(tableCellListener: TableCellListener) {
        this.tableCellListener = tableCellListener
    }

    fun setNormalTouzhuListener(normalTouzhuListener: NormalTouzhuListener) {
        this.normalTouzhuListener = normalTouzhuListener
    }
}

class PeilvSimpleViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)