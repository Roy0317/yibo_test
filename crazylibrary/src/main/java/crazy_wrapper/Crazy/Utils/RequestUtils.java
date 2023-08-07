package crazy_wrapper.Crazy.Utils;

import java.util.HashMap;
import java.util.Map;

import crazy_wrapper.Crazy.request.CrazyRequest;

public class RequestUtils {

    public static final String NATIVE_HOST = "Host";
    public static final String NATIVE_WEBVIEW_HOST = "WebviewHost";
    public static final String ROUTE_TYPE = "routeType";
    public static final String NATIVE_DOMAIN = "domain";
    public static final String NATIVE_FLAG = "Native-Flag";

    /**
     * 获取请求头中的'Host'和'Native-Flag'
     * @param crazyRequest
     * @return
     */
    public static Map<String, Object> getRequestHostFlag(CrazyRequest crazyRequest) {
        Map<String, Object> data = new HashMap<>();
        Map headers = crazyRequest.getHeaders();
        if (headers.containsKey(NATIVE_HOST)) {
            data.put(NATIVE_HOST, headers.get(NATIVE_HOST));
        }
        if (headers.containsKey(NATIVE_FLAG)) {
            data.put(NATIVE_FLAG, headers.get(NATIVE_FLAG));
        }
        if (headers.containsKey(NATIVE_DOMAIN)) {
            data.put(NATIVE_DOMAIN, headers.get(NATIVE_DOMAIN));
        }
        if (headers.containsKey(ROUTE_TYPE)) {
            data.put(ROUTE_TYPE, headers.get(ROUTE_TYPE));
        }
        return data;
    }

}
