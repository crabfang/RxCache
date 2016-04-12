package com.cabe.lib.cache.exception;

/**
 *
 * Created by cabe on 16/4/12.
 */
public class RxException extends RuntimeException {
    public int code = 0;
    public String info = "";
    public RxException(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public RxException(int code) {
        this(code, getExceptionInfo(code));
    }

    private static String getExceptionInfo(int code) {
        ExceptionCode exceptionCode = null;
        if(HttpExceptionCode.isHttpException(code)) {
            exceptionCode = new HttpExceptionCode();
        } else if(HttpExceptionCode.isDiskException(code)) {
            exceptionCode = new DiskExceptionCode();
        }
        return exceptionCode == null ? "" : exceptionCode.getInfo(code);
    }
}
