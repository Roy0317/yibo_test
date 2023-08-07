package crazy_wrapper.Crazy.network;

import crazy_wrapper.Crazy.request.CrazyRequest;

/**
 * Created by zhangy on 2016/11/8.
 * implement class to create each connection handler
 */
public class ConnectionFactoryImpl implements ConnectionFactory{

    @Override public NetworkConnection createConnection(int protocol) {
        if (protocol == CrazyRequest.Protocol.HTTP.ordinal() ||
            protocol == CrazyRequest.Protocol.HTTPS.ordinal()){
            return new StringHandler();
        }else if (protocol == CrazyRequest.Protocol.DOWNLOAD.ordinal()||
                  protocol == CrazyRequest.Protocol.UPLOAD.ordinal()){
            return new FileHandler();
        } else if (protocol == CrazyRequest.Protocol.STORAGE.ordinal()) {
            return new StorageHandler();
        }
        return new StringHandler();
    }

}
