package com.example.anuo.immodule.jsonmodel;

import com.google.gson.annotations.SerializedName;

public class UploadAvatarResponse {


    /**
     * path : /add_file
     * error : Internal Server Error
     * message : Illegal base64 character 3a
     * timestamp : 1600398519245
     * status : 500
     * success : true
     * fileCode : 20092562a141be694a5dba879019a41dbe24
     */
    @SerializedName("path")
    private String path;
    @SerializedName("error")
    private String error;
    @SerializedName("message")
    private String message;
    @SerializedName("timestamp")
    private long timestamp;
    @SerializedName("status")
    private int status;
    @SerializedName("success")
    private boolean success;
    @SerializedName("fileCode")
    private String fileCode;

    public void setPath(String path) {
        this.path = path;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    @Override
    public String toString() {
        return "UploadAvatarResponse{" +
                "path='" + path + '\'' +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", status=" + status +
                ", success=" + success +
                ", fileCode='" + fileCode + '\'' +
                '}';
    }
}
