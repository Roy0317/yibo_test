package com.example.anuo.immodule.activity;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.anuo.immodule.BuildConfig;
import com.example.anuo.immodule.R;
import com.example.anuo.immodule.activity.base.ChatBaseActivity;
import com.example.anuo.immodule.adapter.AuditListAdapter;
import com.example.anuo.immodule.adapter.ChatAdapter;
import com.example.anuo.immodule.adapter.ChatDrawerAdapter;
import com.example.anuo.immodule.adapter.ChatLotteryResultAdapter;
import com.example.anuo.immodule.adapter.MainFloatAdapter;
import com.example.anuo.immodule.bean.AudioMsgBody;
import com.example.anuo.immodule.bean.AuthorityBean;
import com.example.anuo.immodule.bean.BetSlipMsgBody;
import com.example.anuo.immodule.bean.ChatCollectionImagesBean;
import com.example.anuo.immodule.bean.ChatCollectionImagesSelectBean;
import com.example.anuo.immodule.bean.ChatHistoryMessageBean;
import com.example.anuo.immodule.bean.ChatLongDragonBean;
import com.example.anuo.immodule.bean.ChatLotteryBean;
import com.example.anuo.immodule.bean.ChatMessage;
import com.example.anuo.immodule.bean.ChatPersonDataBean;
import com.example.anuo.immodule.bean.ChatPersonPhotoListBean;
import com.example.anuo.immodule.bean.ChatPrivateConversationBean;
import com.example.anuo.immodule.bean.ChatPrivateMessageBean;
import com.example.anuo.immodule.bean.ChatPrivateReceiveMsgBean;
import com.example.anuo.immodule.bean.ChatQuickMessageBean;
import com.example.anuo.immodule.bean.ChatRemarkBean;
import com.example.anuo.immodule.bean.ChatRoomListBean;
import com.example.anuo.immodule.bean.ChatRoomNoticeBean;
import com.example.anuo.immodule.bean.ChatSendMsg;
import com.example.anuo.immodule.bean.ChatShareDataBean;
import com.example.anuo.immodule.bean.ChatSignDataBean;
import com.example.anuo.immodule.bean.ChatToolPermissionBean;
import com.example.anuo.immodule.bean.ChatUserListBean;
import com.example.anuo.immodule.bean.FloatBallBean;
import com.example.anuo.immodule.bean.GetAuditListBean;
import com.example.anuo.immodule.bean.GrabRedPackageBean;
import com.example.anuo.immodule.bean.GroupBean;
import com.example.anuo.immodule.bean.ImageMsgBody;
import com.example.anuo.immodule.bean.JoinChatRoomBean;
import com.example.anuo.immodule.bean.LoginChatBean;
import com.example.anuo.immodule.bean.LotteryDownBean;
import com.example.anuo.immodule.bean.MsgSendStatus;
import com.example.anuo.immodule.bean.MsgType;
import com.example.anuo.immodule.bean.PersonDataBean;
import com.example.anuo.immodule.bean.PlanMsgBody;
import com.example.anuo.immodule.bean.RedPackageDetailBean;
import com.example.anuo.immodule.bean.RedPackageMsgBody;
import com.example.anuo.immodule.bean.SendMsgBean;
import com.example.anuo.immodule.bean.ShareOrderBean;
import com.example.anuo.immodule.bean.SysTextMsgBody;
import com.example.anuo.immodule.bean.TextMsgBody;
import com.example.anuo.immodule.bean.TodayProfitResponse;
import com.example.anuo.immodule.bean.UpLoadFileBean;
import com.example.anuo.immodule.bean.VideoMsgBody;
import com.example.anuo.immodule.bean.WelcomeTextBean;
import com.example.anuo.immodule.bean.WinningNotice;
import com.example.anuo.immodule.bean.base.BetInfo;
import com.example.anuo.immodule.bean.base.FileMsgBody;
import com.example.anuo.immodule.bean.base.MsgBody;
import com.example.anuo.immodule.constant.ConfigCons;
import com.example.anuo.immodule.constant.EventCons;
import com.example.anuo.immodule.constant.UserToolConstant;
import com.example.anuo.immodule.event.CommonEvent;
import com.example.anuo.immodule.eventbus.RefreshFloatMenuEvent;
import com.example.anuo.immodule.interfaces.iview.IIChatMainView;
import com.example.anuo.immodule.jsonmodel.ApplyBanSpeakModel;
import com.example.anuo.immodule.jsonmodel.ChatCollectionImagesModel;
import com.example.anuo.immodule.jsonmodel.ChatJoinPrivateRoomMsg;
import com.example.anuo.immodule.jsonmodel.ChatMessageHistoryModel;
import com.example.anuo.immodule.jsonmodel.ChatRoomNoticeModel;
import com.example.anuo.immodule.jsonmodel.FollowBetJsonModel;
import com.example.anuo.immodule.jsonmodel.GetOnlineJsonModel;
import com.example.anuo.immodule.jsonmodel.GrabRedPackageJsonModel;
import com.example.anuo.immodule.jsonmodel.LotteryHistoryResultResponse;
import com.example.anuo.immodule.jsonmodel.LotteryResultModel;
import com.example.anuo.immodule.jsonmodel.ModifyPersonDataModel;
import com.example.anuo.immodule.manager.MediaManager;
import com.example.anuo.immodule.presenter.ChatMainPresenter;
import com.example.anuo.immodule.presenter.base.ChatBasePresenter;
import com.example.anuo.immodule.service.JWebSocketClient;
import com.example.anuo.immodule.service.JWebSocketClientService;
import com.example.anuo.immodule.utils.AnimateUtil;
import com.example.anuo.immodule.utils.ChatFileUtils;
import com.example.anuo.immodule.utils.ChatSpUtils;
import com.example.anuo.immodule.utils.ChatSysConfig;
import com.example.anuo.immodule.utils.ChatTimeUtil;
import com.example.anuo.immodule.utils.ChatUiHelper;
import com.example.anuo.immodule.utils.CommonUtils;
import com.example.anuo.immodule.utils.GlideUtils;
import com.example.anuo.immodule.utils.LogUtils;
import com.example.anuo.immodule.utils.PictureFileUtil;
import com.example.anuo.immodule.utils.ScreenUtil;
import com.example.anuo.immodule.utils.TimeUtils;
import com.example.anuo.immodule.utils.ToastUtils;
import com.example.anuo.immodule.view.AitEditText;
import com.example.anuo.immodule.view.AuditListDialog;
import com.example.anuo.immodule.view.BetSlipDialog;
import com.example.anuo.immodule.view.ChatLongDragonListWindow;
import com.example.anuo.immodule.view.ChatMainMarqueeTextView;
import com.example.anuo.immodule.view.ChatRoomListWindow;
import com.example.anuo.immodule.view.CircleImageView;
import com.example.anuo.immodule.view.CommonDialog;
import com.example.anuo.immodule.view.IndicatorView;
import com.example.anuo.immodule.view.LotteryHistoryResultWindow;
import com.example.anuo.immodule.view.NestedExpandaleListView;
import com.example.anuo.immodule.view.RecordButton;
import com.example.anuo.immodule.view.RedPackageResultDialog;
import com.example.anuo.immodule.view.RedPackageSendDialog;
import com.example.anuo.immodule.view.RemarkDialog;
import com.example.anuo.immodule.view.StateButton;
import com.example.anuo.immodule.view.TodayProfitDialog;
import com.example.anuo.immodule.view.WrapContentHeightViewPager;
import com.example.anuo.immodule.view.fire.FireView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.simon.utils.DisplayUtil;
import com.simon.widget.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import crazy_wrapper.Crazy.Utils.RequestUtils;
import crazy_wrapper.Crazy.Utils.Utils;
import crazy_wrapper.Crazy.dialog.ActionSheetDialog;
import crazy_wrapper.Crazy.dialog.OnOperItemClickL;
import crazy_wrapper.Crazy.network.FileHandler;
import crazy_wrapper.Crazy.request.CrazyRequest;


public class ChatMainActivity extends ChatBaseActivity implements IIChatMainView {
    private static String TAG = ChatMainActivity.class.getSimpleName();

    TextView secondTitle;
    TextView topInfoTitle;
    TextView topInfoContent;
    LinearLayout llParent;
    LinearLayout llMarquee;
    LinearLayout llTopInfo;
    ImageView topInfoClose;
    ViewPager chatRcResult; //所有彩种开奖结果
    ImageButton imageMore;

    RecyclerView rvChatList;
    SwipeRefreshLayout swipeChat;
    ImageView ivAudio;
    AitEditText etContent;
    RecordButton btnAudio;
    ImageView ivEmo;
    ImageView ivAdd;
    StateButton btnSend;
    LinearLayout llContent;
    WrapContentHeightViewPager vpEmoji;
    IndicatorView indEmoji;
    LinearLayout homeEmoji;
    ImageView ivPhoto;
    ImageView ivVideo;
    ImageView ivFile;
    ImageView ivLocation;
    RelativeLayout bottomLayout;
    LinearLayout llEmotion;
    LinearLayout mLlAdd;//添加布局
    ImageButton float_ball; //悬浮球
    RecyclerView rec_float; //悬浮球
    FrameLayout fr_float; //悬浮球
    TextView tvNewMsgTip;
    TextView tvNotAllowedTalk;
    FireView fireView;
    FrameLayout flContent;
    LinearLayout llChatContent;
    TextView ibt_1;
    TextView ibt_2;
    ImageButton ibt_3;
    TextView ibt_4;
    TextView ibt_5;
    TextView ibt_6;

    TextView tagNotice;
    ChatMainMarqueeTextView noticeText;

    RelativeLayout rlQuickMessage;
    RelativeLayout rlSave;
    RelativeLayout rlPhoto;
    RelativeLayout rlRedPackage;

    RelativeLayout welcomeLayout;
    DrawerLayout drawerLayout;
    FrameLayout draw_right;
    CircleImageView iv_header;
    TextView tv_jryk;
    TextView tv_jrzj;
    TextView tv_jrcz;
    TextView tv_ljcz;
    TextView tv_ztz;
    TextView tv_dml;
    LinearLayout ll_jryk;
    LinearLayout ll_dml;
    LinearLayout ll_jrcz;
    LinearLayout ll_ljcz;
    Button btn_refresh;
    Button btn_share;
    NestedExpandaleListView elv_draw;
    TextView chat_draw_level_name;
    ImageView chat_draw_level_icon;
    TextView chat_draw_money;
    ImageView chat_draw_iv_money;
    TextView chat_draw_account_name;
    TextView tvBanTalk;

    private ChatAdapter mAdapter;
    private ImageView msg_iv_audio;
    public static final String mSenderId = "right";
    public static final String mTargetId = "left";
    public static final int REQUEST_CODE_IMAGE = 0000;
    public static final int REQUEST_CODE_VEDIO = 1111;
    public static final int REQUEST_CODE_FILE = 2222;
    private final int REQUEST_CODE_ROOM_LIST = 3333;
    private final int REQUEST_CODE_PERSON_DATA = 4444;
    public static final int SDK_PERMISSION_REQUEST = 10086;
    private boolean CAN_WRITE_EXTERNAL_STORAGE = true;
    private boolean CAN_RECORD_AUDIO = true;
    private boolean CAN_USE_CAMERA = true;
    // 使用LinkedList
    private List<ChatMessage> chatMsgs = new LinkedList<>();
    // 聊天列表中的图片集合
    private ArrayList<String> imageList = new ArrayList<>();
    private HashMap<Integer, Integer> imagePosition = new HashMap<Integer, Integer>();
    private BetSlipDialog betSlipDialog;
    private Dialog dialog;
    // 房间被禁言
    private boolean roomNotSpeaking = false;
    // 用户被禁言
    private boolean banTalk = false;

//    private JWebSocketClientService.JWebSocketClientBinder binder;
//    private JWebSocketClientService jWebSClientService;
//    private JWebSocketClient client;

    //    private ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            Log.e("MainActivity", "服务与活动成功绑定");
//            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
//            jWebSClientService = binder.getService();
//            client = jWebSClientService.client;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            Log.e("MainActivity", "服务与活动成功断开");
//        }
//    };
    private ChatMessageReceiver chatMessageReceiver;
    private ChatMainPresenter presenter;
    //    private LoginChatBean loginChatBean;
    private List<ChatRoomListBean.SourceBean.DataBean> dataBeans = new ArrayList<>();
    private ChatRoomListBean.SourceBean.DataBean currentChatRoom;
    private int nowRoomPos = 0; //当前房间的位置
    private ChatRoomListWindow roomListWindow;
    private JoinChatRoomBean.SourceBean roomBeanMsg;
    // 是否显示中奖提示
    private boolean showWinningView = true;
    private CommonDialog commonDialog;
    private static List<View> views = new ArrayList<>();
    private Animation viewTransitionOut = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
    // View正在执行动画
    private boolean isAnimating = false;
    private Animation viewTransitionIn = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
    private ChatUiHelper mUiHelper;
    // 新消息条数
    private int newMsgCount = 0;
    // 是否显示新消息提示
    private boolean isShowNewMsgTip = false;
    private List<ChatMessage> sendImageMessages = new ArrayList<>();
    private boolean receivingMsg = false;
    private RedPackageResultDialog resultDialog;

    private ChatLotteryResultAdapter chatLotteryResultAdapter = null;
    private List<ChatLotteryBean.SourceBean.LotteryDataBean> lotteryResultData = new ArrayList<>();

    public ChatSysConfig chatSysConfig;

    public int lotteryPos = 0; //当前页面显示的彩种所在的list位置;
    private int totalPeople = 0;
    private CommonDialog applyBanTalkDialog;
    private ChatLongDragonListWindow dragonListWindow;
    private List<ChatUserListBean.ChatUserBean> source = new ArrayList<>();
    private List<ChatUserListBean.ChatUserBean> searchSource = new ArrayList<>();
    private AuditListDialog auditListDialog;
    public boolean isInRoom() {
        return inRoom;
    }

    private boolean inRoom = false;
    private JoinChatRoomBean.SourceBean.PermissionObjBean permissionObj;

    //private Animation welcomeTxtAnimation = null;

    private View animaView;
    //记录当前点击的语音消息是发送的还是接收的
    private String senderId;
    // 语音播放中
    private boolean broadcasting = false;
    // 代理房间对象，由R70000接口返回
    private ChatRoomListBean.SourceBean.DataBean agentRoomBean;
    private ChatRoomListBean.SourceBean.DataBean configRoomBean;
    private List<GroupBean> drawerDatas = new ArrayList();
    private ChatDrawerAdapter chatDrawerAdapter;
    private GroupBean managerGroup;
    private GroupBean onlineGroup;
    private GroupBean privateGroup;//私聊列表
    private ChatPersonDataBean.SourceBean.WinLostBean personData;

    public int getLotteryPos() {
        return lotteryPos;
    }

    //间隔几秒查询一次开奖结果的倒计时器
    CountDownTimer intervalRequestOpenResultTimer;

    private List<String> violateWordsList = null; //房间禁言词

    private ModifyPersonDataModel modifyPersonDataModel = null;
    private String accountAvatar = "";
    private String accountNickName = "";
    private String accountName = "";

    private List<WelcomeTextBean> welcomeViews = new LinkedList<>(); //欢迎文字集合

    private boolean isLoginSuc = false; //是否登录成功
    boolean isshowfloat = false;
    public boolean isPrivate = false;//是否是私聊， 默认群聊
    public boolean isFromNotifyPrivate = false;
    private LotteryHistoryResultWindow lotteryHistoryResultWindow;
    private int historyPageCount = 20;
    private List<ChatMessage> tempMessages = new ArrayList();

