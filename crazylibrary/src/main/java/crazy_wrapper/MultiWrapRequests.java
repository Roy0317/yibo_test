package crazy_wrapper;


import crazy_wrapper.Crazy.CrazyResponse;
import crazy_wrapper.Crazy.CrazyResult;
import crazy_wrapper.Crazy.network.FileHandler;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangy on 2016/11/28.
 * compound crazy request wrapper sub-class
 * which will combine sub requests into an general compound request
 */
public class MultiWrapRequests<Object extends CrazyResult> extends CrazyRequest<Object> {

    CrazyRequest[] cr;
    List<CrazyRequest<?>> list;

    public MultiWrapRequests(List<CrazyRequest<?>> list){
        this.list = list;
    }

    /**
     * build muiti stream request
     */
    @Override
    public CrazyRequest[] combineRequests(){
        if (list != null && !list.isEmpty()){
            List<CrazyRequest<?>> inner = new ArrayList<CrazyRequest<?>>();
            for (int i=0; i<list.size(); i++){
                CrazyRequest request = list.get(i);
                if(request != null){
                    request.setTag(getTag());
                    request.setStreams(getStreams());
                    if (request.getListener() == null) {
                        if (getListener() != null) {
                            request.setListener(getListener());
                            if (getListener() instanceof SessionResponse.RequestChangeListener<?>) {
                                request.setExpandListener((SessionResponse.RequestChangeListener<?>) getListener());
                            }
                            if (getListener() instanceof FileHandler.FileHandlerListener){
                                request.setFileHandlerListener((FileHandler.FileHandlerListener) getListener());
                            }
                        }
                    }
                    //设置组合请求的请求头
                    if (request.getHeaders() == null) {
                        request.setHeaders(getHeaders());
                    }
                    //设置组合请求的前台加载方式
                    request.setLoadMethod(getLoadMethod());
                    //指定请求是否是最后一个请求
                    if (i == list.size() - 1){
                        request.setLastRequest(true);
                    }
                    inner.add(request);
                }
            }
            list.clear();
            list.addAll(inner);
        }
        return getCombineRequest();
        //end add zhangy 20161124
    }

    @Override
    public CrazyRequest[] getCombineRequest() {
        if (cr == null && list != null && !list.isEmpty()){
            cr = new CrazyRequest[list.size()];
            for (int i=0;i<list.size();i++){
                cr[i] = list.get(i);
            }
            cr = RequestManager.sortDeliveriedRequest(cr);
        }
        return cr;
    }

    @Override public SessionResponse<Object> parseCrazyResponse(CrazyResponse<Object> crazyResponse) {
        return null;
    }

    @Override
    public void deliveryResponse(SessionResponse<Object> response) {

    }


}
