package com.cabe.lib.cache.exception;

/**
 * RxCache Exception<br/>
 * Created by cabe on 16/4/12.
 */
public class RxException extends RuntimeException {
    public Throwable e;
    public int code = 0;
    public String info = "";
    public RxException(int code, String info) {
        this.code = code;
        this.info = info;
    }

    protected RxException(int code, Throwable e) {
        this(code, getExceptionInfo(code));
        this.e = e;
    }

    private static String getExceptionInfo(int code) {
        ExceptionCode exceptionCode;
        if(HttpExceptionCode.isHttpException(code)) {
            exceptionCode = new HttpExceptionCode();
        } else if(HttpExceptionCode.isDiskException(code)) {
            exceptionCode = new DiskExceptionCode();
        } else {
            exceptionCode = new ExceptionCode();
        }
        return exceptionCode.getInfo(code);
    }

    public static RxException build(int code, Throwable e) {
        return new RxException(code, e);
    }
}