    private class ChatMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            LogUtils.e("onReceive:", message);
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
        LogUtils.e("ChatMainActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e("ChatMainActivity", "onPause");
        if (broadcasting) {
            resetAudio(senderId);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
        LogUtils.e("ChatMainActivity", "onStop");
    }

    @Override
    protected int onCreate_(Bundle savedInstanceState) {
        return R.layout.activity_chat_main;
    }

    @Override
    protected void initListener() {
        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.rlPhoto).setOnClickListener(this);
        findViewById(R.id.rlVideo).setOnClickListener(this);
        findViewById(R.id.rlLocation).setOnClickListener(this);
        findViewById(R.id.rlFile).setOnClickListener(this);
        findViewById(R.id.rlRedPackage).setOnClickListener(this);
        findViewById(R.id.rl_top_info_content).setOnClickListener(this);
        findViewById(R.id.top_info_close).setOnClickListener(this);
        findViewById(R.id.chat_draw_share).setOnClickListener(this);
        findViewById(R.id.chat_draw_refresh).setOnClickListener(this);
        findViewById(R.id.chat_draw_header).setOnClickListener(this);
        findViewById(R.id.rlQuickMessage).setOnClickListener(this);
        findViewById(R.id.rlSave).setOnClickListener(this);
        findViewById(R.id.chat_draw_refresh_money).setOnClickListener(this);
        findViewById(R.id.charge).setOnClickListener(this);
        findViewById(R.id.tikuan).setOnClickListener(this);

        etContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    return AitEditText.KeyDownHelper(etContent.getText());
                }
                return false;
            }
        });
        etContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("输入框监听", "111");
                if (mAdapter.getItemCount() >= 1) {
                    rvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
                }
            }
        });
        ibt_1.setOnClickListener(this);
        ibt_2.setOnClickListener(this);
        ibt_3.setOnClickListener(this);
        ibt_4.setOnClickListener(this);
        ibt_5.setOnClickListener(this);
        ibt_6.setOnClickListener(this);
        SpannableString spannableString = new SpannableString("您已被管理员禁言，我要申诉");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (applyBanTalkDialog == null) {
                    applyBanTalkDialog = CommonDialog.create(ChatMainActivity.this, "申诉理由", "请输入申诉内容", "确定", new CommonDialog.DialogClickListener() {
                        @Override
                        public void onInputListener(View view, String input) {
                            if (TextUtils.isEmpty(input)) {
                                showToast("请输入申诉内容");
                                return;
                            }
                            ApplyBanSpeakModel applyBanSpeakModel = new ApplyBanSpeakModel();
                            applyBanSpeakModel.setUserId(ChatSpUtils.instance(ChatMainActivity.this).getUserId());
                            applyBanSpeakModel.setTaskType("1");
                            applyBanSpeakModel.setRoomId(roomBeanMsg.getRoomId());
                            applyBanSpeakModel.setRemark(input);
                            applyBanSpeakModel.setCode(ConfigCons.APPLY_FOR_BAN_SPEAK);
                            presenter.applyBanTalk(applyBanSpeakModel);
                        }

                        @Override
                        public void onClick(View v) {

                        }
                    }, "取消", new CommonDialog.DialogClickListener() {
                        @Override
                        public void onInputListener(View view, String input) {

                        }

                        @Override
                        public void onClick(View v) {

                        }
                    }, false, false, true, false, true, InputType.TYPE_CLASS_TEXT, true);
                }
                applyBanTalkDialog.showAndInput();
            }
        };
        spannableString.setSpan(clickableSpan, 9, 13, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvBanTalk.setMovementMethod(LinkMovementMethod.getInstance());
        tvBanTalk.setText(spannableString);

        float_ball.setOnTouchListener(new DragListener());
        fr_float.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fr_float.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int screenHeight = getWindow().getDecorView().getHeight();
                int frameHeight = fr_float.getHeight();
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) fr_float.getLayoutParams();
                lp.topMargin = (screenHeight - frameHeight)/4;
                fr_float.setLayoutParams(lp);
            }
        });
    }

    @Override
    protected ChatBasePresenter initPresenter() {
        presenter = new ChatMainPresenter(this, this);
        return presenter;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        String domain_url = getIntent().getStringExtra("baseUrl");
        String nativeDomain = getIntent().getStringExtra(RequestUtils.NATIVE_DOMAIN);
        String nativeHost = getIntent().getStringExtra(RequestUtils.NATIVE_HOST);
        String routeType = getIntent().getStringExtra(RequestUtils.ROUTE_TYPE);
        String nativeFlag = getIntent().getStringExtra(RequestUtils.NATIVE_FLAG);
        if (!TextUtils.isEmpty(domain_url)) {
            ConfigCons.YUNJI_BASE_URL = domain_url;
        }
        if (!TextUtils.isEmpty(nativeDomain)) {
            ConfigCons.YUNJI_NATIVE_DOMAIN_URL = nativeDomain;
        }
        if (!TextUtils.isEmpty(nativeHost)) {
            ConfigCons.YUNJI_BASE_HOST_URL = nativeHost;
        }
        if (!TextUtils.isEmpty(routeType)) {
            ConfigCons.YUNJI_ROUTE_TYPE_URL = routeType;
        }
        if (!TextUtils.isEmpty(nativeFlag)) {
            ConfigCons.YUNJI_NATIVE_FLAG_URL = nativeFlag;
        }
//        if (presenter.socketManager.isConnecting()) {
//            String encrypted = ChatSpUtils.instance(this).getENCRYPTED();
//            String cluster_id = ChatSpUtils.instance(this).getCLUSTER_ID();
//            String sign = ChatSpUtils.instance(this).getSIGN();
//            presenter.loginChatRoom(cluster_id, encrypted, sign);
//        } else {
//            presenter.authorization();
//        }
        presenter.authorization();
    }

    @Override
    public void onConnectSuccess(final AuthorityBean authorityBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (authorityBean != null) {
                    presenter.loginChatRoom(authorityBean.getContent().getClusterId(), authorityBean.getContent().getEncrypted(), authorityBean.getContent().getSign());
                } else {
                    String encrypted = ChatSpUtils.instance(ChatMainActivity.this).getENCRYPTED();
                    String cluster_id = ChatSpUtils.instance(ChatMainActivity.this).getCLUSTER_ID();
                    String sign = ChatSpUtils.instance(ChatMainActivity.this).getSIGN();
                    presenter.loginChatRoom(cluster_id, encrypted, sign);
                }
            }
        });
    }

    /**
     * 更新系统配置
     */
    @Override
    public void onUpdateConfig() {
        chatSysConfig = ChatSpUtils.instance(this).getChatSysConfig();
    }

    /**
     * 初始化私聊成功
     */
    @Override
    public void initPrivateConversation(ChatPrivateConversationBean b) {
        ChatPrivateConversationBean.SourceBean sourceBean = b.getSource();
        if (sourceBean != null) {
            switch (sourceBean.getType()) {
                case "1":
                    //初始化私聊 ------> 登录聊天室 -----> 进入房间  ----> 刷新消息私聊状态 ------>
                    presenter.joinPrivateRoom(sourceBean.getRoomId(), "join", ChatSpUtils.instance(ChatMainActivity.this).getUserId(), passiveUserId);
                    break;
                case "2":
                    //拉取私聊列表成功
                    if (privateGroup != null && sourceBean.getRoomList() != null) {
                        privateGroup.setChildBeans(sourceBean.getRoomList());
                        elv_draw.deferNotifyDataSetChanged();
                        chatDrawerAdapter.notifyDataSetChanged();
                    }
                    break;
                case "3":
                    //刷新消息私聊状态 , 将所有未读变成了已读成功返回
                    //拉取私聊列表
                    getPrivateConversationList();
                    break;
                default:
                    //获取备注名
                    break;
            }
        }
    }

    @Override
    public void onRemarkResult(ChatRemarkBean chatRemarkBean, String passiveUserID, String remark) {
        if(chatRemarkBean.getSource() != null && chatRemarkBean.getSource().isSuccess()){
            for(ChatUserListBean.ChatUserBean user: privateGroup.getChildBeans()){
                if(passiveUserID.equals(user.getFromUser().getIdX())){
                    user.setRemarks(remark);
                    chatDrawerAdapter.notifyDataSetChanged();
                    return;
                }
            }
        }else {
            showToast("修改备注时发生错误");
        }
    }

    /**
     * 实际上表示获取系统配置成功的回调，并非登陆成功
     *
     * @param loginChatBean
     */
    @Override
    public void onLoginSuc(LoginChatBean loginChatBean) {
        chatSysConfig = ChatSpUtils.instance(this).getChatSysConfig();
        LoginChatBean.SourceBean userTmp = loginChatBean.getSource();
        ChatSpUtils.instance(this).setUserId(userTmp.getSelfUserId());
        ChatSpUtils.instance(this).setStationId(userTmp.getStationId());
        ChatSpUtils.instance(this).setACCOUNT_TYPE(userTmp.getAccountType());
//        if (userTmp.getAccountType() == 2 || userTmp.getAccountType() == 3) {
//            if (userTmp.getAgentRoom() != null) {
//                ChatSpUtils.instance(this).setAGENT_USER_CODE(userTmp.getAgentRoom().getAgentUserCode());
//            }
//        }
        if (!TextUtils.isEmpty(this.getIntent().getStringExtra("baseUrl"))) {
            ChatSpUtils.instance(this).setMainAppBaseUrl(this.getIntent().getStringExtra("baseUrl"));
            ChatSpUtils.instance(this).setLotteryVersion(getIntent().getIntExtra("lotteryVersion1", 1), getIntent().getIntExtra("lotteryVersion1", 2));
        }
        isLoginSuc = true;
        presenter.initData();
        if (chatSysConfig.getSwitch_lottery_result_show().equals("1")) {
            showLotteryLatestResults();
            presenter.getAllLotteryInfo();
        } else {
            chatRcResult.setVisibility(View.GONE);
            imageMore.setVisibility(View.GONE);
        }
        presenter.getViolateWords(); //获取违禁词

        if (chatSysConfig.getSwitch_yingkui_show().equals("0")) {
//            tv_jryk.setVisibility(View.GONE);
            ll_jryk.setVisibility(View.INVISIBLE);
        }

        if (chatSysConfig.getSwitch_bet_num_show().equals("0")) {
//            tv_dml.setVisibility(View.GONE);
            ll_dml.setVisibility(View.INVISIBLE);
        }

        if (chatSysConfig.getSwitch_today_recharge_show().equals("0")) {
            ll_jrcz.setVisibility(View.INVISIBLE);
        }

        if (chatSysConfig.getSwitch_sum_recharge_show().equals("0")) {
            ll_ljcz.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStopRefresh() {
        chat_draw_iv_money.clearAnimation();
    }

    @Override
    public void onGetCharge(String money) {
        chat_draw_money.setText(money);
    }

    @Override
    public void onGetChatImage(ArrayList<String> imageList, HashMap<Integer, Integer> imagePosition) {
        this.imageList = imageList;
        this.imagePosition = imagePosition;
    }

    @Override
    public void onAgentRoom(ChatRoomListBean.SourceBean.DataBean roomEntify) {
        this.agentRoomBean = roomEntify;
    }

    @Override
    public void onConfigRoom(ChatRoomListBean.SourceBean.DataBean userConfigRoom) {
        this.configRoomBean = userConfigRoom;
    }

    private String passiveUserId;
    private String passiveUserName;
    private String privateRoomId;

    /**
     * 初始化viewpager，显示各彩票最新一期开奖资讯
     */
    private void showLotteryLatestResults() {
        LotteryResultModel model = new LotteryResultModel(ConfigCons.LOTTERY_LIST, ChatSpUtils.instance(this).getStationId(), "app");
        chatRcResult.setVisibility(View.VISIBLE);
        imageMore.setVisibility(View.VISIBLE);
        presenter.getLotteryResult(model); //获取彩种列表
        chatLotteryResultAdapter = new ChatLotteryResultAdapter(this, lotteryResultData);
//        chatLotteryResultAdapter.setCallback(new ChatLotteryResultAdapter.LotteryResultCallback() {
//            @Override
//            public void onResultClicked(ChatLotteryBean.SourceBean.LotteryDataBean bean) {
//                presenter.onKaiJianResultClicked(bean.getCode());
//            }
//        });

        imageMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatLotteryBean.SourceBean.LotteryDataBean bean = lotteryResultData.get(lotteryPos);
                presenter.onKaiJianResultClicked(bean.getCode());
            }
        });
        chatRcResult.setAdapter(chatLotteryResultAdapter);
        chatRcResult.setCurrentItem(lotteryResultData.size() * 5); //增加倍可以左右滑动
        chatRcResult.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                i = i % lotteryResultData.size();
                if (i != lotteryPos) {
                    cancelTimer();
                }
                presenter.getKaiJianResult(lotteryResultData.get(i).getCode());
                lotteryPos = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    /**
     * 加入私聊房间成功
     *
     * @param privateRoomMsg
     */
    @Override
    public void onJoinPrivateRoomSuccess(ChatJoinPrivateRoomMsg privateRoomMsg) {
        ToastUtil.showToast(this, "加入私聊房间成功").setGravity(Gravity.CENTER, 0, 0);
        //拉取历史消息
        privateRoomId = privateRoomMsg.getRoomId();
        presenter.getHistoryMessages(passiveUserId, privateRoomMsg.getRoomId(), 2, ChatSpUtils.instance(this).getUserId(), "", 1);
    }


    /**
     * 2 拉取历史消息成功
     * 1 发送私聊消息成功
     */
    @Override
    public void onGetPrivateChatHistoryMessagesSuccess(ChatPrivateMessageBean chatPrivateMessageBean) {
        isPrivate = true; //当前为私聊
        ivAudio.setVisibility(View.GONE);
        roomListWindow.updataClickRoom(privateRoomId);
        tvNotAllowedTalk.setVisibility(View.GONE);
        tvBanTalk.setVisibility(View.GONE);

        if (chatPrivateMessageBean.isSuccess()) {
            //拉取消息返回
            ChatPrivateMessageBean.SourceBean source = chatPrivateMessageBean.getSource();
            if (source == null) {
                return;
            }
            if (source.getType().equals("1")) {
                //type = 1 发消息
                String msgUUID = "";
                for (int i = mAdapter.getData().size() - 1; i >= 0; i--) {
                    ChatMessage chatMsg = mAdapter.getData().get(i);
                    if (chatMsg.getBody().getMsgId() != null && chatMsg.getBody().getMsgId().equals(source.getUserMessageId())) {
                        msgUUID = chatMsg.getUuid();
                        break;
                    }
                }
                if (!TextUtils.isEmpty(msgUUID)) {
                    onSendSuccess(msgUUID);
                }
            } else if (source.getType().equals("2")) {
                //type =2 拉取历史消息
                //1.如果聊天记录中的消息不为空，则先将之前所有的消息都变成已读
                setPrivateMessagesRead();
                //2.把侧边栏收起
                drawerLayout.closeDrawers();
                //3.清空之前聊天室的所有内容
                mAdapter.getData().clear();
                mAdapter.notifyDataSetChanged();
                //4.暂时隐藏顶部开奖轮播，title显示私聊人名
                chatRcResult.setVisibility(View.GONE);
                imageMore.setVisibility(View.GONE);
                tvMiddleTitle.setText(passiveUserName);
                //5.更新私聊房间消息列表
                //将私聊消息数据类型转化为群聊数据消息类型 -------->以便最大程度复用
                packageData(chatPrivateMessageBean);
            }
            //6.开始准备发消息 ---->sendMessages，发图片，发计划消息，分享注单，分享今日盈亏，发红包(暂不可以)
        } else {
            ToastUtil.showToast(this, "拉取私聊历史记录失败");
        }
    }


    private void packageData(ChatPrivateMessageBean chatPrivateMessageBean) {
        if (chatPrivateMessageBean.getSource().getData() != null && chatPrivateMessageBean.getSource().getData().size() != 0) {
            ChatHistoryMessageBean chatHistoryMessageBean = new ChatHistoryMessageBean();
            chatHistoryMessageBean.setSuccess(chatPrivateMessageBean.isSuccess());
            chatHistoryMessageBean.setCode(chatPrivateMessageBean.getCode());
            chatHistoryMessageBean.setMsg(chatHistoryMessageBean.getMsg());
            List<ChatHistoryMessageBean.SourceBean> sourceBeansList = new LinkedList<>();
            List<ChatPrivateMessageBean.SourceBean.DataBean> data = chatPrivateMessageBean.getSource().getData();
            for (ChatPrivateMessageBean.SourceBean.DataBean datum : data) {
                ChatHistoryMessageBean.SourceBean sourceBean = new ChatHistoryMessageBean.SourceBean();
                sourceBean.setDate(datum.getDate());
                sourceBean.setMsgType(datum.getMsgType() == 6 ? 3 : datum.getMsgType());
                if (datum.getFromUser() != null) {
                    sourceBean.setNickName(datum.getFromUser().getNickName());
                    sourceBean.setLevelName(datum.getFromUser().getLevelName());
                    sourceBean.setUserType(datum.getFromUser().getUserType());
                    sourceBean.setAccount(datum.getFromUser().getAccount());
                    sourceBean.setAvatar(datum.getFromUser().getAvatar());
                    sourceBean.setSender(datum.getFromUser().getId());
                    sourceBean.setLevelIcon(datum.getFromUser().getLevelIcon());
                }
                sourceBean.setNativeContent(datum.getNativeContent());
                sourceBean.setMsgId(datum.getUserMessageId());
                sourceBean.setStopTalkType(datum.getStopTalkType());
                sourceBean.setIsPlanUser(datum.getIsPlanUser());
                sourceBean.setNativeNickName(datum.getNativeNickName());
                sourceBean.setUrl(datum.getRecord());
                sourceBean.setContext(datum.getRecord());
                sourceBean.setTime(datum.getCreateTime());
                sourceBean.setNativeAccount(datum.getNativeAccount());
                sourceBean.setRemark(datum.getRemark());
                if(TextUtils.isEmpty(datum.getPayId())){
                    try{
                        JSONObject obj = new JSONObject(datum.getRecord());
                        sourceBean.setPayId(obj.getString("payId"));
                        sourceBean.setRemark(obj.getString("remark"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    sourceBean.setPayId(datum.getPayId());
                }
                sourceBeansList.add(sourceBean);
            }
            chatHistoryMessageBean.setSource(sourceBeansList);
            onGetHistoryMessage(chatHistoryMessageBean);
        }
    }


    /**
     * 获取快捷发言消息
     */
    @Override
    public void onGetQuickMessages(ChatQuickMessageBean chatQuickMessageBean) {
        showFastAmountDialog(chatQuickMessageBean);
    }

    /**
     * 获取收藏图片信息
     *
     * @param chatCollectionImagesBean
     */
    @Override
    public void onCollectionsImages(ChatCollectionImagesBean chatCollectionImagesBean) {
        //option :
        // 1---查询用户收藏的图片
        // 2--收藏图片
        // 3--删除收藏的图片
        String option = chatCollectionImagesBean.getSource().getOption();
        switch (option) {
            case "1":
                List<String> collectImageArray = chatCollectionImagesBean.getSource().getCollectImageArray();
                ArrayList<ChatCollectionImagesSelectBean> list = new ArrayList<>();
                for (String s : collectImageArray) {
                    ChatCollectionImagesSelectBean chatCollectionImagesBean1 = new ChatCollectionImagesSelectBean();
                    chatCollectionImagesBean1.setSelect(false);
                    chatCollectionImagesBean1.setRecord(s);
                    list.add(chatCollectionImagesBean1);
                }
                ChatImageCollectionActivity.createIntent(this, list);
                break;
            case "2":
                ToastUtils.showToast(this, "收藏成功");
                break;
            case "3":
                ToastUtils.showToast(this, "删除成功");
                break;
        }


    }

    @Override
    public void onGetChatRoomList(ChatRoomListBean chatRoomListBean) {
        // 标记本地储存的RoomID是否有效，默认无效，如果有效则改为false
        boolean invalidRoomId = true;
        // 默认已经不包含代理房间，如果不包含，则添加进房间列表
        boolean containAgentRoom = false;
        boolean containConfigRoom = false;
        dataBeans.clear();
        //TODO 暂时屏蔽筛选条件，后期再加
        dataBeans.addAll(chatRoomListBean.getSource().getData());
        //判断代理房间是否应该存在，不存在的话就添加至最前面
        if (chatRoomListBean.getSource().getData() != null) {
            for (int i = 0; i < chatRoomListBean.getSource().getData().size(); i++) {
                ChatRoomListBean.SourceBean.DataBean dataBean = chatRoomListBean.getSource().getData().get(i);
                if (agentRoomBean != null && dataBean.getId().equals(agentRoomBean.getId())) {
                    containAgentRoom = true;
                }
                //判断是否有配置房间，有的话就替换掉此处的配置房间
                if (configRoomBean != null && dataBean.getId().equals(configRoomBean.getId())) {
                    containConfigRoom = true;
                    dataBean.setRoomKey("");
                    if (i != 0) {
                        //将配置房间放到最前面
                        dataBeans.remove(dataBean);
                        dataBeans.add(0, dataBean);
                    }
                }
            }
        }
        if (!containAgentRoom && agentRoomBean != null) {
            dataBeans.add(0, agentRoomBean);
        }

        if (!containConfigRoom && configRoomBean != null) {
            dataBeans.add(0, configRoomBean);
        }
//        if (ChatSpUtils.instance(this).getACCOUNT_TYPE() == 2 || ChatSpUtils.instance(this).getACCOUNT_TYPE() == 3) {
//            dataBeans.addAll(chatRoomListBean.getSource().getData());
//        } else {
//            List<ChatRoomListBean.SourceBean.DataBean> temp = new ArrayList<>();
//            for (int i = 0; i < chatRoomListBean.getSource().getData().size(); i++) {
//                if (chatRoomListBean.getSource().getData().get(i).getType() != 3) { //普通会员不显示代理房
//                    temp.add(chatRoomListBean.getSource().getData().get(i));
//                }
//            }
//            dataBeans.addAll(temp);
//        }
        if (dataBeans.isEmpty()) {
            showToast("暂无有效房间，请联系客服！");
            return;
        }
        if (roomListWindow == null) {
            roomListWindow = new ChatRoomListWindow(this, dataBeans); //房间列表改变之后需要重新更新ChatRoomListWindow的房间数据
        }
        // 如果不是从通知进来的，roomId为空的话，默认进入第一个房间
        String roomId = getIntent().getStringExtra("roomId");
        //私聊
        privateRoomId = roomId;
        passiveUserName = getIntent().getStringExtra("roomName");
        isFromNotifyPrivate = getIntent().getBooleanExtra("isPrivate", false);
        passiveUserId = getIntent().getStringExtra("passiveUserId");

        if (TextUtils.isEmpty(roomId) || isFromNotifyPrivate) {

            String current_room_id = ChatSpUtils.instance(this).getCURRENT_ROOM_ID();
            ChatRoomListBean.SourceBean.DataBean dataBean = dataBeans.get(0);
            if (TextUtils.isEmpty(current_room_id)) {
                tvMiddleTitle.setText(dataBean.getName());
                presenter.joinChatRoom(dataBean.getId(), dataBean.getRoomKey(), dataBean.getName(), false);
            } else {
                for (ChatRoomListBean.SourceBean.DataBean dataBean1 : dataBeans) {
                    if (dataBean1.getId().equals(current_room_id)) {
                        invalidRoomId = false;
                        presenter.joinChatRoom(dataBean1.getId(), dataBean1.getRoomKey(), dataBean1.getName(), true);
                    }
                }
                if (invalidRoomId) {
                    presenter.joinChatRoom(dataBean.getId(), dataBean.getRoomKey(), dataBean.getName(), false);
                }
            }
        } else {
            for (ChatRoomListBean.SourceBean.DataBean dataBean : dataBeans) {
                if (dataBean.getId().equals(roomId)) {
                    presenter.joinChatRoom(dataBean.getId(), dataBean.getRoomKey(), dataBean.getName(), true);
                }
            }
        }
    }

    @Override
    public void onJoinChatRoom(JoinChatRoomBean joinChatRoomBean) {
        if (roomListWindow == null) {
            //房间列表改变之后需要重新更新ChatRoomListWindow的房间数据
            roomListWindow = new ChatRoomListWindow(this, dataBeans);
        }

        for(ChatRoomListBean.SourceBean.DataBean room: dataBeans){
            if(room.getName().equals(joinChatRoomBean.getSource().getTitle())){
                currentChatRoom = room;
                break;
            }
        }

        String mentorName = joinChatRoomBean.getSource().getMentorName();
        mAdapter.setMentorName(TextUtils.isEmpty(mentorName) ? "导师" : mentorName);
        joinRoomSuc(joinChatRoomBean);
    }

    @Override
    public void onGetOnlineUser(ChatUserListBean chatUserListBean) {
        if (onlineGroup != null) {
            source = chatUserListBean.getSource();

            if(currentChatRoom != null){
                totalPeople = currentChatRoom.getFakePerNum();
            }else {
                totalPeople = Integer.parseInt(chatSysConfig.getName_room_people_num());
            }
            if (source != null) {
                int size = source.size();
                totalPeople += size;
                if (totalPeople < 10) {
                    totalPeople = 10;
                }
                onlineGroup.setName("在线用户——" + totalPeople + "人");
                if (size < 10) {
                    source.addAll(loadMorePeoples(10 - size));
                }
            } else {
                if (totalPeople < 10) {
                    totalPeople = 10;
                }
                onlineGroup.setName("在线用户——" + totalPeople + "人");
                source = new ArrayList<>();
                source.addAll(loadMorePeoples(10));
            }
            onlineGroup.setStatus(1);
            onlineGroup.setChildBeans(source);
            chatDrawerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetManagerList(ChatUserListBean managerListBean) {
        managerGroup.setChildBeans(managerListBean.getSource());
        chatDrawerAdapter.notifyDataSetChanged();
    }

    private List<String> photoData = new ArrayList<>();

    @Override
    public void getPhotoList(ChatPersonPhotoListBean chatPersonPhotoListBean) {
        photoData.clear();
        photoData.addAll(chatPersonPhotoListBean.getSource().getItems());
    }

    private boolean isRefresh = false; //是否在投注刷新数据

    @Override
    public void getPersonData(ChatPersonDataBean chatPersonDataBean) {
        if (chatPersonDataBean.getSource().getWinLost() != null) {
            this.personData = chatPersonDataBean.getSource().getWinLost();
        }
        if (!isRefresh) {
            if (!TextUtils.isEmpty(chatPersonDataBean.getSource().getAvatar())) {
                GlideUtils.loadHeaderPic(ChatMainActivity.this, chatPersonDataBean.getSource().getAvatar(), iv_header);
            }
            if (!TextUtils.isEmpty(chatPersonDataBean.getSource().getLevelIcon())) {
                GlideUtils.loadHeaderPic(ChatMainActivity.this, chatPersonDataBean.getSource().getLevelIcon(), chat_draw_level_icon);
            }
            chat_draw_level_name.setText(chatPersonDataBean.getSource().getLevel());
            chat_draw_account_name.setText(TextUtils.isEmpty(chatPersonDataBean.getSource().getNickName()) ?
                    chatPersonDataBean.getSource().getAccount() : chatPersonDataBean.getSource().getNickName());
        }

        if (chatPersonDataBean.getSource().getWinLost() != null) {
            ChatPersonDataBean.SourceBean.WinLostBean bean = chatPersonDataBean.getSource().getWinLost();
            tv_jrzj.setText(CommonUtils.doubleToString(bean.getAllWinAmount()));
            tv_jryk.setText(CommonUtils.doubleToString(bean.getYingkuiAmount()));
            tv_ljcz.setText(CommonUtils.doubleToString(bean.getSumDepost()));
            tv_jrcz.setText(CommonUtils.doubleToString(bean.getSumDepostToday()));
            tv_ztz.setText(CommonUtils.doubleToString(bean.getAllBetAmount()));
            tv_dml.setText(CommonUtils.doubleToString(bean.getBetNum()));
        }
    }


    @Override
    public void ModifyPersonData(boolean isSuc) {
        if (isSuc) {
            showToast("修改成功");
            Intent intent = new Intent();
            if (modifyPersonDataModel != null) {
                intent.putExtra("model", modifyPersonDataModel);
            }
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void joinRoomSuc(final JoinChatRoomBean joinChatRoomBean) {
        isPrivate = false;
        ivAudio.setVisibility(View.VISIBLE);
        final JoinChatRoomBean.SourceBean source = joinChatRoomBean.getSource();
        ChatSpUtils.instance(this).setAgentRoomHost(source.getAgentRoomHost());
        boolean isShowRv = chatSysConfig.getSwitch_lottery_result_show().equals("1");
        chatRcResult.setVisibility(isShowRv ? View.VISIBLE : View.GONE);
        imageMore.setVisibility(isShowRv ? View.VISIBLE : View.GONE);

        permissionObj = source.getPermissionObj();
        presenter.savePermissions(permissionObj);
        if (!ChatSpUtils.instance(this).getEnterRoom().equals("1")) {
            showToast("您无法加入该房间，请联系客服！");
            ChatSpUtils.instance(this).setCURRENT_ROOM_ID("");
            return;
        }
        if("1".equalsIgnoreCase(chatSysConfig.getSwitch_fuli_logo_config())){
            fr_float.setVisibility(View.VISIBLE);
            initSinglePageFloatball();
        }else {
            fr_float.setVisibility(View.GONE);
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        inRoom = true;
        showWinningView = true;
        roomListWindow.updataClickRoom(joinChatRoomBean.getSource().getRoomId());

        // 判断房间是否需要禁言
        roomNotSpeaking = source.getIsBanSpeak() == 1;
        if(source.getUserType()==4||source.getUserType()==2){
            roomNotSpeaking=false;
        }
        if (roomNotSpeaking) {
            setBanText("房间全体禁言");
            tvNotAllowedTalk.setVisibility(View.VISIBLE);
        } else {
            tvNotAllowedTalk.setVisibility(View.GONE);
        }

        // 用户被禁言
        banTalk = source.getSpeakingFlag() == 1;
        if (banTalk) {
            tvBanTalk.setVisibility(View.VISIBLE);
        } else {
            tvBanTalk.setVisibility(View.GONE);
        }


//        if (roomBeanMsg == null || !roomBeanMsg.getRoomId().equals(source.getRoomId())) {
        chatMsgs.clear();
        mAdapter.notifyDataSetChanged();
        String historyPerpageCount = chatSysConfig.getSwitch_history_perpage_count();
        String count = historyPerpageCount.split(":")[0];
        //start＝0开始时，後端会回传错误的缓存消息，所以从1开始取历史消息
        ChatMessageHistoryModel chatMessageHistoryModel = new ChatMessageHistoryModel(ConfigCons.HISTORY_MESSAGE, ConfigCons.SOURCE,
                source.getRoomId(), 0, historyPageCount);
        presenter.getHistoryMessage(chatMessageHistoryModel);
//        } else {
////            presenter.connectSocket(source.getRoomId());
//        }
        roomBeanMsg = source;
        // 判断当前房间是否有单独配置默认开奖彩种
        if (!dataBeans.isEmpty()) {
            for (ChatRoomListBean.SourceBean.DataBean dataBean : dataBeans) {
                if (roomBeanMsg.getRoomId().equals(dataBean.getId())) {
                    if (!TextUtils.isEmpty(dataBean.getDefaultLotteryCode()) && chatSysConfig.getSwitch_lottery_result_show().equals("1")) {
                        // 有配置房间默认开奖彩种，顶部开奖viewpage自动切换至配置的彩种
                        getLotteryResult(dataBean.getDefaultLotteryCode());
                        break;
                    }
                }
            }
        }
        //获取权限
        presenter.getToolPermission(ChatSpUtils.instance(this).getUserId());

        roomBeanMsg = source;
        initDrawer(joinChatRoomBean.getSource().getUserType() == 4);
        presenter.getPersonData(roomBeanMsg.getRoomId(), false);
        ChatSpUtils.instance(this).setCURRENT_ROOM_ID(roomBeanMsg.getRoomId());
        accountAvatar = roomBeanMsg.getAvatar();
        accountNickName = roomBeanMsg.getNickName();
        accountName = roomBeanMsg.getAccount();
//        ivMoreMenu.setImageResource(R.mipmap.icon_setting);
        ivMoreMenu.setVisibility(View.VISIBLE);
        ChatRoomNoticeModel noticeModel = new ChatRoomNoticeModel(ConfigCons.CHAT_ROOM_NOTICE, ConfigCons.SOURCE, ChatSpUtils.instance(this).getStationId(), roomBeanMsg.getRoomId());
        presenter.getNotice(noticeModel);
        //获取在线用户
        GetOnlineJsonModel getOnlineJsonModel = new GetOnlineJsonModel();
        getOnlineJsonModel.setStationId(ChatSpUtils.instance(this).getStationId());
        getOnlineJsonModel.setRoomId(roomBeanMsg.getRoomId());
        getOnlineJsonModel.setUserId(ChatSpUtils.instance(this).getUserId());
        getOnlineJsonModel.setCode(ConfigCons.GET_ONLINE_USER);
        presenter.getOnlineUser(getOnlineJsonModel);
        getOnlineJsonModel.setCode(ConfigCons.GET_ONLINE_MANAGER);
        presenter.getOnlineUser(getOnlineJsonModel);
        //获取私聊列表
        getPrivateConversationList();
        tvMiddleTitle.setText(roomBeanMsg.getTitle());
        if (!TextUtils.isEmpty(source.getBackGround())) {
            // 设置墙纸
            GlideUtils.loadBackgroundImage(this, source.getBackGround(), llChatContent);
        } else {
            llChatContent.setBackground(null);
        }

        if (TextUtils.isEmpty(ChatSpUtils.instance(this).getSendExpression()) || !ChatSpUtils.instance(this).getSendExpression().equals("1")) {
            // 禁止发送表情
            ivEmo.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(ChatSpUtils.instance(this).getSendBetting()) || !ChatSpUtils.instance(this).getSendBetting().equals("1")) {
            // 禁止分享注单
            ibt_2.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(chatSysConfig.getSwitch_plan_list_show()) && "1".equals(chatSysConfig.getSwitch_plan_list_show())) {
            ibt_6.setVisibility(View.VISIBLE);
        } else {
            ibt_6.setVisibility(View.GONE);
        }

        if (chatSysConfig.getSwitch_room_show().equals("0")) {
            findViewById(R.id.title_indictor).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.title_indictor).setVisibility(View.VISIBLE);
        }

        if (chatSysConfig.getSwitch_front_bet_show().equals("0")) {
            findViewById(R.id.chat_ibt_1).setVisibility(View.GONE);
        }

        if (isFromNotifyPrivate) {
            isFromNotifyPrivate = false;
            //从通知栏进来的话
            presenter.joinPrivateRoom(privateRoomId, "join", ChatSpUtils.instance(ChatMainActivity.this).getUserId(), passiveUserId);
        }

    }

    @Override
    public void onSendSuccess(String msgUUID) {
        for (int i = mAdapter.getData().size() - 1; i >= 0; i--) {
            ChatMessage chatMsg = mAdapter.getData().get(i);
            if (chatMsg.getUuid() != null && chatMsg.getUuid().equals(msgUUID)) {
                chatMsg.setSentStatus(MsgSendStatus.SENT);
                updateMsg(chatMsg);
                break;
            }
        }
    }

    /**
     * 红包发送成功回调
     *
     * @param msgBean
     */
    @Override
    public void onSendRedPackage(SendMsgBean msgBean) {
        SendMsgBean.SourceBean source = msgBean.getSource();
//        SendMsgBean.SourceBean.MsgBean bean = source.getMsg();
        ChatMessage redPackageMsg = getRedPackageMsg(source.getMoney(), source.getCount() + ""
                , source.getRemark(), source.getPayId());
        redPackageMsg.setUuid(source.getMsgUUID());
        redPackageMsg.getBody().setMsgUUID(source.getMsgUUID());
        redPackageMsg.setSentStatus(MsgSendStatus.SENT);
        mAdapter.addData(redPackageMsg);
        rvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    /**
     * 抢红包接口成功
     *
     * @param packageBean
     */
    @Override
    public void onGrabRedPackage(GrabRedPackageBean packageBean) {
        if (resultDialog == null) {
            resultDialog = new RedPackageResultDialog(ChatMainActivity.this, false, false);
        }
        if (!resultDialog.isShowing()) {
            resultDialog.show();
        }
        if (packageBean.getSource().getNewPay() != null) {//自动抢红包机器人抢的
            resultDialog.setGrabMoney(packageBean.getSource().getNewPay().getMoney());
            presenter.getRedPackageDetail(packageBean.getSource().getNewPay().getSysPayId());
        } else {
            resultDialog.setGrabMoney(packageBean.getSource().getPickData().getNewPay().getMoney());
            presenter.getRedPackageDetail(packageBean.getSource().getPickData().getPayId());
        }
    }

    /**
     * 已领取过红包的回调
     *
     * @param payId
     */
    @Override
    public void onGrabed(String payId) {
        if (resultDialog == null) {
            resultDialog = new RedPackageResultDialog(ChatMainActivity.this, false, false);
        }
        if (!resultDialog.isShowing()) {
            resultDialog.show();
        }
        resultDialog.setGrabed();
        presenter.getRedPackageDetail(payId);
    }

    /**
     * 红包已经抢完了回调
     *
     * @param payId
     */
    @Override
    public void onGrabOut(String payId) {
        if (resultDialog == null) {
            resultDialog = new RedPackageResultDialog(ChatMainActivity.this, false, false);
        }
        if (!resultDialog.isShowing()) {
            resultDialog.show();
        }
        resultDialog.setGrabOut();
        presenter.getRedPackageDetail(payId);
    }

    /**
     * 获取红包详情回调
     *
     * @param detailBean
     */
    @Override
    public void onRedPackageDetail(RedPackageDetailBean detailBean) {
        if (resultDialog == null) {
            resultDialog = new RedPackageResultDialog(ChatMainActivity.this, false, false);
        }
        resultDialog.setRedPackageDetail(detailBean);
        if (!resultDialog.isShowing()) {
            resultDialog.show();
        }
    }

    @Override
    public void onSendFail() {
        for (ChatMessage chatMsg : chatMsgs) {
            if (chatMsg.getSentStatus() == MsgSendStatus.SENDING) {
                chatMsg.setSentStatus(MsgSendStatus.FAILED);
                updateMsg(chatMsg);
            }
        }
    }

    @Override
    public void onSendFailMsg(String msgUUID) {
        for (int i = mAdapter.getData().size() - 1; i >= 0; i--) {
            ChatMessage chatMsg = mAdapter.getData().get(i);
            if (chatMsg.getUuid() != null && chatMsg.getUuid().equals(msgUUID)) {
                chatMsg.setSentStatus(MsgSendStatus.FAILED);
                updateMsg(chatMsg);
                break;
            }
        }
    }

    @Override
    public void onReceiverMsg(final ChatSendMsg chatSendMsg, final String source, final boolean isPrivateMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                receiverMsg(chatSendMsg, source, isPrivateMsg);
            }
        });
    }


    @Override
    public void onShareData(ChatShareDataBean chatShareDataBean) {
        if (chatShareDataBean.isSuccess()) {
            for (int i = chatMsgs.size() - 1; i >= 0; i--) {
                ChatMessage chatMessage = chatMsgs.get(i);
                if (chatMessage.getSenderId().equals(mSenderId) && chatMessage.getMsgType().equals(MsgType.SHARE_DATA)
                        && !chatMessage.getSentStatus().equals(MsgSendStatus.SENT)) {
                    chatMessage.setSentStatus(MsgSendStatus.SENT);
                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyItemChanged(finalI);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onShareFail() {
        for (int i = chatMsgs.size() - 1; i >= 0; i--) {
            ChatMessage chatMessage = chatMsgs.get(i);
            if (chatMessage.getSenderId().equals(mSenderId) && chatMessage.getMsgType().equals(MsgType.SHARE_DATA)
                    && !chatMessage.getSentStatus().equals(MsgSendStatus.SENT)) {
                chatMessage.setSentStatus(MsgSendStatus.FAILED);
                final int finalI = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyItemChanged(finalI);
                    }
                });
            }
        }
    }

    /**
     * 文件上传失败回调，根据消息类型来刷新列表
     *
     * @param type
     */
    @Override
    public void onUploadFail(MsgType type) {
        switch (type) {
            case IMAGE:
                for (ChatMessage sendImageMessage : sendImageMessages) {
                    sendImageMessage.setSentStatus(MsgSendStatus.FAILED);
                    updateMsg(sendImageMessage);
                }
                break;
            case AUDIO:
                for (int i = mAdapter.getData().size() - 1; i >= 0; i--) {
                    ChatMessage chatMessage = mAdapter.getData().get(i);
                    if (chatMessage.getSenderId().equals(mSenderId) && chatMessage.getMsgType().equals(MsgType.AUDIO)
                            && chatMessage.getSentStatus().equals(MsgSendStatus.SENDING)) {
                        chatMessage.setSentStatus(MsgSendStatus.FAILED);
                    }
                }
                break;
        }
    }

    /**
     * 文件上传失败回调，根据消息唯一标识来刷新列表
     *
     * @param upLoadFileBean
     */
    @Override
    public void onUploadFail(final UpLoadFileBean upLoadFileBean, MsgType type) {
//        updateFileMsg(upLoadFileBean);
    }


    /**
     * 文件上传成功，然后给消息设置地址值并刷新列表
     *
     * @param upLoadFileBean
     */
    @Override
    public void onUploadSuccess(UpLoadFileBean upLoadFileBean, MsgType type) {
        updateFileMsg(upLoadFileBean, type);
    }


    @Override
    public void onForceOffline() {
        ChatSpUtils.instance(this).setCURRENT_ROOM_ID("");
        presenter.disconnectSocket();
        showToast("您被请出当前房间，请联系客服！");
        finish();
    }

    @Override
    public void onBanSpeak(final String speakingClose, final String userId, final boolean isDeleteMessage, final int i, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //禁言自己
                if (i == 17) {
                    //解除禁言
                    //禁言
                    banTalk = "1".equals(speakingClose);
                    if (banTalk) {
                        tvBanTalk.setVisibility(View.VISIBLE);
                    } else {
                        tvBanTalk.setVisibility(View.GONE);
                        if (!TextUtils.isEmpty(msg) && !roomNotSpeaking) {
                            showToast(msg);
                        }
                    }
                }
                onGetBanSuccessful(Integer.parseInt(speakingClose), userId, isDeleteMessage);
            }
        });
    }

    @Override
    public void applyFail(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        }
    }

    public void setBanText(String str) {
        tvNotAllowedTalk.setVisibility(View.VISIBLE);
        tvNotAllowedTalk.setText(str);
    }


    /**
     * 禁言成功
     *
     * @param
     * @param isDeleteMessage
     */
    @Override
    public void onGetBanSuccessful(int speakingClose, String userId, boolean isDeleteMessage) {
        List<ChatMessage> data = mAdapter.getData();

        Iterator<ChatMessage> iterator = data.iterator();
        while (iterator.hasNext()) {
            ChatMessage next = iterator.next();
            boolean equals = next.getBody().getUserId().equals(userId);
            if (!isDeleteMessage && equals) {
                next.getBody().setStopTalkType(speakingClose);
            }

            if (speakingClose == 1 && isDeleteMessage && equals) {
                iterator.remove();
            }
        }

        if (speakingClose == 1 && isDeleteMessage) {
            mAdapter.notifyDataSetChanged();

        }
    }


    @Override
    public void onBanRoomSpeak(boolean isBankSpeak) {
        roomNotSpeaking = isBankSpeak;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (roomNotSpeaking) {
                    if (tvNotAllowedTalk != null) {
                        tvNotAllowedTalk.setVisibility(View.VISIBLE);
                        setBanText("房间全体禁言");
                    }
                } else {
                    if (tvNotAllowedTalk != null) {
                        tvNotAllowedTalk.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public void onMessageSendSelf(String msgId, String msgUUID) {
        for (ChatMessage datum : mAdapter.getData()) {
            if (datum.getUuid().equals(msgUUID)) {
                datum.getBody().setMsgId(msgId);
                datum.setMsgId(msgId);
            }
        }
    }

    @Override
    public void onMessageRecall(String msgId) {
        List<ChatMessage> data = mAdapter.getData();
        for (int i = data.size() - 1; i >= 0; i--) {
            if (data.get(i).getBody().getMsgId() == null && data.get(i).getMsgId() == null) {
                continue;
            }
            String msgId1 = data.get(i).getBody().getMsgId() == null ? data.get(i).getMsgId() : data.get(i).getBody().getMsgId();
            if (msgId1.equals(msgId)) {
                chatMsgs.remove(i);
                final int finalI = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyItemRemoved(finalI);
                    }
                });
                break;
            }
        }
    }

    private List<String> toolPermissionList;

    @Override
    public void onGetToolPermissionSuccess(List<ChatToolPermissionBean.SourceBean.ToolPermissionBean> toolPermission) {
        if (toolPermission != null) {
            toolPermissionList = new LinkedList<>();
            for (ChatToolPermissionBean.SourceBean.ToolPermissionBean toolPermissionBean : toolPermission) {
                if (!TextUtils.isEmpty(toolPermissionBean.getToolCode())) {
                    toolPermissionList.add(toolPermissionBean.getToolCode());
                }
            }
        }
    }

    @Override
    public void onGetLongDragon(List<ChatLongDragonBean.SourceBean.LongDragonBean> longDragon) {
        if (dragonListWindow == null) {
            dragonListWindow = new ChatLongDragonListWindow(ChatMainActivity.this, false, false, longDragon);
        } else {
            dragonListWindow.update(longDragon);
        }
        dragonListWindow.show();
    }

    @Override
    public void onGetAuditList(List<GetAuditListBean.UserAuditItem> auditItems) {
        Log.d("ChatMainActivity", "onGetAuditList()");
        if (auditListDialog == null) {
            auditListDialog = new AuditListDialog(ChatMainActivity.this, auditItems, new AuditListAdapter.AuditActionListener() {
                @Override
                public void onAuditAction(String auditUserID, int action) {
                    Log.d("ChatMainActivity", "onAuditAction()");
                    switch (action) {
                        case AuditListAdapter.AUDIT_ACTION_AGREE:
                            presenter.postAuditAction(auditUserID, 2);
                            break;
                        case AuditListAdapter.AUDIT_ACTION_REFUSE:
                            presenter.postAuditAction(auditUserID, 3);
                            break;
                        case AuditListAdapter.AUDIT_ACTION_DELETE:
                            presenter.postAuditAction(auditUserID, 0);
                            break;
                    }
                }
            });
            auditListDialog.show();
        } else {
            if (!auditListDialog.isShowing()) {
                auditListDialog.show();
            }
            auditListDialog.setAuditItems(auditItems);
        }
    }

    @Override
    public void onGetAuditListNull() {
        if (auditListDialog != null && auditListDialog.isShowing()) {
            auditListDialog.showEmpty();
        }else {
            showToast("暂无待审核人员！");
        }
    }

    @Override
    public void onSign(ChatSignDataBean bean) {
        if (bean.getSource().isSuccess()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("连续签到" + bean.getSource().getDays() + "天,签到积分" + bean.getSource().getScore());
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else {
            showToast(TextUtils.isEmpty(bean.getSource().getErroMsg()) ? "签到失败" : bean.getSource().getErroMsg());
        }
    }

    @Override
    public void onPingEvent() {

    }

    @Override
    public void onGetNotice(ChatRoomNoticeBean bean) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bean.getSource().size(); i++) {
            sb.append(bean.getSource().get(i).getBody());
        }

        if(TextUtils.isEmpty(sb.toString())){
            tagNotice.setVisibility(View.GONE);
            noticeText.setVisibility(View.GONE);
        }else {
            tagNotice.setVisibility(View.VISIBLE);
            noticeText.setVisibility(View.VISIBLE);
            String html = "<html><body>" + sb.toString() + "</body></html>";
            String htmlStr = Html.fromHtml(html).toString();
            noticeText.setText(htmlStr);
        }
    }

    @Override
    public void onGetLotteryList(ChatLotteryBean chatLotteryBean) {
        lotteryResultData.addAll(chatLotteryBean.getSource().getLotteryData());
        getLotteryResult("");
    }

    /**
     * 获取开奖结果
     *
     * @param
     * @param roomLotteryCode
     */
    private void getLotteryResult(String roomLotteryCode) {
        ChatLotteryBean.SourceBean.LotteryDataBean bean = null;
        if (TextUtils.isEmpty(roomLotteryCode)) {
            // 房间没有单独配置默认开奖彩种，就用站点配置里面的统一默认开奖彩种
            roomLotteryCode = chatSysConfig.getSwitch_lottery_result_default_type_show();
        }
        if (chatSysConfig.getSwitch_lottery_result_show().equals("1")) {
            if (!TextUtils.isEmpty(roomLotteryCode)) { //获取默认彩种
                for (int i = 0; i < lotteryResultData.size(); i++) {
                    if (roomLotteryCode.equals(lotteryResultData.get(i).getCode())) {
                        bean = lotteryResultData.get(i);
                        lotteryResultData.remove(i);
                        break;
                    }
                }
            }
        }

        if (lotteryResultData.size() > 0) {
            if (bean != null) {//默认彩种在第一位
                lotteryResultData.add(0, bean);
            }
            chatLotteryResultAdapter.notifyDataSetChanged();
            chatRcResult.setCurrentItem(lotteryResultData.size() * 5); //增加倍可以左右滑动
            lotteryPos = 0;
            presenter.getKaiJianResult(lotteryResultData.get(0).getCode());
        }
    }

    @Override
    public void onGetLotteryDetail(LotteryDownBean chatLotteryDetailBean) {
        if (intervalRequestOpenResultTimer == null) {
            createRequestLastResultTimer(ConfigCons.INTERVAL_REQUEST_OPENRESULT_DURATION);
            intervalRequestOpenResultTimer.start();
        }

        if (chatLotteryDetailBean != null && chatLotteryDetailBean.getLast() != null) {
            setQihao(chatLotteryDetailBean);
//            if (lotteryResultData.get(lotteryPos).getCode().equals(chatLotteryDetailBean.getContent().getLotCode())) {
//                if (TextUtils.isEmpty(lotteryResultData.get(lotteryPos).getHaoMa())) {
//                    setQihao(chatLotteryDetailBean);
//                } else {
//                    if (!lotteryResultData.get(lotteryPos).getHaoMa().equals(chatLotteryDetailBean.getContent().getHaoMa())) {
//                        setQihao(chatLotteryDetailBean);
//                    }
//                }
//            }
        }
    }


    // 历史消息
    @Override
    public void onGetHistoryMessage(ChatHistoryMessageBean chatHistoryMessageBean) {
        swipeChat.setRefreshing(false);
        List<ChatHistoryMessageBean.SourceBean> source = chatHistoryMessageBean.getSource();
        if (source.isEmpty())
            return;

        tempMessages.clear();
        for (int i = 0; i < source.size(); i++) {
            handleHistoryMsg(source.get(i));
        }
        boolean scrollToBottom = chatMsgs.isEmpty();
        chatMsgs.addAll(0, tempMessages);
        mAdapter.notifyDataSetChanged();
        mAdapter.isHistoryMsg(true);
        presenter.loadImageList(mAdapter.getData());
        //mLinearLayout.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, Integer.MIN_VALUE);
        if (scrollToBottom) {
            rvChatList.scrollToPosition(chatMsgs.size()-1);
        }else {
            rvChatList.scrollToPosition(historyPageCount);
        }
//        presenter.connectSocket(roomBeanMsg.getRoomId());
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mAdapter.isHistoryMsg(false); //10秒之后不再自动滑动
//            }
//        }, 10000);
    }

    /**
     * 获取导师计划消息列表
     *
     * @param masterPlans
     */
    @Override
    public void onGetMasterPlan(ChatHistoryMessageBean masterPlans) {
        Intent intent = new Intent(ChatMainActivity.this, MasterPlanListActivity.class);
        intent.putExtra("master_plan", masterPlans);
        startActivity(intent);
    }

    @Override
    public void onGetHistoryFail() {
        swipeChat.setRefreshing(false);
//        presenter.connectSocket(roomBeanMsg.getRoomId());
    }

    @Override
    public void onLoadingImg(int precent, String uuid) {
        for (int pos = chatMsgs.size() - 1; pos >= 0; pos--) {
            if (chatMsgs.get(pos).getBody().getMsgUUID().equals(uuid)) {
                ((ImageMsgBody) chatMsgs.get(pos).getBody()).setPercent(precent);
                // mAdapter.notifyItemChanged(pos);
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void onGetViolateWords(List<String> vWordsList) {
        violateWordsList = vWordsList;
    }

    @Override
    public void showLotteryHistoryResults(LotteryHistoryResultResponse response, String cpBianma, String cpType) {
        Utils.logd(TAG, "in showLotteryHistoryResults, results.size = " + response.getContent().size());
        lotteryHistoryResultWindow = new LotteryHistoryResultWindow(this);
        int offsetY = llParent.getTop() + llMarquee.getHeight() + chatRcResult.getHeight();
        lotteryHistoryResultWindow.showHistoryResults(response, cpBianma, cpType);
        lotteryHistoryResultWindow.showWindow(llParent, offsetY);
    }

    @Override
    public void onGetTodayProfitList(List<TodayProfitResponse.Prize> prizeList) {
        TodayProfitDialog dialog = new TodayProfitDialog(this, prizeList);
        dialog.show();
    }

    private void setQihao(LotteryDownBean chatLotteryDetailBean) { //设置期号
        lotteryResultData.get(lotteryPos).setHaoMa(chatLotteryDetailBean.getLast().getHaoMa());
        lotteryResultData.get(lotteryPos).setQiHao(chatLotteryDetailBean.getLast().getQiHao());
//        lotteryResultData.get(lotteryPos).setData(chatLotteryDetailBean.getContent().getOpenStatus());
        chatLotteryResultAdapter.notifyDataSetChanged();
//        cancelTimer();
//        createRequestLastResultTimer(ConfigCons.INTERVAL_REQUEST_OPENRESULT_DURATION);
//        intervalRequestOpenResultTimer.start();
    }


    /**
     * 创建查询开奖结果倒计时(在开盘时间到时，获取一次开奖结果获取不到新的开奖结果时使用)
     *
     * @param duration
     */
    private void createRequestLastResultTimer(final long duration) {
        intervalRequestOpenResultTimer = new CountDownTimer(duration, 10000) {
            public void onTick(long millisUntilFin) {
                if (presenter != null)
                    presenter.getKaiJianResult(lotteryResultData.get(lotteryPos).getCode());
            }

            public void onFinish() {
                cancelTimer();
                createRequestLastResultTimer(ConfigCons.INTERVAL_REQUEST_OPENRESULT_DURATION);
                intervalRequestOpenResultTimer.start();
            }
        };
    }

    private void cancelTimer() {
        if (intervalRequestOpenResultTimer != null) {
            intervalRequestOpenResultTimer.cancel();
            intervalRequestOpenResultTimer = null;
        }
    }

    /**
     * 设置中奖进入动画
     *
     * @param i
     */
    private void setTransition(final int i) {
        String name_win_lottery_animation_interval = chatSysConfig.getName_win_lottery_animation_interval();
        final long duration;
        if (TextUtils.isEmpty(name_win_lottery_animation_interval)) {
            duration = 2000;
        } else {
            duration = Long.parseLong(name_win_lottery_animation_interval) * 1000;
        }
        if (!showWinningView) {
            if (isAnimating) {
                setTransitionOut(animaView);
            }
            return;
        }
        if (isAnimating) {
            setTransitionOut(animaView);
        }
        if (views == null || i == views.size() || views.isEmpty()) {
            return;
        }
        animaView = views.get(i);
        viewTransitionIn.setDuration(800);
        viewTransitionIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animaView.setVisibility(View.VISIBLE);
                startFireAnima(duration);
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (views == null)
                            return;
                        if (i + 1 == views.size()) {
                            isAnimating = false;
                            setTransitionOut(animaView);
                        } else {
                            setTransition(i + 1);
                        }
                    }
                }, duration);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animaView.startAnimation(viewTransitionIn);
    }

    /**
     * 设置中奖退出动画
     *
     * @param view
     */
    private void setTransitionOut(final View view) {
        viewTransitionOut.setDuration(800);
        viewTransitionOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (flContent == null)
                            return;
                        flContent.removeView(view);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(viewTransitionOut);
    }

    /**
     * 设置进入房间动画
     */
    private void setWelcomeTransition(Animation welcomeTxtAnimation, final View view, final String uuid) {

        welcomeTxtAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (welcomeLayout == null)
                            return;
                        LogUtils.e("进房提示", "清除view");
                        view.clearAnimation();
                        welcomeLayout.removeView(view);
                        clearWelcomtxt(uuid);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(welcomeTxtAnimation);
    }

    public synchronized String addWelcomtxt(View view) {
        WelcomeTextBean bean = new WelcomeTextBean(UUID.randomUUID().toString(), view);
        welcomeViews.add(bean);
        return bean.getUuid();
    }

    public synchronized void clearWelcomtxt(String uuid) {
        for (int i = 0; i < welcomeViews.size(); i++) {
            if (uuid.equals(welcomeViews.get(i).getUuid())) {
                welcomeViews.remove(i);
                break;
            }
        }
    }

    /**
     * 创建中奖横幅View
     *
     * @param type
     * @param name
     * @param account
     * @return
     */
    private View creatView(String type, String name, String account) {
        SpannableString text = new SpannableString("恭喜" + name + "在" + type + "玩法中奖" + account + "元");
        text.setSpan(new ForegroundColorSpan(Color.GREEN), 2, 2 + name.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        text.setSpan(new ForegroundColorSpan(Color.YELLOW), 7 + name.length() + type.length(), text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        View view = View.inflate(this, R.layout.winning_notice, null);
        view.setVisibility(View.GONE);
        view.measure(0, 0);
        TextView tv = (TextView) view.findViewById(R.id.tv_winning);
        tv.setText(text);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_winning_close);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commonDialog == null) {
                    commonDialog = CommonDialog.create(ChatMainActivity.this, "", "关闭中奖消息提示将不再提示中奖，是否关闭？", "确定",
                            new CommonDialog.DialogClickListener() {
                                @Override
                                public void onInputListener(View view, String input) {
                                }

                                @Override
                                public void onClick(View v) {
                                    showWinningView = false;
                                }

                            }, "取消", new CommonDialog.DialogClickListener() {
                                @Override
                                public void onInputListener(View view, String input) {
                                }

                                @Override
                                public void onClick(View v) {
                                    if (commonDialog != null) {
                                        commonDialog.dismiss();
                                    }
                                }
                            }, false, false, false);
                }
                commonDialog.show();
            }
        });
        return view;
    }


    private View createWelcomeView(String txt) {
        View view = View.inflate(this, R.layout.item_welcome_text, null);
        TextView tv = view.findViewById(R.id.item_welcome_txt);
        String html = "欢迎<font color='#854AEA'><big>" + txt + "</big></font>进入房间";
        tv.setText(Html.fromHtml(html));
        return view;
    }

    /**
     * 绑定服务
     */
