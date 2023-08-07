package com.yibo.yiboapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.reflect.TypeToken;
import com.simon.utils.WindowUtil;
import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.R;
import com.yibo.yiboapp.data.Urls;
import com.yibo.yiboapp.data.UsualMethod;
import com.yibo.yiboapp.data.YiboPreference;
import com.yibo.yiboapp.entify.GameItemResult;
import com.yibo.yiboapp.entify.GameItemResultNew;
import com.yibo.yiboapp.entify.ThirdGameWrapper;
import com.yibo.yiboapp.ui.LAdapter;
import com.yibo.yiboapp.ui.LViewHolder;
import com.yibo.yiboapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.GsonConverterFactory;
import crazy_wrapper.Crazy.request.AbstractCrazyRequest;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;
import crazy_wrapper.RequestManager;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author johnson
 * MG,PT游戏列表主界面
 */

public class GameListActivity extends BaseActivity implements SessionResponse.Listener<CrazyResult<Object>> {


    public static final String TAG = GameListActivity.class.getSimpleName();
    GridView caipiaos;
    List<GameItemResultNew> cpData = new ArrayList<>();
    GameListAdapter gameListAdapter;
    String gameCode;
    MyHandler myHandler;
    String gameName;
    private LinearLayout ll_filter_layout;
    private EditText edtUserName;
    private Button btnCancel;
    private Button btnConfirm;
    private List<GameItemResultNew> cpDataB = new ArrayList<>();
    public static final int FORWARD_REQUEST = 0x01;
    public static final int DATAS_REQUEST = 0x02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_list);
        initView();

        String title = getIntent().getStringExtra("title");
        tvMiddleTitle.setText(title);
        gameCode = getIntent().getStringExtra("gameCode");
        myHandler = new MyHandler(this);
        ll_filter_layout = findViewById(R.id.ll_filter_layout);
        edtUserName = findViewById(R.id.edtUserName);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);
        gameListAdapter = new GameListAdapter(this, cpData, R.layout.game_item);
        caipiaos.setAdapter(gameListAdapter);
        caipiaos.setVerticalScrollBarEnabled(false);

        startProgress();
        tvRightText.setText("筛选");
        tvRightText.setVisibility(View.VISIBLE);
        btnConfirm.setOnClickListener(v -> searchResult());
        btnCancel.setOnClickListener(v -> cancel());
        tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_filter_layout.getVisibility() == View.GONE) {
                    ll_filter_layout.setVisibility(View.VISIBLE);
                } else {
                    ll_filter_layout.setVisibility(View.GONE);
                }
            }
        });
        getDatas();
