package com.cabe.lib.cache.exception;

/**
 * Disk Exception<br>
 * Created by cabe on 16/4/12.
 */
public class DiskExceptionCode extends ExceptionCode {
    /** 数据保存到磁盘出错 */
    public final static int DISK_EXCEPTION_SAVE = -101;
    /** 磁盘数据检测出错  */
    public final static int DISK_EXCEPTION_CHECK = DISK_EXCEPTION_SAVE - 1;
    /** 获取磁盘数据出错  */
    public final static int DISK_EXCEPTION_GET = DISK_EXCEPTION_SAVE - 2;
    /** 磁盘缓存路径错误 */
    public final static int DISK_EXCEPTION_PATH = DISK_EXCEPTION_SAVE - 3;
    /** 磁盘缓存类型错误 */
    public final static int DISK_EXCEPTION_TYPE = DISK_EXCEPTION_SAVE - 4;

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
                case DISK_EXCEPTION_PATH:
                    info = "缓存路径错误";
                    break;
                case DISK_EXCEPTION_TYPE:
                    info = "缓存类型错误";
                    break;
            }
        }

        return info;
    }
}
