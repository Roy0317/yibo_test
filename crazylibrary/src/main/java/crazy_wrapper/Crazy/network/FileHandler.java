package crazy_wrapper.Crazy.network;

import android.os.SystemClock;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import crazy_wrapper.Crazy.CrazyException;
import crazy_wrapper.Crazy.CrazyResponse;
import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.Utils.Utils;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;

import static crazy_wrapper.Crazy.CrazyResponse.NORMAL_REQUEST;

/**
 * Created by zhangy on 2016/11/8.
 * handle file connection request,include file download and upload
 * result the file stream save path.
 */
public class FileHandler extends NetworkConnection {

    private static final String TAG = FileHandler.class.getSimpleName();
    public static HttpClient client;
    IdleConnectionMonitorThread monitorThread;
    ByteArrayPool byteArrayPool;//memory save byte array pool which can improve
    //GC performance in copy operation.
    private static int DEFAULT_POOL_SIZE = 4096;

    public FileHandler() {
        byteArrayPool = new ByteArrayPool(DEFAULT_POOL_SIZE);
    }

    public interface FileHandlerListener {
        void onHandleStatus(CrazyRequest<?> request, long currentSize, long totalSize, boolean notError);

        long sizeOf(String path);
    }

    /**
     * 执行crazy请求
     *
     * @param crazyRequest
     * @return 返回一个CrazyResponse请求结果
     */

    @Override
    public <T extends CrazyResult> SessionResponse<T> runConnection(CrazyRequest<T> crazyRequest) throws CrazyException, IOException {

        String streams = crazyRequest.getPath();
        if (Utils.isEmptyString(streams)) {
            throw new CrazyException(TAG + "you havent put file paths, we cant runConnection");
        }
        long startNetworkTime = SystemClock.elapsedRealtime();
        HttpURLConnection httpURLConnection = runNetworkConnection(crazyRequest);
        if (httpURLConnection == null) {
            throw new CrazyException("create connection fail in runConnection!");
        }
        if (crazyRequest.isCanceled()) {
            Utils.LOG(TAG, "request cancel! location: runConnection() in " + FileHandler.class.getSimpleName());
            throw new CrazyException("request cancel! location: runConnection() in " + FileHandler.class.getSimpleName());
        }
        try {
            CrazyResult result = null;
            if (crazyRequest.getProtocol() == CrazyRequest.Protocol.UPLOAD.ordinal()) {
                //result = pushStream(httpURLConnection, crazyRequest);
                result = pushStreamWithApache(httpURLConnection, crazyRequest);
            } else if (crazyRequest.getProtocol() == CrazyRequest.Protocol.DOWNLOAD.ordinal()) {
                result = pullStream(httpURLConnection, crazyRequest);
            }
//            CrazyResult crazyResult = new CrazyResult<T>();
//            crazyResult.crazySuccess = !Utils.isEmptyString(result) ? true : false;
//            if (crazyResult.crazySuccess) {
//                crazyResult.result = result;
//            }
            CrazyResponse<T> crazyResponse = new CrazyResponse(crazyRequest.getUrl(),
                    crazyRequest.getSeqnumber(), result, SystemClock.elapsedRealtime()
                    - startNetworkTime, NORMAL_REQUEST, crazyRequest.isLastRequest());
            return crazyRequest.parseCrazyResponse(crazyResponse);
        } catch (CrazyException error) {
            throw error;
        }
    }

    /**
     * push stream to network
     *
     * @param httpURLConnection
     * @param crazyRequest
     * @return the file stream location which has been push success
     * @throws CrazyException
     */

