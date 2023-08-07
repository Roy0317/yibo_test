package crazy_wrapper.Crazy.network;

import android.os.SystemClock;
import android.util.LruCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;

import crazy_wrapper.Crazy.CrazyException;
import crazy_wrapper.Crazy.CrazyResponse;
import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.CrazyTimeOutException;
import crazy_wrapper.Crazy.RequestQueue;
import crazy_wrapper.Crazy.Utils.Utils;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;

import static crazy_wrapper.Crazy.CrazyResponse.NORMAL_REQUEST;

/**
 * Created by zhangy on 2016/10/28.
 * we optimize all network request oprate
 * 1, use HttpUrlConnection which is recommanded by android offical-web
 * 2, set an proxy server when people use
 * 3, we use exception follow to known all error when perform network request.
 * 4, add zhangy 20161117: use memory save custom byte[] pool to improve GC performance
 */
public class StringHandler extends NetworkConnection {

    private static final String TAG = StringHandler.class.getSimpleName();

    ByteArrayPool byteArrayPool;//memory save byte array pool which can improve
    //GC performance in copy operation.
    private static int DEFAULT_POOL_SIZE = 4096;

    public StringHandler() {
        // If a pool isn't passed in, then build a small default pool that will give us a lot of
        // benefit and not use too much memory.
        byteArrayPool = new ByteArrayPool(DEFAULT_POOL_SIZE);
    }

    /**
     * @param byteArrayPool a buffer pool that improves GC performance in copy operations
     */
    public StringHandler(ByteArrayPool byteArrayPool) {
        this.byteArrayPool = byteArrayPool;
    }

    @Override
    public <T extends CrazyResult> SessionResponse<T> runConnection(CrazyRequest<T> crazyRequest)
            throws CrazyException, IOException {

        long startNetworkTime = SystemClock.elapsedRealtime();

        if (!crazyRequest.isHitFromCache()) {
            SessionResponse<T> fromCache = findFromCache(crazyRequest);
            if (fromCache != null) {
                return fromCache;
            }
        }

        //start rquest result from network
        Utils.writeLogToFile(TAG, "the crazy request url " + URLDecoder.decode(crazyRequest.getUrl(),"UTF-8"));
        int httpCode = -1;
        try {
            HttpURLConnection httpURLConnection = runNetworkConnection(crazyRequest);
            if (httpURLConnection == null) {
                return null;
            }
            if (crazyRequest.isCanceled()) {
                Utils.LOG(TAG, "request cancel! location: runConnection() in " + StringHandler.class.getSimpleName());
                throw new CrazyException("request cancel! location: runConnection() in " + StringHandler.class.getSimpleName());
            }
            httpCode = httpURLConnection.getResponseCode();
            String strResult = null;
            //zhangy optimize 20161117 memory custome by use google recommaned saving byte[] pool
            byte[] result = entityToBytes(httpURLConnection);
            if (result != null) {
                strResult = new String(result, Utils.CHAR_FORMAT);
            }
            Utils.LOG(TAG, "the request back json = " + strResult);
            /** we convert the string result to user specify result type**/
            CrazyResult<T> cresult = null;
            //end 20161117
            Utils.writeLogToFile(TAG, "crazy request http code = " + httpCode);
            if (httpCode != 200) {
                throw new CrazyException("request http code " + httpCode);
            }
            if (!Utils.isEmptyString(strResult)) {

                cresult = convertResponse(crazyRequest, httpCode, strResult);
                /**
                 * after real network reqeust,we cache the result into cache if cache stragery allow.
                 * Created by zhangy on 2017/2/14 9:31
                 **/
                if (httpCode == 200 && crazyRequest.ismShouldCache() && cresult != null) {
                    //do cache operate according to request url later...
                    //we get a cache strategy from request. and to judge that whether we can cache this cache or not.
                    //we only cache the result json string when the request is success return.
                    CrazyRequest.CrazyStategory crazyStategory = crazyRequest.getCrazyStategory();
                    if (crazyStategory != null && crazyStategory.cacheRule(cresult)) {
                        if (!Utils.isEmptyString(strResult)) {
                            CrazyRequest.Entry entry = new CrazyRequest.Entry();
                            entry.cacheDuration = crazyRequest.getCachePeroid();
                            entry.cacheTime = System.currentTimeMillis();
                            entry.resultString = strResult;
                            LruCache lruCache = RequestQueue.getInstance().getCache();
                            if (lruCache != null) {
                                lruCache.put(Utils.stringToMD5(crazyRequest.getUrl()), entry);
                            }
                        }
                    } else {
                        Utils.writeLogToFile(TAG, "the request cache stragory not allow cache this request result");
                    }
                }
            }

            //处理完请求返回结果后，关闭连接
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            /**
             * contruct crazy response and then parse it
             * Created by zhangy on 2017/2/14 9:36
             **/
            CrazyResponse<T> crazyResponse = new CrazyResponse(crazyRequest.getUrl(),
                    crazyRequest.getSeqnumber(), cresult, SystemClock.elapsedRealtime()
                    - startNetworkTime, NORMAL_REQUEST, crazyRequest.isLastRequest());
            return crazyRequest.parseCrazyResponse(crazyResponse);
        } catch (CrazyException error) {
            String errMsg = error instanceof CrazyTimeOutException ? ((CrazyTimeOutException) error).getErrorMsg() : error.getOriginExceptionMsg();
            Utils.writeLogToFile(TAG, "http connect error " + errMsg);
            error.setErrorCode("100");
            error.setErrorResponse(errMsg);
            error.setStatusCode(String.valueOf(httpCode).equals("-1") ? "failed" : String.valueOf(httpCode));
            throw error;
        } catch (Exception error) {
            Utils.writeLogToFile(TAG, "http connect error2 " + error.getMessage());
            error.printStackTrace();
            CrazyException exception = new CrazyException();
            exception.setStatusCode(String.valueOf(httpCode).equals("-1") ? "failed" : String.valueOf(httpCode));
            exception.setOriginExceptionMsg(error.toString());
            throw exception;
        }
    }

    //zhangy add 20161117

    /**
     * Reads the contents of connectiond response into a byte[].
     */
    private byte[] entityToBytes(HttpURLConnection httpConnection) throws IOException, CrazyException {
        PoolingByteArrayOutputStream bytes =
                new PoolingByteArrayOutputStream(byteArrayPool, (int) httpConnection.getContentLength());
        byte[] buffer = null;
        InputStream in = null;
        try {
            in = httpConnection.getInputStream();
            if (in == null) {
                throw new CrazyException("connection inputStream empty");
            }
            buffer = byteArrayPool.getBuf(512);
            int count;
            while ((count = in.read(buffer)) != -1) {
                bytes.write(buffer, 0, count);
            }
            return bytes.toByteArray();
        } finally {
            try {
                // Close the InputStream and release the resources by "consuming the content".
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // This can happen if there was an exception above that left the entity in
                // an invalid state.
                Utils.LOG(TAG, "Error occured when calling close inputstream");
                throw e;
            }
            byteArrayPool.returnBuf(buffer);
            bytes.close();
        }
    }
    //end add

}
