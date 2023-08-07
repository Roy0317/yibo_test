package com.yibo.yiboapp.views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yibo.yiboapp.R;

public class ChouJiangDialog extends AlertDialog {
    public ChouJiangDialog(Context context) {
        super(context);
    }

    ImageView touzi1;
    ImageView touzi2;
    ImageView touzi3;
    ImageView touzi4;
    ImageView touzi5;
    ImageView touzi6;
    TextView tv_shiwan;
    TextView tv_guanbi;
    TextView tv_jieguo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_autumn_choujiang);
        touzi1 = findViewById(R.id.tv_touzi1);
        touzi2 = findViewById(R.id.tv_touzi2);
        touzi3 = findViewById(R.id.tv_touzi3);
        touzi4 = findViewById(R.id.tv_touzi4);
        touzi5 = findViewById(R.id.tv_touzi5);
        touzi6 = findViewById(R.id.tv_touzi6);
        tv_guanbi = findViewById(R.id.tv_guanbi);
        tv_shiwan = findViewById(R.id.tv_shiwan);
        tv_jieguo = findViewById(R.id.tv_jieguo);

    }

    //点击试玩
    public void setOnPlayListener(View.OnClickListener listener) {
        tv_shiwan.setOnClickListener(listener);
    }

    //点击关闭
    public void setOnExitListener(View.OnClickListener listener) {
        tv_guanbi.setOnClickListener(listener);
    }

    //显示结果
    public void showJieguo(int[] res) {
        String jiangxiang;
        //计算各个数字有多少
        int[] nums = new int[6];
        for (int i : res) {
            nums[i]++;
        }

        if (nums[3] == 4 && nums[0] == 2) {
            jiangxiang = "状元插金花";
        } else if (nums[3] == 6) {
            jiangxiang = "六杯红";
        } else if (nums[0] == 6 || nums[1] == 6 || nums[2] == 6 || nums[4] == 6 || nums[5] == 6) {
            jiangxiang = "满地榜";
        } else if (nums[3] == 5) {
            jiangxiang = "五红";
        } else if (nums[0] == 5 || nums[1] == 5 || nums[2] == 5 || nums[4] == 5 || nums[5] == 5) {
            jiangxiang = ("五子登科");
        } else if (nums[3] == 4) {
            jiangxiang = ("状元");
        } else if (nums[0] > 0 && nums[1] > 0 && nums[2] > 0 && nums[3] > 0 && nums[4] > 0 && nums[5] > 0) {
            jiangxiang = ("对堂");
        } else if (nums[3] == 3) {
            jiangxiang = ("三红");
        } else if (nums[0] == 4 || nums[1] == 4 || nums[2] == 4 || nums[4] == 4 || nums[5] == 4) {
            jiangxiang = ("四进");
        } else if (nums[3] == 2) {
            jiangxiang = ("二举");
        } else if (nums[3] == 1) {
            jiangxiang = ("一秀");
        } else {
            jiangxiang = ("未中奖");
        }

        chosePicbyNum(touzi1, res[0]);
        chosePicbyNum(touzi2, res[1]);
        chosePicbyNum(touzi3, res[2]);
        chosePicbyNum(touzi4, res[3]);
        chosePicbyNum(touzi5, res[4]);
        chosePicbyNum(touzi6, res[5]);
        String haoma = "";
        for (int i = 0; i < 6; i++) {
            if (i == 5) {
                haoma = haoma + (res[i] + 1);
                continue;
            }
            haoma = haoma + (res[i] + 1) + ",";
        }
        tv_jieguo.setText("摇中号码：" + haoma + "(" + jiangxiang + ")");
    }


    void chosePicbyNum(ImageView img, int i) {
        switch (i) {
            case 0:
                img.setImageDrawable(getContext().getResources().getDrawable(R.drawable.d1));
                break;
            case 1:
                img.setImageDrawable(getContext().getResources().getDrawable(R.drawable.d2));
                break;
            case 2:
                img.setImageDrawable(getContext().getResources().getDrawable(R.drawable.d3));
                break;
            case 3:
                img.setImageDrawable(getContext().getResources().getDrawable(R.drawable.d4));
                break;
            case 4:
                img.setImageDrawable(getContext().getResources().getDrawable(R.drawable.d5));
                break;
            case 5:
                img.setImageDrawable(getContext().getResources().getDrawable(R.drawable.d6));
                break;
        }
    }

}
