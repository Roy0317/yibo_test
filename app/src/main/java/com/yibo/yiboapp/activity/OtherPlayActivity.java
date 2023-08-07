package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Constant;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.OtherPlay;
import com.yibo.yiboapp.entify.OtherPlayWrapper;
import com.yibo.yiboapp.entify.SavedGameData;
import com.yibo.yiboapp.entify.SysConfig;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;


public class OtherPlayActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>> {

    public static final String TAG = CaipiaoActivity.class.getSimpleName();
    LinearLayout fastTouzhuView;
    GridView ballon_wrapper;
    Button randomTouzhuView;
    Button touzhuBtn;

    GridView caipiaos;
    List<OtherPlay> cpData;
    ListAdapter dataAdapter;

    public static final int PLAYS_REQUEST = 0x07;
    public static final int REAL_REQUEST = 0x08;
    public static final int GAME_REQUEST = 0x09;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chipin_mall);
        initView();

        cpData = new ArrayList<>();
        dataAdapter = new ListAdapter(this, cpData, R.layout.caipiao_item);
        caipiaos.setAdapter(dataAdapter);
        caipiaos.setVerticalScrollBarEnabled(false);
        int moduleCode = getIntent().getIntExtra("code", Constant.SPORT_MODULE_CODE);
//        if (YiboPreference.instance(this).getAccountMode() != Constant.ACCOUNT_PLATFORM_TEST_GUEST) {
        actionData(moduleCode);
