package crazy_wrapper.Crazy.network;

/**
 * Created by zhangy on 2016/11/8.
 * create connection handler instance according to url protocol
 */
public interface ConnectionFactory {
    NetworkConnection createConnection(int protocol);
}
