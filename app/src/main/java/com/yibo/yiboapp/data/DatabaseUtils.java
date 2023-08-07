package com.yibo.yiboapp.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.yibo.yiboapp.entify.OrderDataInfo;
import com.yibo.yiboapp.entify.SavedGameData;
import com.yibo.yiboapp.utils.Utils;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import crazy_wrapper.Crazy.CrazyResult;

/**
 * Created by johnson on 2017/9/19.
 */

public class DatabaseUtils {

    public static final String TAG = DatabaseUtils.class.getSimpleName();
    ContentResolver mContentResolver;
    static DatabaseUtils databaseUtils;
    Context context;

    public static String[] LOGIN_PROJECTION =
            new String[] {"_id", "username", "password", "is_remember"};

    public static String[] CART_PROJECTION =
            new String[] {"_id", "play_name", "play_code", "sub_play_name", "sub_play_code", "beishu",
                    "numbers", "mode", "zhushu", "money", "save_time", "user","orderno","lotcode",
            "lottype","rate","cp_code"};

    public static String[] USUAL_GAME_PROJECTION =
            new String[] {"_id", "game_module", "lot_name", "lot_code", "lot_type", "play_name",
                    "play_code", "sub_play_name", "sub_play_code","cp_version", "ball_type",
                    "game_type","ft_play_code","bk_play_code","zr_img_url","zr_play_code","dz_img_url",
                    "dz_play_code","user","add_time","duration"};

    public static String[] EVENT_TRACK_PROJECTION =
            new String[]{"_id", "uid", "event", "timestamp", "url", "headers", "status_code", "response"};

    private DatabaseUtils(Context context) {
        this.context = context;
        mContentResolver = context.getContentResolver();
    }

    public static DatabaseUtils getInstance(Context context) {
        if (databaseUtils == null) {
            databaseUtils = new DatabaseUtils(context.getApplicationContext());
        }
        return databaseUtils;
    }

    public void saveLoginUser(String name, String pwd, boolean isAuto) {
        ContentValues values = new ContentValues();
        values.put("username", name);
        values.put("password", pwd);
        values.put("is_remember", isAuto ? 1 : 0);
        mContentResolver.insert(YiboProvider.LOGIN_URI, values);
    }

