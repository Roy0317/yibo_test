package com.example.anuo.immodule.interfaces.iview;

import com.example.anuo.immodule.bean.AuthorityBean;
import com.example.anuo.immodule.bean.ChatCollectionImagesBean;
import com.example.anuo.immodule.bean.ChatHistoryMessageBean;
import com.example.anuo.immodule.bean.ChatLongDragonBean;
import com.example.anuo.immodule.bean.ChatLotteryBean;
import com.example.anuo.immodule.bean.ChatLotteryDetailBean;
import com.example.anuo.immodule.bean.ChatPersonDataBean;
import com.example.anuo.immodule.bean.ChatPersonPhotoListBean;
import com.example.anuo.immodule.bean.ChatPrivateConversationBean;
import com.example.anuo.immodule.bean.ChatPrivateMessageBean;
import com.example.anuo.immodule.bean.ChatQuickMessageBean;
import com.example.anuo.immodule.bean.ChatRemarkBean;
import com.example.anuo.immodule.bean.ChatRoomListBean;
import com.example.anuo.immodule.bean.ChatRoomNoticeBean;
import com.example.anuo.immodule.bean.ChatSendMsg;
import com.example.anuo.immodule.bean.ChatShareDataBean;
import com.example.anuo.immodule.bean.ChatSignDataBean;
import com.example.anuo.immodule.bean.ChatToolPermissionBean;
import com.example.anuo.immodule.bean.ChatUserListBean;
import com.example.anuo.immodule.bean.GetAuditListBean;
import com.example.anuo.immodule.bean.GrabRedPackageBean;
import com.example.anuo.immodule.bean.JoinChatRoomBean;
import com.example.anuo.immodule.bean.LoginChatBean;
import com.example.anuo.immodule.bean.LotteryDownBean;
import com.example.anuo.immodule.bean.MsgType;
import com.example.anuo.immodule.bean.RedPackageDetailBean;
import com.example.anuo.immodule.bean.SendMsgBean;
import com.example.anuo.immodule.bean.TodayProfitResponse;
import com.example.anuo.immodule.bean.UpLoadFileBean;
import com.example.anuo.immodule.interfaces.iview.base.IChatBaseView;
import com.example.anuo.immodule.jsonmodel.ChatJoinPrivateRoomMsg;
import com.example.anuo.immodule.jsonmodel.LotteryHistoryResultResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 * <p>
 * Author:anuo
 * Date  :20/06/2019
 * Desc  :com.example.anuo.immodule.interfaces.iview
 */
public interface IIChatMainView extends IChatBaseView {
    void onGetCharge(String money);

    void onGetChatImage(ArrayList<String> imageList, HashMap<Integer, Integer> imagePosition);

    void onGetChatRoomList(ChatRoomListBean chatRoomListBean);

    void onJoinChatRoom(JoinChatRoomBean joinChatRoomBean);

    void onSendSuccess(String msgUUID);

    void onSendFail();

    void onSendFailMsg(String msgUUID);

    void onReceiverMsg(ChatSendMsg chatSendMsg, String source, boolean isPrivateMsg);

    void onPingEvent();

    void onUploadFail(MsgType type);

    void onUploadFail(UpLoadFileBean upLoadFileBean, MsgType type);

    void onUploadSuccess(UpLoadFileBean upLoadFileBean, MsgType type);

    void onSendRedPackage(SendMsgBean msgBean);

    void onGrabRedPackage(GrabRedPackageBean packageBean);

    void onRedPackageDetail(RedPackageDetailBean detailBean);

    void onGetLotteryList(ChatLotteryBean chatLotteryBean);

    void onGetNotice(ChatRoomNoticeBean chatRoomNoticeBean);

    void onGetLotteryDetail(LotteryDownBean chatLotteryDetailBean);

    void onGetHistoryFail();

    void onGetHistoryMessage(ChatHistoryMessageBean chatHistoryMessageBean);

    void onGrabed(String payId);

    void onGrabOut(String payId);

    void onLoadingImg(int precent, String uuid); //进度

    void onGetViolateWords(List<String> vWordsList); //禁言词

    void onReceiverWinningMsg(ChatSendMsg chatSendMsg);

    void onLoginSuc(LoginChatBean loginChatBean);

    void onJoinRoomNotice(String nickName);

    void onConnectSuccess(AuthorityBean authorityBean);

    void onForceOffline();

    void getPersonData(ChatPersonDataBean chatPersonDataBean);

    void ModifyPersonData(boolean isSuc);

    void onAgentRoom(ChatRoomListBean.SourceBean.DataBean roomEntify);

    void onGetQuickMessages(ChatQuickMessageBean chatQuickMessageBean);

    void onCollectionsImages(ChatCollectionImagesBean chatCollectionImagesBean);

    void onGetOnlineUser(ChatUserListBean chatUserListBean);

    void onShareData(ChatShareDataBean chatShareDataBean);

    void onShareFail();

    void onGetManagerList(ChatUserListBean managerListBean);

    void onSign(ChatSignDataBean bean);

    void onBanSpeak(String speakingClose, String userId, boolean isDeleteMessage, int i, String msg);

    void onStopRefresh();

    void onUpdateConfig();

    void initPrivateConversation(ChatPrivateConversationBean conversationBean);

    void onRemarkResult(ChatRemarkBean chatRemarkBean, String passiveUserID, String remark);

    void onGetMasterPlan(ChatHistoryMessageBean masterPlans);

    void getPhotoList(ChatPersonPhotoListBean chatPersonPhotoListBean);

    void onGetBanSuccessful(int speakingClose, String userId, boolean isDeleteMessage);

    void onBanRoomSpeak(boolean isBanSpeak);

    void onMessageSendSelf(String msgId, String msgUUID);

    void onMessageRecall(String msgId);

    void onGetToolPermissionSuccess(List<ChatToolPermissionBean.SourceBean.ToolPermissionBean> toolPermission);

    void onConfigRoom(ChatRoomListBean.SourceBean.DataBean userConfigRoom);

    void onJoinPrivateRoomSuccess(ChatJoinPrivateRoomMsg privateRoomMsg);

    void onGetPrivateChatHistoryMessagesSuccess(ChatPrivateMessageBean chatPrivateMessageBean);

    void applyFail(String msg);

    void onGetLongDragon(List<ChatLongDragonBean.SourceBean.LongDragonBean> longDragon);

    void onGetAuditList(List<GetAuditListBean.UserAuditItem> auditItems);

    void onGetAuditListNull();

    void showLotteryHistoryResults(LotteryHistoryResultResponse response, String cpBianma, String cpType);

    void onGetTodayProfitList(List<TodayProfitResponse.Prize> prizeList);
}