    private String pushStream(HttpURLConnection httpURLConnection, CrazyRequest crazyRequest, String path) throws CrazyException {
        try {
            if (httpURLConnection == null || crazyRequest == null) {
                return null;
            }
            if (Utils.isEmptyString(crazyRequest.getUrl())) {
                throw new CrazyException("Your url is empty when post stream!");
            }
            if (Utils.isEmptyString(path)) {
                throw new CrazyException("Your upload file path is empty!");
            }
            File f = new File(path);
            if (!f.exists()) {
                throw new CrazyException("Your upload file is unexist!");
            }

            OutputStream outputSteam = httpURLConnection.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outputSteam);
            StringBuffer sb = new StringBuffer();
            sb.append(NetworkConnection.UPLOAD_STREAM_LINE_END);
            List<NameValuePair> params = URLEncodedUtils.parse(URI.create(crazyRequest.getUrl()), Utils.CHAR_FORMAT);

            if (params != null && !params.isEmpty()) {
                for (NameValuePair nv : params) {
                    sb.append(NetworkConnection.UPLOAD_STREAM_PREFIX).append(BOUNDARY).
                            append(NetworkConnection.UPLOAD_STREAM_LINE_END);
                    sb.append("Content-Disposition: form-data; name=" + nv.getName() + NetworkConnection.UPLOAD_STREAM_LINE_END);
                    sb.append("Content-Type: text/plain; charset=" + Utils.CHAR_FORMAT + NetworkConnection.UPLOAD_STREAM_LINE_END);
                    sb.append("Content-Transfer-Encoding: 8bit" + NetworkConnection.UPLOAD_STREAM_LINE_END);
                    sb.append(NetworkConnection.UPLOAD_STREAM_LINE_END);
                    sb.append(nv.getValue());
                    sb.append(NetworkConnection.UPLOAD_STREAM_LINE_END);
                }
            }

            sb.append(NetworkConnection.UPLOAD_STREAM_PREFIX);
            sb.append(BOUNDARY);
            sb.append(NetworkConnection.UPLOAD_STREAM_LINE_END);
            sb.append("Content-Disposition: form-data; name=file; filename=" + f.getName()
                    + NetworkConnection.UPLOAD_STREAM_LINE_END);
            sb.append("Content-Type: application/octet-stream; charset=" + Utils.CHAR_FORMAT);
            sb.append(NetworkConnection.UPLOAD_STREAM_LINE_END);
            sb.append("Content-Transfer-Encoding: binary");
            sb.append(NetworkConnection.UPLOAD_STREAM_LINE_END);
            sb.append(NetworkConnection.UPLOAD_STREAM_LINE_END);
            dos.write(sb.toString().getBytes());

            InputStream is = new FileInputStream(f);
            byte[] bytes = new byte[1024];

            long totalbytes = f.length();
            long curbytes = 0;
            int len = 0;
            int currentProgressCount = 0;

            FileHandlerListener fileHandlerListener = crazyRequest.getFileHandlerListener();
            //initFileHandler(fileHandlerListener);

            while ((len = is.read(bytes)) != -1) {
                curbytes += len;
                dos.write(bytes, 0, len);
                int curProgress = (int) ((curbytes * 100) / totalbytes);
                if (curProgress != currentProgressCount) {
                    currentProgressCount = curProgress;
                    if (curProgress != 100) {
                        doSend(crazyRequest, curbytes, totalbytes, true);
                    }
                }
            }
            is.close();
            dos.write(NetworkConnection.UPLOAD_STREAM_LINE_END.getBytes());
            byte[] end_data = (NetworkConnection.UPLOAD_STREAM_PREFIX + BOUNDARY +
                    NetworkConnection.UPLOAD_STREAM_PREFIX + NetworkConnection.UPLOAD_STREAM_LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            dos.close();

            int code = httpURLConnection.getResponseCode();
            if (code == 200) {
                sb.setLength(0);
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                httpURLConnection.disconnect();
                doSend(crazyRequest, totalbytes, totalbytes, true);
                return path;
            } else {
                throw new CrazyException("send stream response code != 200");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CrazyException error) {
            error.printStackTrace();
            Utils.writeExceptionToFile(TAG, error);
            throw error;
        }
        return null;
    }

    /**
     * 使用apache HttpPost兼容文件上传(临时替代方法)
     *
     * @param httpURLConnection
     * @param crazyRequest
     * @return fileCode which has been pushed
     * @throws CrazyException
     */
    private CrazyResult pushStreamWithApache(HttpURLConnection httpURLConnection, CrazyRequest<?> crazyRequest) throws CrazyException {
        try {
            if (httpURLConnection == null || crazyRequest == null) {
                return null;
            }
            httpURLConnection.disconnect();//暂时不使用HttpUrlConnection,先断开资源连接
            if (Utils.isEmptyString(crazyRequest.getUrl())) {
                throw new CrazyException("Your url is empty when post stream!");
            }
            if (Utils.isEmptyString(crazyRequest.getPath())) {
                throw new CrazyException("Your upload file path is empty!");
            }
            File f = new File(crazyRequest.getPath());
            if (!f.exists()) {
                throw new CrazyException("Your upload file is unexist!");
            }
            long uploadedLength = crazyRequest.getLastPushPos();
            Utils.LOG(TAG, "the uploadedLength = " + uploadedLength);
            FileInputStream fis = null;
            HttpPost mHttpPost = new HttpPost();
            try {
                fis = new FileInputStream(f);
                fis.skip(uploadedLength < 0 ? 0 : uploadedLength);
                URI uri = URI.create(crazyRequest.getUrl());
                mHttpPost.setURI(uri);
                Map<String, String> headers = crazyRequest.getHeaders();
                if (headers != null) {
                    for (String pKey : headers.keySet()) {
                        mHttpPost.addHeader(pKey, headers.get(pKey));
                    }
                }
                //cu.addUploadTask(crazyRequest.getFileLocation(), mHttpPost);
                long fileLength = f.length();
                //FileHandlerListener listener = crazyRequest.getFileHandlerListener();
                //initFileHandler(listener);

                // 保存需上传文件信息
                MultipartEntityBuilder entitys = MultipartEntityBuilder.create();
                entitys.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                entitys.setCharset(Charset.forName(HTTP.UTF_8));
                entitys.addPart("file", new FileBody(f));
                HttpEntity httpEntity = entitys.build();
                CustomInputStreamEntity entity = new CustomInputStreamEntity(httpEntity, crazyRequest, fis,
                        uploadedLength > -1 ? uploadedLength : 0, crazyRequest.getPath(), fileLength);
                entity.setHttpPost(mHttpPost);

                mHttpPost.setEntity(entity);
                HttpClient httpClient = getNewHttpClient(crazyRequest.getUrl(), false);
                HttpResponse response = httpClient.execute(mHttpPost);
                if (crazyRequest.isCanceled()) {
                    Utils.LOG(TAG, "request cancel! location: pushStreamWithApache() in " + FileHandler.class.getSimpleName());
                    throw new CrazyException("request cancel! location: pushStreamWithApache() in " + FileHandler.class.getSimpleName());
                }
                int code = response.getStatusLine().getStatusCode();
                String s = EntityUtils.toString(response.getEntity());
                Utils.LOG(TAG, "the request back json = " + s);
                Utils.writeLogToFile(TAG, "uploadFileStream response code = " + response.getStatusLine().getStatusCode());
                if (code == 200) {
                    /** when upload stream response 200, we send last 100% progress callback to ui **/
                    doSend(crazyRequest, fileLength, fileLength, true);
                } else {
                    if (null != response && response.getEntity() != null) {
                        response.getEntity().consumeContent();
                    }
                }
                return convertResponse(crazyRequest, code, s);
            } catch (Exception e) {
                e.printStackTrace();
                doSend(crazyRequest, 0, 0, false);
                Utils.LOG(TAG, "upload stream exception --------------");
                throw new CrazyException("request cancel! location: abort stream request in pushStreamWithApache()"
                        + CustomInputStreamEntity.class.getSimpleName() + "&" + FileHandler.class.getSimpleName());
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (CrazyException error) {
            error.printStackTrace();
            Utils.LOG(TAG, "request cancel! throw stream upload exception out ------");
            //Utils.writeExceptionToFile(TAG,error);
            throw error;
        }
//        return null;

//        String[] message = {""};
//        String boundary = "------IMModuleBoundary";//分割线
//        Map<String, String> params = crazyRequest.getHeaders();//以键值对的方式放传给服务器的参数
//        DataOutputStream dataOutputStream = null;
//        FileInputStream fileInputStream = null;
//        InputStream inputStream = null;
//        File file = new File(crazyRequest.getPath());
//        try {
//            if (httpURLConnection == null || crazyRequest == null){
//                return null;
//            }
//            httpURLConnection.disconnect();//暂时不使用HttpUrlConnection,先断开资源连接
//            if (Utils.isEmptyString(crazyRequest.getUrl())){
//                throw new CrazyException("Your url is empty when post stream!");
//            }
//            if (Utils.isEmptyString(crazyRequest.getPath())){
//                throw new CrazyException("Your upload file path is empty!");
//            }
//            File f = new File(crazyRequest.getPath());
//            if (!f.exists()){
//                throw new CrazyException("Your upload file is unexist!");
//            }
//            long uploadedLength = crazyRequest.getLastPushPos();
//            Utils.LOG(TAG,"the uploadedLength = "+uploadedLength);
//            httpURLConnection.setRequestMethod("POST");//设置请求方式为post
//            httpURLConnection.addRequestProperty("Connection", "Keep-Alive");//设置与服务器保持连接
//            httpURLConnection.addRequestProperty("Charset", "UTF-8");//设置字符编码类型
//            httpURLConnection.addRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);//post请求，上传数据时的编码类型，并且指定了分隔符
//            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
//            // http正文内，因此需要设为true, 默认情况下是false;
//            httpURLConnection.setDoOutput(true);
//            //设置是否从httpUrlConnection读入，默认情况下是true;
//            httpURLConnection.setDoInput(true);
//            // Post 请求不能使用缓存
//            httpURLConnection.setUseCaches(false);
//            httpURLConnection.setConnectTimeout(10000);
//            //getOutStream会隐含的进行connect，所以也可以不调用connect
//            dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
//            fileInputStream = new FileInputStream(file);
//            dataOutputStream.writeBytes("--" + boundary + "\r\n");
//            // 设定传送的内容类型是可序列化的java对象
//            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
//            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
//                    + encode(file.getName(), "UTF-8") + "\"\r\n");
//            dataOutputStream.writeBytes("\r\n");
//            byte[] b = new byte[1024];
//            while ((fileInputStream.read(b)) != -1) {
//                dataOutputStream.write(b);
//            }
//            dataOutputStream.writeBytes("\r\n");
//            dataOutputStream.writeBytes("--" + boundary + "\r\n");
//            try {
//                Set<String> keySet = params.keySet();
//                for (String param : keySet) {
//                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\""
//                            + encode(param) + "\"\r\n");
//                    dataOutputStream.writeBytes("\r\n");
//                    String value = params.get(param);
//                    dataOutputStream.writeBytes(encode(value) + "\r\n");
//                    dataOutputStream.writeBytes("--" + boundary + "\r\n");
//
//                }
//            } catch (Exception e) {
//                Log.e("uploadFile ", e.toString());
//            }
//
//            inputStream = httpURLConnection.getInputStream();//得到一个输入流（服务端发回的数据
//            byte[] data = new byte[1024];
//            StringBuffer sb1 = new StringBuffer();
//            int length = 0;
//            while ((length = inputStream.read(data)) != -1) {
//                String s = new String(data, Charset.forName("utf-8"));
//                sb1.append(s);
//            }
//            message[0] = sb1.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                dataOutputStream.close();
//                inputStream.close();
//                fileInputStream.close();
//                httpURLConnection.disconnect();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return message[0];
    }


    /**
     * zhangy modify 20161018 to fix some third-party request maybe cant perform success.
     * because port register error.
     *
     * @param url
     * @param createClient we create a new client which config apply port register category.
     * @return
     */
    private HttpClient getNewHttpClient(String url, boolean createClient) {
        try {
            if (client == null || createClient) {
                Utils.LOG(TAG, "the login create new http client -----------");
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                SSLSocketFactory sf = new SSLUtil(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, CONN_TIMEOUT);

                HttpConnectionParams.setSoTimeout(httpParameters, SOCK_TIMEOUT);
                HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
                HttpProtocolParams.setUseExpectContinue(httpParameters, true);

                SchemeRegistry registry = new SchemeRegistry();
                ConnManagerParams.setMaxConnectionsPerRoute(httpParameters, new ConnPerRouteBean(150));
                ConnManagerParams.setMaxTotalConnections(httpParameters, 250);
                HttpConnectionParams.setStaleCheckingEnabled(httpParameters, false);
                //zhangy modify 20161018 to fix each domain request url maybe happen domian name parse error ,timeout.
                int port = URI.create(url).getPort();
                Utils.LOG(TAG, "the http or https === port = " + port);
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), port == -1 ? 80 : port));
                registry.register(new Scheme("https", sf, port == -1 ? 443 : port));
                //end modify

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParameters, registry);
                HttpClient client = new DefaultHttpClient(ccm, httpParameters);
                if (this.client == null && !createClient) {
                    Utils.LOG(TAG, "create httpClient Connection manager once ");
                    if (!createClient) {
                        if (monitorThread == null) {
                            monitorThread = new IdleConnectionMonitorThread(ccm);
                        }
                        monitorThread.start();
                    }
                    this.client = client;
                    return client;
                }
                return client;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new DefaultHttpClient();
        }
        return client;
    }
//end modify

