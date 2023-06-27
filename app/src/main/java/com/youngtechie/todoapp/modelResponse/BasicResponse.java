package com.youngtechie.todoapp.modelResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasicResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("data")
    @Expose
    private UserResponse data;

    public BasicResponse() {
    }

    public BasicResponse(Boolean success, String msg , UserResponse data) {
        super();
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public UserResponse getData() {return  data;}
    public  void setData(UserResponse data) {this.data = data;}

}
