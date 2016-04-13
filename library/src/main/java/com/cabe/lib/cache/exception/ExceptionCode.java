package com.cabe.lib.cache.exception;

/**
 *
 * Created by cabe on 16/4/12.
 */
public class ExceptionCode {
    public static int RX_EXCEPTION_DEFAULT = -100;
    public String getInfo(int code) {
        String info = "";
        if(code == RX_EXCEPTION_DEFAULT) {
            info = "未知错误";
        }
        return info;
    }

    public static boolean isHttpException(int code) {
        return code > -100;
    }

    public static boolean isDiskException(int code) {
        return code >= -200 && code < -100;
    }
}