    public static class IdleConnectionMonitorThread extends Thread {
        private final ClientConnectionManager connMgr;
        private volatile boolean shutdown;

        public IdleConnectionMonitorThread(ClientConnectionManager connMgr) {
            super();
            this.connMgr = connMgr;
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(5000);
                        // Close expired connections
                        //Utils.LOG("CoreRequest","close expired connections ------------");
                        connMgr.closeExpiredConnections();
                        connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
                    }
                }
            } catch (InterruptedException ex) {
                // do something
            }
        }

        public void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }


    /**
     * apache文件进度监听
     */
    public class CustomInputStreamEntity extends HttpEntityWrapper {

        private final long uploadedLengh;
        private final long totalLength;
        private final String path;
        CrazyRequest request;
        HttpPost mHttpPost;

        /**
         * @param instream
         * @param uploadedLength
         * @param path
         * @param totalLength
         */
        public CustomInputStreamEntity(HttpEntity entity, CrazyRequest request, InputStream instream, long uploadedLength,
                                       String path, long totalLength) {
            super(entity);
            this.totalLength = totalLength;
            this.uploadedLengh = uploadedLength;
            this.path = path;
            this.request = request;
        }

        public void setHttpPost(HttpPost httpPost) {
            mHttpPost = httpPost;
        }

        @Override
        public void writeTo(final OutputStream outstream) throws IOException {
            super.writeTo(new CustomOutputStream(request, outstream, uploadedLengh, path, totalLength));
        }

        final class CustomOutputStream extends FilterOutputStream {

            private long transferred;
            private long totalLength;
            private long uploadedLength;//已经上传的文件大小
            private String path;
            CrazyRequest<?> request;
            int currentProgressCount = 0;

            public CustomOutputStream(CrazyRequest<?> request, final OutputStream out,
                                      final long uploadedLength, String path, long totalLength) {
                super(out);
                //this.transferred = uploadedLength;
                this.totalLength = totalLength;
                this.uploadedLength = uploadedLength;
                this.path = path;
                this.request = request;
            }

            public void write(byte[] b, int off, int len) throws IOException {

                if (totalLength == 0) {
                    return;
                }
                out.write(b, off, len);
                this.transferred += len;
                int progress = (int) (((transferred + uploadedLength) * 100) / totalLength);
                if (request.isCanceled()) {
                    Utils.LOG(TAG, "request cancel! location: writeTo() in " + CustomInputStreamEntity.class.getSimpleName() +
                            "&" + FileHandler.class.getSimpleName() + "stop at = " + progress);
                    if (mHttpPost != null) {
                        mHttpPost.abort();//this will throw an exception
                    }
                    return;
                }
                if (request.getFileHandlerListener() != null) {
                    if (progress != currentProgressCount) {
                        currentProgressCount = progress;
                        Utils.writeLogToFile(TAG, "write single file progress = " + progress);
                        doSend(request, transferred, totalLength, true);
                    }
                }
            }

            public void write(int b) throws IOException {
                out.write(b);
                this.transferred++;
                if (request.getFileHandlerListener() != null) {
                    doSend(request, transferred, totalLength, true);
                }
            }
        }

    }

    /**
     * pull file stream from network
     *
     * @param httpURLConnection
     * @param crazyRequest
     * @return the file stream save location
     * @throws CrazyException
     */
    private CrazyResult pullStream(HttpURLConnection httpURLConnection, CrazyRequest crazyRequest) throws CrazyException {

        if (crazyRequest == null || httpURLConnection == null) {
            return null;
        }
        String path = crazyRequest.getPath();
        if (Utils.isEmptyString(path)) {
            path = Utils.createFilepath(Utils.DIR_CATEGORY.CACHE, System.currentTimeMillis() + ".jpg");
        }
        File file = new File(path);
        try {
            if (crazyRequest.isCanceled()) {
                throw new CrazyException("request is canceled by user");
            }
            int responseCode = httpURLConnection.getResponseCode();
            String strResult = null;
            //zhangy optimize 20161117 memory custome by use google recommaned saving byte[] pool
            byte[] result = entityToBytes(httpURLConnection);
            if (result != null) {
                strResult = new String(result, Utils.CHAR_FORMAT);
            }
            Utils.LOG(TAG, "the request back json = " + strResult);
            if (strResult.contains("987200156")) {
                throw new CrazyException(strResult);
            }
            if (responseCode == 200) {
                InputStream is = httpURLConnection.getInputStream();
                if (is == null) {
                    throw new CrazyException("input stream from connection is null when pull stream");
                }
                RandomAccessFile fileOutputStream = null;
                try {
                    fileOutputStream = new RandomAccessFile(file, "rwd");
                    fileOutputStream.seek(0);
                    int ch = -1;
                    final byte[] buf = new byte[512];
                    long mCurCount = 0;
                    long currentProgressCount = 0;
                    FileHandlerListener fileHandlerListener = crazyRequest.getFileHandlerListener();
                    //initFileHandler(fileHandlerListener);
                    long size = fileHandlerListener.sizeOf(path);
                    /**
                     * if size is not define by user,we pick it from http connection response headers
                     */
                    if (size == 0) {
                        Map<String, List<String>> headerFields = httpURLConnection.getHeaderFields();
                        if (headerFields != null) {
                            if (headerFields.containsKey("file_length")) {
                                List<String> list = headerFields.get("file_length");
                                if (list != null && !list.isEmpty()) {
                                    String file_length = list.get(0);
                                    if (!Utils.isEmptyString(file_length)) {
                                        if (Utils.isNumeric(file_length)) {
                                            size = Long.parseLong(file_length);
                                            Utils.LOG(TAG, "the file header file size = " + size);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    /** try to acquire file size from connection **/
                    if (size == 0) {
                        size = httpURLConnection.getContentLength();
                        Utils.writeLogToFile(TAG, "file size from http connection = " + size);
                    }

                    while ((ch = is.read(buf)) != -1) {
                        if (crazyRequest.isCanceled()) {
                            break;
                        }
                        fileOutputStream.write(buf, 0, ch);
                        mCurCount += ch;
                        if (size == 0 || fileHandlerListener == null) {
                            continue;
                        }
                        int curProgress = (int) ((mCurCount * 100) / size);
                        if (curProgress != currentProgressCount) {
                            currentProgressCount = curProgress;
                            doSend(crazyRequest, mCurCount, size, true);
                        }
                    }
                } catch (Exception e) {
                    Utils.writeExceptionToFile(TAG, e);
                    file.delete();
                } finally {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
                if (crazyRequest.isCanceled()) {
                    doSend(crazyRequest, 0, 0, false);
                    Utils.LOG(TAG, "request cancel! location: pullStream() in " + FileHandler.class.getSimpleName());
                    throw new CrazyException("request cancel! location: pullStream() in " + FileHandler.class.getSimpleName());
                }
                if (!file.exists()) {
                    return null;
                }
                return convertResponse(crazyRequest, responseCode, strResult);
            }
        } catch (CrazyException error) {
            Utils.writeExceptionToFile(TAG, error);
            if (file != null) {
                file.delete();
            }
            throw error;
        } catch (IOException error) {
            Utils.writeExceptionToFile(TAG, error);
            if (file != null) {
                file.delete();
            }
            error.printStackTrace();
        }
        return null;
    }

    /**
     * send out the file perform progress in runnable work
     *
     * @param current
     * @param totalSize
     * @param notError  whether happen error when doSend(...)
     */
    private void doSend(CrazyRequest<?> request, long current, long totalSize, boolean notError) {
        new FileStatusPoster(current, totalSize, request, notError).send();
    }

    private final class FileStatusPoster {
        long current;
        long totalSize;
        CrazyRequest<?> request;
        boolean notError;

        FileStatusPoster(long current, long totalSize,
                         CrazyRequest<?> request, boolean notError) {
            this.current = current;
            this.totalSize = totalSize;
            this.request = request;
            this.notError = notError;
        }

        public void send() {
            if (request.getFileHandlerListener() == null) {
                return;
            }
            request.getFileHandlerListener().onHandleStatus(request, current, totalSize, notError);
        }
    }

    //    /**
//     * 初始化File handler
//     *
//     * @param fileHandlerListener
//     */
//    private void initFileHandler(FileHandlerListener fileHandlerListener){
//        //if callback listener is null,we dont need to post progress.
//        if (fileHandlerListener == null){
//            return;
//        }
//    }
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

}
