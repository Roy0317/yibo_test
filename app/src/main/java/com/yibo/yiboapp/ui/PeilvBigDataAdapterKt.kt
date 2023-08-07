package com.yibo.yiboapp.ui

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ToastUtils
import com.yibo.yiboapp.R
import com.yibo.yiboapp.data.PeilvPlayData
import com.yibo.yiboapp.data.UsualMethod
import com.yibo.yiboapp.data.YiboPreference
import com.yibo.yiboapp.databinding.TableLayoutBinding
import com.yibo.yiboapp.interfaces.NormalTouzhuListener
import com.yibo.yiboapp.utils.Utils


class PeilvBigDataAdapterKt(val context: Context, val position: Int) :
    RecyclerView.Adapter<PeilvViewHolder>() {

    private lateinit var normalTouzhuListener: NormalTouzhuListener
    private lateinit var tableCellListener: TableCellListener
    private var dataList = ArrayList<PeilvPlayData>()


    private var cpCode = ""
    private var isFengpan = false //判断是否为封盘


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeilvViewHolder {
        return PeilvViewHolder(TableLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: PeilvViewHolder, position: Int) {

        val binding = holder.binding as TableLayoutBinding
        val data = dataList[position]

        binding.apply {
            money.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    val playData: PeilvPlayData = data
                    val willChange =
                        if (!Utils.isEmptyString(s.toString())) playData.money != s.toString()
                            .toFloat() else playData.money != 0f
                    if (!willChange) {
                        return
                    }
                    if (!Utils.isEmptyString(s.toString())) {
                        playData.money = s.toString().toFloat()
                        playData.isSelected = true
                        playData.focusDrawable = R.drawable.table_textview_bg_press
                    } else {
                        playData.money = 0f
                        playData.isSelected = false
                        playData.focusDrawable = 0
                    }
                    switchCellBg(playData.focusDrawable, cell)
                    dataList[position] = playData
                    //通知前台，更新底部面板中的注数及总金额
                    if (this@PeilvBigDataAdapterKt::normalTouzhuListener.isInitialized) {
                        normalTouzhuListener.onNormalUpdate(0, position, false)
                    }
                }
            })
            switchCellBg(data.focusDrawable, cell)
            if (!Utils.isEmptyString(data.helpNumber)) {
                layout.visibility = View.VISIBLE
                if (Utils.isNumeric(data.number)) {
                    categoryNumberBallon.visibility = View.VISIBLE
                    category.visibility = View.GONE
                    categoryNumberBallon.text = data.number
                } else {
                    categoryNumberBallon.visibility = View.GONE
                    category.visibility = View.VISIBLE
                    category.text = data.number
                }
            } else {
                layout.visibility = View.GONE
            }
            

            if (!Utils.isEmptyString(data.number)) {
                val number = data.number.trim { it <= ' ' }
                var helpNumber: String? = null
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
                        UsualMethod.figureImgeByCpCode(
                            cpCode,
                            showTxt,
                            version,
                            binding.number,
                            0,
                            context
                        )
                        binding.number.setTextColor(ContextCompat.getColor(context,R.color.grey))
                    } else {
                        numberTv.visibility = View.VISIBLE
                        binding.number.visibility = View.GONE
                        numberTv.text = showTxt
                        if (showTxt!!.length >= 3) {
                            numberTv.textSize = 13f
                        } else {
                            numberTv.
                            textSize = 15f
                        }
                        numberTv.background = null
                        numberTv.setTextColor(ContextCompat.getColor(context,R.color.grey))
                    }
                }
            } else {
                binding.number.visibility = View.GONE
                numberTv.visibility = View.GONE
            }

            if (data.isCheckbox) {
                checkbox.visibility = View.VISIBLE
                money.visibility = View.GONE
                checkbox.isChecked = data.isSelected
            } else {
                checkbox.visibility = View.GONE
                money.visibility = View.VISIBLE
                money.setText(if (data.money > 0) data.money.toString() else "")
            }

            if (!Utils.isEmptyString(data.peilv)) {
                if (!Utils.isEmptyString(data.peilv)) {
                    peilv.visibility = View.VISIBLE
                    //                peilv.setText(Utils.doubleToString(Double.valueOf(data.getPeilv())));
                    peilv.text = data.peilv
                } else {
                    peilv.visibility = View.GONE
                }
            } else {
                peilv.visibility = View.GONE
            }


            //点击每一个投注面板触发的点击事件
            cell.setOnClickListener(View.OnClickListener {
                //                String moneyValue = moneyTV.getText().toString().trim();
                if (this@PeilvBigDataAdapterKt::tableCellListener.isInitialized) {
                    if (isFengpan) {
                        ToastUtils.showShort(R.string.result_not_open_now)
                        return@OnClickListener
                    }
                    if (data.isSelected) {
                        data.money = 0f
                        money.setText("")
                        data.isSelected = false
                        data.focusDrawable = R.drawable.table_textview_bg
                    } else {
                        data.isSelected = true
                        data.focusDrawable = R.drawable.table_textview_bg_press
                    }
                    switchCellBg(data.focusDrawable, cell)
                    dataList[position] = data
                    //通知前台，更新底部面板中的注数及总金额
                    if (this@PeilvBigDataAdapterKt::normalTouzhuListener.isInitialized) {
                        normalTouzhuListener.onNormalUpdate(0, position, true)
                    }
                    //                    tableCellListener.onCellSelect(playData,cellIndex);
                }
            })
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun switchCellBg(focusDrawable: Int, cell: LinearLayout?) {
        if (cell == null) {
            return
        }
        for (i in 0 until cell.childCount) {
            val v = cell.getChildAt(i)
            if (focusDrawable > 0) {
                v.background = ContextCompat.getDrawable(context,focusDrawable)
            } else {
                v.background = ContextCompat.getDrawable(context,R.drawable.table_textview_bg)
            }
        }
    }


    fun setList(list: List<PeilvPlayData>) {
        dataList.clear()
        dataList.addAll(list)
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

class PeilvViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)