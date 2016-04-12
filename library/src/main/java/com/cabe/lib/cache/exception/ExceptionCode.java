package com.cabe.lib.cache.exception;

/**
 *
 * Created by cabe on 16/4/12.
 */
public abstract class ExceptionCode {
    public abstract String getInfo(int code);

    public static boolean isHttpException(int code) {
        return code > 0;
    }

    public static boolean isDiskException(int code) {
        return code > 0;
    }
}
