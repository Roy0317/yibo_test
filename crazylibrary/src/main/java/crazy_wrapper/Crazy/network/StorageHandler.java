package crazy_wrapper.Crazy.network;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import crazy_wrapper.Crazy.CrazyException;
import crazy_wrapper.Crazy.CrazyResponse;
import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.Utils.Utils;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;

import java.io.IOException;
import java.net.URI;

import static crazy_wrapper.Crazy.CrazyResponse.NORMAL_REQUEST;

/**
 * Created by zhangy on 2016/11/8.
 * handle local storage time-cosume request
 */
public class StorageHandler extends NetworkConnection {

    private static final String TAG = StorageHandler.class.getSimpleName();

    @Override public <T extends CrazyResult> SessionResponse<T> runConnection(CrazyRequest<T> crazyRequest) throws CrazyException, IOException {

        long startNetworkTime = SystemClock.elapsedRealtime();
        SessionResponse<T> fromCache = findFromCache(crazyRequest);
        if (fromCache != null) {
            return fromCache;
        }
        //start fetch result from local machine
        if (crazyRequest.isCanceled()){
            Utils.LOG(TAG,"request cancel! location: runConnection() in "+StringHandler.class.getSimpleName());
            throw new CrazyException("request cancel! location: runConnection() in "+StringHandler.class.getSimpleName());
        }

        /**
         * storage url standard format :
         * (file:or content:)//db_name/table_name?curl=xxxx
         */
        String url = crazyRequest.getUrl();
        if (Utils.isEmptyString(url)) {
            Utils.LOG(TAG,"request cancel! url empty "+StringHandler.class.getSimpleName());
            throw new CrazyException("request cancel! url empty in "+StringHandler.class.getSimpleName());
        }
        URI uri = URI.create(url);
        String secheme = uri.getScheme();
        String dbName = uri.getAuthority();
        String tableName = uri.getPath();//path，所以带有前缀/
        String curl = uri.getQuery();
        String curlValue = null;
        if (curl.lastIndexOf("=") != -1) {
            curlValue = curl.substring(curl.lastIndexOf("=") + 1);
        }
        CrazyResponse<T> crazyResponse = null;
        //SQLiteDatabase sqliteDatabase = context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
        SQLiteDatabase sqliteDatabase = null;
        CrazyResult crazyResult = new CrazyResult<T>();
        if (sqliteDatabase != null) {
            if (secheme.equalsIgnoreCase("content")) {
                if (curlValue.equalsIgnoreCase("query")) {
                    sqliteDatabase.beginTransaction();
                    Cursor cursor = null;
                    try {
                        cursor = sqliteDatabase.rawQuery(crazyRequest.getAttachCommand(), null);
                        crazyResult.crazySuccess = true;
                        crazyResult.result = cursor;
                        sqliteDatabase.setTransactionSuccessful();
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (cursor != null) {
                            cursor.close();
                        }
                    } finally {
                        sqliteDatabase.endTransaction();
                    }
                } else if (curlValue.equalsIgnoreCase("delete") || curlValue.equalsIgnoreCase("update")||curlValue.equalsIgnoreCase("insert")) {
                    sqliteDatabase.beginTransaction();
                    try {
                        sqliteDatabase.execSQL(crazyRequest.getAttachCommand());
                        crazyResult.crazySuccess = true;
                        crazyResult.result = true;
                        sqliteDatabase.setTransactionSuccessful();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        sqliteDatabase.endTransaction();
                    }
                }
                //sqliteDatabase.close();
            } else if (secheme.equalsIgnoreCase("file")) {
                // TODO: 2017/2/27 load local file task......
            }
        }

        crazyResponse = new CrazyResponse(crazyRequest.getUrl(),
            crazyRequest.getSeqnumber(),crazyResult, SystemClock.elapsedRealtime()
            - startNetworkTime,NORMAL_REQUEST);
        return crazyRequest.parseCrazyResponse(crazyResponse);
    }

}
