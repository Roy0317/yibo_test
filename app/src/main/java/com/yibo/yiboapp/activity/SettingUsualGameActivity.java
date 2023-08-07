package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.entify.SavedGameData;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.ui.XEditText;
import com.yibo.yiboapp.ui.XListView;
import com.yibo.yiboapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 常玩游戏设置，添加
 */
public class SettingUsualGameActivity extends BaseActivity {

    XEditText gameSearchInput;
    Button searchBtn;
    GridView recommandView;
    XListView gamesView;
    RecommandAdapter recommandAdapter;
    List<SavedGameData> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_usual_game);
        initView();
        List<SavedGameData> datas = UsualMethod.getUsualGame(this);
        recommandAdapter = new RecommandAdapter(this, datas, R.layout.recommand_item);
        recommandView.setAdapter(recommandAdapter);
        startProgress();
        new LoadGameThread(this).start();
    }

    private final class LoadGameThread extends Thread {

        Context context;
        LoadGameThread(Context context) {
            this.context = context;
        }
        @Override
        public void run() {
            super.run();
//            List<SavedGameData> datas = UsualMethod.getUsualGame(context);
//            Type listType = new TypeToken<ArrayList<GameItemResult>>() {}.getType();
//            Message message = myHandler.obtainMessage(0, list);
//            myHandler.sendMessage(message);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(Utils.isEmptyString(getIntent().getStringExtra("title"))?
                getString(R.string.add_usual_game_title):getIntent().getStringExtra("title"));
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText("帮助说明");

        gameSearchInput = (XEditText) findViewById(R.id.input_key);
        searchBtn = (Button) findViewById(R.id.ok);
        searchBtn.setOnClickListener(this);
        recommandView = (GridView) findViewById(R.id.recommand);
        gamesView = (XListView) findViewById(R.id.xlistview);

    }

    public static void createIntent(Context context, String title) {
        Intent intent = new Intent(context, SettingUsualGameActivity.class);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ok:
                break;
            case R.id.right_text:
                break;
            default:
                break;
        }
    }

    public class RecommandAdapter extends LAdapter<SavedGameData> {

        Context context;
        List<SavedGameData> mDatas;
        public RecommandAdapter(Context mContext, List<SavedGameData> mDatas,
                              int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
            this.mDatas = mDatas;
        }

        @Override
        public void convert(final int position, LViewHolder holder, ViewGroup parent, final SavedGameData item) {
            TextView ball = holder.getView(R.id.name);
            ImageView img = holder.getView(R.id.img);
            if (item.getGameModuleCode() == SavedGameData.LOT_GAME_MODULE) {
                UsualMethod.updateLocImage(context,img,item.getLotCode());
            }else{
                img.setBackgroundResource(R.drawable.default_lottery);
            }
            ball.setText(item.getLotName());
        }
    }
}
