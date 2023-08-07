package com.yibo.yiboapp.views.gestureview;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.ihsg.patternlocker.OnPatternChangeListener;
import com.github.ihsg.patternlocker.PatternLockerView;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.activity.BaseActivity;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.views.gestureview.util.PatternHelper;

import java.util.List;

/**
 * @author: soxin
 * @version: 1
 * @project: trunk
 * @package: com.yibo.yiboapp.views.gestureview
 * @description:
 * @date: 2019-10-07
 * @time: 20:40
 */
public class DefaultPatternSettingActivity extends BaseActivity {

    private PatternHelper patternHelper;
    private TextView textMsg;
    private SimpleDraweeView imageHead;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_pattern_setting);
        YiboPreference.instance(this).setUserCheckedGesture(false);
        YiboPreference.instance(this).setUserGesture(false);
        textMsg = findViewById(R.id.textMsg);
        PatternLockerView patternLockerView = findViewById(R.id.patternLockerView);
        imageHead = findViewById(R.id.header) ;

        patternLockerView.setOnPatternChangedListener(new OnPatternChangeListener() {
            @Override
            public void onStart(PatternLockerView patternLockerView) {

            }

            @Override
            public void onChange(PatternLockerView patternLockerView, List<Integer> list) {

            }

            @Override
            public void onComplete(PatternLockerView view, List<Integer> hitIndexList) {
                boolean isOk = isPatternOk(hitIndexList);
                view.updateStatus(!isOk);
                updateMsg();
                YiboPreference.instance(DefaultPatternSettingActivity.this).setUserCheckedGesture(isOk);
                YiboPreference.instance(DefaultPatternSettingActivity.this).setUserGesture(isOk);
            }

            @Override
            public void onClear(PatternLockerView patternLockerView) {
                finishIfNeeded();
            }
        });


        this.textMsg.setText("设置解锁图案");
        this.patternHelper = new PatternHelper();
    }


    private boolean isPatternOk(List<Integer> hitIndexList) {
        this.patternHelper.validateForSetting(hitIndexList);
        return this.patternHelper.isOk();
    }

    private void updateMsg() {
        this.textMsg.setText(this.patternHelper.getMessage());
        if (this.patternHelper.isOk()) {
            textMsg.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        } else {
            textMsg.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        }
    }

    private void finishIfNeeded() {
        if (this.patternHelper.isFinish()){
            finish();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        UsualMethod.LoadUserImage(this, imageHead);
    }
}
