package com.yibo.yiboapp.data;

public class VerifyData {

    /**
     * msg : 驗證碼
     * code : verify_code
     * src : data:image/jpg;base64,/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAjAFADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD3+iiucvvB2nXl3NeT3N0GkYu2HUAf+O9KqKi/idjKrKpFXpxv87HR0V5hpejwap4iMNkZDYQtuaSQgkqPoB1PSvR7q1+1BVMroo6he9aVKag0rmGFxUq8XLltbTe9/wACxRXPX1utjMgt5nLHqM8j8q1Ly1mv9KNuLh7aWRV3SJ95ehOP1FS4JW10Z0RqN3VtUXaK8qXT0j8cRWUckkyR3KAvIcs2MFs/kazP2gtQ22Oi6Yrf6yWS4Yem0BV/9CatsXh1h1FqV7q+1iIVuZNtbHtFFeB6J4F+Fuu3UdlZ+KtRlvGAHll1j3t3C74hn6DNe7WdrFY2UFpAMQwRrEg9FUYH6CuRO5rGTZNXJ+MdYdETSLTLXFxgSBeoU9F+p/l9a6yse28N2dvqzam0s89yxJzKwIBPcAAVrScYvmkc+LhVqQ9nT0vu+yH6Bo6aNpiQ8GZ/mlYd29PoKvXEMV7D5Zc7QcnYRU9Z76NbOxOZFz6N/jS5ry5pPU1jTVOChBaIzJUFlfqLZ/MIxjODz6V0dVbfT7e2bciEsOjMc4rP1nwzZ67PHLdTXCGNdqrGygdc9wa0vCckpOy72uKMZQTaXyOP8M/6f45luuoDSzfnkD/0KuvvNE8N69qZnvbOw1C7tl8orMFl8sZzgqcgHn0zWd/wr3Sf+fi9/wC+0/8AiaxtU+CvhbVLk3DyajDK2N5inB3EDGTuU/pgemK6MwrUq0lKm72VrWM6MJwTTR5V8QtN0w/E+3sPCMcUcreUjJa4CJcFj93HAwNmcdDmvpquT8L/AA48OeEp/tOn2ryXeNoubh97gd8cAD8AK6yvOSsbwja7YUUUVRYUUUUAFFFFABRRRQAUUUUAf//Z
     * success : false
     * token :
     */

    private String msg;
    private String code;
    private String src;
    private boolean success;
    private String token;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