//        }
        fastTouzhuView.setVisibility(View.GONE);
        ballon_wrapper.setVisibility(View.GONE);

    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getIntent().getStringExtra("gameName"));

        caipiaos = (GridView) findViewById(R.id.caipiao_mall);
        //快速投注view
        ballon_wrapper = (GridView) findViewById(R.id.ballon_wrapper);
        fastTouzhuView = (LinearLayout) findViewById(R.id.func_layout);
        randomTouzhuView = (Button) findViewById(R.id.random_touzhu);
        touzhuBtn = (Button) findViewById(R.id.touzhu_btn);
        randomTouzhuView.setOnClickListener(this);
        touzhuBtn.setOnClickListener(this);
    }


    public static void createIntent(Context context, String gameName, int code) {
        Intent intent = new Intent(context, OtherPlayActivity.class);
        intent.putExtra("gameName", gameName);
        intent.putExtra("code", code);
        context.startActivity(intent);
    }

    //获取一些主页显示的后台数据
    private void actionData(int code) {
        //获取列表数据
        StringBuilder lunboUrl = new StringBuilder();
        lunboUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.GET_OTHER_PLAY_DATA);
        lunboUrl.append("?code=" + code);
        CrazyRequest<CrazyResult<OtherPlayWrapper>> request = new AbstractCrazyRequest.Builder().
                url(lunboUrl.toString())
                .seqnumber(PLAYS_REQUEST)
                .shouldCache(true)
                .headers(Urls.getHeader(this))
                .placeholderText(getString(R.string.loading))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<OtherPlayWrapper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.LOADING.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cpData.clear();
    }

    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == PLAYS_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
                return;
            }
            Object regResult = result.result;
            OtherPlayWrapper reg = (OtherPlayWrapper) regResult;
            if (!reg.isSuccess()) {
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (reg.getContent() != null) {
                cpData.addAll(reg.getContent());
                dataAdapter.notifyDataSetChanged();
            }
        } else if (action == REAL_REQUEST || action == GAME_REQUEST) {
            CrazyResult<Object> result = response.result;
            if (result == null) {
                showToast(R.string.jump_fail);
                return;
            }
            if (!result.crazySuccess) {
                showToast(R.string.jump_fail);
                return;
            }
            Object regResult = result.result;
            String reg = (String) regResult;
            if(reg.contains("html")&&!reg.contains("url")){
                //html内容需要自定义浏览器访问，暂时先调用外部浏览器。
                BBinActivity.createIntent(this, reg, "bbin");
            }else{
                try {
                    JSONObject json = new JSONObject(reg);
                    if (!json.isNull("success")) {
                        boolean success = json.getBoolean("success");
                        if (success) {
//                        showToast(R.string.fee_convert_success);
                            //AG,MG,AB,OG,DS都返回跳转链接
                            //BBIN 返回的是一段html内容
                            String url = !json.isNull("url") ? json.getString("url") : "";
                            if (!Utils.isEmptyString(url)) {
                                SysConfig config = UsualMethod.getConfigFromJson(this);
                                if (config.getZrdz_jump_broswer().equals("on")) {
                                    UsualMethod.actionViewGame(OtherPlayActivity.this, url);
                                } else {
                                    SportNewsWebActivity.createIntent(this, url, nowGameName);
                                }

                            } else {
                                String html = !json.isNull("html") ? json.getString("html") : "";
                                //html内容需要自定义浏览器访问，暂时先调用外部浏览器。
//                            UsualMethod.actionViewGame(OtherPlayActivity.this,html);
                                BBinActivity.createIntent(this, html, "bbin");
                            }

                        } else {
                            if (!json.isNull("msg")) {
                                String msg = json.getString("msg");
                                showToast(msg);
                                if (msg.contains("超时") || msg.contains("其他")) {
                                    UsualMethod.loginWhenSessionInvalid(this);
                                }
                            } else {
                                showToast(R.string.jump_fail);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public class ListAdapter extends LAdapter<OtherPlay> {

        Context context;

        public ListAdapter(Context mContext, List<OtherPlay> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent, final OtherPlay item) {
            LinearLayout view = holder.getView(R.id.item);
            final ImageView ivThumb = holder.getView(R.id.img);
            final TextView tvName = holder.getView(R.id.name);
            if (Utils.isEmptyString(item.getTitle())) {
                tvName.setText(getString(R.string.nodata));
            } else {
                tvName.setText(item.getTitle());
            }
            if (!Utils.isEmptyString(item.getImgUrl())) {
                String url = Urls.BASE_URL + Urls.PORT + item.getImgUrl();
                GlideUrl gu = UsualMethod.getGlide(context, url);
                int defaultIcon = R.drawable.icon_ft;
                int moduleCode = getIntent().getIntExtra("code", Constant.SPORT_MODULE_CODE);
                if (moduleCode == Constant.SPORT_MODULE_CODE) {
                    defaultIcon = R.drawable.icon_ft;
                } else if (moduleCode == Constant.REAL_MODULE_CODE) {
                    defaultIcon = R.drawable.icon_real;
                } else if (moduleCode == Constant.GAME_MODULE_CODE) {
                    defaultIcon = R.drawable.icon_game;
                }
                RequestOptions options = new RequestOptions().placeholder(defaultIcon)
                        .error(defaultIcon);
                Glide.with(context).load(gu).
                        apply(options)
                        .into(ivThumb);
            } else {
                ivThumb.setBackgroundResource(R.drawable.widget_ssq);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (item.getDataCode() == Constant.SPORT_MODULE_CODE) {
//                        if (item.getPlayCode().equals("hgty")) {
//                            actionSport(item.getTitle());
//                        }
//                    } else if (item.getDataCode() == Constant.REAL_MODULE_CODE) {
//                        nowGameName = item.getTitle();
//                        UsualMethod.forwardRealInCommon(OtherPlayActivity.this,REAL_REQUEST,item.getPlayCode(),
//                                OtherPlayActivity.this);
//                    } else if (item.getDataCode() == Constant.GAME_MODULE_CODE) {
//                        nowGameName = item.getTitle();
//                        UsualMethod.forwardGame(OtherPlayActivity.this,item.getPlayCode(),GAME_REQUEST,
//                                OtherPlayActivity.this);
//                    }

                    if (item.getIsListGame() == 1) {
                        GameListActivity.createIntent(getmContext(), item.getTitle(), item.getPlayCode());
                    } else if (item.getIsListGame() == 2) {
                        SportActivity.createIntent(getmContext(), item.getTitle(), Constant.SPORT_MODULE_CODE + "");
                    } else if (item.getIsListGame() == 0) {
                        String result = UsualMethod.forwardGame(getmContext(), item.getPlayCode(), REAL_REQUEST, OtherPlayActivity.this, item.getForwardUrl());
                        if (!Utils.isEmptyString(result)) {
                            showToast(result);
                        }
                    }


                    saveCurrentLotData(item);
                }
            });
        }

        private void actionSport(String gameName) {
            SportActivity.createIntent(OtherPlayActivity.this, gameName, Constant.SPORT_MODULE_CODE + "");
        }


    }

    private String nowGameName = "";

    private void saveCurrentLotData(OtherPlay item) {
        SavedGameData data = new SavedGameData();
        data.setGameModuleCode(item.getDataCode() == Constant.REAL_MODULE_CODE ? SavedGameData.REALMAN_GAME_MODULE
                : SavedGameData.DIANZI_GAME_MODULE);
        data.setAddTime(System.currentTimeMillis());
        data.setUser(YiboPreference.instance(this).getUsername());
        data.setLotName(item.getTitle());
        if (item.getDataCode() == SavedGameData.REALMAN_GAME_MODULE) {
            data.setZhenrenImgUrl(item.getImgUrl());
            data.setZrPlayCode(item.getPlayCode());
        } else {
            data.setDzImgUrl(item.getImgUrl());
            data.setDzPlayCode(item.getPlayCode());
        }
        UsualMethod.localeGameData(this, data);
    }

}
