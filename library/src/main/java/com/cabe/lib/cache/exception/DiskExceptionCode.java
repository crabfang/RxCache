package com.cabe.lib.cache.exception;

/**
 * Disk Exception
 * Created by cabe on 16/4/12.
 */
public class DiskExceptionCode extends ExceptionCode {
    /** 数据保存到磁盘出错 */
    public final static int DISK_EXCEPTION_SAVE = -1000;
    /** 磁盘数据检测出错  */
    public final static int DISK_EXCEPTION_CHECK = -1001;
    /** 获取磁盘数据出错  */
    public final static int DISK_EXCEPTION_GET = -1002;

    @Override
    public String getInfo(int code) {
        String info = null;

        if(ExceptionCode.isDiskException(code)) {
            switch (code) {
                case DISK_EXCEPTION_SAVE:
                    info = "数据保持失败";
                    break;
                case DISK_EXCEPTION_CHECK:
                    info = "本地数据检测失败";
                    break;
                case DISK_EXCEPTION_GET:
                    info = "本地数据获取失败";
                    break;
            }
        }

        return info;
    }
}
