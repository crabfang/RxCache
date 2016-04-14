package com.cabe.lib.cache.exception;

/**
 * RxCache Default Exception Code<br>
 * Created by cabe on 16/4/12.
 */
public class ExceptionCode {
    public final static int RX_EXCEPTION_DEFAULT = -100;
    public final static int RX_EXCEPTION_TYPE_UNKNOWN = -101;
    public String getInfo(int code) {
        String info = "";
        switch (code) {
            case RX_EXCEPTION_DEFAULT:
                info = "未知错误";
                break;
            case RX_EXCEPTION_TYPE_UNKNOWN:
                info = "未知转换类型";
                break;
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
