package crazy_wrapper.Crazy;

import crazy_wrapper.Crazy.network.ConnectionFactory;
import crazy_wrapper.Crazy.network.ConnectionFactoryImpl;
import crazy_wrapper.Crazy.request.CrazyRequest;
import crazy_wrapper.Crazy.response.SessionResponse;

import java.io.IOException;

/**
 * Created by zhangy on 2016/11/18.
 * this is an implementent of crazy frame,it do perform request
 * it can considence normal string request , file stream request and so on...
 */
public class BasicCrazy implements CrazyPlugins{

    @Override public SessionResponse<?> performRequest(CrazyRequest crazyRequest) throws CrazyException,IOException {
        if (crazyRequest == null){
            throw new CrazyException("CrazyRequest is null,Maybe you havent put in request");
        }
        if (crazyRequest.isCanceled()){
            throw new CrazyException("request is cancel by user");
        }
        ConnectionFactory connectionFactory = new ConnectionFactoryImpl();
        try {
            SessionResponse<?> response = connectionFactory.createConnection(crazyRequest.getProtocol())
                .runConnection(crazyRequest);
            return response;
        } catch (CrazyTimeOutException e) {
            throw e;
        }catch (IOException e){
            throw e;
        }
    }

}
