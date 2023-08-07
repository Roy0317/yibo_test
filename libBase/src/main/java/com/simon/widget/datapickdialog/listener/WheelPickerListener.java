package com.simon.widget.datapickdialog.listener;


import com.simon.widget.datapickdialog.bean.DateBean;
import com.simon.widget.datapickdialog.bean.DateType;

/**
 * Created by codbking on 2016/9/22.
 */

public interface WheelPickerListener {
     void onSelect(DateType type, DateBean bean);
}
