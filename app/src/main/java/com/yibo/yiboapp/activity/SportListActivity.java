package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.LotteryData;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.SBSportResultWrapper;
import com.yibo.yiboapp.entify.SportBetResultWraper;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;

/**
 * @author johnson
 * 体育列表
 */

public class SportListActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>>{


    public static final String TAG = GameListActivity.class.getSimpleName();
    GridView caipiaos;
    List<LotteryData> cpData = new ArrayList<>();
    GameListAdapter gameListAdapter;

    public static final int SBSPORT_REQUEST = 0x10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_list);
        initView();

        String title = getIntent().getStringExtra("title");
        String datas = getIntent().getStringExtra("datas");
        if (!Utils.isEmptyString(datas)) {
            Type listType = new TypeToken<ArrayList<LotteryData>>() {}.getType();
            List<LotteryData> list = new Gson().fromJson(datas, listType);
            if (list != null) {
                this.cpData.clear();
                this.cpData.addAll(list);
            }
        }
        tvMiddleTitle.setText(title);
        gameListAdapter = new GameListAdapter(this,cpData, R.layout.game_item);
        caipiaos.setAdapter(gameListAdapter);
        caipiaos.setVerticalScrollBarEnabled(false);
    }

    public static void createIntent(Context context,String datas) {
        Intent intent = new Intent(context, SportListActivity.class);
        intent.putExtra("datas", datas);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.elec_game));
        caipiaos = (GridView) findViewById(R.id.listview);
    }

    public class GameListAdapter extends LAdapter<LotteryData> {

        Context context;
        public GameListAdapter(Context mContext, List<LotteryData> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }

        @Override public void convert(int position, LViewHolder holder, ViewGroup parent,
                                      final LotteryData data) {
            LinearLayout view = holder.getView(R.id.item);
            final ImageView ivThumb = holder.getView(R.id.img);
            final TextView tvName = holder.getView(R.id.name);
            UsualMethod.updateLocImageWithUrl(SportListActivity.this,ivThumb, Urls.BASE_URL+ Urls.PORT+data.getImgUrl());
            tvName.setText(data.getName());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.getIsListGame() == 1) {
                        GameListActivity.createIntent(getmContext(), data.getName(), data.getCzCode());
                    } else if (data.getIsListGame() == 2) {
                        SportActivity.createIntent(getmContext(), data.getName(), Constant.SPORT_MODULE_CODE+"");
                    } else if (data.getIsListGame() == 0) {
                        String result = UsualMethod.forwardGame(getmContext(), data.getCzCode(), SBSPORT_REQUEST, SportListActivity.this, data.getForwardUrl());
                        if (!Utils.isEmptyString(result)) {
                            showToast(result);
                        }
                    }
                }
            });
        }

//        private void clickGameItem(Context context,String itemCode) {
//            if (itemCode.equalsIgnoreCase("hgty")) {
//                SportActivity.createIntent(SportListActivity.this,"皇冠体育", Constant.SPORT_MODULE_CODE+"");
//            } else if (itemCode.equalsIgnoreCase("sbty")) {
//                requestsbUrl();
//            }
//        }
    }

//    private void requestsbUrl() {
//
//        StringBuilder url = new StringBuilder();
//        url.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.SBSPORT_JUMP_URL);
//        CrazyRequest<CrazyResult<SBSportResultWrapper>> request = new AbstractCrazyRequest.Builder().
//                url(url.toString())
//                .seqnumber(SBSPORT_REQUEST)
//                .listener(this)
//                .headers(Urls.getHeader(this))
//                .placeholderText(getString(R.string.forward_jumping))
//                .execMethod(CrazyRequest.ExecuteMethod.FORM.ordinal())
//                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
//                .convertFactory(null)
//                .convertFactory(GsonConverterFactory.create(new TypeToken<SBSportResultWrapper>(){}.getType()))
//                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
//                .create();
//
//        RequestManager.getInstance().startRequest(this,request);
//    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        stopProgress();
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == SBSPORT_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(getString(R.string.jump_fail));
                return;
            }
            if (!result.crazySuccess) {
                showToast(getString(R.string.jump_fail));
                return;
            }
            Object regResult = result.result;
            String reg1 = (String) regResult;
            SBSportResultWrapper stw=new Gson().fromJson(reg1,SBSportResultWrapper.class);
            if (!stw.isSuccess()) {
                showToast(!Utils.isEmptyString(stw.getMsg())?stw.getMsg():
                        "跳转失败");
                //超時或被踢时重新登录，因为后台帐号权限拦截抛出的异常返回没有返回code字段
                //所以此接口当code == 0时表示帐号被踢，或登录超时
                if (stw.getCode() == 0) {
                    UsualMethod.loginWhenSessionInvalid(this);
                }
                return;
            }
            YiboPreference.instance(this).setToken(stw.getAccessToken());
            if (!Utils.isEmptyString(stw.getContent())) {
                UsualMethod.viewLink(this,stw.getContent());
            }else{
                showToast("没有链接，无法跳转");
            }
        }
    }

}