//    private void bindService() {
//        Intent bindIntent = new Intent(this, JWebSocketClientService.class);
//        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
//    }

    /**
     * 启动服务（websocket客户端服务）
     */
    private void startJWebSClientService() {
        Intent intent = new Intent(this, JWebSocketClientService.class);
        startService(intent);
    }

    /**
     * 动态注册广播
     */
    private void doRegisterReceiver() {
        chatMessageReceiver = new ChatMessageReceiver();
        IntentFilter filter = new IntentFilter(ConfigCons.MSG_RECEIVER_ACTION);
        registerReceiver(chatMessageReceiver, filter);
    }

    /**
     * 检测是否开启通知
     *
     * @param context
     */
    private void checkNotification(final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = ConfigCons.CHANNEL_ID;
            String channelName = ConfigCons.CHANNEL_NAME;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
        }

        if (!CommonUtils.isNotificationEnabled(context)) {
            new AlertDialog.Builder(context).setTitle("温馨提示")
                    .setMessage("你还未开启系统通知或聊天消息通知，将影响消息的接收，要去开启吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CommonUtils.setNotification(context);
                            dialog.dismiss();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    protected void initView() {
        super.initView();

        secondTitle = findViewById(R.id.second_title);
        topInfoTitle = findViewById(R.id.top_info_title);
        topInfoContent = findViewById(R.id.top_info_content);
        llParent = findViewById(R.id.ll_parent);
        llMarquee = findViewById(R.id.chat_tip_info);
        llTopInfo = findViewById(R.id.ll_top_info);
        topInfoClose = findViewById(R.id.top_info_close);
        chatRcResult = findViewById(R.id.chat_vp_result);
        imageMore = findViewById(R.id.imageMore);
        rvChatList = findViewById(R.id.rv_chat_list);
        swipeChat = findViewById(R.id.swipe_chat);
        ivAudio = findViewById(R.id.ivAudio);
        etContent = findViewById(R.id.et_content);
        btnAudio = findViewById(R.id.btnAudio);
        ivEmo = findViewById(R.id.ivEmo);
        ivAdd = findViewById(R.id.ivAdd);
        btnSend = findViewById(R.id.btn_send);
        llContent = findViewById(R.id.llContent);
        vpEmoji = findViewById(R.id.vp_emoji);
        indEmoji = findViewById(R.id.ind_emoji);
        homeEmoji = findViewById(R.id.home_emoji);
        ivPhoto = findViewById(R.id.ivPhoto);
        ivVideo = findViewById(R.id.ivVideo);
        ivFile = findViewById(R.id.ivFile);
        ivLocation = findViewById(R.id.ivLocation);
        bottomLayout = findViewById(R.id.bottom_layout);
        llEmotion = findViewById(R.id.rlEmotion);
        mLlAdd = findViewById(R.id.llAdd);
        float_ball = findViewById(R.id.float_ball);
        rec_float = findViewById(R.id.rec_float);
        fr_float = findViewById(R.id.fr_float);
        tvNewMsgTip = findViewById(R.id.tv_new_msg_tip);
        tvNotAllowedTalk = findViewById(R.id.tv_not_allowed_talk);
        fireView = findViewById(R.id.fv);
        flContent = findViewById(R.id.fl_content);
        llChatContent = findViewById(R.id.ll_chat_content);
        ibt_1 = findViewById(R.id.chat_ibt_1);
        ibt_2 = findViewById(R.id.chat_ibt_2);
        ibt_3 = findViewById(R.id.chat_ibt_3);
        ibt_4 = findViewById(R.id.chat_ibt_4);
        ibt_5 = findViewById(R.id.chat_ibt_5);
        ibt_6 = findViewById(R.id.chat_ibt_6);
        tagNotice = findViewById(R.id.tagNotice);
        noticeText = findViewById(R.id.notice_tip);
        rlQuickMessage = findViewById(R.id.rlQuickMessage);
        rlSave = findViewById(R.id.rlSave);
        rlPhoto = findViewById(R.id.rlPhoto);
        rlRedPackage = findViewById(R.id.rlRedPackage);
        welcomeLayout = findViewById(R.id.act_chat_main_welcome_layout);
        drawerLayout = findViewById(R.id.chat_draw_layout);
        draw_right = findViewById(R.id.draw_right);
        iv_header = findViewById(R.id.chat_draw_header);
        tv_jryk = findViewById(R.id.chat_draw_jryk);
        tv_jrzj = findViewById(R.id.chat_draw_jrzj);
        tv_jrcz = findViewById(R.id.chat_draw_jrcz);
        tv_ljcz = findViewById(R.id.chat_draw_ljcz);
        tv_ztz = findViewById(R.id.chat_draw_ztz);
        tv_dml = findViewById(R.id.chat_draw_dml);
        ll_jryk = findViewById(R.id.ll_jryk);
        ll_dml = findViewById(R.id.ll_dml);
        ll_jrcz = findViewById(R.id.ll_jrcz);
        ll_ljcz = findViewById(R.id.ll_ljcz);
        btn_refresh = findViewById(R.id.chat_draw_refresh);
        btn_share = findViewById(R.id.chat_draw_share);
        elv_draw = findViewById(R.id.chat_draw_elv);
        chat_draw_level_name = findViewById(R.id.chat_draw_name);
        chat_draw_level_icon = findViewById(R.id.chat_draw_level_icon);
        chat_draw_money = findViewById(R.id.chat_draw_left_money);
        chat_draw_iv_money = findViewById(R.id.chat_draw_refresh_money);
        chat_draw_account_name = findViewById(R.id.chat_draw_account_name);
        tvBanTalk = findViewById(R.id.tv_ban_talk);


        //检测通知是否开启
        checkNotification(this);
        //启动服务
//        startJWebSClientService();
//        //绑定服务
//        bindService();
//        //注册广播
//        doRegisterReceiver();
        initContent();
    }

    private void initDrawer(boolean isFrontManager) {
        drawerDatas.clear();
        managerGroup = new GroupBean();
        managerGroup.setIcon(R.drawable.icon_manager);
        managerGroup.setLayoutType(0);
        managerGroup.setName("管理员列表");
        drawerDatas.add(managerGroup);
        if (chatSysConfig.getSwitch_room_people_admin_show().equals("1") && (!"2".equals(ChatSpUtils.instance(this).getUSER_TYPE())
                && !"4".equals(ChatSpUtils.instance(this).getUSER_TYPE()))) {
        } else {
            onlineGroup = new GroupBean();
            onlineGroup.setIcon(R.drawable.icon_online_2);
            onlineGroup.setName("在线用户");
            onlineGroup.setLayoutType(1);
            drawerDatas.add(onlineGroup);
        }

        List<String> toolPermission = roomBeanMsg.getToolPermission();
        if (toolPermissionList != null) {
            toolPermission = toolPermissionList;
        }
        if (toolPermission != null && toolPermission.contains(UserToolConstant.PRIVATE_CHAT)) {
            hasPrivatePermission = true;
        } else {
            hasPrivatePermission = false;
        }

        privateGroup = new GroupBean();
        privateGroup.setIcon(R.drawable.icon_private_conversation);
        privateGroup.setName("私聊列表");
        privateGroup.setLayoutType(2);
        drawerDatas.add(privateGroup);

        chatDrawerAdapter = new ChatDrawerAdapter(this, drawerDatas,
                roomBeanMsg.getUserType() == 4, hasPrivatePermission);
        chatDrawerAdapter.setOnLoadMoreClickListener(new ChatDrawerAdapter.OnLoadMoreClickListener() {
            @Override
            public void onLoadMoreClick(int group, int status) {
                if (!inRoom) {
                    showToast(R.string.please_into_room_first);
                    return;
                }
                if (status == 1) {
                    int currentNum = source.size();
                    if (totalPeople > currentNum) {
                        List<ChatUserListBean.ChatUserBean> chatUserBeans = loadMorePeoples(totalPeople - currentNum >= 10 ? 10 : totalPeople - currentNum);
                        source.addAll(chatUserBeans);
                        chatDrawerAdapter.notifyDataSetChanged();
                    } else {
                        showToast("没有更多数据了！");
                    }
                } else {
                    onlineGroup.setStatus(1);
                    onlineGroup.setChildBeans(source);
                    chatDrawerAdapter.notifyDataSetChanged();
                }

            }
        });
        draw_right.setOnClickListener(this);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                isRefresh = false;
                presenter.getPersonData(roomBeanMsg.getRoomId(), false);
                if (photoData.isEmpty()) {
                    presenter.getPhotoList(ChatSpUtils.instance(ChatMainActivity.this).getStationId(), ChatSpUtils.instance(ChatMainActivity.this).getUserId());
                }
                presenter.getCharge();
                //获取在线用户
                GetOnlineJsonModel getOnlineJsonModel = new GetOnlineJsonModel();
                getOnlineJsonModel.setStationId(ChatSpUtils.instance(ChatMainActivity.this).getStationId());
                getOnlineJsonModel.setRoomId(roomBeanMsg.getRoomId());
                getOnlineJsonModel.setUserId(ChatSpUtils.instance(ChatMainActivity.this).getUserId());
                getOnlineJsonModel.setCode(ConfigCons.GET_ONLINE_USER);
                presenter.getOnlineUser(getOnlineJsonModel);
                getOnlineJsonModel.setCode(ConfigCons.GET_ONLINE_MANAGER);
                presenter.getOnlineUser(getOnlineJsonModel);
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
        elv_draw.setAdapter(chatDrawerAdapter);
        elv_draw.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                showToast("点击第" + groupPosition + "组");
                return false;
            }
        });
        elv_draw.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                showToast("点击第" + groupPosition + "组，第" + childPosition + "条");
                return false;
            }
        });
        elv_draw.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < chatDrawerAdapter.getGroupCount(); i++) {
                    if (i != groupPosition) {
                        elv_draw.collapseGroup(i);
                    }
                }
            }
        });

        //私聊
        chatDrawerAdapter.setOnChatClickListener(new ChatDrawerAdapter.OnChatClickListener() {
            /**
             * @param chatUserBean 所选择要私聊的用户类型
             * @param isPrivate  是否是从私聊列表中进入
             */
            @Override
            public void onChatClick(ChatUserListBean.ChatUserBean chatUserBean, boolean isPrivate) {
                if (chatUserBean.getRoomIdX() != null) {
                    privateRoomId = chatUserBean.getRoomIdX();
                }
                if (isPrivate) {
                    passiveUserId = chatUserBean.getFromUser().getIdX();
                    passiveUserName = TextUtils.isEmpty(chatUserBean.getFromUser().getNickName()) ? chatUserBean.getFromUser().getAccount() : chatUserBean.getFromUser().getNickName();
                    //直接进入私聊
                    presenter.joinPrivateRoom(chatUserBean.getRoomIdX(), "join", ChatSpUtils.instance(ChatMainActivity.this).getUserId(), passiveUserId);
                } else {
                    passiveUserId = chatUserBean.getId();
                    passiveUserName = TextUtils.isEmpty(chatUserBean.getNickName()) ? chatUserBean.getAccount() : chatUserBean.getNickName();
                    //初始化私聊列表
                    initPrivateConversationList(chatUserBean.getUserType());
                }
            }

            @Override
            public void onRemarkClick(final ChatUserListBean.ChatUserBean chatUserBean) {
                RemarkDialog dialog = new RemarkDialog(ChatMainActivity.this, new RemarkDialog.RemarkListener() {
                    @Override
                    public void onGetRemark(String remark) {
                        String userId = ChatSpUtils.instance(ChatMainActivity.this).getUserId();
                        String stationId = ChatSpUtils.instance(ChatMainActivity.this).getStationId();
                        presenter.postPrivateChatRemark(userId, chatUserBean.getFromUser().getIdX(), remark, stationId);
                    }
                });
                dialog.show();
            }
        });

        // 搜索在线用户
        chatDrawerAdapter.setOnSearchListener(new ChatDrawerAdapter.OnSearchListener() {
            @Override
            public void onSearch(String content) {
                if (TextUtils.isEmpty(content)) {
                    showToast("请输入会员名字!");
                    return;
                }
                searchSource.clear();
                for (ChatUserListBean.ChatUserBean chatUserBean : source) {
                    String nickName = chatUserBean.getNickName();
                    String account = chatUserBean.getAccount();
                    if ((TextUtils.isEmpty(nickName) ? account : nickName).contains(content)) {
                        searchSource.add(chatUserBean);
                    }
                }
                if (searchSource.isEmpty())
                    showToast("没找到相关会员!");
                onlineGroup.setStatus(2);
                onlineGroup.setChildBeans(searchSource);
                chatDrawerAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 点击加载更多在线会员
     *
     * @param num 加载的数量
     * @return 数据集合
     */
    private List<ChatUserListBean.ChatUserBean> loadMorePeoples(int num) {
        List<ChatUserListBean.ChatUserBean> morePeoples = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            ChatUserListBean.ChatUserBean chatUserBean = new ChatUserListBean.ChatUserBean();
            chatUserBean.setNickName(CommonUtils.getRandomString(new Random().nextInt(4) + 4));
            if (photoData.isEmpty()) {
                chatUserBean.setAvatar(i + "");
            } else {
                int anInt = new Random().nextInt(photoData.size() - 1);
                chatUserBean.setAvatar(photoData.get(anInt));
            }
            morePeoples.add(chatUserBean);
        }
        return morePeoples;
    }

    private void initContent() {
        EventBus.getDefault().post(new RefreshFloatMenuEvent(false));
//        tvMiddleTitle.setText("普通房");
        titleIndictor.setVisibility(View.VISIBLE);
        mAdapter = new ChatAdapter(this, chatMsgs);
        LinearLayoutManager mLinearLayout = new LinearLayoutManager(this, LinearLayout.VERTICAL, false) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller linearSmoothScroller =
                        new LinearSmoothScroller(recyclerView.getContext()) {
                            @Override
                            protected int calculateTimeForScrolling(int dx) {
                                // 此函数计算滚动dx的距离需要多久，当要滚动的距离很大时，比如说52000，
                                // 经测试，系统会多次调用此函数，每10000距离调一次，所以总的滚动时间
                                // 是多次调用此函数返回的时间的和，所以修改每次调用该函数时返回的时间的
                                // 大小就可以影响滚动需要的总时间，可以直接修改些函数的返回值，也可以修改
                                // dx的值，这里暂定使用后者.
                                // (See LinearSmoothScroller.TARGET_SEEK_SCROLL_DISTANCE_PX)
                                if (dx > 3000) {
                                    dx = 3000;
                                }
                                return super.calculateTimeForScrolling(dx);
                            }
                        };
                linearSmoothScroller.setTargetPosition(position);
                startSmoothScroll(linearSmoothScroller);
            }
        };
        //mLinearLayout.setStackFromEnd(true);
        swipeChat.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //MsgType.MSG_TIME的消息是本地端自建的，不能算进去start里
                //不过因为init时我们从start＝1开始取消息，所以要做修正
                int count = countOfTimeType(chatMsgs);
                ChatMessageHistoryModel chatMessageHistoryModel = new ChatMessageHistoryModel(ConfigCons.HISTORY_MESSAGE, ConfigCons.SOURCE,
                        roomBeanMsg.getRoomId(), chatMsgs.size()-count+1, historyPageCount);
                presenter.getHistoryMessage(chatMessageHistoryModel);
            }
        });
        rvChatList.setLayoutManager(mLinearLayout);
        rvChatList.setAdapter(mAdapter);
        rvChatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    //判断是当前layoutManager是否为LinearLayoutManager
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
//                    if (layoutManager instanceof LinearLayoutManager) {
//
//                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
//                        //获取最后一个可见view的位置
//                        int lastItemPosition = linearManager.findLastCompletelyVisibleItemPosition();
//                        if (lastItemPosition == chatMsgs.size() - 1) {
//                            // 滑动到底部了，最后一个完成可见的item是最后一条消息，隐藏消息提示
//                            newMsgCount = 0;
//                            isShowNewMsgTip = false;
//                            mUiHelper.hideNewMsgTip();
//                        } else {
//                            isShowNewMsgTip = true;
//                        }
//                    }

                    //得到当前显示的最后一个item的view
                    View lastChildView = layoutManager.getChildAt(layoutManager.getChildCount() - 1);
                    //得到lastChildView的bottom坐标值
                    if (lastChildView != null) {
                        int lastChildBottom = lastChildView.getBottom();
                        //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                        int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
                        //通过这个lastChildView得到这个view当前的position值
                        int lastPosition = layoutManager.getPosition(lastChildView);
                        //判断lastChildView的bottom值跟recyclerBottom
                        //判断lastPosition是不是最后一个position
                        //如果两个条件都满足则说明是真正的滑动到了底部
                        if (lastChildBottom == recyclerBottom && lastPosition == layoutManager.getItemCount() - 1) {
                            // 滑动到底部了，最后一个完成可见的item是最后一条消息，隐藏消息提示
                            newMsgCount = 0;
                            isShowNewMsgTip = false;
                            mUiHelper.hideNewMsgTip();
                        } else {
                            isShowNewMsgTip = true;
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        initChatUi();
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final ChatMessage chatMessage = chatMsgs.get(position);
                if (view.getId() == R.id.ll_msg_title) {
                    if (("2".equals(ChatSpUtils.instance(ChatMainActivity.this).getUSER_TYPE())
                            || "4".equals(ChatSpUtils.instance(ChatMainActivity.this).getUSER_TYPE()))) {
                        chatMessage.setShowAccount(!chatMessage.isShowAccount());
                        adapter.notifyItemChanged(position);
                    }
                    return;
                }
                if (chatMessage.getMsgType() == MsgType.IMAGE) {
                    Intent intent = new Intent(ChatMainActivity.this, ImageViewActivity.class);
                    intent.putStringArrayListExtra("images", imageList);
                    intent.putExtra("clickedIndex", imagePosition.get(position));
                    startActivity(intent);
                } else if (chatMessage.getMsgType() == MsgType.AUDIO) {
                    if (broadcasting) {
                        resetAudio(senderId);
                    } else {
                        broadcasting = true;
                        senderId = chatMessage.getSenderId();
                        msg_iv_audio = view.findViewById(R.id.ivAudio);
                        MediaManager.reset();
                        if (senderId.equals(mSenderId)) {
                            msg_iv_audio.setBackgroundResource(R.drawable.audio_animation_right_list);
                        } else {
                            msg_iv_audio.setBackgroundResource(R.drawable.audio_animation_left_list);
                        }
                        AnimationDrawable drawable = (AnimationDrawable) msg_iv_audio.getBackground();
                        drawable.start();
                        AudioMsgBody body = (AudioMsgBody) mAdapter.getData().get(position).getBody();
                        if (body.getAudioMsg() != null) {
                            body = body.getAudioMsg();
                        }
                        MediaManager.playSound(ChatMainActivity.this, body.getLocalPath(), new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                LogUtils.d("onCompletion", "播放结束");
                                resetAudio(senderId);
                            }
                        }, new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                showToast("语音播放出错！");
                                resetAudio(senderId);
                                return false;
                            }
                        });
                    }
                } else if (chatMessage.getMsgType() == MsgType.VIDEO) {
                    // 如果有语音消息正在播放，先停止语音播放
                    if (broadcasting) {
                        resetAudio(senderId);
                    }
                    PictureSelector.create(ChatMainActivity.this).externalPictureVideo(((VideoMsgBody) chatMessage.getBody()).getLocalPath());
                } else if (chatMessage.getMsgType() == MsgType.FILE) {
                    // 如果有语音消息正在播放，先停止语音播放
                    if (broadcasting) {
                        resetAudio(senderId);
                    }
                    String localPath = ((FileMsgBody) chatMessage.getBody()).getLocalPath();
                    if (TextUtils.isEmpty(localPath)) {
                        showToast("文件不存在！");
                    } else {
                        showOpenMethod(localPath);
                    }
                } else if (chatMessage.getMsgType() == MsgType.SYS_PLAN) {
                    // TODO 暂时屏蔽计划跟投功能
//                    PlanMsgBody planMsgBody = (PlanMsgBody) chatMessage.getBody();
//                    if (betSlipDialog != null) {
//                        betSlipDialog.setBetinfo(planMsgBody.getBetInfo());
//                    } else {
//                        betSlipDialog = new BetSlipDialog(ChatMainActivity.this, planMsgBody.getBetInfo());
//                        betSlipDialog.show();
//                    }
                } else if (chatMessage.getMsgType() == MsgType.BET_SLIP) {
                    final BetSlipMsgBody betSlipMsgBody = (BetSlipMsgBody) chatMessage.getBody();
                    // 注单消息点击响应
                    final BetInfo betInfo;
                    List<BetInfo> betInfos = betSlipMsgBody.getBetInfos();
                    if (betInfos != null && betInfos.size() != 0) {
                        betInfo = betInfos.get(0);
                    } else {
                        betInfo = betSlipMsgBody.getBetInfo();
                    }
                    if (betInfo == null) {
                        showToast("无效注单！");
                        return;
                    }
                    if (betSlipDialog != null) {
                        betSlipDialog.setBetinfo(betInfo);
                    } else {
                        betSlipDialog = new BetSlipDialog(ChatMainActivity.this, betInfo);
                    }
                    betSlipDialog.setOnFollowBetListener(new BetSlipDialog.OnFollowBetListener() {
                        @Override
                        public void onFollowBet(String betMoney, String qihao, int model) {
                            String ipAddress = CommonUtils.getIPAddress(ChatMainActivity.this);
                            if (TextUtils.isEmpty(ipAddress)) {
                                showToast("IP地址获取失败，请检查网络！");
                                return;
                            }
                            // 跟单
                            FollowBetJsonModel followBetJsonModel = new FollowBetJsonModel();
                            followBetJsonModel.setMsgUUID(betSlipMsgBody.getMsgUUID());
                            followBetJsonModel.setUserId(ChatSpUtils.instance(ChatMainActivity.this).getUserId());
                            followBetJsonModel.setRoomId(betSlipMsgBody.getRoomId());
                            followBetJsonModel.setBetMoney(betMoney);
                            followBetJsonModel.setOrderId(betSlipMsgBody.getOrders().get(0).getOrderId());
                            followBetJsonModel.setCode(ConfigCons.FOLLOW_BET);
                            followBetJsonModel.setMsgId(betSlipMsgBody.getMsgId());
                            followBetJsonModel.setSource(ConfigCons.SOURCE);
                            followBetJsonModel.setQihao(qihao);
                            followBetJsonModel.setModel(model);
                            followBetJsonModel.setUserIp(ipAddress);
                            presenter.followBet(followBetJsonModel);
                        }
                    });
                    betSlipDialog.show();
                } else if (chatMessage.getMsgType() == MsgType.RED_PACKAGE) {
                    // 红包消息点击响应
                    RedPackageMsgBody redPackageMsgBody = (RedPackageMsgBody) chatMessage.getBody();
                    String userId = ChatSpUtils.instance(ChatMainActivity.this).getUserId();
                    GrabRedPackageJsonModel jsonModel = new GrabRedPackageJsonModel();
                    jsonModel.setMsgUUID(chatMessage.getUuid());
                    jsonModel.setCode(ConfigCons.GET_RED_PACKAGE);
                    jsonModel.setSource(ConfigCons.SOURCE);
                    jsonModel.setUserId(userId);
                    jsonModel.setPayId(redPackageMsgBody.getPayId());
                    if (roomBeanMsg != null) {
                        jsonModel.setRoomId(roomBeanMsg.getRoomId());
                        jsonModel.setAgentRoomHost(roomBeanMsg.getAgentRoomHost());
                    }
                    if (isPrivate) {
                        jsonModel.setRoomId(privateRoomId);
                    }
                    jsonModel.setAgentRoomHost(ChatSpUtils.instance(ChatMainActivity.this).getAgentRoomHost());
                    presenter.grabRedPackage(jsonModel);
                } else if (chatMessage.getMsgType() == MsgType.BET_SLIP_LIST) {
                    // 合并跟单点击响应
                    final BetSlipMsgBody betSlipMsgBody = (BetSlipMsgBody) chatMessage.getBody();
                    CommonDialog followBetDialog = CommonDialog.create(ChatMainActivity.this, "合并跟单", "请输入单注金额/倍数",
                            "跟单", new CommonDialog.DialogClickListener() {
                                @Override
                                public void onInputListener(View view, String input) {
                                    if (TextUtils.isEmpty(input)) {
                                        showToast("请输入单注金额/倍数");
                                        return;
                                    }
                                    String ipAddress = CommonUtils.getIPAddress(ChatMainActivity.this);
                                    if (TextUtils.isEmpty(ipAddress)) {
                                        showToast("IP地址获取失败，请检查网络！");
                                        return;
                                    }
                                    FollowBetJsonModel followBetJsonModel = new FollowBetJsonModel();
                                    followBetJsonModel.setMsgUUID(betSlipMsgBody.getMsgUUID());
                                    followBetJsonModel.setUserId(ChatSpUtils.instance(ChatMainActivity.this).getUserId());
                                    followBetJsonModel.setRoomId(betSlipMsgBody.getRoomId());
                                    followBetJsonModel.setBetMoney(input);
                                    StringBuffer orderIds = new StringBuffer();
                                    List<ShareOrderBean> orders = betSlipMsgBody.getOrders();
                                    for (int i = 0; i < orders.size(); i++) {
                                        if (i == 0) {
                                            orderIds.append(orders.get(i).getOrderId());
                                        } else {
                                            orderIds.append("," + orders.get(i).getOrderId());
                                        }
                                    }
                                    followBetJsonModel.setOrderIds(orderIds.toString());
                                    followBetJsonModel.setCode(ConfigCons.FOLLOW_BET);
                                    followBetJsonModel.setSource(ConfigCons.SOURCE);
                                    followBetJsonModel.setUserIp(ipAddress);
                                    presenter.followBet(followBetJsonModel);
                                }

                                @Override
                                public void onClick(View v) {

                                }
                            }
                            , "取消", new CommonDialog.DialogClickListener() {
                                @Override
                                public void onInputListener(View view, String input) {

                                }

                                @Override
                                public void onClick(View v) {

                                }
                            }, false, false, true, false, true, -1, true);
                    followBetDialog.showAndInput();
                }
            }
        });

        mAdapter.setSingleFollowBetListener(new ChatAdapter.SingleFollowBetListener() {
            @Override
            public void onSingleFollow(BetInfo betInfo, final String orderId, final String uuid, final String roomId) {
                // 注单消息单注跟单
                if (betInfo == null) {
                    showToast("无效注单！");
                    return;
                }
                if (betSlipDialog != null) {
                    betSlipDialog.setBetinfo(betInfo);
                } else {
                    betSlipDialog = new BetSlipDialog(ChatMainActivity.this, betInfo);
                }
                betSlipDialog.setOnFollowBetListener(new BetSlipDialog.OnFollowBetListener() {
                    @Override
                    public void onFollowBet(String betMoney, String qihao, int model) {
                        String ipAddress = CommonUtils.getIPAddress(ChatMainActivity.this);
                        if (TextUtils.isEmpty(ipAddress)) {
                            showToast("IP地址获取失败，请检查网络！");
                            return;
                        }
                        // 跟单
                        FollowBetJsonModel followBetJsonModel = new FollowBetJsonModel();
                        followBetJsonModel.setMsgUUID(uuid);
                        followBetJsonModel.setUserId(ChatSpUtils.instance(ChatMainActivity.this).getUserId());
                        followBetJsonModel.setRoomId(roomId);
                        followBetJsonModel.setBetMoney(betMoney);
                        followBetJsonModel.setOrderId(orderId);
                        followBetJsonModel.setCode(ConfigCons.FOLLOW_BET);
                        followBetJsonModel.setSource(ConfigCons.SOURCE);
                        followBetJsonModel.setQihao(qihao);
                        followBetJsonModel.setModel(model);
                        followBetJsonModel.setUserIp(ipAddress);
                        presenter.followBet(followBetJsonModel);
                    }
                });
                betSlipDialog.show();
            }
        });

        mAdapter.setOnImageLongClickListener(new ChatAdapter.OnImageLongClickListener() {
            @Override
            public void onItemLongClick(final ChatMessage item, int position) {
                String[] list = new String[]{"收藏"};
                final ListPopupWindow mListPop = showListPopWindow(list, position, R.id.bivPic, true);
                mListPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        //收藏
                        if (position == 0) {
                            ImageMsgBody imageMsgBody = (ImageMsgBody) item.getBody();
                            if (imageMsgBody.getPicMsg() != null) {
                                imageMsgBody = imageMsgBody.getPicMsg();
                            }
                            ChatCollectionImagesModel chatCollectionImagesModel = new ChatCollectionImagesModel();
                            chatCollectionImagesModel.setOption("2");
                            String record = imageMsgBody.getRecord();
                            if (record.contains("/")) {
                                String[] split = record.split("/");
                                record = split[split.length - 1];
                            }
                            chatCollectionImagesModel.setImageId(record);
                            chatCollectionImagesModel.setUserId(ChatSpUtils.instance(ChatMainActivity.this).getUserId());
                            presenter.saveImages(chatCollectionImagesModel);
                        }


                        mListPop.dismiss();
                    }
                });
                mListPop.show();
            }
        });

        mAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                final ChatMessage chatMessage = chatMsgs.get(position);
                if (chatMessage.getMsgType() == MsgType.TEXT) {
//                    CommonUtils.vibrate(ChatMainActivity.this, 100);
//                    final TextMsgBody textMsgBody = (TextMsgBody) chatMessage.getBody();
//                    // 手机震动提示
//                    dialog = new Dialog(ChatMainActivity.this, R.style.BetSlipDialog);
//                    View dialogView = View.inflate(ChatMainActivity.this, R.layout.dialog_text_msg_operate, null);
//                    dialog.setContentView(dialogView);
//                    dialogView.findViewById(R.id.tv_dialog_copy).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            CommonUtils.copy(ChatMainActivity.this, textMsgBody.getRecord());
//                            dialog.dismiss();
//                        }
//                    });
//                    dialog.show();

                    String[] strings = new String[]{"复制"};
                    final ListPopupWindow mListPop = showListPopWindow(strings, position, R.id.chat_item_content_text, true);
                    final TextMsgBody textMsgBody = (TextMsgBody) chatMessage.getBody();
                    mListPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            //复制
                            if (position == 0) {
                                CommonUtils.copy(ChatMainActivity.this, textMsgBody.getRecord());
                                ToastUtil.showToastCenter(ChatMainActivity.this, "已复制", 1000);
                            }
                            mListPop.dismiss();
                        }
                    });
                    mListPop.show();
                }
                return false;
            }
        });


        /**
         * 点击用户头像
         */
        mAdapter.setOnItemAitClickListener(new ChatAdapter.OnItemAitClickListener() {
            @Override
            public void onItemAitClick(final ChatMessage item, int position) {

                //计划消息和机器人文本消息
                if (item.getMsgType() == MsgType.HTML || item.getMsgType() == MsgType.SYS_PLAN) {
                    return;
                }

                boolean right = item.getSenderId().equals("right");
                List<String> toolPermission = roomBeanMsg.getToolPermission();
                if (toolPermissionList != null) {
                    toolPermission = toolPermissionList;
                }
                if (toolPermission == null) {
                    return;
                }
                if (toolPermission.contains(UserToolConstant.PRIVATE_CHAT)) {
                    hasPrivatePermission = true;
                }

                List<String> list = new ArrayList<>();
                if (!right) {
                    if (!isPrivate) {
                        list.add("@Ta");
                    }
                } else {
                    list.add("用户资料");
                }
                if (!isPrivate) {
                    list.add("清屏");
                }
                if (toolPermission.size() != 0 && !isPrivate) {
                    if (toolPermission.contains(UserToolConstant.BAN_SPEAK_USER) && !right) {
                        list.add("禁止此人发言");
                        list.add("解除此人禁言");
                    }
                    if (toolPermission.contains(UserToolConstant.BAN_SPEAK_ROOM) && !right) {
                        list.add("全体禁言");
                        list.add("解除全体禁言");
                    }
                    if (toolPermission.contains(UserToolConstant.RETRACT_MSG) && item.getMsgType() != MsgType.RED_PACKAGE) {
                        list.add("撤回");
                    }
//                    if (toolPermission.contains(UserToolConstant.PRIVATE_CHAT) && !right && item.isPrivatePermission()) {
//                        list.add("私聊");
//                    }
                }

                //非私聊房间，点击其他人头像
                if (!isPrivate && !right) {
                    if(item.getUserType() == 4){
                        //对方是管理员身份的话，以对方权限为主，对方有权限就能私聊，对方没有私聊权限即使自己有也不能私聊
                        if(item.isPrivatePermission()){
                            list.add("私聊");
                        }
                    }else if(roomBeanMsg.getUserType() == 4){
                        //本身是管理员的话，就看自己有没有权限可以私聊
                        if (toolPermission.contains(UserToolConstant.PRIVATE_CHAT)) {
                            list.add("私聊");
                        }
                    }else {
                        //两边都是一般会员的话，一方有权限就可以私聊
                        if (toolPermission.contains(UserToolConstant.PRIVATE_CHAT)
                                || item.isPrivatePermission()) {
                            list.add("私聊");
                        }
                    }
                }

                setHeaderClick(list, item, position, right);
            }
        });

        mAdapter.setOnRepeatSendListener(new ChatAdapter.OnRepeatSendClickListener() {
            @Override
            public void onRepeatSendListener(ChatMessage item) {
                // 禁言状态无法重发
                if (roomNotSpeaking || banTalk) {
                    showToast("禁言中，请联系客服！");
                    return;
                }
                //  发送失败点击重发功能
                switch (item.getMsgType()) {
                    case IMAGE:
                        presenter.upLoadFile(item, "jpeg", fileHandlerListener);
                        break;
                    case AUDIO:
                        presenter.upLoadFile(item, "amr", fileHandlerListener);
                        break;
                    default:
                        presenter.sentMsg(item, roomBeanMsg);
                        break;
                }
                item.setSentStatus(MsgSendStatus.SENDING);
                mAdapter.notifyDataSetChanged();
            }
        });

        mAdapter.setChatAdapterScrollInterFace(new ChatAdapter.ChatAdapterScrollInterFace() {//防止有图片加载导致下拉不到最底部
            @Override
            public void Scroll(final int pos) {
                LogUtils.e("聊天列表滑动", pos + "");
                //mLinearLayout.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, Integer.MIN_VALUE);
                if (pos == mAdapter.getData().size() - 1) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.e("聊天列表滑动", "最后一张" + pos);
                            rvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
                            mAdapter.isHistoryMsg(false);
                        }
                    }, 800);
                } else {
                    rvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
                }

            }
        });
    }

    private boolean hasPrivatePermission = false;


    //统一封装的下拉弹出窗
    private ListPopupWindow showListPopWindow(String[] strings, int position, int anchor, boolean isVibrate) {
        ListPopupWindow mListPop = new ListPopupWindow(ChatMainActivity.this);
        //手机振动提示
        if (isVibrate) {
            CommonUtils.vibrate(ChatMainActivity.this, 100);
        }
        mListPop.setAdapter(new ArrayAdapter<>(ChatMainActivity.this, android.R.layout.simple_list_item_1, strings));
        int maxWidth = 0;
        for (String str : strings) {
            TextView textView = (TextView) View.inflate(this, android.R.layout.simple_list_item_1, null);
            textView.setText(str);
            textView.measure(0, 0);
            maxWidth = maxWidth > textView.getMeasuredWidth() ? maxWidth : textView.getMeasuredWidth();
        }
        mListPop.setWidth(maxWidth);
        mListPop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        mListPop.setVerticalOffset(DisplayUtil.dip2px(ChatMainActivity.this, 5));
        mListPop.setAnchorView(mAdapter.getViewByPosition(rvChatList, position, anchor));
        mListPop.setModal(true);
        mListPop.setBackgroundDrawable(getResources().getDrawable(R.drawable.list_popwindow));
        return mListPop;
    }

    private void setHeaderClick(List<String> list, final ChatMessage item, int position, boolean right) {

        int anchor = R.id.chat_item_header;
        final ListPopupWindow listPopupWindow = showListPopWindow(list.toArray(new String[0]), position, anchor, false);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    String s = textView.getText().toString();
                    switch (s) {
                        case "用户资料":
                            Intent intent = new Intent(ChatMainActivity.this, PersonDataActivity.class);
                            intent.putExtra("photo", roomBeanMsg.getAvatar());
                            intent.putExtra("roomId", roomBeanMsg.getRoomId());
                            startActivityForResult(intent, REQUEST_CODE_PERSON_DATA);
                            break;
                        case "@Ta":
                            //禁言状态无法@
                            if (roomNotSpeaking || banTalk) {
                                return;
                            }
                            // 系统计划消息不给@
                            if (item.getBody() instanceof PlanMsgBody || item.getBody() instanceof SysTextMsgBody) {
                                return;
                            }
                            etContent.addSpan("@" + item.getAccount_name() + " ");
                            break;
                        case "清屏":
                            mAdapter.getData().clear();
                            mAdapter.notifyDataSetChanged();
                            break;
                        case "私聊":
                            if (passiveUserName == null) {
                                passiveUserName = item.getUserAccount();
                            }
                            passiveUserId = item.getBody().getUserId();
                            initPrivateConversationList(item.getUserType());
                            break;
                        case "全体禁言":
                            presenter.muteAll(1, roomBeanMsg.getRoomId());
                            break;
                        case "解除全体禁言":
                            presenter.muteAll(0, roomBeanMsg.getRoomId());
                            break;
                        case "撤回":
                            if (isPrivate) {
//                                presenter.recall(item, privateRoomId);
                            } else {
                                // 非管理员非代理无权撤回其他人的消息
                                if (!ChatSpUtils.instance(ChatMainActivity.this).getUSER_TYPE().equals("2")
                                        && !ChatSpUtils.instance(ChatMainActivity.this).getUSER_TYPE().equals("4")
                                        && !ChatSpUtils.instance(ChatMainActivity.this).getAgentRoomHost().equals("1")
                                        && !item.getBody().getUserId().equals(ChatSpUtils.instance(ChatMainActivity.this).getUserId())) {
                                    showToast("您不能撤回其他用户的消息！");
                                } else {
                                    presenter.recall(item, roomBeanMsg.getRoomId());
                                }
                            }
                            break;

                        case "禁止此人发言":
                            presenter.mute(item, 1, roomBeanMsg.getRoomId());
                            break;
                        case "解除此人禁言":
                            presenter.mute(item, 0, roomBeanMsg.getRoomId());
                            break;
                    }
                }
                listPopupWindow.dismiss();
            }
        });

        if (!right) {
            listPopupWindow.setHorizontalOffset(DisplayUtil.dip2px(ChatMainActivity.this, 45));
            listPopupWindow.setVerticalOffset(DisplayUtil.dip2px(ChatMainActivity.this, 0));
        } else {
            listPopupWindow.setHorizontalOffset(DisplayUtil.dip2px(ChatMainActivity.this, -95));
            listPopupWindow.setVerticalOffset(DisplayUtil.dip2px(ChatMainActivity.this, -20));
        }

        listPopupWindow.show();
    }


    private int loadPhotoPer = 0;//图片发送上次的百分比

    public FileHandler.FileHandlerListener fileHandlerListener = new FileHandler.FileHandlerListener() {
        @Override
        public void onHandleStatus(CrazyRequest<?> request, long currentSize, long totalSize, boolean notError) {
            LogUtils.e("onHandleStatus:", "currentSize:" + currentSize + "---totalSize:" + totalSize + "---notError:" + notError);
            if (currentSize > 0 && totalSize > 0) {
                float d = (currentSize * 100) / totalSize;
                int per = (int) d;
//                if (per < 5) {
//                    //iView.onLoadingImg(per, chatMessage.getBody().getMsgUUID());
//                }else if (per - loadPhotoPer > 20) {
//                    for (int pos = chatMsgs.size() - 1;  pos >= 0; pos --) {
//                        if (chatMsgs.get(pos).getBody().getMsgUUID().equals(uuid)) {
//                            chatMsgs.get(pos).getBody().setPercent(per);
//                            // mAdapter.notifyItemChanged(pos);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mAdapter.notifyDataSetChanged();
//                                }
//                            });
//                            break;
//                        }
//                    }
//                    loadPhotoPer = per;
//                }
//                if (per == 100) {
//                    for (int pos = chatMsgs.size() - 1;  pos >= 0; pos --) {
//                        if (chatMsgs.get(pos).getBody().getMsgUUID().equals(uuid)) {
//                            chatMsgs.get(pos).getBody().setPercent(per);
//                            // mAdapter.notifyItemChanged(pos);
//                            mAdapter.notifyDataSetChanged();
//                            break;
//                        }
//                    }
//                    loadPhotoPer = 0;
//                }
            }
        }

        @Override
        public long sizeOf(String path) {
            return 0;
        }
    };

    private void resetAudio(String senderId) {
        broadcasting = false;
        MediaManager.reset();
        if (msg_iv_audio != null) {
            if (senderId.equals(mSenderId)) {
                msg_iv_audio.setBackgroundResource(R.mipmap.audio_animation_list_right_3);
            } else {
                msg_iv_audio.setBackgroundResource(R.mipmap.audio_animation_list_left_3);
            }
            msg_iv_audio = null;
        }
    }


    //显示打开方式
    public void showOpenMethod(String filesPath) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        startActivity(showOpenTypeDialog(filesPath));
    }

    public Intent showOpenTypeDialog(String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }


    private void initChatUi() {
        //mBtnAudio
        mUiHelper = ChatUiHelper.with(this);
        mUiHelper.bindContentLayout(llContent)
                .bindttToSendButton(btnSend)
                .bindEditText(etContent)
                .bindBottomLayout(bottomLayout)
                .bindRecyclerView(rvChatList)
                .bindNewMsgTip(tvNewMsgTip)
                .bindEmojiLayout(llEmotion)
                .bindAddLayout(mLlAdd)
                .bindToAddButton(ivAdd)
                .bindToEmojiButton(ivEmo)
                .bindAudioBtn(btnAudio)
                .bindAudioIv(ivAudio)
                .bindEmojiData();
        //底部布局弹出,聊天列表上滑
        rvChatList.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    rvChatList.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mAdapter.getItemCount() > 0) {
                                rvChatList.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                            }
                        }
                    });
                }
            }
        });
        //点击空白区域关闭键盘
        rvChatList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mUiHelper.hideBottomLayout(false);
                mUiHelper.hideSoftInput();
                etContent.clearFocus();
                ivEmo.setImageResource(R.mipmap.ic_emoji);
                return false;
            }
        });
        // 录音开始监听设置
        btnAudio.setStartRecordListener(new RecordButton.OnStartRecordListener() {
            @Override
            public void onStartRecord() {
                if (broadcasting) {
                    resetAudio(senderId);
                }
            }
        });
        // 录音结束监听设置
        btnAudio.setOnFinishedRecordListener(new RecordButton.OnFinishedRecordListener() {
            @Override
            public void onFinishedRecord(String audioPath, int time) {
                LogUtils.e(this, "录音结束回调");
                //  录音结束
                File file = new File(audioPath);
                if (file.exists()) {
                    sendAudioMessage(audioPath, time);
                }
            }
        });
    }

    /**
     * 开始烟花动画
     */
    private void startFireAnima(long duration) {
        if (fireView == null)
            return;
        fireView.lunchFireWork(fireView.getWidth() / 3, fireView.getHeight() / 3 * 2 + fireView.getY()
                , 0, 0, 0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fireView != null) {
                    fireView.lunchFireWork(fireView.getWidth() / 3 * 2, fireView.getHeight() / 3 + fireView.getY()
                            , 0, 0, 0);
                }
            }
        }, duration / 2);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.middle_title || i == R.id.title_indictor) {
            if (isLoginSuc && chatSysConfig.getSwitch_room_show().equals("1")) {
                CommonUtils.hideSoftInput(this, etContent);
                if (roomListWindow == null) {
                    return;
                }
                roomListWindow.setListener(new ChatRoomListWindow.onSelectListener() {
                    @Override
                    public void onMoreChatRoom() {
                        roomListWindow.dismiss();
                        Intent intent = new Intent(ChatMainActivity.this, ChatRoomListActivity.class);
                        if (roomBeanMsg != null) {
                            intent.putExtra("oldRoomId", roomBeanMsg.getRoomId());
                            if (agentRoomBean != null)
                                intent.putExtra("agentRoom", agentRoomBean);
                        }
                        startActivityForResult(intent, REQUEST_CODE_ROOM_LIST);
                    }

                    @Override
                    public void onJoinChatRoom(ChatRoomListBean.SourceBean.DataBean dataBean, int pos) {
                        for (int i = 0; i < welcomeViews.size(); i++) {
                            welcomeViews.get(i).getView().clearAnimation();
                        }
                        welcomeLayout.removeAllViews();
                        welcomeViews.clear();
                        roomListWindow.dismiss();
                        presenter.joinChatRoom(dataBean.getId(), dataBean.getRoomKey(), dataBean.getName(), true);
                        nowRoomPos = pos;
                    }
                });
                roomListWindow.showAsDropDown(mLayout);
            }
        } else if (i == R.id.chat_ibt_1) {
//            betLotteryByConfig();
            if (!inRoom) {
                showToast(R.string.please_into_room_first);
                return;
            }
//            if (roomBeanMsg.getIsBanSpeak() == 1) {
//                // 房间被禁言
//                return;
//            } else if (roomBeanMsg.getIsBanSpeak() == 0) {
//                // 房间没被禁言
//                if (roomBeanMsg.getSpeakingFlag() == 1) {
//                    // 用户被禁言
//                    return;
//                }
//            }
            Intent intent = new Intent();
            intent.setAction(BuildConfig.package_name + ".betForChatActivity");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
            overridePendingTransition(R.anim.pop_enter_anim, R.anim.pop_exit_anim);

        } else if (i == R.id.chat_ibt_2) {
            if (!inRoom) {
                showToast(R.string.please_into_room_first);
                return;
            }
//            if (roomBeanMsg.getIsBanSpeak() == 1) {
//                // 房间被禁言
//                return;
//            } else if (roomBeanMsg.getIsBanSpeak() == 0) {
//                // 房间没被禁言
//                if (roomBeanMsg.getSpeakingFlag() == 1) {
//                    // 用户被禁言
//                    return;
//                }
//            }

            //历史投注列表
            Intent intent = new Intent(this, ChatBetHistoryActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.pop_enter_anim, R.anim.pop_exit_anim);
        } else if (i == R.id.chat_ibt_3) {

        } else if (i == R.id.chat_ibt_4) {
            if (!inRoom) {
                showToast(R.string.please_into_room_first);
                return;
            }
            Intent intent = new Intent();
            intent.setAction(BuildConfig.package_name + ".NewChargeMoneyActivity");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);

            //提款
        } else if (i == R.id.chat_ibt_5) {

            if (!inRoom) {
                showToast(R.string.please_into_room_first);
                return;
            }
            Intent intent = new Intent();
            intent.setAction(BuildConfig.package_name + ".PickMoneyActivity");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
        } else if (i == R.id.iv_more_menu) {
            //打开侧边栏
            drawerLayout.openDrawer(draw_right);
        } else if (i == R.id.chat_ibt_6) {
            if (!inRoom) {
                showToast(R.string.please_into_room_first);
                return;
            }
            //历史计划
            Intent intent = new Intent(this, ChatPlanNewsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.pop_enter_anim, R.anim.pop_exit_anim);

        }else if (i == R.id.btn_send) {
            if (!isPrivate && TextUtils.isEmpty(ChatSpUtils.instance(this).getSendText()) || !ChatSpUtils.instance(this).getSendText().equals("1")) {
                // 禁止发送文字
                showToast("您无法在该房间发送文字，请联系客服！");
                return;
            }
            String s = etContent.getText().toString();
            if (roomBeanMsg.getUserType() == 4 && chatSysConfig.getSwitch_front_admin_ban_send().equals("1")) {
            } else {
                if (violateWordsList != null) {
                    String temp = CommonUtils.CheckViolateWord(violateWordsList, s);
                    if (!TextUtils.isEmpty(temp)) {
                        showToast(temp);
                        return;
                    }
                }
            }
            if(roomBeanMsg.getUserType() == 1){
                int speakLimit = 100;
                try{
                    speakLimit = Integer.parseInt(chatSysConfig.getName_every_speaking_word_limit());
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(s.length() > speakLimit){
                    showToast("每次发言限制字数:"+speakLimit);
                    return;
                }
            }else {
                if(s.length() > 10000){
                    showToast("每次发言限制字数:10000");
                    return;
                }
            }
            ChatMessage sendTextMsg = sendTextMsg(s);
            presenter.sentMsg(sendTextMsg, roomBeanMsg);
            etContent.setText("");
        } else if (i == R.id.rlQuickMessage) {
            if (!ChatSpUtils.instance(this).getFastTalk().equals("1")) {
                showToast("您无法在该房间快速发言，请联系客服！");
                return;
            }
            presenter.getQuickMessage(roomBeanMsg.getRoomId(), ChatSpUtils.instance(this).getStationId());
        } else if (i == R.id.rlPhoto) {
            // 发送图片
            if (chatSysConfig.getSwitch_send_image_show().equals("0") || TextUtils.isEmpty(ChatSpUtils.instance(this).getSendImage())
                    || !ChatSpUtils.instance(this).getSendImage().equals("1")) {
                // 禁止发送图片
                showToast("您无法在该房间发送图片，请联系客服！");
                return;
            }
            PictureFileUtil.openGalleryPic(ChatMainActivity.this, REQUEST_CODE_IMAGE);
        } else if (i == R.id.rlVideo) {
            PictureFileUtil.openGalleryAudio(ChatMainActivity.this, REQUEST_CODE_VEDIO);
        } else if (i == R.id.rlFile) {
            PictureFileUtil.openFile(ChatMainActivity.this, REQUEST_CODE_FILE);
        } else if (i == R.id.rlLocation) {// 定位功能，后面再加

        } else if (i == R.id.rl_top_info_content) {// 滚动到置顶的那条消息

        } else if (i == R.id.top_info_close) {// 关闭此条置顶消息
            llTopInfo.setVisibility(View.GONE);
        } else if (i == R.id.rlRedPackage) {
            if (isPrivate && (!ChatSpUtils.instance(this).getUSER_TYPE().equals("4"))) {
                //私聊只有前台管理员才可以发红包
                ToastUtil.showToast(this, "只有前台管理员才可以发红包");
                return;
            }
            // 发送红包
            if (chatSysConfig.getSwitch_red_bag_send().equals("0") || TextUtils.isEmpty(ChatSpUtils.instance(this).getSendRedPacket())
                    || !ChatSpUtils.instance(this).getSendRedPacket().equals("1")) {
                // 禁止发送红包
                showToast("您无法在该房间发送红包，请联系客服！");
                return;
            }

            if (chatSysConfig.getSwitch_red_bag_send().equals("2") && roomBeanMsg.getUserType() != 4) {
                // 前台管理员可发红包
                showToast("您无法在该房间发送红包，请联系客服！");
                return;
            }

            RedPackageSendDialog sendDialog = new RedPackageSendDialog(this, false, false);
            if (isPrivate) {
                sendDialog.setLinearLayoutCountGone(true);
            } else {
                sendDialog.setLinearLayoutCountGone(false);
            }

            sendDialog.setOnSendListener(new RedPackageSendDialog.OnSendListener() {
                @Override
                public void onSend(String amount, String num, String content) {
                    if (TextUtils.isEmpty(amount) || Double.parseDouble(amount) == 0) {
                        showToast("请输入红包金额！");
                        return;
                    }
                    //私聊默认为1
                    if (isPrivate) {
                        num = "1";
                    }

                    if (TextUtils.isEmpty(num)) {
                        showToast("请输入红包数量！");
                        return;
                    } else {
                        if (num.length() > 10) {
                            showToast("红包数量过多，无法发红包");
                            return;
                        } else if (Integer.parseInt(num) == 0) {
                            showToast("请输入红包数量！");
                            return;
                        }
                    }
                    if (chatSysConfig.getSwitch_red_bag_remark_show().equals("0")) {
                        content = "暂无名称";
                    } else {
                        if (TextUtils.isEmpty(content)) {
                            content = chatSysConfig.getName_red_bag_remark_info();
                        }
                    }
                    ChatMessage chatMessage = getRedPackageMsg(amount, num, content, "");
                    presenter.sentMsg(chatMessage, roomBeanMsg);
                }
            });
            mUiHelper.hideBottomLayout(false);
            sendDialog.show();
        } else if (i == R.id.chat_draw_header) {
            // 点击侧边栏头像，进入个人设置页
            if (!inRoom) {
                showToast(R.string.please_into_room_first);
                return;
            }
            // 进入个人设置页
            Intent intent = new Intent(ChatMainActivity.this, PersonDataActivity.class);
            intent.putExtra("photo", roomBeanMsg.getAvatar());
            intent.putExtra("nickName", roomBeanMsg.getNickName());
            intent.putExtra("roomId", roomBeanMsg.getRoomId());
            startActivityForResult(intent, REQUEST_CODE_PERSON_DATA);
        } else if (i == R.id.chat_draw_refresh) {
            // 刷新个人数据
            if (!inRoom) {
                showToast(R.string.please_into_room_first);
                return;
            }
            isRefresh = true;
            presenter.getPersonData(roomBeanMsg.getRoomId(), true);
        } else if (i == R.id.chat_draw_share) {
            // 分享今日盈亏
            if (!inRoom) {
                showToast(R.string.please_into_room_first);
                return;
            }
            if (roomNotSpeaking || banTalk) {
                showToast("禁言中无法分享今日盈亏!");
                return;
            }
            if (personData != null) {
                ChatMessage baseSendMessage = getBaseSendMessage(MsgType.SHARE_DATA);
                TextMsgBody textMsgBody = new TextMsgBody();
                textMsgBody.setType(9);
                textMsgBody.setSpeakType("2");
                if (roomBeanMsg != null) {
                    textMsgBody.setRoomId(roomBeanMsg.getRoomId());
                }
                textMsgBody.setSource(ConfigCons.SOURCE);
                textMsgBody.setSentTime(TimeUtils.getCurrentTimeString());
                textMsgBody.setCode(ConfigCons.SEND_MSG);
                textMsgBody.setUserId(ChatSpUtils.instance(this).getUserId());
                textMsgBody.setMsgUUID(baseSendMessage.getUuid());
                textMsgBody.setAgentRoomHost(ChatSpUtils.instance(this).getAgentRoomHost());
                PersonDataBean personDataBean = new PersonDataBean(personData.getAllWinAmount()
                        , personData.getAllBetAmount()
                        , personData.getYingkuiAmount());
                String json = new Gson().toJson(personDataBean);
                textMsgBody.setRecord(json);
                if (isPrivate) {
                    textMsgBody.setType(1);
                    textMsgBody.setCode(ConfigCons.GET_PRIVATE_GROUP_HISTORY);
                    textMsgBody.setPassiveUserId(passiveUserId);
                    textMsgBody.setStationId(ChatSpUtils.instance(this).getStationId());
                    textMsgBody.setMsgType(TextMsgBody.PRIVATE_WIN_LOST);
                    textMsgBody.setRoomId(privateRoomId);
                }

                baseSendMessage.setBody(textMsgBody);
                baseSendMessage.setSentStatus(MsgSendStatus.SENDING);
                mAdapter.addData(baseSendMessage);
                rvChatList.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                presenter.sentMsg(baseSendMessage, roomBeanMsg);
                drawerLayout.closeDrawers();
            } else {
                showToast("数据异常，请刷新数据再分享！");
            }
        } else if (i == R.id.rlSave) {
            // 查看收藏图片
            ChatCollectionImagesModel chatCollectionImagesModel = new ChatCollectionImagesModel();
            chatCollectionImagesModel.setOption("1");
            presenter.saveImages(chatCollectionImagesModel);

        } else if (i == R.id.chat_draw_refresh_money) {
            //获取用户余额
            if (!inRoom) {
                showToast(R.string.please_into_room_first);
                return;
            }
            AnimateUtil.startRotateAnim(this, chat_draw_iv_money);
            presenter.getCharge();
        } else if (i == R.id.charge) {
            if (!inRoom) {
                showToast(R.string.please_into_room_first);
                return;
            }
            Intent intent = new Intent();
            intent.setAction(BuildConfig.package_name + ".NewChargeMoneyActivity");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
        } else if (i == R.id.tikuan) {
            if (!inRoom) {
                showToast(R.string.please_into_room_first);
                return;
            }
            Intent intent = new Intent();
            intent.setAction(BuildConfig.package_name + ".PickMoneyActivity");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
        }
    }


    /**
     * 快捷发言
     */
    public void showFastAmountDialog(ChatQuickMessageBean chatQuickMessageBean) {
        List<ChatQuickMessageBean.SourceBean> source = chatQuickMessageBean.getSource();
        final List<String> list = new ArrayList<>();
        for (ChatQuickMessageBean.SourceBean sourceBean : source) {
            list.add(sourceBean.getRecord());
        }
        String[] recordArray = list.toArray(new String[list.size()]);

        if (recordArray.length == 0) {
            ToastUtil.showToastCenter(this, "暂无快捷发言数据", 1000);
            return;
        }

        final ActionSheetDialog dialog = new ActionSheetDialog(this, recordArray, null);
        dialog.title("快捷发言");
        dialog.titleTextColor(Color.BLACK);
        dialog.titleBgColor(Color.WHITE);
        dialog.heightScale(0.6f);
        dialog.isTitleShow(true).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                //点击直接发消息
                ChatMessage sendTextMsg = sendTextMsg(list.get(position));
                presenter.sentMsg(sendTextMsg, roomBeanMsg);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(draw_right)) {
            drawerLayout.closeDrawer(draw_right);
            return;
        }
        if (mUiHelper.isBottomLayoutShown()) {
            mUiHelper.hideBottomLayout(false);
            return;
        }

        if (lotteryHistoryResultWindow != null && lotteryHistoryResultWindow.isShowing()) {
            lotteryHistoryResultWindow.dismiss();
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FILE:
                    //  文件选择结果回调
                    String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                    LogUtils.d("获取到的文件路径:", "" + filePath);
//                    getFileMessage(filePath);
                    break;
                case REQUEST_CODE_IMAGE:
                    sendImageMessages.clear();
                    //  图片选择结果回调
                    List<LocalMedia> selectListPic = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectListPic) {
                        LogUtils.d("获取图片路径成功:", "" + media.getPath());
                        ChatMessage sendImageMessage = getImageMessage(media);
                        sendImageMessages.add(sendImageMessage);
                        presenter.upLoadFile(sendImageMessage, "jpeg", fileHandlerListener);
                    }
                    break;
                case REQUEST_CODE_VEDIO:
                    //  视频选择结果回调
                    List<LocalMedia> selectListVideo = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectListVideo) {
                        LogUtils.d("获取视频路径成功:", "" + media.getPath());
//                        sendVedioMessage(media);
                    }
                    break;
                case REQUEST_CODE_ROOM_LIST: //点击选择房间之后
                    String room = data.getStringExtra("room");
                    JoinChatRoomBean bean = new Gson().fromJson(room, JoinChatRoomBean.class);
                    if (roomBeanMsg == null || !bean.getSource().getRoomId().equals(roomBeanMsg.getRoomId())) { //选中当前的房间，不用做操作
                        joinRoomSuc(bean);
                        presenter.setOldRoomId(bean.getSource().getRoomId());
                    }

                    break;
                case REQUEST_CODE_PERSON_DATA: //点击个人资料页面返回
                    modifyPersonDataModel = data.getParcelableExtra("model");
                    accountAvatar = modifyPersonDataModel.getAvatar();
                    accountNickName = modifyPersonDataModel.getNickName();
                    roomBeanMsg.setAvatar(accountAvatar);
                    roomBeanMsg.setNickName(accountNickName);
                    GlideUtils.loadHeaderPic(ChatMainActivity.this, accountAvatar, iv_header);
                    chat_draw_account_name.setText(TextUtils.isEmpty(accountNickName) ? roomBeanMsg.getAccount() : accountNickName);
                    chatMsgs.clear();
                    mAdapter.notifyDataSetChanged();
                    ChatMessageHistoryModel chatMessageHistoryModel = new ChatMessageHistoryModel(ConfigCons.HISTORY_MESSAGE, ConfigCons.SOURCE,
                            roomBeanMsg.getRoomId(), 1, historyPageCount);
                    presenter.getHistoryMessage(chatMessageHistoryModel);
                    break;
            }
        }
    }

    private ChatMessage getBaseSendMessage(MsgType msgType) {
        ChatMessage mMessgae = new ChatMessage();
        mMessgae.setUuid(UUID.randomUUID().toString());
        mMessgae.setSenderId(mSenderId);
        mMessgae.setTargetId(mTargetId);
        mMessgae.setSentStatus(MsgSendStatus.SENDING);
        mMessgae.setMsgType(msgType);
        mMessgae.setAccount_pic(accountAvatar);
        mMessgae.setAccount_level(roomBeanMsg.getLevelIcon());
        mMessgae.setAccount_level_name(roomBeanMsg.getLevel());
        mMessgae.setUserAccount(accountName);
        if (TextUtils.isEmpty(accountNickName)) {
            mMessgae.setAccount_name(accountName);
        } else {
            mMessgae.setAccount_name(accountNickName);
        }
        if (roomBeanMsg != null) {
            mMessgae.setPlanUser(roomBeanMsg.getPlanUser());
            mMessgae.setUserType(roomBeanMsg.getUserType());
        }
        return mMessgae;
    }

    /**
     * 随机获取中奖集合中的若干条不重复的中奖消息
     *
     * @param list
     * @param count
     * @return
     */
    public List<WinningNotice> getSubStringByRadom(List<WinningNotice> list, int count) {
        List backList = null;
        backList = new ArrayList<WinningNotice>();
        Random random = new Random();
        int backSum = 0;
        if (list.size() >= count) {
            backSum = count;
        } else {
            backSum = list.size();
        }
        for (int i = 0; i < backSum; i++) {
//			随机数的范围为0-list.size()-1
            int target = random.nextInt(list.size());
            backList.add(list.get(target));
            list.remove(target);
        }
        return backList;
    }

    @Override
    public void onJoinRoomNotice(final String name) {
        // 不在房间内或者在私聊的时候不需要进房提示
        if (!inRoom || isPrivate)
            return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                final View welcomeView = createWelcomeView(name);
                if (welcomeLayout != null) {
                    LogUtils.e("进房提示", name);
                    welcomeLayout.addView(welcomeView);
                    int top = ScreenUtil.dip2px(ChatMainActivity.this, CommonUtils.getRandomNum());
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) welcomeView.getLayoutParams();
                    layoutParams.topMargin = welcomeView.getTop() + top;
                    welcomeView.setLayoutParams(layoutParams);
                    welcomeView.layout(0, top, 0, 0);
                    Animation welcomeTxtAnimation = AnimationUtils.loadAnimation(ChatMainActivity.this, R.anim.welcome_txt_anim);

                    setWelcomeTransition(welcomeTxtAnimation, welcomeView, addWelcomtxt(welcomeView));
                }
            }
        });
    }

    @Override
    public void onReceiverWinningMsg(final ChatSendMsg chatSendMsg) {
        if (!inRoom)
            return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String msgStr = chatSendMsg.getMsgStr();
                    JSONObject jsonObject = new JSONObject(msgStr);
                    // 中奖消息
                    if (chatSysConfig != null && chatSysConfig.getSwitch_winning_banner_show().equals("1")) {
                        List<WinningNotice> winningNotices = new Gson().fromJson(jsonObject.getJSONObject("source").getString("winningList"), new TypeToken<List<WinningNotice>>() {
                        }.getType());
                        if (views == null)
                            views = new ArrayList<>();
                        if (!isAnimating) {
                            views.clear();
                        }
                        if (winningNotices.size() > 10) {
                            winningNotices = getSubStringByRadom(winningNotices, 10);
                        }
                        for (WinningNotice winningNotice : winningNotices) {
                            View view = creatView(winningNotice.getPrizeProject(), winningNotice.getUserName(), winningNotice.getPrizeMoney());
                            if (flContent != null) {
                                flContent.addView(view);
                                view.layout(view.getMeasuredWidth(), 0, 2 * view.getMeasuredWidth(), view.getMeasuredHeight());
                                views.add(view);
                            }
                        }
                        if (!isAnimating) {
                            setTransition(0);
                        }
                    } else {
                        showWinningView = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 服务器推送处理方法
     *
     * @param chatSendMsg
     * @param source
     */
    private void receiverMsg(ChatSendMsg chatSendMsg, String source, boolean isPrivateMsg) {
        //在私聊房间收到非私聊消息
        //在私聊房间收到非当前私聊对象发的消息
        //isPrivate 当前是否是私聊
        if (isPrivate) {
            if (!isPrivateMsg) {
                return;
            }
            if (!passiveUserId.equals(chatSendMsg.getUserId())) {
                getPrivateConversationList();
                return;
            } else {
                setPrivateMessagesRead();
            }
        } else {
            if (isPrivateMsg) {
                getPrivateConversationList();
                return;
            }
        }

        if (mAdapter.getData().size() > 500) {
            mAdapter.remove(0);
        }

        ChatMessage mMessgae = new ChatMessage();
        String msgStr = chatSendMsg.getMsgStr();
        ChatSendMsg.MsgResultBean.UserEntityBean userEntity = chatSendMsg.getMsgResult().getUserEntity();
        ChatSendMsg.MsgResultBean.UserEntityBean.UserDetailEntityBean userDetailEntity = chatSendMsg.getMsgResult().getUserEntity().getUserDetailEntity();
        mMessgae.setSenderId(mTargetId);
        mMessgae.setTargetId(mSenderId);
        mMessgae.setAccount_name(TextUtils.isEmpty(userEntity.getNickName()) ? userEntity.getAccount() : userEntity.getNickName());
        mMessgae.setUserAccount(userEntity.getAccount());
        mMessgae.setAccount_pic(userDetailEntity != null ? userDetailEntity.getAvatarCode() : "0");
        mMessgae.setAccount_level(userEntity.getLevelIcon());
        mMessgae.setAccount_level_name(TextUtils.isEmpty(userEntity.getLevelName()) ? "后台管理员" : userEntity.getLevelName());
        mMessgae.setUserType(userEntity.getUserType());
        mMessgae.setPlanUser(userEntity.getPlanUser());
        mMessgae.setPrivatePermission(userEntity.isPrivatePermission());

        if (isPrivateMsg) {
            ChatPrivateReceiveMsgBean chatPrivateReceiveMsgBean = new Gson().fromJson(chatSendMsg.getMsgStr(), ChatPrivateReceiveMsgBean.class);
            if (chatPrivateReceiveMsgBean.getMsgType() == MsgBody.PRIVATE_WIN_LOST) {
                chatSendMsg.setMsgType(9);
            } else if (chatPrivateReceiveMsgBean.getCode().equals(ConfigCons.SEND_RED_PACKAGE) ||
                    chatPrivateReceiveMsgBean.getCode().equals(ConfigCons.SEND_RED_PACKAGE_WAP) ||
                    chatPrivateReceiveMsgBean.getCode().equals(ConfigCons.SEND_RED_PACKAGE_PC)) {
                chatSendMsg.setMsgType(3);
            } else {
                chatSendMsg.setMsgType(chatPrivateReceiveMsgBean.getMsgType());
            }
        }

        switch (chatSendMsg.getMsgType()) {
//            SYSTEM_MSG_TYPE = 0;//系统消息
//            TEXT_MSG_TYPE = 1;//文本消息
//            IMAGE_MSG_TYPE = 2;//图片消息
//            REDPACKET_MSG_TYPE = 3;//发红包
//            PICK_REDPACKET_MSG_TYPE = 4;//领红包
//            PLAN_MSG_TYPE = 6;//计划消息
//            FOLLOW_BET_MSG_TYPE = 8;//跟注消息
//            SHARE_BET_MSG_TYPE = 5;//注单消息
//            VIDEO_MSG_TYPE = 10;//视频消息
//            AUDIO_BET_MSG_TYPE = 11;//语音消息
//            MODIFY_NICKNAME_PHOTO = 12;//修改昵称或头像
//            ENTER_ROOM_MSG = 13;//进房消息
//            WIN_FAKE_DATA_MSG = 14;//中奖榜单消息
//            ROBOT_MSG_TYPE = 7;//机器人发言
//            msgType=16 ;//强制退出聊天室
            case 0:
                // 系统消息
                sendSysTextMsg(chatSendMsg);
                break;
            case 1:
                // 文字消息
                TextMsgBody msgBody = new Gson().fromJson(msgStr, TextMsgBody.class);
                mMessgae.setMsgType(MsgType.TEXT);
                if (TextUtils.isEmpty(msgBody.getSentTime())) {
                    msgBody.setSentTime(TimeUtils.getCurrentTimeString());
                }
                mMessgae.setBody(msgBody);
                mMessgae.setUuid(msgBody.getMsgUUID());
                mMessgae.setMsgId(msgBody.getMsgId());
                if (isPrivateMsg) {
                    mMessgae.setMsgId(msgBody.getUserMessageId());
                }
                //将消息添加至消息列表
                mAdapter.addData(mMessgae);
                break;
            case 2:
                // 图片消息
                ImageMsgBody imageMsgBody = new Gson().fromJson(msgStr, ImageMsgBody.class);
//                imageMsgBody.setExtra(result.getTextRecord());
                if (TextUtils.isEmpty(imageMsgBody.getSentTime())) {
                    imageMsgBody.setSentTime(TimeUtils.getCurrentTimeString());
                }
                mMessgae.setMsgType(MsgType.IMAGE);
                mMessgae.setBody(imageMsgBody);
                mMessgae.setUuid(imageMsgBody.getMsgUUID());
                mMessgae.setMsgId(imageMsgBody.getMsgId());
                if (isPrivateMsg) {
                    mMessgae.setMsgId(imageMsgBody.getUserMessageId());
                }
                //将消息添加至消息列表
                mAdapter.addData(mMessgae);
                presenter.loadImageList(mAdapter.getData());
                break;
            case 11:
                // 语音消息
                AudioMsgBody audioMsgBody = new Gson().fromJson(msgStr, AudioMsgBody.class);
//                imageMsgBody.setExtra(result.getTextRecord());
                if (audioMsgBody.getAudioMsg() != null) {
                    mMessgae.setMsgId(audioMsgBody.getMsgId());
                    audioMsgBody = audioMsgBody.getAudioMsg();
                }
                if (TextUtils.isEmpty(audioMsgBody.getSentTime())) {
                    audioMsgBody.setSentTime(TimeUtils.getCurrentTimeString());
                }
                audioMsgBody.setLocalPath("");
                mMessgae.setMsgType(MsgType.AUDIO);
                mMessgae.setBody(audioMsgBody);
                mMessgae.setUuid(audioMsgBody.getMsgUUID());
                if (isPrivateMsg) {
                    mMessgae.setMsgId(audioMsgBody.getUserMessageId());
                }
                //将消息添加至消息列表
                mAdapter.addData(mMessgae);
                break;
            case 3:
                //红包消息
                RedPackageMsgBody packageMsgBody = new Gson().fromJson(msgStr, RedPackageMsgBody.class);
                mMessgae.setMsgType(MsgType.RED_PACKAGE);
                if (TextUtils.isEmpty(packageMsgBody.getSentTime())) {
                    packageMsgBody.setSentTime(TimeUtils.getCurrentTimeString());
                }
                mMessgae.setBody(packageMsgBody);
                mMessgae.setUuid(packageMsgBody.getMsgUUID());
                //将消息添加至消息列表
                mAdapter.addData(mMessgae);
                break;
            case 5:
                //注单消息
                BetSlipMsgBody betSlipMsgBody = new Gson().fromJson(msgStr, BetSlipMsgBody.class);
                if (TextUtils.isEmpty(betSlipMsgBody.getSentTime())) {
                    betSlipMsgBody.setSentTime(TimeUtils.getCurrentTimeString());
                }
                List<BetInfo> betInfos = betSlipMsgBody.getBetInfos();
                if (betInfos != null && betInfos.size() > 1) {
                    mMessgae.setMsgType(MsgType.BET_SLIP_LIST);
                } else {
                    mMessgae.setMsgType(MsgType.BET_SLIP);
                }
                mMessgae.setBody(betSlipMsgBody);
                mMessgae.setWinOrLost(betSlipMsgBody.getWinOrLost());
                mMessgae.setUuid(betSlipMsgBody.getMsgUUID());
                mMessgae.setMsgId(betSlipMsgBody.getMsgId());
                if (isPrivateMsg) {
                    mMessgae.setMsgId(betSlipMsgBody.getUserMessageId());
                }
                mAdapter.addData(mMessgae);
                break;
//            case 8:
//                //注单消息
//                BetSlipMsgBody slipMsgBody = new Gson().fromJson(msgStr, BetSlipMsgBody.class);
//                if (TextUtils.isEmpty(slipMsgBody.getSentTime())) {
//                    slipMsgBody.setSentTime(TimeUtils.getCurrentTimeString());
//                }
//                mMessgae.setMsgType(MsgType.BET_SLIP);
//                mMessgae.setBody(slipMsgBody);
//                mMessgae.setUuid(slipMsgBody.getMsgUUID());
//                mAdapter.addData(mMessgae);
//                break;
            case 6:
                // 计划消息
                PlanMsgBody body = new Gson().fromJson(msgStr, PlanMsgBody.class);
                if (TextUtils.isEmpty(body.getSentTime())) {
                    body.setSentTime(TimeUtils.getCurrentTimeString());
                }
                mMessgae.setMsgType(MsgType.SYS_PLAN);
                mMessgae.setBody(body);
                mMessgae.setUuid(body.getMsgUUID());
                mMessgae.setMsgId(body.getMsgId());
                mMessgae.setAccount_name(body.getNickName());
                mMessgae.setAccount_pic(body.getAvatar());
                mMessgae.setUserAccount(body.getAccount());
                mAdapter.addData(mMessgae);
                break;
            case 7:
            case 20:
                // 机器人消息
                TextMsgBody textMsgBody = new Gson().fromJson(msgStr, TextMsgBody.class);
                TextMsgBody.FromUserBean formUser = textMsgBody.getFromUser();
                mMessgae.setMsgType(MsgType.HTML);
                if (TextUtils.isEmpty(textMsgBody.getSentTime())) {
                    textMsgBody.setSentTime(TimeUtils.getCurrentTimeString());
                }
                mMessgae.setBody(textMsgBody);
                mMessgae.setAccount_pic(formUser == null ? "" : formUser.getRobotImage());
                mMessgae.setUuid(textMsgBody.getMsgUUID());
                mMessgae.setMsgId(textMsgBody.getMsgId());
                //将消息添加至消息列表
                mAdapter.addData(mMessgae);
                break;
            case 9:
                //分享今日输赢
                TextMsgBody textBody = new Gson().fromJson(msgStr, TextMsgBody.class);
                mMessgae.setMsgType(MsgType.SHARE_DATA);
                if (TextUtils.isEmpty(textBody.getSentTime())) {
                    textBody.setSentTime(TimeUtils.getCurrentTimeString());
                }
                mMessgae.setBody(textBody);
                mMessgae.setUuid(textBody.getMsgUUID());
                mMessgae.setMsgId(textBody.getMsgId());
                if (isPrivateMsg) {
                    mMessgae.setMsgId(textBody.getUserMessageId());
                }
                //将消息添加至消息列表
                mAdapter.addData(mMessgae);
                break;
        }
        if (mMessgae.getBody() != null) {
            mMessgae.getBody().setSource(source);
        }
        if (isShowNewMsgTip) {
            newMsgCount++;
            mUiHelper.showNewMsgTip(newMsgCount <= 99 ? newMsgCount : 99);
        } else {
            if (rvChatList != null) {
                rvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        }
        LogUtils.e("mAdapter.size():", mAdapter.getData().size());
    }


    /**
     * 历史消息处理方法
     *
     * @param msgBean
     */
    private void handleHistoryMsg(ChatHistoryMessageBean.SourceBean msgBean) {
        ChatMessage mMessgae = new ChatMessage();
        if (msgBean.getSender().equals(ChatSpUtils.instance(this).getUserId())) {
            mMessgae.setSenderId(mSenderId);
            mMessgae.setTargetId(mTargetId);
            mMessgae.setSentStatus(MsgSendStatus.SENT);
        } else {
            mMessgae.setSenderId(mTargetId);
            mMessgae.setTargetId(mSenderId);
        }
        mMessgae.setPlanUser(msgBean.getIsPlanUser());
        mMessgae.setUuid(msgBean.getMsgId());
        mMessgae.setMsgId(msgBean.getMsgId());
        mMessgae.setAccount_level(msgBean.getLevelIcon());
        mMessgae.setAccount_level_name(msgBean.getLevelName());
        mMessgae.setUserType(msgBean.getUserType());
        mMessgae.setUserAccount(msgBean.getAccount());
        mMessgae.setPrivatePermission(msgBean.isPrivatePermission());
        if (msgBean.getUserType() == 2) {
            // 系统管理员
//            mMessgae.setAccount_name(msgBean.getNativeAccount());
            mMessgae.setAccount_pic(msgBean.getNativeAvatar());
        } else {
            mMessgae.setAccount_pic(msgBean.getAvatar());
        }
        String nickName = msgBean.getNickName();
        if (msgBean.getSender().equals(ChatSpUtils.instance(this).getUserId())) {
            mMessgae.setAccount_name(TextUtils.isEmpty(nickName) ? msgBean.getAccount() : nickName);
        } else {
            mMessgae.setAccount_name(TextUtils.isEmpty(nickName) ? msgBean.getNativeAccount() : nickName);
        }
//        if (msgBean.getUserType() != 3) {
        switch (msgBean.getMsgType()) {
            case 0:
                //系统消息
                mMessgae.setAccount_name("系统消息");
                SysTextMsgBody sysTextMsgBody = new SysTextMsgBody();
                sysTextMsgBody.setMsg(msgBean.getContext());
                mMessgae.setMsgType(MsgType.SYS_TEXT);
                mMessgae.setBody(sysTextMsgBody);
                break;
            case 1:
                //文本消息
                TextMsgBody mTextMsgBody = new TextMsgBody();
                mTextMsgBody.setRecord(msgBean.getContext());
                mTextMsgBody.setType(msgBean.getUserType());
                mMessgae.setMsgType(MsgType.TEXT);
                mMessgae.setBody(mTextMsgBody);
                break;
            case 2:
                //图片消息
                ImageMsgBody mImageMsgBody = new ImageMsgBody();
                String url = msgBean.getUrl().trim();
                if (msgBean.getUserType() != 3) {
                    //非机器人消息图片
                    if (url.contains("/")) {
                        String[] split = url.split("/");
                        url = split[split.length - 1];
                    }
                }
                mImageMsgBody.setRecord(url);
                mMessgae.setMsgType(MsgType.IMAGE);
                mMessgae.setBody(mImageMsgBody);
                break;
            case 3:
                //红包消息
                RedPackageMsgBody redPackageMsgBody = new RedPackageMsgBody();
                redPackageMsgBody.setPayId(msgBean.getPayId());
                redPackageMsgBody.setRemark(msgBean.getRemark());
                mMessgae.setMsgType(MsgType.RED_PACKAGE);
                mMessgae.setBody(redPackageMsgBody);
                break;
            case 6:
                //计划消息
                PlanMsgBody body = new PlanMsgBody();
                body.setText(msgBean.getText());
                body.setLotteryName(msgBean.getLotteryName());
                mMessgae.setMsgType(MsgType.SYS_PLAN);
                mMessgae.setBody(body);
                break;
            case 5:
                //注单消息
                try {
                    if (isPrivate) {
                        BetSlipMsgBody slipMsgBody = new Gson().fromJson(msgBean.getNativeContent(), BetSlipMsgBody.class);
                        if (slipMsgBody == null) {
                            return;
                        }
                        List<BetInfo> betInfos = slipMsgBody.getBetInfos();
                        if (betInfos != null && betInfos.size() > 1) {
                            mMessgae.setMsgType(MsgType.BET_SLIP_LIST);
                        } else {
                            mMessgae.setMsgType(MsgType.BET_SLIP);
                        }
                        mMessgae.setWinOrLost(msgBean.getWinOrLost());
                        mMessgae.setBody(slipMsgBody);
                    } else {
                        BetSlipMsgBody betSlipMsgBody = new Gson().fromJson(msgBean.getContext(), BetSlipMsgBody.class);
                        mMessgae.setMsgType(MsgType.BET_SLIP);
                        mMessgae.setWinOrLost(msgBean.getWinOrLost());
                        mMessgae.setBody(betSlipMsgBody);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 8:
                //注单消息
                try {
                    BetSlipMsgBody slipMsgBody = new Gson().fromJson(msgBean.getNativeContent(), BetSlipMsgBody.class);
                    if (slipMsgBody == null) {
                        return;
                    }
                    List<BetInfo> betInfos = slipMsgBody.getBetInfos();
                    if (betInfos != null && betInfos.size() > 1) {
                        mMessgae.setMsgType(MsgType.BET_SLIP_LIST);
                    } else {
                        mMessgae.setMsgType(MsgType.BET_SLIP);
                    }
                    mMessgae.setWinOrLost(msgBean.getWinOrLost());
                    mMessgae.setBody(slipMsgBody);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 10:
                //视频消息
                break;
            case 11:
                //语音消息
                AudioMsgBody audioMsgBody = new AudioMsgBody();
                String record = msgBean.getContext();
                if (record.contains("/")) {
                    String[] split = record.split("/");
                    record = split[split.length - 1];
                }
                audioMsgBody.setRecord(record);
                if (msgBean.getRemark() != null)
                    audioMsgBody.setDuration(Integer.parseInt(msgBean.getRemark()));
                mMessgae.setMsgType(MsgType.AUDIO);
                mMessgae.setBody(audioMsgBody);
                break;
            case 7:
                // 机器人消息
                TextMsgBody textMsgBody = new TextMsgBody();
                textMsgBody.setRecord(msgBean.getContext());
                textMsgBody.setType(msgBean.getUserType());
                mMessgae.setMsgType(MsgType.HTML);
                mMessgae.setBody(textMsgBody);
                break;
            case 4://私聊4是今日盈亏
            case 9:
                //分享今日盈亏
                TextMsgBody msgBody = new TextMsgBody();
                msgBody.setRecord(msgBean.getContext());
                msgBody.setType(9);
                mMessgae.setMsgType(MsgType.SHARE_DATA);
                mMessgae.setBody(msgBody);
                break;
        }
        if (mMessgae.getBody() != null) {
            if (tempMessages.size() == 0) {
                long date = ChatTimeUtil.getStringToDate(msgBean.getTime(), "yyyy-MM-dd HH:mm:ss");
                TextMsgBody txtTimeBody = new TextMsgBody();
                txtTimeBody.setRecord(msgBean.getContext());
                txtTimeBody.setType(msgBean.getUserType());
                txtTimeBody.setRemark(ChatTimeUtil.getNewChatTime(date));
                mMessgae.setMsgType(MsgType.MSG_TIME);
                mMessgae.setBody(txtTimeBody);
                mMessgae.getBody().setSentTime(msgBean.getTime());
                mMessgae.getBody().setSource(ConfigCons.SOURCE);
                mMessgae.getBody().setUserId(msgBean.getSender());
                tempMessages.add(mMessgae);
                handleHistoryMsg(msgBean);
                return;
            } else {
                String time = tempMessages.get(tempMessages.size() - 1).getBody().getSentTime();
                long date = ChatTimeUtil.getStringToDate(msgBean.getTime(), "yyyy-MM-dd");
                long proDate = ChatTimeUtil.getStringToDate(time, "yyyy-MM-dd");
                if (date != proDate) {
                    long newdate = ChatTimeUtil.getStringToDate(msgBean.getTime(), "yyyy-MM-dd HH:mm:ss");
                    TextMsgBody txtTimeBody = new TextMsgBody();
                    txtTimeBody.setRecord(msgBean.getContext());
                    txtTimeBody.setType(msgBean.getUserType());
                    txtTimeBody.setRemark(ChatTimeUtil.getNewChatTime(newdate));
                    mMessgae.setMsgType(MsgType.MSG_TIME);
                    mMessgae.setBody(txtTimeBody);
                    mMessgae.getBody().setSentTime(msgBean.getTime());
                    mMessgae.getBody().setSource(ConfigCons.SOURCE);
                    mMessgae.getBody().setUserId(msgBean.getSender());
                    mMessgae.getBody().setStopTalkType(msgBean.getStopTalkType());
                    tempMessages.add(mMessgae);
                    handleHistoryMsg(msgBean);
                    return;
                }
            }

            mMessgae.getBody().setSentTime(msgBean.getTime());
            mMessgae.getBody().setMsgId(msgBean.getMsgId());
            mMessgae.getBody().setMsgUUID(msgBean.getMsgId());
            mMessgae.getBody().setSource(ConfigCons.SOURCE);
            mMessgae.getBody().setUserId(msgBean.getSender());
            tempMessages.add(mMessgae);
        }
    }

    //文本消息
    private ChatMessage sendTextMsg(String hello) {
        final ChatMessage mMessgae = getBaseSendMessage(MsgType.TEXT);
        TextMsgBody mTextMsgBody = new TextMsgBody();
        mTextMsgBody.setRecord(hello);
        mTextMsgBody.setMsgUUID(mMessgae.getUuid());
        if (roomBeanMsg != null) {
            mTextMsgBody.setRoomId(roomBeanMsg.getRoomId());
            mTextMsgBody.setType(1);
        }
        mTextMsgBody.setSource(ConfigCons.SOURCE);
        mTextMsgBody.setSentTime(TimeUtils.getCurrentTimeString());
        mTextMsgBody.setCode(ConfigCons.SEND_MSG);
        mTextMsgBody.setSpeakType("2");
        mTextMsgBody.setUserId(ChatSpUtils.instance(this).getUserId());
        mTextMsgBody.setAgentRoomHost(ChatSpUtils.instance(this).getAgentRoomHost());
        if (isPrivate) {
            mTextMsgBody.setCode(ConfigCons.GET_PRIVATE_GROUP_HISTORY);
            mTextMsgBody.setPassiveUserId(passiveUserId);
            mTextMsgBody.setMsgType(TextMsgBody.PRIVATE_TXT);
            mTextMsgBody.setStationId(ChatSpUtils.instance(this).getStationId());
            mTextMsgBody.setRoomId(privateRoomId);
        }

        mMessgae.setBody(mTextMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        rvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
        return mMessgae;
    }

    //系统文本消息
    private void sendSysTextMsg(ChatSendMsg chatSendMsg) {
        ChatSendMsg.MsgResultBean.UserMsgEntityBean userMsgEntity = chatSendMsg.getMsgResult().getUserMsgEntity();
        ChatMessage mMessgae = new ChatMessage();
        mMessgae.setUuid(UUID.randomUUID().toString());
        mMessgae.setMsgType(MsgType.SYS_TEXT);
        SysTextMsgBody mTextMsgBody = new SysTextMsgBody();
        mTextMsgBody.setMsg(userMsgEntity.getTextRecord());
        mTextMsgBody.setMsgUUID(mMessgae.getUuid());
        mTextMsgBody.setSource(ConfigCons.SOURCE);
        mTextMsgBody.setSentTime(TimeUtils.getCurrentTimeString());
        mTextMsgBody.setSpeakType("2");
        mTextMsgBody.setUserId(ChatSpUtils.instance(this).getUserId());
        if (roomBeanMsg != null) {
            mMessgae.setPlanUser(roomBeanMsg.getPlanUser());
            mMessgae.setUserType(roomBeanMsg.getUserType());
        }
        mMessgae.setBody(mTextMsgBody);
        mMessgae.setAccount_name("系统消息");
        //开始发送
        mAdapter.addData(mMessgae);
    }

    private ChatMessage getRedPackageMsg(String amount, String num, String content, String payId) {
        ChatMessage chatMessage = getBaseSendMessage(MsgType.RED_PACKAGE);
        RedPackageMsgBody redPackageMsgBody = new RedPackageMsgBody();
        if (roomBeanMsg != null) {
            redPackageMsgBody.setRoomId(roomBeanMsg.getRoomId());
        }
        redPackageMsgBody.setAmount(new BigDecimal(amount));
        redPackageMsgBody.setCount(Integer.parseInt(num));
        redPackageMsgBody.setRemark(content);
        redPackageMsgBody.setMsgUUID(chatMessage.getUuid());
        redPackageMsgBody.setCode(ConfigCons.SEND_RED_PACKAGE);
        redPackageMsgBody.setSource(ConfigCons.SOURCE);
        redPackageMsgBody.setUserId(ChatSpUtils.instance(this).getUserId());
        redPackageMsgBody.setStationId(ChatSpUtils.instance(this).getStationId());
        redPackageMsgBody.setPayId(payId);
        redPackageMsgBody.setSentTime(TimeUtils.getCurrentTimeString());
        redPackageMsgBody.setAgentRoomHost(ChatSpUtils.instance(this).getAgentRoomHost());
        if (isPrivate) {
            redPackageMsgBody.setRoomId(privateRoomId);
            redPackageMsgBody.setPassiveUserId(passiveUserId);
        }
        chatMessage.setBody(redPackageMsgBody);
        return chatMessage;
    }

    private void sendBetSlipMessage(BetSlipMsgBody betSlipMsgBody) {
        List<BetInfo> betInfos = betSlipMsgBody.getBetInfos();
        ChatMessage chatMessage;
        if (betInfos != null && betInfos.size() > 1) {
            chatMessage = getBaseSendMessage(MsgType.BET_SLIP_LIST);
        } else {
            chatMessage = getBaseSendMessage(MsgType.BET_SLIP);
        }
        if (roomBeanMsg != null) {
            betSlipMsgBody.setRoomId(roomBeanMsg.getRoomId());
        }
        betSlipMsgBody.setSource(ConfigCons.SOURCE);
        betSlipMsgBody.setCode(ConfigCons.SHARE_BET);
        betSlipMsgBody.setUserId(ChatSpUtils.instance(this).getUserId());
        betSlipMsgBody.setMsgUUID(chatMessage.getUuid());
        betSlipMsgBody.setStationId(ChatSpUtils.instance(this).getStationId());
        betSlipMsgBody.setSentTime(TimeUtils.getCurrentTimeString());
        betSlipMsgBody.setAgentRoomHost(ChatSpUtils.instance(this).getAgentRoomHost());
        betSlipMsgBody.setUserType("1");
        betSlipMsgBody.setSpeakType("4");
        betSlipMsgBody.setMultiBet(1);
        chatMessage.setWinOrLost(this.personData);
        if (isPrivate) {
            betSlipMsgBody.setType(1);
            betSlipMsgBody.setCode(ConfigCons.GET_PRIVATE_GROUP_HISTORY);
            betSlipMsgBody.setPassiveUserId(passiveUserId);
            betSlipMsgBody.setStationId(ChatSpUtils.instance(this).getStationId());
            betSlipMsgBody.setMsgType(TextMsgBody.PRIVATE_SHARE_BET);
            betSlipMsgBody.setRoomId(privateRoomId);
        }

        chatMessage.setBody(betSlipMsgBody);
        mAdapter.addData(chatMessage);
        rvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
        presenter.sentMsg(chatMessage, roomBeanMsg);
    }

    //视频消息

    private void sendVedioMessage(final LocalMedia media) {
        final ChatMessage mMessgae = getBaseSendMessage(MsgType.VIDEO);
        //生成缩略图路径
        String vedioPath = media.getPath();
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(vedioPath);
        String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//        String height = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
//        String width = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
//        LogUtils.e("MediaMetadataRetriever", "METADATA_KEY_VIDEO_HEIGHT:" + height + "---METADATA_KEY_VIDEO_WIDTH:" + width);
        if (TextUtils.isEmpty(duration)) {
            duration = "00:00";
        } else {
            duration = TimeUtils.secToTime(Integer.parseInt(duration) / 1000);
        }
        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime();
        String imgname = System.currentTimeMillis() + ".jpg";
        String urlpath = Environment.getExternalStorageDirectory() + "/" + imgname;
        File f = new File(urlpath);
        try {
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            LogUtils.d("视频缩略图路径获取失败：", "" + e.toString());
            e.printStackTrace();
        }
        VideoMsgBody videoMsgBody = new VideoMsgBody();
        videoMsgBody.setDuration(duration);
        videoMsgBody.setLocalPath(vedioPath);
        videoMsgBody.setExtra(urlpath);
        videoMsgBody.setMsgUUID(mMessgae.getUuid());
        videoMsgBody.setMsgId(mMessgae.getMsgId());
        videoMsgBody.setSentTime(TimeUtils.getCurrentTimeString());
        mMessgae.setBody(videoMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        //模拟两秒后发送成功
//        updateMsg(mMessgae);

    }

    //图片消息
    private ChatMessage getImageMessage(final LocalMedia media) { // type = 2
        final ChatMessage mMessgae = getBaseSendMessage(MsgType.IMAGE);
        ImageMsgBody mImageMsgBody = new ImageMsgBody();
        ImageMsgBody childBody = new ImageMsgBody();
        mImageMsgBody.setThumbPath(media.getCompressPath());
        mImageMsgBody.setLocalPath(media.getCompressPath());
        mImageMsgBody.setType(2);
        mImageMsgBody.setMsgUUID(mMessgae.getUuid());
        mImageMsgBody.setMsgId(mMessgae.getMsgId());
        if (roomBeanMsg != null) {
            mImageMsgBody.setRoomId(roomBeanMsg.getRoomId());
        }
        mImageMsgBody.setSource(ConfigCons.SOURCE);
        mImageMsgBody.setCode(ConfigCons.SEND_IMAGE);
        mImageMsgBody.setUserId(ChatSpUtils.instance(this).getUserId());
        mImageMsgBody.setSentTime(TimeUtils.getCurrentTimeString());
        childBody.setThumbPath(media.getCompressPath());
        childBody.setLocalPath(media.getCompressPath());
        childBody.setType(2);
        childBody.setMsgUUID(mMessgae.getUuid());
        childBody.setMsgId(mMessgae.getMsgId());
        if (roomBeanMsg != null) {
            childBody.setRoomId(roomBeanMsg.getRoomId());
        }
        childBody.setSource(ConfigCons.SOURCE);
        childBody.setCode(ConfigCons.SEND_IMAGE);
        childBody.setUserId(ChatSpUtils.instance(this).getUserId());
        mImageMsgBody.setPicMsg(childBody);
        if (isPrivate) {
            mImageMsgBody.setType(1);
            mImageMsgBody.setCode(ConfigCons.GET_PRIVATE_GROUP_HISTORY);
            mImageMsgBody.setPassiveUserId(passiveUserId);
            mImageMsgBody.setStationId(ChatSpUtils.instance(this).getStationId());
            mImageMsgBody.setMsgType(TextMsgBody.PRIVATE_IMAGE);
            mImageMsgBody.setRoomId(privateRoomId);
        }

        mMessgae.setBody(mImageMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        rvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
        // 将聊天记录中的图片添加进集合
        presenter.loadImageList(chatMsgs);
        return mMessgae;
        //模拟两秒后发送成功
//        updateMsg(mMessgae);
    }


    //图片消息
    private ChatMessage getImageMessage(String media) { // type = 2
        final ChatMessage mMessgae = getBaseSendMessage(MsgType.IMAGE);
        ImageMsgBody mImageMsgBody = new ImageMsgBody();
        ImageMsgBody childBody = new ImageMsgBody();
        mImageMsgBody.setRecord(media);
        mImageMsgBody.setType(2);
        mImageMsgBody.setMsgUUID(mMessgae.getUuid());
        mImageMsgBody.setMsgId(mMessgae.getMsgId());
        if (roomBeanMsg != null) {
            mImageMsgBody.setRoomId(roomBeanMsg.getRoomId());
        }
        mImageMsgBody.setSource(ConfigCons.SOURCE);
        mImageMsgBody.setCode(ConfigCons.SEND_IMAGE);
        mImageMsgBody.setUserId(ChatSpUtils.instance(this).getUserId());
        mImageMsgBody.setSentTime(TimeUtils.getCurrentTimeString());
        childBody.setRecord(media);
        childBody.setType(2);
        childBody.setMsgUUID(mMessgae.getUuid());
        childBody.setMsgId(mMessgae.getMsgId());
        if (roomBeanMsg != null) {
            childBody.setRoomId(roomBeanMsg.getRoomId());
        }
        childBody.setSource(ConfigCons.SOURCE);
        childBody.setCode(ConfigCons.SEND_IMAGE);
        childBody.setUserId(ChatSpUtils.instance(this).getUserId());
        mImageMsgBody.setPicMsg(childBody);
        if (isPrivate) {
            mImageMsgBody.setType(1);
            mImageMsgBody.setCode(ConfigCons.GET_PRIVATE_GROUP_HISTORY);
            mImageMsgBody.setPassiveUserId(passiveUserId);
            mImageMsgBody.setStationId(ChatSpUtils.instance(this).getStationId());
            mImageMsgBody.setMsgType(TextMsgBody.PRIVATE_IMAGE);
            mImageMsgBody.setRoomId(privateRoomId);
        }

        mMessgae.setBody(mImageMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        rvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
        // 将聊天记录中的图片添加进集合
        presenter.loadImageList(chatMsgs);
        return mMessgae;
        //模拟两秒后发送成功
    }


    //文件消息
    private ChatMessage getFileMessage(final String path) {
        final ChatMessage mMessgae = getBaseSendMessage(MsgType.FILE);
        FileMsgBody mFileMsgBody = new FileMsgBody();
        mFileMsgBody.setLocalPath(path);
        mFileMsgBody.setDisplayName(ChatFileUtils.getFileName(path));
        mFileMsgBody.setSize(ChatFileUtils.getFileLength(path));
        mFileMsgBody.setMsgUUID(mMessgae.getUuid());
        mFileMsgBody.setSentTime(TimeUtils.getCurrentTimeString());
        mMessgae.setBody(mFileMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        //模拟两秒后发送成功
//        updateMsg(mMessgae);
        return mMessgae;
    }

    //语音消息
    private void sendAudioMessage(final String path, int time) { //type = 11
        final ChatMessage mMessgae = getBaseSendMessage(MsgType.AUDIO);
        AudioMsgBody audioMsgBody = new AudioMsgBody();
        AudioMsgBody childBody = new AudioMsgBody();
        audioMsgBody.setLocalPath(path);
        audioMsgBody.setDuration(time);
        if (roomBeanMsg != null) {
            audioMsgBody.setRoomId(roomBeanMsg.getRoomId());
        }
        audioMsgBody.setSource(ConfigCons.SOURCE);
        audioMsgBody.setCode(ConfigCons.SEND_IMAGE);
        audioMsgBody.setUserId(ChatSpUtils.instance(this).getUserId());
        audioMsgBody.setSentTime(TimeUtils.getCurrentTimeString());
        audioMsgBody.setType("11");
        audioMsgBody.setMsgUUID(mMessgae.getUuid());
        audioMsgBody.setMsgId(mMessgae.getMsgId());
        childBody.setLocalPath(path);
        childBody.setDuration(time);
        if (roomBeanMsg != null) {
            childBody.setRoomId(roomBeanMsg.getRoomId());
        }
        childBody.setSource(ConfigCons.SOURCE);
        childBody.setCode(ConfigCons.SEND_IMAGE);
        childBody.setUserId(ChatSpUtils.instance(this).getUserId());
        childBody.setSentTime(TimeUtils.getCurrentTimeString());
        childBody.setType("11");
        childBody.setMsgUUID(mMessgae.getUuid());
        childBody.setMsgId(mMessgae.getMsgId());
        audioMsgBody.setAudioMsg(childBody);
        mMessgae.setBody(audioMsgBody);
        //开始发送
        mAdapter.addData(mMessgae);
        rvChatList.scrollToPosition(mAdapter.getItemCount() - 1);
        //模拟两秒后发送成功
//        updateMsg(mMessgae);
        presenter.upLoadFile(mMessgae, "amr", fileHandlerListener);
    }


    private void updateMsg(final ChatMessage mMessgae) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //更新单个子条目
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    ChatMessage mAdapterMessage = mAdapter.getData().get(i);
                    if (mMessgae.getUuid().equals(mAdapterMessage.getUuid())) {
                        mAdapter.notifyItemChanged(i);
                    }
                }
            }
        });
    }

    private void updateFileMsg(final UpLoadFileBean upLoadFileBean, final MsgType type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //更新单个子条目
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    ChatMessage mAdapterMessage = mAdapter.getData().get(i);
                    MsgType msgType = mAdapterMessage.getMsgType();
                    if (msgType == type) {
                        FileMsgBody fileMsgBody = (FileMsgBody) mAdapterMessage.getBody();
                        String localPath = fileMsgBody.getLocalPath();
                        if (upLoadFileBean != null && localPath != null && localPath.equals(upLoadFileBean.getFileString())) {
                            switch (type) {
                                case IMAGE:
                                    ((ImageMsgBody) fileMsgBody).setRecord(upLoadFileBean.getFileCode());
                                    ((ImageMsgBody) fileMsgBody).getPicMsg().setRecord(upLoadFileBean.getFileCode());
                                    break;
                                case AUDIO:
                                    ((AudioMsgBody) fileMsgBody).setRecord(upLoadFileBean.getFileCode());
                                    ((AudioMsgBody) fileMsgBody).getAudioMsg().setRecord(upLoadFileBean.getFileCode());
                                    break;
                            }
                            fileMsgBody.setAgentRoomHost(ChatSpUtils.instance(ChatMainActivity.this).getAgentRoomHost());
                            presenter.sentMsg(mAdapterMessage, roomBeanMsg);
                        }
                    }
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().post(new CommonEvent(EventCons.FINISH_ACTIVITY));
        super.onDestroy();
        if (chatMessageReceiver != null) {
            unregisterReceiver(chatMessageReceiver);
        }
        // 释放资源
        if (chatSysConfig != null && chatSysConfig.getSwitch_new_msg_notification().equals("0")) {
            //不接收后台消息通知，断开Socket
            presenter.disconnectSocket();
        }
        if (btnAudio != null) {
            btnAudio.release();
        }
        if (views != null) {
            views.clear();
            views = null;
        }
        if (welcomeViews != null) {
            welcomeViews.clear();
        }
        MediaManager.release();
        cancelTimer();

        EventBus.getDefault().post(new RefreshFloatMenuEvent(true));

//        if (serviceConnection != null) {
//            unbindService(serviceConnection);
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CommonEvent event) {
        switch (event.getTag()) {
            case EventCons.DIALOG_DISMISS:
                CommonUtils.hideSoftInput(ChatMainActivity.this, etContent);
                break;
            case EventCons.GF_BET_MSG:
                //官方投注成功消息
                if ((roomNotSpeaking || banTalk) && !isPrivate) {
                    showToast("禁言中无法分享注单!");
                    return;
                }
                List<BetSlipMsgBody> betSlipMsgBodies = (List<BetSlipMsgBody>) event.getData();
                for (BetSlipMsgBody betSlipMsgBody : betSlipMsgBodies) {
                    sendBetSlipMessage(betSlipMsgBody);
                }
                break;
            case EventCons.XY_BET_MSG:
            case EventCons.SHARE_BET:
                //信用投注成功消息
//                if (roomNotSpeaking) {
//                    showToast("禁言中无法分享注单!");
//                    return;
//                }
//                BetSlipMsgBody betSlipMsgBody = (BetSlipMsgBody) event.getData();
//                sendBetSlipMessage(betSlipMsgBody);
//                break;
                //历史注单记录里面分享出来的注单
                if ((roomNotSpeaking || banTalk) && !isPrivate) {
                    showToast("禁言中无法分享注单!");
                    return;
                }
                BetSlipMsgBody betSlipMsgBody = (BetSlipMsgBody) event.getData();
                sendBetSlipMessage(betSlipMsgBody);
                break;

            case EventCons.DELETE_IMAGES:
                //删除图片
                ChatCollectionImagesModel chatCollectionImagesModel = new ChatCollectionImagesModel();
                chatCollectionImagesModel.setOption("3");
                chatCollectionImagesModel.setImageIds((String) event.getData());
                chatCollectionImagesModel.setUserId(ChatSpUtils.instance(ChatMainActivity.this).getUserId());
                presenter.saveImages(chatCollectionImagesModel);
                break;
            case EventCons.SEND_IMAGES:
                //发送图片
                if (chatSysConfig.getSwitch_send_image_show().equals("0") || TextUtils.isEmpty(ChatSpUtils.instance(this).getSendImage())
                        || !ChatSpUtils.instance(this).getSendImage().equals("1")) {
                    // 禁止发送图片
                    showToast("您无法在该房间发送图片，请联系客服！");
                    return;
                }
                String tag = (String) event.getData();
                String[] split = tag.split(",");
                for (String s : split) {
                    ChatMessage imageMessage = getImageMessage(s);
                    presenter.sentMsg(imageMessage, roomBeanMsg);
                }

                break;
            case EventCons.UNREAD_MESSAGE:
                int count = (int) event.getData();
                ivRedTips.setText(count + "");
                ivRedTips.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
                break;

        }
    }


    /**
     * 初始化悬浮框
     */
    private void initSinglePageFloatball() {
        float_ball.setClickable(true);
        List<FloatBallBean> list = new ArrayList<>();
        //添加列表项数据，包括名称和ICON。
        if (chatSysConfig != null && "1".equals(chatSysConfig.getSwitch_winning_list_show())) {
            FloatBallBean winningList = new FloatBallBean();
            winningList.setImg(R.drawable.icon_rank_chat);
            winningList.setTitle("排行榜");
            list.add(winningList);
        }
        if(chatSysConfig != null && "1".equalsIgnoreCase(chatSysConfig.getSwitch_today_earnings_list_info())){
            FloatBallBean profit = new FloatBallBean();
            profit.setImg(R.drawable.icon_profit);
            profit.setTitle("盈利");
            list.add(profit);
        }
        if (chatSysConfig != null && "1".equalsIgnoreCase(chatSysConfig.getSwitch_check_in_show())) {
            FloatBallBean sign = new FloatBallBean();
            sign.setImg(R.drawable.icon_sign_chat);
            sign.setTitle("签到");
            list.add(sign);
        }
        FloatBallBean kefu = new FloatBallBean();
        kefu.setImg(R.drawable.icon_kefu_chat);
        kefu.setTitle("客服");
        list.add(kefu);
        if (chatSysConfig != null && "1".equalsIgnoreCase(chatSysConfig.getSwitch_plan_user_show())) {
            FloatBallBean masterplan = new FloatBallBean();
            masterplan.setImg(R.drawable.icon_master_plan_chat);
            masterplan.setTitle("导师计划");
            list.add(masterplan);
        }
        if (chatSysConfig != null && "1".equalsIgnoreCase(chatSysConfig.getSwitch_long_dragon_show())) {
            FloatBallBean changlong = new FloatBallBean();
            changlong.setImg(R.drawable.icon_changlong);
            changlong.setTitle("长龙");
            list.add(changlong);
        }

        if(presenter.getLoginChatBean() != null && presenter.getLoginChatBean().getSource().getUserType() == 4){
            //如果是前台管理员，要显示审核名单功能
            FloatBallBean audit = new FloatBallBean();
            audit.setImg(R.drawable.audit);
            audit.setTitle("审核");
            list.add(audit);
        }

        final int count;
        //列表项展示上限高度为8个，如果高于八个，则只显示八个，但是可以通过滑动展示其他的子项。
        if (list.size() > 8) {
            count = 8;
        } else {
            count = list.size();
        }
        MainFloatAdapter adapter = new MainFloatAdapter(this, list);
        rec_float.setLayoutManager(new LinearLayoutManager(this));
        rec_float.setAdapter(adapter);
        FrameLayout.LayoutParams rps = (FrameLayout.LayoutParams) rec_float.getLayoutParams(); //设置悬浮球列表的宽高和偏移量
        rps.height = float_ball.getHeight() * count + DisplayUtil.dip2px(this, 4 * count);
        rps.width = float_ball.getWidth();
        rps.setMargins(0, 0, 0, -float_ball.getHeight() * count - DisplayUtil.dip2px(this, 4 * count));
        rec_float.setLayoutParams(rps);
        RelativeLayout.LayoutParams rcs = (RelativeLayout.LayoutParams) fr_float.getLayoutParams();//设置父布局的宽高属性
        rcs.height = float_ball.getHeight() * (count + 1) + DisplayUtil.dip2px(this, 4 * count);
        fr_float.setLayoutParams(rcs);
        float_ball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animator;
                if (isshowfloat) {
                    //收回列表动画
                    animator = ObjectAnimator.ofFloat(rec_float, "translationY", rec_float.getHeight() + float_ball.getHeight());
                    isshowfloat = false;
                } else {
                    //弹出列表动画
                    animator = ObjectAnimator.ofFloat(rec_float, "translationY", -rec_float.getHeight() - float_ball.getHeight());
                    isshowfloat = true;
                }
                animator.setDuration(500);//执行时间
//                animator.setInterpolator(new LinearInterpolator());//插值器
                animator.setRepeatCount(0);//-1 代表无限循环执行
                animator.start();

            }
        });
        adapter.setOnItemClickListencer(new MainFloatAdapter.OnItemAitClickListener() {
            @Override
            public void onItemAitClick(String item) {
                switch (item) {
                    case "排行榜":
                        startActivity(new Intent(ChatMainActivity.this, ChatWinningListDialogActivity.class));
                        break;
                    case "盈利":
                        presenter.onClickTodayProfit();
                        break;
                    case "签到":
//                        Intent intent = new Intent();
//                        intent.setAction(BuildConfig.package_name + ".SignInActivity");
//                        intent.addCategory(Intent.CATEGORY_DEFAULT);
//                        startActivity(intent);
                        presenter.sign();
                        break;
                    case "客服":
                        String linkUrl = "";
                        if (chatSysConfig != null) {
                            linkUrl = chatSysConfig.getName_station_online_service_link();
                        }
                        if (Utils.isEmptyString(linkUrl)) {
                            ToastUtil.showToast(ChatMainActivity.this, "暂无在线客服");
                            return;
                        }
                        Intent intent = new Intent();
                        intent.putExtra("url", linkUrl);
                        intent.setAction(BuildConfig.package_name + ".Live800ChattingActivity");
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        startActivity(intent);
                        break;
                    case "导师计划":
                        presenter.getMasterPlan(roomBeanMsg.getRoomId());
                        break;
                    case "长龙":
                        presenter.getLongDragon(ChatSpUtils.instance(ChatMainActivity.this).getStationId());
                        break;
                    case "审核":
                        presenter.getAuditList();
                        break;
                }
            }
        });

    }

    /**
     * 初始化私聊列表
     *
     * @param userType
     */
    private void initPrivateConversationList(int userType) {
        presenter.getPrivateConversation(passiveUserId, userType, passiveUserName, null, "1");
    }

    /**
     * 将私聊消息设为已读
     */
    private void setPrivateMessagesRead() {
        presenter.getPrivateConversation(passiveUserId, Integer.parseInt(ChatSpUtils.instance(this).getUSER_TYPE()), "", privateRoomId, "3");
    }


    /**
     * 获取私聊列表
     */
    private void getPrivateConversationList() {
        presenter.getPrivateConversation("", 1, null, "", "2");
    }

    private int countOfTimeType(List<ChatMessage> messages){
        int count = 0;
        for(ChatMessage m: messages){
            if(m.getMsgType() == MsgType.MSG_TIME){
                count++;
            }
        }

        return count;
    }

    private class DragListener implements View.OnTouchListener{
        private float downY;
        private float downRawY;
        private boolean isDraggingFloatBall;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    downY = event.getY();
                    downRawY = event.getRawY();
                    isDraggingFloatBall = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int deltaY = (int) (event.getY() - downY);
                    fr_float.layout(fr_float.getLeft(), fr_float.getTop()+deltaY, fr_float.getRight(), fr_float.getBottom()+deltaY);
                    if(Math.abs(event.getRawY() - downRawY) > 100){
                        isDraggingFloatBall = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if(isDraggingFloatBall){
                        //阻止onClick事件触发
                        return true;
                    }
                    break;
            }

            return false;
        }
    }
}
