package crazy_wrapper.Crazy.network;


import android.os.SystemClock;
import android.util.LruCache;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import crazy_wrapper.Crazy.CrazyException;
import crazy_wrapper.Crazy.CrazyResponse;
import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.CrazyTimeOutException;
import crazy_wrapper.Crazy.RequestQueue;
import crazy_wrapper.Crazy.ResponseConverter;
import crazy_wrapper.Crazy.Utils.Utils;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;

import static crazy_wrapper.Crazy.CrazyResponse.CACHE_REQUEST;

/**
 * Created by Administrator on 2016/11/8.
 */
public abstract class NetworkConnection {

    protected static final int CONN_TIMEOUT = 30 * 1000;
    protected static final int SOCK_TIMEOUT = 30 * 1000;
    protected String protocol;
    public static final String TAG = NetworkConnection.class.getSimpleName();

    protected static String UPLOAD_STREAM_CONTENT_TYPE = "multipart/form-data";
    protected static int UPLOAD_STREAM_TIME_OUT = 10 * 10000000; //超时时间
    protected static String UPLOAD_STREAM_PREFIX = "--";
    protected static String UPLOAD_STREAM_LINE_END = "\r\n";
    protected final String BOUNDARY = UUID.randomUUID().toString();

    /**
     * run real connection
     *
     * @param crazyRequest
     * @return
     * @throws CrazyException
     */
    public abstract <T extends CrazyResult> SessionResponse<T> runConnection(CrazyRequest<T> crazyRequest) throws CrazyException, IOException;

    /**
     * 将旧接口的网络请求方式对接到CrazyRequest上
     * create connection with crazy request
     *
     * @param crazyRequest
     * @return SessionResponse an enterprise response
     * zhangy add 20161101 to adapter old app request into crazy request
     */
    protected HttpURLConnection runNetworkConnection(CrazyRequest<?> crazyRequest) throws CrazyTimeOutException, IOException {

        if (crazyRequest == null) {
            return null;
        }
        String url = crazyRequest.getUrl();
        if (Utils.isEmptyString(url)) {
            return null;
        }
        if (crazyRequest.getExecuteMethod() != CrazyRequest.ExecuteMethod.GET.ordinal()) {
            url = getBaseUrl(url);
        }

//        OkHttpClient client = new OkHttpClient.Builder().dns()

        trustAllHosts();//dont vertify ssl certificate....
        URL urlURL = null;
        HttpURLConnection httpConn = null;
        try {
            urlURL = new URL(url);
            httpConn = (HttpURLConnection) urlURL.openConnection();
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setConnectTimeout(CONN_TIMEOUT);
            httpConn.setReadTimeout(SOCK_TIMEOUT);
            httpConn.setDoInput(true);
            httpConn.addRequestProperty("Accept-Charset", HTTP.UTF_8);
            httpConn.addRequestProperty("Accept", "application/json");

            if (Utils.isEmptyString(crazyRequest.getContentType())) {
                httpConn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            } else {
                httpConn.addRequestProperty("Content-Type", crazyRequest.getContentType());
            }
            httpConn.addRequestProperty("Connection", "Keep-Alive");
            httpConn.addRequestProperty("Accept-Language", HTTP.UTF_8);
            //httpConn.addRequestProperty("X-Requested-With", "true");
            //httpConn.addRequestProperty("Cookie", "sid=" + GuardPreference.getinstance(context).getToken());
            //httpConn.addRequestProperty("user-agent", "android/" + Utils.getVersionName(context));
            Map<String, String> headers = crazyRequest.getHeaders();
            if (headers != null) {
                for (String pKey : headers.keySet()) {
                    httpConn.addRequestProperty(pKey, headers.get(pKey));
                }
            }
            if (crazyRequest.getExecuteMethod() == CrazyRequest.ExecuteMethod.GET.ordinal()) {
                httpConn.setRequestMethod("GET");
                httpConn.setUseCaches(true);
            } else if (crazyRequest.getExecuteMethod() == CrazyRequest.ExecuteMethod.FORM.ordinal()) {
                httpConn.setRequestMethod("POST");
                httpConn.setDoOutput(true);
                httpConn.setUseCaches(false);
            } else if (crazyRequest.getExecuteMethod() == CrazyRequest.ExecuteMethod.BODY.ordinal()) {
                httpConn.setRequestMethod("POST");
                //转换为字节数组
                byte[] data = (crazyRequest.getBody()).getBytes();
                // 设置文件长度
                httpConn.setRequestProperty("Content-Length", String.valueOf(data.length));
                httpConn.setDoOutput(true);
                httpConn.setUseCaches(false);
            }

            /**
             * if the crazy request protocol is upload
             * we set some attach header for http connection which is need for upload stream
             */
//            if (crazyRequest.getProtocol() == CrazyRequest.Protocol.UPLOAD.ordinal()){
//                httpConn.setRequestProperty("Content-Type", UPLOAD_STREAM_CONTENT_TYPE + ";boundary=" + BOUNDARY);
//                httpConn.setDoInput(true);
//                httpConn.setDoOutput(true);
//                httpConn.setUseCaches(false);
//            }
            try {
                httpConn.connect();
            } catch (Exception e) {
                Utils.LOG(TAG, "http connect error == " + e.getMessage());
                Utils.writeLogToFile(TAG, "http connect error == " + e.getMessage());
                CrazyTimeOutException exception = new CrazyTimeOutException("request cancel! location: http connection socket timeout", "100");
                exception.setOriginExceptionMsg(e.toString());
                throw exception;
            }
            if (crazyRequest.getProtocol() != CrazyRequest.Protocol.UPLOAD.ordinal()) {
                if (crazyRequest.getExecuteMethod() != CrazyRequest.ExecuteMethod.GET.ordinal()) {
                    PrintWriter pw = new PrintWriter(httpConn.getOutputStream());
                    if (crazyRequest.getExecuteMethod() == CrazyRequest.ExecuteMethod.FORM.ordinal()) {
                        List<NameValuePair> params = URLEncodedUtils.parse(URI.create(crazyRequest.getUrl()), Utils.CHAR_FORMAT);
                        String p = URLEncodedUtils.format(params, Utils.CHAR_FORMAT);
                        pw.print(p);
                    } else if (crazyRequest.getExecuteMethod() == CrazyRequest.ExecuteMethod.BODY.ordinal()) {
                        //建立输入流，向指向的URL传入参数
                        OutputStream outputStream = httpConn.getOutputStream();
                        String body = crazyRequest.getBody();
                        outputStream.write(body.getBytes("utf-8"));
                        outputStream.flush();
                        outputStream.close();
                    }
                    pw.flush();
                    pw.close();
                }
            }
            return httpConn;
        } catch (IOException e) {
            e.printStackTrace();
            if (httpConn != null) {
                httpConn.disconnect();
            }
            throw e;
        }
    }

