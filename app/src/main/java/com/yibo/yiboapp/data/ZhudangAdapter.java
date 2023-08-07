package com.yibo.yiboapp.data;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.BallListItemInfo;
import com.yibo.yiboapp.entify.OrderDataInfo;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.TouzhuFuncView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnson on 2017/9/21.
 */

public final class ZhudangAdapter extends LAdapter<OrderDataInfo> {

    Context context;
    DecimalFormat decimalFormat;
    DecimalFormat peilvdecimalFormat;
    boolean isPeilvVersion;

    public ZhudangAdapter(Context mContext, List<OrderDataInfo> mDatas, int layoutId) {
        super(mContext, mDatas, layoutId);
        context = mContext;
        decimalFormat = new DecimalFormat("0.00");
        peilvdecimalFormat = new DecimalFormat("0.00");
    }

    public void setPeilvVersion(boolean isPeilvVersion) {
        this.isPeilvVersion = isPeilvVersion;
    }

    private List<BallListItemInfo> getIconInfo(String playCode) {

        BallListItemInfo info = new BallListItemInfo();
        info.setNum("注");
        if (playCode.equals(PlayCodeConstants.dxds)) {
            info.setNum("大");
        } else if (playCode.equals(PlayCodeConstants.dwd)) {
            info.setNum("定");
        } else if (playCode.equals(PlayCodeConstants.longhh)) {
            info.setNum("龙");
        } else if (playCode.equals(PlayCodeConstants.exzx)) {
            info.setNum("二");
        } else if (playCode.equals(PlayCodeConstants.sxzx)) {
            info.setNum("三");
        } else if (playCode.equals(PlayCodeConstants.sxwf_var)) {
            info.setNum("三");
        } else if (playCode.equals(PlayCodeConstants.sixing_wf)) {
            info.setNum("四");
        } else if (playCode.equals(PlayCodeConstants.wuxing_wf)) {
            info.setNum("五");
        } else if (playCode.equals(PlayCodeConstants.caibaozi)) {
            info.setNum("猜");
        } else if (playCode.equals(PlayCodeConstants.bdw)) {
            info.setNum("不");
        } else if (playCode.equals(PlayCodeConstants.q1_str) ||
                playCode.equals(PlayCodeConstants.q2_str) ||
                playCode.equals(PlayCodeConstants.qiansan)) {
            info.setNum("前");
        } else if (playCode.equals(PlayCodeConstants.hz)) {
            info.setNum("和");
        } else if (playCode.equals(PlayCodeConstants.zhi_xuan_str)) {
            info.setNum("直");
        } else if (playCode.equals(PlayCodeConstants.zhuxuan_str) ||
                playCode.equals(PlayCodeConstants.zuxuan_san)) {
            info.setNum("组");
        } else if (playCode.equals(PlayCodeConstants.er_ma_str)) {
            info.setNum("二");
        } else if (playCode.equals(PlayCodeConstants.rxfs)) {
            info.setNum("任");
        } else if (playCode.equals(PlayCodeConstants.shang_ma_str)) {
            info.setNum("三");
        } else if (playCode.equals(PlayCodeConstants.sthtx)) {
            info.setNum("三");
        } else if (playCode.equals(PlayCodeConstants.sthdx)) {
            info.setNum("三");
        } else if (playCode.equals(PlayCodeConstants.sbtx)) {
            info.setNum("三");
        } else if (playCode.equals(PlayCodeConstants.slhtx)) {
            info.setNum("三");
        } else if (playCode.equals(PlayCodeConstants.ethfx)) {
            info.setNum("二");
        } else if (playCode.equals(PlayCodeConstants.ebth)) {
            info.setNum("二");
        } else if (playCode.equals(PlayCodeConstants.zhenghe)) {
            info.setNum("整");
        } else if (playCode.equals(PlayCodeConstants.wanwei)) {
            info.setNum("万");
        } else if (playCode.equals(PlayCodeConstants.qianwei)) {
            info.setNum("千");
        } else if (playCode.equals(PlayCodeConstants.baiwei)) {
            info.setNum("百");
        } else if (playCode.equals(PlayCodeConstants.shiwei)) {
            info.setNum("十");
        } else if (playCode.equals(PlayCodeConstants.gewei)) {
            info.setNum("个");
        } else if (playCode.equals(PlayCodeConstants.heweishu)) {
            info.setNum("和");
        } else if (playCode.equals(PlayCodeConstants.longhh)) {
            info.setNum("龙");
        } else if (playCode.equals(PlayCodeConstants.qipai)) {
            info.setNum("棋");
        } else if (playCode.equals(PlayCodeConstants.heshu)) {
            info.setNum("和");
        } else if (playCode.equals(PlayCodeConstants.yizi)) {
            info.setNum("一");
        } else if (playCode.equals(PlayCodeConstants.erzi) ||
                playCode.equals(PlayCodeConstants.erzidingwei)) {
            info.setNum("二");
        } else if (playCode.equals(PlayCodeConstants.sanzi) ||
                playCode.equals(PlayCodeConstants.sanzidingwei)) {
            info.setNum("三");
        } else if (playCode.equals(PlayCodeConstants.zuxuan_liu)) {
            info.setNum("六");
        } else if (playCode.equals(PlayCodeConstants.kuadu)) {
            info.setNum("跨");
        }
        List<BallListItemInfo> data = new ArrayList<BallListItemInfo>();
        data.add(info);
        return data;
    }

