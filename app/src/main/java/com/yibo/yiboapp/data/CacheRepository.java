package com.yibo.yiboapp.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import com.example.anuo.immodule.utils.AESUtils;
import com.example.anuo.immodule.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yibo.yiboapp.BuildConfig;
import com.yibo.yiboapp.entify.ActivesResultWraper;
import com.yibo.yiboapp.entify.KaijianEntify;
import com.yibo.yiboapp.entify.PeilvWebResult;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CacheRepository {

    private static String TAG = CacheRepository.class.getSimpleName();
    private static final String LOTTERY_FILE_NAME = "all_games_file.txt";//缓存大厅彩种信息
    private static final String LOTTERY_PLAY_INFO = "lottery_playinfo_";//缓存下注页面玩法信息资料夹
    private static final String LOTTERY_PLAY_DIR = "lottery_play_";//缓存下注页面玩法信息资料夹
    private static final String LOTTERY_ODDS_DIR = "lottery_odds_";//缓存下注页面赔率信息资料夹
    private static final String OPEN_RESULT_FILE = "open_result_file.txt";//缓存开奖公告信息
    private static final String ACQUIRE_ACTIVES_FILE = "acquire_actives_file.txt";//缓存优惠活动信息

    private static final String DEFAULT_IV = "8J5#4*6(~9!u#D&P";
    private static final String DEFAULT_KEY = "9eR)(%G&v3#TI{:#";

    private static CacheRepository instance = new CacheRepository();
    public static CacheRepository getInstance(){
        return instance;
    }

    private CacheRepository(){}

    public void clearData(Context context){
        Single.fromCallable(() -> {
            YiboPreference.instance(context).saveLotterys("");
            context.deleteFile(LOTTERY_FILE_NAME);
            context.deleteFile(OPEN_RESULT_FILE);
            context.deleteFile(ACQUIRE_ACTIVES_FILE);

            //delete all files of play rule & play odd
            File dir = context.getFilesDir();
            if(dir.isDirectory()){
                File[] files = dir.listFiles();
                for (File file: files){
                    String filename = file.getName();
                    if(filename.startsWith(LOTTERY_PLAY_DIR) ||
                            filename.startsWith(LOTTERY_PLAY_INFO) ||
                            filename.startsWith(LOTTERY_ODDS_DIR)){
                        file.delete();
                    }
                }
            }

            return true;
        }).subscribeOn(Schedulers.io())
                .subscribe((aBoolean, throwable) -> {
                    if(throwable != null) throwable.printStackTrace();
                });
    }

    public void saveLotteryData(Context context, List<LotteryData> data){
        Single.fromCallable(() -> {
            Type listType = new TypeToken<ArrayList<LotteryData>>() {}.getType();
            String lotteryJson = new Gson().toJson(data, listType);
            writeToFile(context, LOTTERY_FILE_NAME, lotteryJson);
            if(BuildConfig.DEBUG)
                writeToExternalFile(context, "/Download/" + LOTTERY_FILE_NAME, lotteryJson);
            YiboPreference.instance(context).saveLotterys(lotteryJson);
            return true;
        }).subscribeOn(Schedulers.io())
                .subscribe((aBoolean, throwable) -> {
                    if(throwable != null) throwable.printStackTrace();
                });
    }

    public Single<String> decryptAndSaveLotteryDataJson(Context context, String encrypted){
        return Single.fromCallable(() -> {
            String lotteryJson = AESUtils.decrypt(encrypted, DEFAULT_KEY, DEFAULT_IV);
//            Utils.LOG(TAG, "lotteryJson = " + lotteryJson);
            writeToFile(context, LOTTERY_FILE_NAME, lotteryJson);
            if(BuildConfig.DEBUG)
                writeToExternalFile(context, "/Download/" + LOTTERY_FILE_NAME, lotteryJson);
            YiboPreference.instance(context).saveLotterys(lotteryJson);
            return lotteryJson;
        });
    }

    public Single<List<LotteryData>> loadLotteryData(Context context){
        return Single.fromCallable((Callable<String>) () -> {
            //先从file找备份，没有的话再找sp
            String json = readFromFile(context, LOTTERY_FILE_NAME);
            if(json == null || json.isEmpty()){
                json = YiboPreference.instance(context).getLotterys();
            }
            return json;
        }).map(new Function<String, List<LotteryData>>() {
            @Override
            public List<LotteryData> apply(String json) throws Exception {
                if(json == null)
                    json = "";

                Type listType = new TypeToken<ArrayList<LotteryData>>() {}.getType();
                return new Gson().fromJson(json, listType);
            }
        });
    }

    public String getEncryptedDelegateData() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("domain", Urls.BASE_URL);
        obj.put("project", 0);//项目类型 0--易博 1-云迹
        obj.put("apiUrl", "/native/api/app/findAllGames.do");
        LogUtils.e("data",obj.toString());
        return AESUtils.encrypt(obj.toString(), DEFAULT_KEY, DEFAULT_IV);
    }

    /**
     * 缓存开奖公告
     * */
    @SuppressLint("CheckResult")
    public void saveOpenResultData(Context context, List<KaijianEntify> data){
            Single.fromCallable(() -> {
                String openResultJson = new Gson().toJson(data, new TypeToken<List<KaijianEntify>>() {}.getType());
                writeToFile(context, OPEN_RESULT_FILE, openResultJson);
                if(BuildConfig.DEBUG)
                    writeToExternalFile(context, "/Download/" + OPEN_RESULT_FILE, openResultJson);
                return true;
            }).subscribeOn(Schedulers.io())
                    .subscribe((aBoolean, throwable) -> {
                        if(throwable != null) throwable.printStackTrace();
                    });
    }

    public Single<List<KaijianEntify>> loadOpenResultData(Context context){
        return Single.fromCallable((Callable<String>) () -> {
            String json = readFromFile(context, OPEN_RESULT_FILE);
//            if(json == null || json.isEmpty()){
//                json = YiboPreference.instance(context).getLotterys();
//            }
            return json;
        }).map(new Function<String, List<KaijianEntify>>() {
            @Override
            public List<KaijianEntify> apply(@NotNull String json) throws Exception {
                Type listType = new TypeToken<ArrayList<KaijianEntify>>() {}.getType();
                return new Gson().fromJson(json, listType);
            }
        });
    }

    /**
     * 缓存优惠活动
     * */
    @SuppressLint("CheckResult")
    public void saveAcquireActivesData(Context context, List<ActivesResultWraper.ContentBean> data){
        Single.fromCallable(() -> {
            String openResultJson = new Gson().toJson(data, new TypeToken<List<ActivesResultWraper.ContentBean>>() {}.getType());
            writeToFile(context, ACQUIRE_ACTIVES_FILE, openResultJson);
            if(BuildConfig.DEBUG)
                writeToExternalFile(context, "/Download/" + ACQUIRE_ACTIVES_FILE, openResultJson);
            return true;
        }).subscribeOn(Schedulers.io())
                .subscribe((aBoolean, throwable) -> {
                    if(throwable != null) throwable.printStackTrace();
                });
    }

    public Single<List<ActivesResultWraper.ContentBean>> loadAcquireActivesData(Context context){
        return Single.fromCallable((Callable<String>) () -> {
            String json = readFromFile(context, ACQUIRE_ACTIVES_FILE);
//            if(json == null || json.isEmpty()){
//                json = YiboPreference.instance(context).getLotterys();
//            }
            return json;
        }).map(new Function<String, List<ActivesResultWraper.ContentBean>>() {
            @Override
            public List<ActivesResultWraper.ContentBean> apply(@NotNull String json) throws Exception {
                Type listType = new TypeToken<ArrayList<ActivesResultWraper.ContentBean>>() {}.getType();
                return new Gson().fromJson(json, listType);
            }
        });
    }

    private void writeToFile(Context context, String fileName, String data){
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))) {
            outputStreamWriter.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToExternalFile(Context context, String fileName, String data) throws IOException{
        OutputStreamWriter outputStreamWriter = null;
        try{
            File file = new File(Environment.getExternalStorageDirectory() + fileName);
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
            outputStreamWriter.write(data);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(outputStreamWriter != null){
                outputStreamWriter.close();
            }
        }
    }

    private String readFromFile(Context context, String fileName) throws IOException{
        String ret = "";

        InputStream inputStream = context.openFileInput(fileName);
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append("\n").append(receiveString);
            }

            inputStream.close();
            ret = stringBuilder.toString();
        }

        return ret;
    }

    public void savePlayInfo(Context context, long playID, String json){
        Single.fromCallable(() -> {
            writeToFile(context, LOTTERY_PLAY_INFO + playID, json);
            if(BuildConfig.DEBUG)
                writeToExternalFile(context, "/Download/" + LOTTERY_PLAY_INFO + playID, json);
            return true;
        }).subscribeOn(Schedulers.io())
                .subscribe((aBoolean, throwable) -> {
                    if(throwable != null) throwable.printStackTrace();
                });
    }

    public Single<String> loadLotteryPlayInfoJson(Context context, String playID){
        return Single.fromCallable((Callable<String>) () -> readFromFile(context, LOTTERY_PLAY_INFO + playID));
    }

    public void saveLotteryPlayJson(Context context, String gameCode, String json){
        Single.fromCallable(() -> {
            writeToFile(context, LOTTERY_PLAY_DIR + gameCode, json);
            if(BuildConfig.DEBUG)
                writeToExternalFile(context, "/Download/" + LOTTERY_PLAY_DIR + gameCode, json);
            return true;
        }).subscribeOn(Schedulers.io())
                .subscribe((aBoolean, throwable) -> {
                    if(throwable != null) throwable.printStackTrace();
                });
    }

    public Single<String> loadLotteryPlayJson(Context context, String gameCode){
        return Single.fromCallable((Callable<String>) () -> readFromFile(context, LOTTERY_PLAY_DIR + gameCode));
    }

    public void saveLotteryPlayOdds(Context context, String gameCode, String playCode, String lotteryType, List<PeilvWebResult> peilvs){
        String fileName = LOTTERY_ODDS_DIR + gameCode + "_" + playCode + "_type" + lotteryType;
        Single.fromCallable(() -> {
            Type listType = new TypeToken<List<PeilvWebResult>>() {}.getType();
            String json = new Gson().toJson(peilvs, listType);
            writeToFile(context, fileName, json);
            if(BuildConfig.DEBUG)
                writeToExternalFile(context, "/Download/" + fileName, json);
            return true;
        }).subscribeOn(Schedulers.io())
                .subscribe((aBoolean, throwable) -> {
                    if(throwable != null) throwable.printStackTrace();
                });
    }

    public Single<String> loadLotteryPlayOdds(Context context, String gameCode, String playCode, String lotteryType){
        String fileName = LOTTERY_ODDS_DIR + gameCode + "_" + playCode + "_type" + lotteryType;
        return Single.fromCallable((Callable<String>) () -> readFromFile(context, fileName));
    }
}