//        new LoadGameThread(this).start();

    }

    private void getDatas() {
        StringBuilder msgUrl = new StringBuilder();
        msgUrl.append(Urls.BASE_URL).append(Urls.PORT).append(Urls.ACQUIRE_GAMES_DATAV2);
        msgUrl.append("?gameType=").append(gameCode);
        CrazyRequest<CrazyResult<ThirdGameWrapper>> unreadRequest = new AbstractCrazyRequest.Builder().
                url(msgUrl.toString())
                .seqnumber(DATAS_REQUEST)
                .headers(Urls.getHeader(this))
                .execMethod(CrazyRequest.ExecuteMethod.GET.ordinal())
                .protocol(CrazyRequest.Protocol.HTTP.ordinal())
                .convertFactory(GsonConverterFactory.create(new TypeToken<ThirdGameWrapper>() {
                }.getType()))
                .loadMethod(CrazyRequest.LOAD_METHOD.NONE.ordinal())
                .create();
        RequestManager.getInstance().startRequest(this, unreadRequest);

    }

    private final class MyHandler extends Handler {

        private WeakReference<GameListActivity> mReference;
        private GameListActivity fragment;

        public MyHandler(GameListActivity fragment) {
            mReference = new WeakReference<>(fragment);
            if (mReference != null) {
                this.fragment = mReference.get();
            }
        }

        public void handleMessage(Message message) {
            stopProgress();
            if (fragment == null) {
                return;
            }
            List<GameItemResultNew> results = (List<GameItemResultNew>) message.obj;
            if (results != null) {
                fragment.cpData.addAll(results);
                fragment.cpDataB.addAll(results);
                fragment.gameListAdapter.notifyDataSetChanged();
            }
        }
    }


    public static void createIntent(Context context, String title, String code) {
        Intent intent = new Intent(context, GameListActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("gameCode", code);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        tvMiddleTitle.setText(getString(R.string.elec_game));
        caipiaos = (GridView) findViewById(R.id.listview);
    }

    public class GameListAdapter extends LAdapter<GameItemResultNew> {

        Context context;

        public GameListAdapter(Context mContext, List<GameItemResultNew> mDatas, int layoutId) {
            super(mContext, mDatas, layoutId);
            context = mContext;
        }

        @Override
        public void convert(int position, LViewHolder holder, ViewGroup parent,
                            final GameItemResultNew item) {
            LinearLayout view = holder.getView(R.id.item);
            final ImageView ivThumb = holder.getView(R.id.img);
            final TextView tvName = holder.getView(R.id.name);
            updateLocImage(ivThumb, item.getButtonImagePath(), gameCode);
            tvName.setText(item.getDisplayName());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gameName = item.getDisplayName();
                    clickGameItemInLink(context, item.getFinalRelatveUrl());
                }
            });
        }


        private void clickGameItemInLink(Context context, String link) {
            UsualMethod.forwardGameUseWebLink(context, FORWARD_REQUEST, Urls.BASE_URL + link);
        }

        private void updateLocImage(ImageView lotImageView, String lotCode, String gameCode) {
            //彩种的图地址是根据彩种编码号为姓名构成的
            String imgUrl = Urls.BASE_URL + Urls.PORT + lotCode;
            if (Utils.isEmptyString(imgUrl)) {
                return;
            }
            GlideUrl glideUrl = UsualMethod.getGlide(context, imgUrl);
            RequestOptions options = new RequestOptions().placeholder(R.drawable.default_lottery)
                    .error(R.drawable.default_lottery);
            Glide.with(context).load(glideUrl).
                    apply(options)
                    .into(lotImageView);
        }

    }


    @Override
    public void onResponse(SessionResponse<CrazyResult<Object>> response) {
        RequestManager.getInstance().afterRequest(response);
        stopProgress();
        if (isFinishing() || response == null) {
            return;
        }
        int action = response.action;
        if (action == FORWARD_REQUEST) {
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
            try {
                JSONObject json = new JSONObject(reg);
                if (!json.isNull("success")) {
                    boolean success = json.getBoolean("success");
                    if (success) {
                        String url = !json.isNull("url") ? json.getString("url") : "";
                        if (!Utils.isEmptyString(url)) {
                            if (UsualMethod.getConfigFromJson(this).getZrdz_jump_broswer().equals("off")) {
                                Intent intent = new Intent(this, KefuActivity.class);
                                intent.putExtra("url", url);
                                intent.putExtra("title", gameName);
                                startActivity(intent);
                            } else {
                                actionViewGame(url);
                            }

                        }
                    } else {
                        if (!json.isNull("msg")) {
                            String msg = json.getString("msg");
                            showToast(msg);
                            if (msg.contains("登录") || msg.contains("登陆")) {
                                UsualMethod.loginWhenSessionInvalid(this);
                                return;
                            }
                        } else {
                            showToast(R.string.jump_fail);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (action == DATAS_REQUEST) {
            CrazyResult<Object> result = response.result;

            if (result == null) {
                return;
            }
            if (!result.crazySuccess) {
                String errorString = Urls.parseResponseResult(result.error);
                showToast(Utils.isEmptyString(errorString) ? getString(R.string.request_fail) : errorString);
                return;
            }
            Object regResult = result.result;
            ThirdGameWrapper reg = (ThirdGameWrapper) regResult;
            if (!reg.isSuccess()) {
                showToast(!Utils.isEmptyString(reg.getMsg()) ? reg.getMsg() : "获取失败");
                return;
            }
            YiboPreference.instance(this).setToken(reg.getAccessToken());
            if (reg.getContent() != null) {
                Message message = myHandler.obtainMessage(0, reg.getContent());
                myHandler.sendMessage(message);
            }
        }
    }

    private void actionViewGame(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索
     */
    private void searchResult() {
        if (TextUtils.isEmpty(edtUserName.getText().toString().trim())) {
            return;
        }

        String gameName = edtUserName.getText().toString().trim();

        Observable.create((ObservableOnSubscribe<List<GameItemResultNew>>) emitter -> {
            List<GameItemResultNew> list = new ArrayList<>();
            for (GameItemResultNew cpDatum : cpDataB) {
                if (!TextUtils.isEmpty(cpDatum.getDisplayName())) {
                    if (cpDatum.getDisplayName().contains(gameName)) {
                        list.add(cpDatum);
                    }
                    //YG棋牌用的是name
                } else if (!TextUtils.isEmpty(cpDatum.getDisplayName())) {
                    if (cpDatum.getDisplayName().contains(gameName)) {
                        list.add(cpDatum);
                    }
                }
            }
            emitter.onNext(list);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    if (list.size() == 0) {
                        ToastUtils.showShort("暂无此游戏");
                    } else {
                        cpData.clear();
                        cpData.addAll(list);
                        gameListAdapter.notifyDataSetChanged();
                    }
                    ll_filter_layout.setVisibility(View.GONE);
                    edtUserName.setText("");
                    WindowUtil.hideSoftInput(this);
                }).isDisposed();
    }

    /**
     * 取消或者重置
     */
    private void cancel() {
        cpData.clear();
        cpData.addAll(cpDataB);
        gameListAdapter.notifyDataSetChanged();
        ll_filter_layout.setVisibility(View.GONE);
        edtUserName.setText("");
        WindowUtil.hideSoftInput(this);
    }
}
