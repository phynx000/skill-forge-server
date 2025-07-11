package com.skillforge.skillforge_api.entity;

public class RestResponse<T> {
    private int statusCode;
    private String error;

    // message may String or arraylist
    private Object message;
    private T data;

    public int getStatusCode() {
        return statusCode;
    }

//    public static <T> RestResponse<T> error(int code, Object message, String error) {
//        RestResponse<T> res = new RestResponse<>();
//        res.setStatusCode(code);
//        res.setMessage(message);
//        res.setError(error);
//        res.setData(null);
//        return res;
//    }


    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
