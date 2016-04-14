package com.cabe.lib.cache;

import com.google.gson.reflect.TypeToken;

/**
 * 磁盘缓存相关接口
 * Created by cabe on 16/4/14.
 */
public interface DiskCacheRepository {
    <T> boolean exits(TypeToken<T> typeToken);
    <T> T get(TypeToken<T> typeToken);
    <T> boolean put(TypeToken<T> typeToken, T data);
}