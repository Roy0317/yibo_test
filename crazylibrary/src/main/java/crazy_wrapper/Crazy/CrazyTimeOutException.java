package crazy_wrapper.Crazy;

/**
 * Created by johnson on 2017/11/7.
 */

public class CrazyTimeOutException extends CrazyException {

    String errorMsg;

    public CrazyTimeOutException(String errorMsg, String ec) {
        this.errorMsg = errorMsg;
        errorCode = ec;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
