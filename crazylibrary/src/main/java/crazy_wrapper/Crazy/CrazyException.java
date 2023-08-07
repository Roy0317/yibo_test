package crazy_wrapper.Crazy;


/**
* crazy inner define exception to listener crazy logic flow smoothly
* Created by zhangy on 2017/2/14 10:33
**/
public class CrazyException extends Exception{

    CrazyResponse<? extends CrazyResult> crazyResponse;
    //the error http resonse object when http code is not 200
    String errorResponse;
    protected String errorCode;
    private String statusCode;
    private String originExceptionMsg;

    public CrazyException() {
        crazyResponse = null;
    }

    public CrazyException(CrazyResponse<? extends CrazyResult> crazyResponse){
        this.crazyResponse = crazyResponse;
    }

    public CrazyException(String message,String response) {
        super(message);
        this.errorResponse = response;
    }

    public CrazyException(String detailMessage) {
        super(detailMessage);
        crazyResponse = null;
    }

    public CrazyException(String detailMessage,CrazyResponse<? extends CrazyResult> crazyResponse) {
        super(detailMessage);
        this.crazyResponse = crazyResponse;
    }

    public String getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(String errorResponse) {
        this.errorResponse = errorResponse;
    }

    public String getErrCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public CrazyException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        crazyResponse = null;
    }

    public CrazyException(Throwable throwable) {
        super(throwable);
        crazyResponse = null;
    }

    public CrazyResponse getCrazyResponse() {
        return crazyResponse;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getOriginExceptionMsg() {
        return originExceptionMsg;
    }

    public void setOriginExceptionMsg(String originExceptionMsg) {
        this.originExceptionMsg = originExceptionMsg;
    }
}