    /**
     * trust all cert when request is https protocol
     */
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Utils.LOG(TAG, "checkClientTrusted");
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Utils.LOG(TAG, "checkServerTrusted");
            }
        }};

        // Install the all-trusting trust manager
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

            //            SSLContext sc = SSLContext.getInstance("TLS");
            //            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            //            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * find the crazy request result from cahce.
     * if hit cache at here,we directly return this result to UI caller
     *
     * @param crazyRequest
     * @return
     */
    protected <T extends CrazyResult> SessionResponse<T> findFromCache(CrazyRequest<T> crazyRequest) throws IOException, CrazyException {

        long startNetworkTime = SystemClock.elapsedRealtime();
        boolean shouldCache = crazyRequest.ismShouldCache();
        LruCache lruCache = RequestQueue.getInstance().getCache();
        if (shouldCache && !crazyRequest.isHitFromCache()) {
            /** get result from cache first**/
            CrazyRequest.Entry entry = (CrazyRequest.Entry) lruCache.get(Utils.stringToMD5(crazyRequest.getUrl()));
            if (entry != null && !entry.isExpired() && !Utils.isEmptyString(entry.resultString)) {
                Utils.writeLogToFile(TAG, "fetch request string result from cache,key = " + crazyRequest.getUrl());
                CrazyResult<T> crazyResult = convertResponse(crazyRequest, 200, entry.resultString);
                CrazyResponse crazyResponse = new CrazyResponse(crazyRequest.getUrl(),
                        crazyRequest.getSeqnumber(), crazyResult, SystemClock.elapsedRealtime() - startNetworkTime,
                        CACHE_REQUEST, crazyRequest.isLastRequest());
                return crazyRequest.parseCrazyResponse(crazyResponse);
            }
            if (entry != null && entry.isExpired()) {
                lruCache.remove(crazyRequest.getUrl());
                Utils.writeLogToFile(TAG, "the request cache is expired expired period time = ,"
                        + crazyRequest.getCachePeroid() + ",key = " + crazyRequest.getUrl());
            }
        }
        return null;
    }

    protected <T extends CrazyResult<T>> CrazyResult convertResponse(CrazyRequest<T> request, int httpCode, String response) throws IOException, CrazyException {
        if (request == null) {
            return null;
        }
        CrazyResult cresult = new CrazyResult<T>();
        /** we convert the string result to user specify result type**/
        if (request.getConvertFactory() != null) {
            if (httpCode == 200) {
                ResponseConverter<String, T> successConverter = request.getConvertFactory().responseBodyConverter();
                cresult.crazySuccess = true;
                cresult.result = successConverter.convert(response);
            } else {
                cresult.error = response;
            }
        } else {
            if (httpCode == 200) {
                cresult.result = response;
                cresult.crazySuccess = true;
            } else {
                cresult.error = response;
            }
        }
        cresult.statusCode = String.valueOf(httpCode);
        return cresult;
    }

    /**
     * 截取请求地址中的相对路径部分
     *
     * @param url
     * @return
     */
    private String getBaseUrl(String url) {
        if (Utils.isEmptyString(url)) {
            return null;
        }
        if (url.indexOf("?") != -1) {
            String baseUrl = url.substring(0, url.lastIndexOf("?"));
            return baseUrl;
        }
        return url;
    }

}