    public void saveCart(OrderDataInfo orderDataInfo) {

        ContentValues values = new ContentValues();
        values.put("play_name", orderDataInfo.getPlayName());
        values.put("play_code", orderDataInfo.getPlayCode());
        values.put("sub_play_name", orderDataInfo.getSubPlayName());
        values.put("sub_play_code", orderDataInfo.getSubPlayCode());
        values.put("beishu", orderDataInfo.getBeishu());
        values.put("numbers", orderDataInfo.getNumbers());
        values.put("mode", orderDataInfo.getMode());
        values.put("zhushu", orderDataInfo.getZhushu());
        values.put("money", orderDataInfo.getMoney());
        values.put("save_time", System.currentTimeMillis());
        values.put("lotcode", orderDataInfo.getLotcode());
        values.put("lottype", orderDataInfo.getLottype());
        values.put("rate", orderDataInfo.getRate());
        values.put("cp_code", orderDataInfo.getCpCode());
        values.put("user", Utils.isEmptyString(orderDataInfo.getUser())?
                YiboPreference.instance(context).getUsername():orderDataInfo.getUser());
        String orderno = String.valueOf(System.currentTimeMillis());
        try {
            orderno = orderno + Utils.getMD5(orderDataInfo.getNumbers());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        values.put("orderno", orderno);
        mContentResolver.insert(YiboProvider.CART_URI, values);
    }

    public void saveUsualGame(SavedGameData data) {
        ContentValues values = new ContentValues();
        values.put("game_module", data.getGameModuleCode());
        values.put("lot_name", data.getLotName());
        values.put("lot_code", data.getLotCode());
        values.put("lot_type", data.getLotType());
        values.put("play_name", data.getPlayName());
        values.put("play_code", data.getPlayCode());
        values.put("sub_play_name", data.getSubPlayName());
        values.put("sub_play_code", data.getSubPlayCode());
        values.put("cp_version", data.getCpVersion());
        values.put("duration", data.getDuration());
        values.put("ball_type", data.getBallType());
        values.put("game_type", data.getGameType());
        values.put("ft_play_code", data.getFtPlayCode());
        values.put("bk_play_code", data.getBkPlayCode());
        values.put("zr_img_url", data.getZhenrenImgUrl());
        values.put("zr_play_code", data.getPlayCode());
        values.put("dz_img_url", data.getDzImgUrl());
        values.put("dz_play_code", data.getDzPlayCode());
        values.put("add_time", System.currentTimeMillis());
        values.put("user", Utils.isEmptyString(data.getUser())?
                YiboPreference.instance(context).getUsername():data.getUser());
        mContentResolver.insert(YiboProvider.USUAL_GAME_URI, values);
    }

    public void deleteUsualGame(Context context,int gameModule) {
        String username = YiboPreference.instance(context).getUsername();
        String selection = "user = '" + username + "' and game_module = '"+gameModule+"'";
        int row = mContentResolver.delete(YiboProvider.USUAL_GAME_URI, selection, null);
        Utils.LOG(TAG, "delete game row = " + row);
    }

    public void deleteAllOrder() {
        int row = mContentResolver.delete(YiboProvider.CART_URI, "", null);
        Utils.LOG(TAG, "delete order row = " + row);
    }

    public void deleteOrder(String orderno) {
        String selection = "orderno = '"+orderno+"'";
        int row = mContentResolver.delete(YiboProvider.CART_URI, selection, null);
        Utils.LOG(TAG, "delete order row = " + row);
    }

    public boolean isBetOrderExist(String lotCode, String playCode, String subPlayCode) {

        String selection =  "lotcode = '"+lotCode+"' and play_code = '"+playCode
                +"' and sub_play_code = '"+subPlayCode+"'";

        Cursor c = mContentResolver.query(YiboProvider.CART_URI, CART_PROJECTION, selection, null,
                "save_time desc");
        try {
            if (c != null) {
                return c.getCount() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return false;
    }

    public int getOrderCount() {
        Cursor c = mContentResolver.query(YiboProvider.CART_URI, CART_PROJECTION, "", null,
                "save_time desc");
        try {
            if (c != null) {
                return c.getCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (c != null) {
                c.close();
            }
        }
        return 0;
    }

    public List<SavedGameData> getUsualCaipiaoGames(Context context) {
        return getUsualGames(context, SavedGameData.LOT_GAME_MODULE);
    }

    /**
     * 查出最近5条常用游戏记录
     * @param context
     * @return
     */
    public List<SavedGameData> getUsualGames(Context context,int module) {

        String username = YiboPreference.instance(context).getUsername();
        List<SavedGameData> info = new ArrayList<>();
        String selection = "";
        if (module == -1) {
            selection = "user = '" + username + "'";
        }else{
            selection =  "game_module = '"+module+"' and user = '"+username+"'";
        }
        Cursor c = mContentResolver.query(YiboProvider.USUAL_GAME_URI, USUAL_GAME_PROJECTION, selection, null,
                "add_time desc");
        try {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    SavedGameData data = new SavedGameData();
                    data.setGameModuleCode(c.getInt(c.getColumnIndex("game_module")));
                    data.setUser(c.getString(c.getColumnIndex("user")));
                    data.setLotName(c.getString(c.getColumnIndex("lot_name")));
                    data.setLotCode(c.getString(c.getColumnIndex("lot_code")));
                    data.setLotType(c.getString(c.getColumnIndex("lot_type")));
                    data.setPlayName(c.getString(c.getColumnIndex("play_name")));
                    data.setPlayCode(c.getString(c.getColumnIndex("play_code")));
                    data.setSubPlayName(c.getString(c.getColumnIndex("sub_play_name")));
                    data.setSubPlayCode(c.getString(c.getColumnIndex("sub_play_code")));
                    data.setCpVersion(c.getString(c.getColumnIndex("cp_version")));
                    data.setDuration(c.getLong(c.getColumnIndex("duration")));
                    data.setBallType(c.getString(c.getColumnIndex("ball_type")));
                    data.setGameType(c.getString(c.getColumnIndex("game_type")));
                    data.setFtPlayCode(c.getString(c.getColumnIndex("ft_play_code")));
                    data.setBkPlayCode(c.getString(c.getColumnIndex("bk_play_code")));
                    data.setZhenrenImgUrl(c.getString(c.getColumnIndex("zr_img_url")));
                    data.setZrPlayCode(c.getString(c.getColumnIndex("zr_play_code")));
                    data.setDzImgUrl(c.getString(c.getColumnIndex("dz_img_url")));
                    data.setDzPlayCode(c.getString(c.getColumnIndex("dz_play_code")));
                    data.setAddTime(c.getLong(c.getColumnIndex("add_time")));
                    info.add(data);
                    if (info.size() >= 6) {
                        break;
                    }
                    c.moveToNext();
                }
                return info;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return info;
    }

    public List<OrderDataInfo> getCartOrders() {
        return getCartOrders("");
    }

    public List<OrderDataInfo> getCartOrders(String lotCode) {

        List<OrderDataInfo> info = new ArrayList<>();
        String selection = "";
        if (!Utils.isEmptyString(lotCode)) {
            selection = "lotcode = '"+lotCode+"'";
        }
        Cursor c = mContentResolver.query(YiboProvider.CART_URI, CART_PROJECTION, selection, null,
                "save_time desc");
        try {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    OrderDataInfo data = new OrderDataInfo();
                    data.setPlayName(c.getString(c.getColumnIndex("play_name")));
                    data.setPlayCode(c.getString(c.getColumnIndex("play_code")));
                    data.setSubPlayName(c.getString(c.getColumnIndex("sub_play_name")));
                    data.setSubPlayCode(c.getString(c.getColumnIndex("sub_play_code")));
                    data.setBeishu(c.getInt(c.getColumnIndex("beishu")));
                    data.setNumbers(c.getString(c.getColumnIndex("numbers")));
                    data.setMode(c.getInt(c.getColumnIndex("mode")));
                    data.setZhushu(c.getInt(c.getColumnIndex("zhushu")));
                    data.setMoney(c.getFloat(c.getColumnIndex("money")));
                    data.setSaveTime(c.getLong(c.getColumnIndex("save_time")));
                    data.setUser(c.getString(c.getColumnIndex("user")));
                    data.setOrderno(c.getString(c.getColumnIndex("orderno")));
                    data.setLotcode(c.getString(c.getColumnIndex("lotcode")));
                    data.setLottype(c.getString(c.getColumnIndex("lottype")));
                    data.setRate(c.getFloat(c.getColumnIndex("rate")));
                    info.add(data);
                    c.moveToNext();
                }
                return info;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return null;
    }

    public void saveEventTrack(RequestEventTrack requestEventTrack) {
        ContentValues values = new ContentValues();
        values.put("uid", requestEventTrack.getUid());
        values.put("event", requestEventTrack.getEvent());
        values.put("timestamp", requestEventTrack.getTimestamp());
        values.put("url", requestEventTrack.getUrl());
        values.put("headers", requestEventTrack.headerToString());
        values.put("status_code", requestEventTrack.getStatusCode());
        values.put("response", requestEventTrack.getResponse());
        mContentResolver.insert(YiboProvider.EVENT_TRACK_URI, values);
    }

    public void updateEventTrack(String eventUid, CrazyResult<String> result) {
        ContentValues values = new ContentValues();
        values.put("status_code", result.statusCode);
        values.put("response", result.crazySuccess ? result.result : result.originExceptionMsg);
        mContentResolver.update(YiboProvider.EVENT_TRACK_URI, values, "uid = '" + eventUid + "'", null);
    }

    public List<RequestEventTrack> getEventTracks(){
        List<RequestEventTrack> tracks = new ArrayList<>();
        Cursor c = mContentResolver.query(YiboProvider.EVENT_TRACK_URI, EVENT_TRACK_PROJECTION,
                null, null, "_id desc limit 300");
        try{
            if(c != null && c.getCount() > 0){
                c.moveToFirst();
                while (!c.isAfterLast()){
                    RequestEventTrack track = new RequestEventTrack();
//                    track.setUid(c.getString(c.getColumnIndex("uid")));
                    track.setEvent(c.getString(c.getColumnIndex("event")));
                    track.setTimestamp(c.getString(c.getColumnIndex("timestamp")));
                    track.setHeaderString(c.getString(c.getColumnIndex("headers")));
                    track.setStatusCode(c.getString(c.getColumnIndex("status_code")));
                    track.setResponse(c.getString(c.getColumnIndex("response")));
                    track.setUrl(c.getString(c.getColumnIndex("url")));
                    tracks.add(track);
                    c.moveToNext();
                }
                return tracks;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(c != null) c.close();
        }

        return null;
    }

    public List<RequestEventTrack> getEventTrackByID(String uid){
        List<RequestEventTrack> tracks = new ArrayList<>();
        Cursor c = mContentResolver.query(YiboProvider.EVENT_TRACK_URI, EVENT_TRACK_PROJECTION, "uid=?", new String[]{uid}, null);
        try{
            if(c != null && c.getCount() > 0){
                c.moveToFirst();
                while (!c.isAfterLast()){
                    RequestEventTrack track = new RequestEventTrack();
                    track.setUid(c.getString(c.getColumnIndex("uid")));
                    track.setEvent(c.getString(c.getColumnIndex("event")));
                    track.setTimestamp(c.getString(c.getColumnIndex("timestamp")));
                    track.setHeaderString(c.getString(c.getColumnIndex("headers")));
                    track.setStatusCode(c.getString(c.getColumnIndex("status_code")));
                    track.setResponse(c.getString(c.getColumnIndex("response")));
                    track.setUrl(c.getString(c.getColumnIndex("url")));
                    tracks.add(track);
                    c.moveToNext();
                }
                return tracks;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(c != null) c.close();
        }

        return null;
    }
}