    @Override
    public void convert(int position, LViewHolder holder, ViewGroup parent, OrderDataInfo item) {

        TouzhuFuncView icon = holder.getView(R.id.icon);
        List<BallListItemInfo> info = getIconInfo(item.getPlayCode());
        icon.fillBallons(info, true);

        TextView playName = holder.getView(R.id.playname);
        TextView numbers = holder.getView(R.id.nums);
        TextView beiShu = holder.getView(R.id.beishu);
        TextView mode = holder.getView(R.id.mode);
        TextView zhushu = holder.getView(R.id.zhushu);
        TextView fee = holder.getView(R.id.total_fee);
        TextView winMoney = holder.getView(R.id.winMoney);

        playName.setText(item.getPlayName() + "-" + item.getSubPlayName());
        numbers.setText(item.getNumbers());
        if (!isPeilvVersion) {
            mode.setVisibility(View.VISIBLE);
            beiShu.setText(String.format(context.getString(R.string.order_beishu_format), item.getBeishu()));
            mode.setText(String.format(context.getString(R.string.some_mode_format),
                    UsualMethod.convertMode(item.getMode())));
        } else {
            mode.setVisibility(View.GONE);
            String rateString = peilvdecimalFormat.format(item.getRate());
            beiShu.setText(String.format(context.getString(R.string.listitem_peilv_format), rateString));
        }

        zhushu.setText(String.format(context.getString(R.string.zhu_format), item.getZhushu()));
        String moneyString = decimalFormat.format(item.getMoney());
        fee.setText(String.format(mContext.getString(R.string.xiazhu_fee_order_format), moneyString));
        winMoney.setText(String.format(mContext.getString(R.string.xiazhu_keying_format),
                calculateWinMoney(isPeilvVersion, item.getZhushu(), item.getBeishu(), item.getMode(), item.getRate(), (float) item.getMoney())));
    }

    private String calculateWinMoney(boolean isPeilvVersion, Integer buyZhuShu, Integer multiple, Integer model, float peilv, float buyMoney) {
        String money = "元";
        if (!isPeilvVersion) {
            money = buyZhuShu * multiple * peilv / model + "元";
        } else {
            float singleMoney = buyMoney;
            if (buyZhuShu != 0){
                singleMoney = buyMoney / buyZhuShu;
            }
            BigDecimal decimal = new BigDecimal(Float.toString(peilv)).multiply(new BigDecimal(Float.toString(singleMoney)));
            BigDecimal decimal1 = new BigDecimal(Float.toString(singleMoney));
            Float value = decimal.subtract(decimal1).floatValue();
            money = value + "元";
        }
        return money;
    }
}